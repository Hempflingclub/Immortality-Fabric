package net.hempflingclub.immortality.util;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.hempflingclub.immortality.enchantments.ImmortalityEnchants;
import net.hempflingclub.immortality.entitys.ImmortalWither.ImmortalWither;
import net.hempflingclub.immortality.item.ImmortalityItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

import java.util.Objects;

import static net.hempflingclub.immortality.util.ImmortalityStatus.*;

public final class ImmortalityInvokeImmortality {//TODO: use Immortality Status
    public final static DamageSource soulBoundDamageSource = new DamageSource("immortality.soulBound").setBypassesArmor().setBypassesProtection().setUnblockable();
    private static boolean killedByImmortalWither = false;
    private static final float witherDamageMultiplier = 2.5f;
    private static final float witherDamageMultiplierEasy = 2.0f;

    public static float damageManager(LivingEntity livingEntity, DamageSource dmgSource, float damageAmount) {
        if (livingEntity.isPlayer() && !livingEntity.getWorld().isClient()) {
            ServerPlayerEntity playerEntity = (ServerPlayerEntity) livingEntity;
            return handlePlayerDeath(playerEntity, dmgSource, damageAmount);
        } else {
            if ((livingEntity.getHealth() - damageAmount) <= 0) {
                //Non Player would die
                return handleEntityDeath(livingEntity, dmgSource, damageAmount);
            }
        }
        return damageAmount;
    }

    private static float handlePlayerDeath(ServerPlayerEntity playerEntity, DamageSource dmgSource, float damageAmount) {
        if (playerShouldNotDie(playerEntity, dmgSource, damageAmount)) {
            return playerImmortalityHandling(playerEntity, dmgSource, damageAmount);
        }
        //No faulty handling
        else if (dmgSource == null) return damageAmount;
            //If Not Killed By Immortal Wither just give damage
        else if (!(dmgSource.getSource() instanceof WitherSkullEntity witherSkullEntity)) return damageAmount;
            //Not doing logic when killed by buggy Immortal Wither Skulls
        else if (!(witherSkullEntity.getOwner() instanceof ImmortalWither)) return damageAmount;
            //Give appropriate damage, and apply ImmortalWither Damage
        else {
            return ImmortalWitherDamageWithDifficulty(playerEntity.getWorld(), damageAmount);
        }
    }

    private static void teleportPlayerToSpawnPoint(ServerPlayerEntity playerEntity) {
        //If in Void taking damage then Teleport to Spawnpoint/Bed of Player, When no Bed is found then yeet them to Overworld Spawn
        FabricDimensions.teleport(playerEntity
                , Objects.requireNonNull(playerEntity.world.getServer()).getWorld((playerEntity).getSpawnPointDimension())
                , new TeleportTarget(
                        Vec3d.of(((playerEntity).getSpawnPointPosition()) == null ?
                                Objects.requireNonNull(playerEntity.getWorld().getServer()).getOverworld().getSpawnPos() :
                                ((playerEntity).getSpawnPointPosition()))
                        , Vec3d.ZERO, playerEntity.getHeadYaw(), playerEntity.getPitch()
                ));
        playerEntity.fallDistance = 0;
    }

    private static boolean hasAnyImmortality(ServerPlayerEntity playerEntity) {
        boolean isDeltaImmortal = getBool(playerEntity, ImmortalityData.DataTypeBool.DeltaImmortality);
        if (isDeltaImmortal) return true;
        boolean isGammaImmortal = getBool(playerEntity, ImmortalityData.DataTypeBool.GammaImmortality);
        if (isGammaImmortal) return true;
        boolean isBetaImmortal = getBool(playerEntity, ImmortalityData.DataTypeBool.BetaImmortality);
        if (isBetaImmortal) return true;
        boolean isAlphaImmortal = getBool(playerEntity, ImmortalityData.DataTypeBool.AlphaImmortality);
        if (isAlphaImmortal) return true;
        //No Valid Immortality found
        return false;
    }

    private static float ImmortalWitherDamageWithDifficulty(ServerWorld world, float damageAmount) {
        //2.5 multiplier on Normal / Hard | 2.0 multiplier on Easy
        // (Against Players, Animals get Default damage)
        return ((damageAmount) * ((world.getDifficulty() == Difficulty.HARD || world.getDifficulty() == Difficulty.NORMAL) ? witherDamageMultiplier : witherDamageMultiplierEasy));
    }

    private static boolean playerShouldNotDie(ServerPlayerEntity playerEntity, DamageSource dmgSource, float damageAmount) {
        boolean hasAnyImmortality = hasAnyImmortality(playerEntity);
        boolean wouldDie = (playerEntity.getHealth() - damageAmount) <= 0;
        boolean wouldDieToImmortalWither;
        {
            //Would Die To Immortal Wither
            float immortalWitherDamageWithDifficulty = ImmortalWitherDamageWithDifficulty(playerEntity.getWorld(), damageAmount);
            boolean wouldActuallyDieToImmortalWither = (playerEntity.getHealth() - immortalWitherDamageWithDifficulty) <= 0;
            wouldDieToImmortalWither = (dmgSource != null && dmgSource.getSource() instanceof WitherSkullEntity witherSkullEntity &&
                    witherSkullEntity.getOwner() instanceof ImmortalWither && wouldActuallyDieToImmortalWither);

        }
        boolean shouldNotDie = (hasAnyImmortality && (wouldDie || wouldDieToImmortalWither));
        return shouldNotDie;
    }

    private static float playerImmortalityHandling(ServerPlayerEntity playerEntity, DamageSource dmgSource, float damageAmount) {

        // Stop Endlessly falling into void
        if (playerEntity.getY() <= playerEntity.world.getBottomY() && dmgSource == DamageSource.OUT_OF_WORLD)
            teleportPlayerToSpawnPoint(playerEntity);
        //Stop Void Death, this will not count as a Death
        if (dmgSource == DamageSource.OUT_OF_WORLD) return 0;

        //Confirmed died a valid way
        {
            //On Death Effects
            //Feedback Death Effect
            playerEntity.getWorld().playSoundFromEntity(null, playerEntity, SoundEvents.BLOCK_AMETHYST_CLUSTER_FALL, SoundCategory.PLAYERS, 5, 1);
            (playerEntity.getWorld()).spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), 64, 0, 5, 0, 1);
            //Extinguish and refill Air
            playerEntity.setAir(playerEntity.getMaxAir());
            playerEntity.setOnFireFor(0);
            playerEntity.setOnFire(false);
            //Give temporary Weakness
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20, 0, false, false));
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1, false, false));
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 15 * 20, 2, true, true));
            //Increase Death Counter in Statistics
            playerEntity.incrementStat(Stats.DEATHS);
        }
        // when not Delta or Gamma Immortal stop their Death for sure
        boolean isDeltaImmortality = getBool(playerEntity, ImmortalityData.DataTypeBool.DeltaImmortality);
        boolean isGammaImmortality = getBool(playerEntity, ImmortalityData.DataTypeBool.GammaImmortality);
        boolean stopDeath = !(isDeltaImmortality || isGammaImmortality);

        //Increase Immortals Death Counter
        ImmortalityStatus.incrementGeneric(playerEntity, ImmortalityData.DataTypeInt.ImmortalDeaths);
        sendDeathMessage(playerEntity, dmgSource);
        //Giving Negative Hearts at Death Time, so no recursive loops happen because of Immortality Status time based checks
        int maxHealth = (int) playerEntity.getMaxHealth();
        {
            boolean isDeltaImmortal = getBool(playerEntity, ImmortalityData.DataTypeBool.DeltaImmortality);
            boolean isGammaImmortal = getBool(playerEntity, ImmortalityData.DataTypeBool.GammaImmortality);
            //Needs to be either or to get Negative Hearts, or for any underlying Logic as Negative Hearts are applied first anyway
            if (isDeltaImmortal || isGammaImmortal) {
                //This will result in this Negative function being called, and applying the first part, of giving negative Hearts
                incrementGeneric(playerEntity, ImmortalityData.DataTypeInt.TemporaryNegativeHearts);
                maxHealth += negativeImmortalityHeartsHealthAddition; //Calculate New Temporary Heart State so determination of should die still works
            }
        }

        //Reset Life Elixir Time
        int LifeElixirTime = getInt(playerEntity, ImmortalityData.DataTypeInt.LifeElixirCooldownSeconds);
        addGeneric(playerEntity, ImmortalityData.DataTypeInt.LifeElixirCooldownSeconds, -LifeElixirTime);
        //TODO: Ensure Bane of Life | Immortal Wither Kills work as expected

        if (stopDeath) return stopDeath(playerEntity);
        //Check if Death is a final Death, if not stop Death
        if (maxHealth >= 2) return stopDeath(playerEntity);
        //Ensure Death, reducing likely de-syncs
        playerEntity.setHealth(1);
        return damageAmount + 1;
    }

    private static void sendDeathMessage(ServerPlayerEntity immortalPlayer, DamageSource dmgSource) {
        boolean deltaImmortality = getBool(immortalPlayer, ImmortalityData.DataTypeBool.DeltaImmortality);
        boolean gammaImmortality = getBool(immortalPlayer, ImmortalityData.DataTypeBool.GammaImmortality);
        boolean betaImmortality = getBool(immortalPlayer, ImmortalityData.DataTypeBool.BetaImmortality);
        boolean alphaImmortality = getBool(immortalPlayer, ImmortalityData.DataTypeBool.AlphaImmortality);
        boolean isPureGammaImmortal = gammaImmortality && !betaImmortality && !alphaImmortality;
        String translationKeyByPlayer, translationKeyNormal;

        //Reset Bane Of Life to negative so Immortality Status logic will do the reset logic
        int KilledByBaneOfLifeTime = getInt(immortalPlayer, ImmortalityData.DataTypeInt.KilledByBaneOfLifeTime) + 1;
        addGeneric(immortalPlayer, ImmortalityData.DataTypeInt.KilledByBaneOfLifeTime, -KilledByBaneOfLifeTime);
        int KilledByBaneOfLifeCurrentAmount = getInt(immortalPlayer, ImmortalityData.DataTypeInt.KilledByBaneOfLifeCurrentAmount) + 1;
        addGeneric(immortalPlayer, ImmortalityData.DataTypeInt.KilledByBaneOfLifeCurrentAmount, -KilledByBaneOfLifeCurrentAmount);

        translationKeyByPlayer =
                deltaImmortality ? "immortality.last.death.player" :
                        isPureGammaImmortal ? "immortality.semiImmortal_slayed.death.player" :
                                alphaImmortality ? "immortality.trueImmortal_slayed.death.player" :
                                        "immortality.immortal_slayed.death.player";// Must be normal Immortality
        translationKeyNormal =
                deltaImmortality ? "immortality.last.death" :
                        isPureGammaImmortal ? "immortality.semiImmortal_slayed.death" :
                                alphaImmortality ? "immortality.trueImmortal_slayed.death" :
                                        "immortality.immortal_slayed.death";// Must be normal Immortality
        sendMessageToAllPlayers(immortalPlayer, dmgSource, translationKeyByPlayer, translationKeyNormal);
    }

    private static void sendMessageToAllPlayers(ServerPlayerEntity immortalPlayer, DamageSource dmgSource, String translationKeyByPlayer, String translationKeyNormal) {
        for (PlayerEntity player : Objects.requireNonNull(immortalPlayer.getServer()).getPlayerManager().getPlayerList()) {
            if (dmgSource.getSource() != null && dmgSource.getSource() != immortalPlayer) {
                sendMessage(immortalPlayer, dmgSource, player, translationKeyByPlayer);
            } else {
                sendMessage(immortalPlayer, dmgSource, player, translationKeyNormal);
            }
        }
    }

    private static void sendMessage(ServerPlayerEntity immortalPlayer, DamageSource dmgSource, PlayerEntity playerToSendTo, String translationKey) {
        if (dmgSource.getSource() == null) return;
        playerToSendTo.sendMessage(Text.translatable(translationKey, immortalPlayer.getName().getString(), dmgSource.getSource().getName().getString()));
    }

    private static float stopDeath(ServerPlayerEntity immortalPlayer) {
//Catching True Immortality in every Case, and also temporary Semi Immortality
        ImmortalityAdvancementGiver.giveImmortalityAchievements(immortalPlayer);
        //Prevent Death
        immortalPlayer.setHealth(immortalPlayer.getMaxHealth());
        return 0;
    }


    private static float handleEntityDeath(LivingEntity livingEntity, DamageSource dmgSource, float damageAmount) {
        if (livingEntity instanceof ImmortalWither immortalWither)
            return handleImmortalWitherDeath(immortalWither, dmgSource, damageAmount);
        //Not Affected by any Immortality Attributes
        return damageAmount;
    }

    private static boolean isKilledByBaneOfLife(LivingEntity killedEntity, DamageSource dmgSource) {
        boolean killedByPlayer = dmgSource.getSource() != null && dmgSource.getSource() != killedEntity && dmgSource.getSource().isPlayer();
        if (!killedByPlayer) {
            return false;
        }
        PlayerEntity attackingPlayer = (PlayerEntity) dmgSource.getSource();
        boolean attackerWeaponHasEnchantments = attackingPlayer.getMainHandStack().hasEnchantments();
        if (!attackerWeaponHasEnchantments) {
            return false;
        }
        boolean baneOfLifeEnchanted = EnchantmentHelper.getLevel(ImmortalityEnchants.Bane_Of_Life, attackingPlayer.getMainHandStack()) > 0;
        return baneOfLifeEnchanted;
    }


    private static float handleImmortalWitherDeath(ImmortalWither immortalWither, DamageSource dmgSource, float damageAmount) {
        //If Bane Of Life, it will count as Kill
        int immortalWitherDeaths = 0;
        if (isKilledByBaneOfLife(immortalWither, dmgSource) && dmgSource.getSource() instanceof ServerPlayerEntity attackingPlayer) {
            //Increases Immortal Deaths, and will set new Max Health from ImmortalityStatus
            immortalWitherDeaths = incrementGeneric(immortalWither, ImmortalityData.DataTypeInt.ImmortalDeaths);
            //Will give Liver on every Death
            World world = attackingPlayer.world;
            int x = (int) attackingPlayer.getX();
            int y = (int) attackingPlayer.getY();
            int z = (int) attackingPlayer.getZ();
            world.spawnEntity(new ItemEntity(world, x, y, z, new ItemStack(ImmortalityItems.LiverOfImmortality)));
        }
        if (immortalWitherDeaths < 3) { // Kill it 3 Times, and it won't be able to recover
            // Spawn Effects to give indication
            immortalWither.getWorld().playSoundFromEntity(null, immortalWither, SoundEvents.BLOCK_AMETHYST_CLUSTER_FALL, SoundCategory.HOSTILE, 5, 1);
            ((ServerWorld) immortalWither.getWorld()).spawnParticles(ParticleTypes.SOUL, immortalWither.getX(), immortalWither.getY(), immortalWither.getZ(), 64, 0, 5, 0, 1);
            immortalWither.setInvulTimer(220);
            return 0;
        }
        //Void Damage Immunity
        if (dmgSource == DamageSource.OUT_OF_WORLD)
            return 0;
        //Give Normal Damage
        return damageAmount;
    }

    private static boolean killedByImmortalWither(DamageSource damageSource) {
        boolean directlyByImmortalWither = damageSource.getSource() instanceof ImmortalWither;
        boolean byWitherSkullFromImmortalWither = damageSource.getSource() instanceof WitherSkullEntity witherSkullEntity && witherSkullEntity.getOwner() instanceof ImmortalWither;
        boolean killedByImmortalWither = directlyByImmortalWither || byWitherSkullFromImmortalWither;
        return killedByImmortalWither;
    }
}
