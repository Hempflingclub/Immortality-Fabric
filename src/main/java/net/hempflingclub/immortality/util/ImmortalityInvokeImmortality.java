package net.hempflingclub.immortality.util;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.hempflingclub.immortality.enchantments.ImmortalityEnchants;
import net.hempflingclub.immortality.entitys.ImmortalWither.ImmortalWither;
import net.hempflingclub.immortality.item.ImmortalityItems;
import net.hempflingclub.immortality.statuseffect.ModEffectRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.WolfEntity;
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

import java.util.Objects;

public final class ImmortalityInvokeImmortality {
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
            return damageAmount;
        }
    }

    private static float handlePlayerDeath(ServerPlayerEntity playerEntity, DamageSource dmgSource, float damageAmount) {
        if (ImmortalityStatus.hasTargetGiftedImmortal(playerEntity) && dmgSource.getSource() != null && dmgSource.getSource() != playerEntity) {
            LivingEntity immortalEntity = ImmortalityStatus.getTargetGiftedImmortalLivingEntity(playerEntity);
            handleSoulBoundTeleportAndAggression(immortalEntity, playerEntity, dmgSource);
        }
        if (playerShouldNotDie(playerEntity, dmgSource, damageAmount)) {
            return playerImmortalityHandling(playerEntity, dmgSource, damageAmount);
        } else if (dmgSource != null && dmgSource.getSource() instanceof WitherSkullEntity witherSkullEntity && witherSkullEntity.getOwner() instanceof ImmortalWither) {
            //Give appropriate damage, and apply ImmortalWither Damage
            return ((damageAmount) * ((playerEntity.getWorld().getDifficulty() == Difficulty.HARD || playerEntity.getWorld().getDifficulty() == Difficulty.NORMAL) ? witherDamageMultiplier : witherDamageMultiplierEasy));
        }
        //Can Survive Damage or ain't immortal
        return damageAmount;
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

    private static void handleSoulBoundTeleportAndAggression(LivingEntity soulBoundEntity, ServerPlayerEntity playerEntity, DamageSource dmgSource) {
        if (soulBoundEntity instanceof WolfEntity wolfEntity) {
            if (ImmortalityStatus.getSummonedTeleport(playerEntity) && wolfEntity.isTeammate(playerEntity)) {
                if (wolfEntity.getWorld() != playerEntity.getWorld() || wolfEntity.distanceTo(playerEntity) > 10) {
                    teleportSoulBoundToPlayer(wolfEntity, playerEntity);
                }
                wolfEntity.setAngryAt(Objects.requireNonNull(dmgSource.getSource()).getUuid());
                wolfEntity.chooseRandomAngerTime();
            }
        } else if (soulBoundEntity instanceof HostileEntity hostileEntity && Objects.requireNonNull(dmgSource.getSource()).isPlayer()) {
            if (ImmortalityStatus.getSummonedTeleport(playerEntity)) {
                if (hostileEntity.getWorld() != playerEntity.getWorld() || hostileEntity.distanceTo(playerEntity) > 10) {
                    teleportSoulBoundToPlayer(hostileEntity, playerEntity);
                }
                hostileEntity.setAttacking((PlayerEntity) dmgSource.getSource());
                hostileEntity.setAttacking(true);
            }
        }

    }

    private static void teleportSoulBoundToPlayer(LivingEntity soulBoundEntity, ServerPlayerEntity playerEntity) {
        soulBoundEntity.fallDistance = 0;
        FabricDimensions.teleport(soulBoundEntity, playerEntity.getWorld(), new TeleportTarget(playerEntity.getPos(), Vec3d.ZERO, soulBoundEntity.getYaw(), soulBoundEntity.getPitch()));
        ((ServerWorld) soulBoundEntity.getWorld()).spawnParticles(ParticleTypes.SOUL, soulBoundEntity.getX(), soulBoundEntity.getY(), soulBoundEntity.getZ(), 64, 0, 5, 0, 1);
    }

    private static boolean playerShouldNotDie(ServerPlayerEntity playerEntity, DamageSource dmgSource, float damageAmount) {
        boolean hasImmortality = (ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.getLiverImmortality(playerEntity) || ImmortalityStatus.isSemiImmortal(playerEntity) || ImmortalityStatus.isTrueImmortal(playerEntity));
        boolean wouldDie = (playerEntity.getHealth() - damageAmount) <= 0;
        boolean wouldDieToImmortalWither = (dmgSource != null && dmgSource.getSource() instanceof WitherSkullEntity witherSkullEntity &&
                witherSkullEntity.getOwner() instanceof ImmortalWither &&
                (playerEntity.getHealth() - ((damageAmount) * ((playerEntity.getWorld().getDifficulty() == Difficulty.HARD || playerEntity.getWorld().getDifficulty() == Difficulty.NORMAL) ? witherDamageMultiplier : witherDamageMultiplierEasy))) <= 0);
        boolean shouldNotDie = (hasImmortality && (wouldDie || wouldDieToImmortalWither));
        return shouldNotDie;
    }

    private static float playerImmortalityHandling(ServerPlayerEntity playerEntity, DamageSource dmgSource, float damageAmount) {
        // This is Server, Player is Immortal and would've Died
        if (playerEntity.getY() <= playerEntity.world.getBottomY() && dmgSource == DamageSource.OUT_OF_WORLD) {
            teleportPlayerToSpawnPoint(playerEntity);
        }
        if (dmgSource == DamageSource.OUT_OF_WORLD) {
            return 0;// Not counting OUT_OF_WORLD as a Death in any way
        }
        playerEntity.getWorld().playSoundFromEntity(null, playerEntity, SoundEvents.BLOCK_AMETHYST_CLUSTER_FALL, SoundCategory.PLAYERS, 5, 1);
        (playerEntity.getWorld()).spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), 64, 0, 5, 0, 1);
        //Extinguish and refill Air
        playerEntity.setAir(playerEntity.getMaxAir());
        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20, 0, false, false));
        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1, false, false));
        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 15 * 20, 2, true, true));
        //Increase Immortals Death Counter, if DamageType is not Void Damage
        boolean isKillableRealImmortal = (ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.isTrueImmortal(playerEntity)) && ImmortalityStatus.isSemiImmortal(playerEntity);
        boolean realImmortalNormalDeath = dmgSource != soulBoundDamageSource && (ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.isTrueImmortal(playerEntity));
        if (!isKillableRealImmortal && realImmortalNormalDeath) {
            ImmortalityStatus.incrementImmortalityDeath(playerEntity);
            //Immortals shouldn't be strengthened by SoulBound Deaths
            if (ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.isTrueImmortal(playerEntity)) {
                ImmortalityStatus.addImmortalityArmorT(playerEntity);
            }
        }
        //Increase Death Counter in Statistics
        playerEntity.incrementStat(Stats.DEATHS);
        killedByImmortalWither = killedByImmortalWither(dmgSource);
        boolean hasRealImmortality = !ImmortalityStatus.isSemiImmortal(playerEntity) && !ImmortalityStatus.getLiverImmortality(playerEntity);
        if (!hasRealImmortality) {
            //If LiverImmortality then use Degrading Mechanic
            //Remove 1 Heart per Death
            if (dmgSource.getSource() == null || dmgSource.getSource() == playerEntity) {
                ImmortalityStatus.addNegativeHearts(playerEntity);
                return 0;// Bow Suicide, or buggy DamageSource, so special handling
            }
            if (isKilledByBaneOfLife(playerEntity, dmgSource)) {
                PlayerEntity attackingPlayer = (PlayerEntity) dmgSource.getSource();
                ImmortalityStatus.setKilledByBaneOfLifeTime(playerEntity, ImmortalityStatus.getCurrentTime(playerEntity));
                ImmortalityStatus.incrementKilledByBaneOfLifeCount(playerEntity);
                ImmortalityStatus.addNegativeHearts(playerEntity); //Second Negative Hearts
                if (ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.getImmortality(playerEntity)) {
                    playerEntity.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.bane_of_life, 60 * 20, 0, true, true));
                    if (!ImmortalityData.getLiverExtracted(ImmortalityStatus.getPlayerComponent(playerEntity))) {
                        ImmortalityData.setLiverExtracted(ImmortalityStatus.getPlayerComponent(playerEntity), true);
                        ImmortalityData.setLiverExtractionTime(ImmortalityStatus.getPlayerComponent(playerEntity), ImmortalityStatus.getCurrentTime(playerEntity));
                        ImmortalityStatus.addRegrowingLiver(playerEntity);
                        playerEntity.sendMessage(Text.translatable("immortality.status.liver_removed_forcefully"), true);
                        attackingPlayer.giveItemStack(new ItemStack(ImmortalityItems.LiverOfImmortality));
                    }
                }
            }
            if (killedByImmortalWither) {
                ImmortalityStatus.setKilledByBaneOfLifeTime(playerEntity, ImmortalityStatus.getCurrentTime(playerEntity));
                ImmortalityStatus.incrementKilledByBaneOfLifeCount(playerEntity);
                ImmortalityStatus.addNegativeHearts(playerEntity);
                if (ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.getImmortality(playerEntity)) {
                    playerEntity.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.bane_of_life, 60 * 20, 0, true, true));
                }
            }
            if (ImmortalityStatus.isSemiImmortal(playerEntity)) {
                ImmortalityStatus.setSemiImmortalityLostHeartTime(playerEntity, ImmortalityStatus.getCurrentTime(playerEntity));
            }
            ImmortalityStatus.addNegativeHearts(playerEntity);
            if (playerEntity.getMaxHealth() < 2) {
                //0 Hearts then remove LiverImmortality
                if (ImmortalityStatus.getLiverImmortality(playerEntity)) {
                    ImmortalityStatus.removeFalseImmortality(playerEntity);
                }
                if (ImmortalityStatus.isSemiImmortal(playerEntity)) {
                    ImmortalityStatus.removeNegativeHearts(playerEntity);
                }
                for (PlayerEntity players : playerEntity.getWorld().getPlayers()) {
                    players.playSound(SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 1, 1);
                }
                boolean elixirRestoreTimePassed = ImmortalityStatus.getCurrentTime(playerEntity) >= (ImmortalityStatus.getLifeElixirDropTime(playerEntity) + 300 * 20);
                boolean shouldDropElixir = ImmortalityStatus.getLifeElixirDropTime(playerEntity) == 0 || elixirRestoreTimePassed;
                if (shouldDropElixir) {
                    ImmortalityStatus.setLifeElixirDropTime(playerEntity, ImmortalityStatus.getCurrentTime(playerEntity));
                    ItemEntity itemEntity = new ItemEntity(EntityType.ITEM, playerEntity.getWorld());
                    itemEntity.setPosition(playerEntity.getPos());
                    itemEntity.setStack(new ItemStack(ImmortalityItems.LifeElixir));
                    playerEntity.getWorld().spawnEntity(itemEntity);
                }
                sendDeathMessage(playerEntity, dmgSource);
                if (ImmortalityStatus.hasTargetGiftedImmortal(playerEntity)) {
                    if (ImmortalityStatus.getTargetGiftedImmortalLivingEntity(playerEntity) != null) {
                        Objects.requireNonNull(ImmortalityStatus.getTargetGiftedImmortalLivingEntity(playerEntity)).setHealth(1);
                        Objects.requireNonNull(ImmortalityStatus.getTargetGiftedImmortalLivingEntity(playerEntity)).damage(soulBoundDamageSource, 1000);
                    } else {
                        ImmortalityStatus.removeTargetGiftedImmortal(playerEntity);
                    }
                }
                playerEntity.setHealth(1); //Guaranteed Death, Just in Case
                return damageAmount + 1;
            }

            return stopDeath(playerEntity);
        }
//Because of stopDeath above only realImmortality will be run here
        realImmortalDeathHandling(playerEntity, dmgSource);//No Return necessary, as they will become Semi Immortal, in order to be killable
        return stopDeath(playerEntity);
    }

    private static void sendDeathMessage(ServerPlayerEntity immortalPlayer, DamageSource dmgSource) {
        boolean isFalseImmortal = ImmortalityStatus.getLiverImmortality(immortalPlayer);
        boolean isSemiImmortal = ImmortalityStatus.isSemiImmortal(immortalPlayer);
        boolean isImmortal = ImmortalityStatus.getImmortality(immortalPlayer);
        boolean isTrueImmortal = ImmortalityStatus.isTrueImmortal(immortalPlayer);
        boolean isImmortalOrTrueImmortal = isImmortal || isTrueImmortal; // Not actually needed just for readability
        boolean isPureSemiImmortal = isSemiImmortal && !isImmortal && !isTrueImmortal;
        String translationKeyByPlayer;
        String translationKeyNormal;

        ImmortalityStatus.resetKilledByBaneOfLifeTime(immortalPlayer);
        ImmortalityStatus.resetKilledByBaneOfLifeCount(immortalPlayer);

        translationKeyByPlayer =
                isFalseImmortal ? "immortality.last.death.player" :
                        isPureSemiImmortal ? "immortality.semiImmortal_slayed.death.player" :
                                isTrueImmortal ? "immortality.trueImmortal_slayed.death.player" :
                                        "immortality.immortal_slayed.death.player";// Must be normal Immortality
        translationKeyNormal =
                isFalseImmortal ? "immortality.last.death" :
                        isPureSemiImmortal ? "immortality.semiImmortal_slayed.death" :
                                isTrueImmortal ? "immortality.trueImmortal_slayed.death" :
                                        "immortality.immortal_slayed.death";// Must be normal Immortality
        sendMessageToAllPlayers(immortalPlayer, dmgSource, translationKeyByPlayer, translationKeyNormal);
        if (isImmortalOrTrueImmortal) {
            ImmortalityStatus.convertSemiImmortalityIntoOtherImmortality(immortalPlayer);
        }
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
        playerToSendTo.sendMessage(Text.translatable(translationKey, immortalPlayer.getName().getString(), Objects.requireNonNull(dmgSource.getSource()).getName().getString()));
    }

    private static float stopDeath(ServerPlayerEntity immortalPlayer) {
//Catching True Immortality in every Case, and also temporary Semi Immortality
        ImmortalityAdvancementGiver.giveImmortalityAchievements(immortalPlayer);
        //Prevent Death
        immortalPlayer.setHealth(immortalPlayer.getMaxHealth());
        return 0;
    }


    private static float handleEntityDeath(LivingEntity livingEntity, DamageSource dmgSource, float damageAmount) {
        if (ImmortalityStatus.hasTargetGiverImmortal(livingEntity)) {
            return handleImmortalEntityDeath(livingEntity, dmgSource, damageAmount);
        } else if (livingEntity instanceof ImmortalWither immortalWither) {
            return handleImmortalWitherDeath(immortalWither, dmgSource, damageAmount);
        }
        //Not Affected by any Immortality Attributes
        return damageAmount;
    }

    private static float handleImmortalEntityDeath(LivingEntity immortalEntity, DamageSource dmgSource, float damageAmount) {
        //Is SoulBound to Player
        if (ImmortalityStatus.getTargetGiverImmortalPlayerEntity(immortalEntity) == null) {
            return damageAmount;
        }
        //Player is Online
        PlayerEntity unspecifiedPlayerEntity = ImmortalityStatus.getTargetGiverImmortalPlayerEntity(immortalEntity);
        if (unspecifiedPlayerEntity.getWorld().isClient()) {
            return damageAmount;
        }
        ServerPlayerEntity giverImmortal = (ServerPlayerEntity) unspecifiedPlayerEntity;
        if (!ImmortalityStatus.hasTargetGiftedImmortal(giverImmortal) && ImmortalityStatus.getTargetGiftedImmortalLivingEntity(giverImmortal) != immortalEntity) { //Prevent Abuse of Multiple SoulBounds
            ImmortalityStatus.setTargetGiftedImmortal(giverImmortal, immortalEntity.getUuid());
            giverImmortal.sendMessage(Text.translatable("immortality.status.soulBond_restored"), true);
        }
        if (ImmortalityStatus.getTargetGiftedImmortalLivingEntity(giverImmortal) != immortalEntity) {
            return damageAmount;
        }
        if (!(ImmortalityStatus.getImmortality(giverImmortal) || ImmortalityStatus.isTrueImmortal(giverImmortal) || ImmortalityStatus.isSemiImmortal(giverImmortal) || ImmortalityStatus.getLiverImmortality(giverImmortal))) {
            //Player is no longer Immortal
            return damageAmount;
        }
        //Check if Killed by Bane Of Life
        if (isKilledByBaneOfLife(immortalEntity, dmgSource)) {
            PlayerEntity attackingPlayer = (PlayerEntity) dmgSource.getSource();
            //Killed By Bane Of Life
            assert attackingPlayer != null;
            giverImmortal.sendMessage(Text.translatable("immortality.soulBound_killed_with_baneOfLife", Objects.requireNonNull(immortalEntity.getCustomName()).getString(), attackingPlayer.getName().getString()));
            giverImmortal.setHealth(1);
            giverImmortal.damage(soulBoundDamageSource, 1000);
            return damageAmount;
        }
        if (immortalEntity.getY() <= immortalEntity.world.getBottomY() && dmgSource == DamageSource.OUT_OF_WORLD) {
            //If in Void taking damage then Teleport to Spawnpoint/Bed of Player, When no Bed is found then yeet them to Overworld Spawn
            teleportSoulBoundToPlayer(immortalEntity, giverImmortal);
        } else if (dmgSource != DamageSource.OUT_OF_WORLD) {
            if (immortalEntity.isOnFire()) {
                immortalEntity.extinguish();
            }
            giverImmortal.setHealth(1);
            giverImmortal.damage(soulBoundDamageSource, 1000);
            immortalEntity.getWorld().playSoundFromEntity(null, immortalEntity, SoundEvents.BLOCK_AMETHYST_CLUSTER_FALL, SoundCategory.NEUTRAL, 5, 1);
            ((ServerWorld) immortalEntity.getWorld()).spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, immortalEntity.getX(), immortalEntity.getY(), immortalEntity.getZ(), 64, 0, 5, 0, 1);
            immortalEntity.setAir(immortalEntity.getMaxAir());
            immortalEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20, 0, false, false));
            immortalEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1, false, false));
            immortalEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 15 * 20, 2, true, true));
        }
        immortalEntity.setHealth(immortalEntity.getMaxHealth());
        return 0;
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
        //If Bane Of Life, it will count as 2 Kills
        if (dmgSource.getSource() != null && dmgSource.getSource() != immortalWither && dmgSource.getSource().isPlayer()) {
            PlayerEntity attackingPlayer = (PlayerEntity) dmgSource.getSource();
            if (attackingPlayer.getMainHandStack().hasEnchantments()) {
                if (EnchantmentHelper.getLevel(ImmortalityEnchants.Bane_Of_Life, attackingPlayer.getMainHandStack()) > 0) {
                    ImmortalityStatus.incrementImmortalWitherDeaths(immortalWither);
                    //Will give Liver on every Death
                    attackingPlayer.giveItemStack(new ItemStack(ImmortalityItems.LiverOfImmortality));
                }
            }
        }
        ImmortalityStatus.incrementImmortalWitherDeaths(immortalWither);
        if (ImmortalityStatus.getImmortalWitherDeaths(immortalWither) < 5) { // Kill it 5 Times, and it won't be able to recover
            float lostLifePercent = (1.0F * ImmortalityStatus.getImmortalWitherDeaths(immortalWither)) / 5; // 0.2f -> 1.0f
            immortalWither.setHealth(immortalWither.getMaxHealth() * (1 - lostLifePercent));
            immortalWither.getWorld().playSoundFromEntity(null, immortalWither, SoundEvents.BLOCK_AMETHYST_CLUSTER_FALL, SoundCategory.HOSTILE, 5, 1);
            ((ServerWorld) immortalWither.getWorld()).spawnParticles(ParticleTypes.SOUL, immortalWither.getX(), immortalWither.getY(), immortalWither.getZ(), 64, 0, 5, 0, 1);
            immortalWither.setInvulTimer(220);
            return 0;
        } else if (dmgSource != DamageSource.OUT_OF_WORLD) {
            return damageAmount;
        } else {
            return 0;
        }
    }

    private static boolean killedByImmortalWither(DamageSource damageSource) {
        boolean directlyByImmortalWither = damageSource.getSource() instanceof ImmortalWither;
        boolean byWitherSkullFromImmortalWither = damageSource.getSource() instanceof WitherSkullEntity witherSkullEntity && witherSkullEntity.getOwner() instanceof ImmortalWither;
        boolean killedByImmortalWither = directlyByImmortalWither || byWitherSkullFromImmortalWither;
        return killedByImmortalWither;
    }

    private static void realImmortalDeathHandling(ServerPlayerEntity immortalPlayer, DamageSource damageSource) {
        //If real Immortality not LiverImmortality then use Leveling Mechanic
        if (damageSource.getSource() != null && damageSource.getSource() != immortalPlayer) {
            if (damageSource.getSource().isPlayer()) {
                PlayerEntity attackingPlayer = (PlayerEntity) damageSource.getSource();
                if (attackingPlayer.getMainHandStack().hasEnchantments()) {
                    if (EnchantmentHelper.getLevel(ImmortalityEnchants.Bane_Of_Life, attackingPlayer.getMainHandStack()) > 0) {
                        //Killed By Bane Of Life
                        ImmortalityStatus.setKilledByBaneOfLifeTime(immortalPlayer, ImmortalityStatus.getCurrentTime(immortalPlayer));
                        ImmortalityStatus.incrementKilledByBaneOfLifeCount(immortalPlayer);
                        if (ImmortalityStatus.getKilledByBaneOfLifeCount(immortalPlayer) >= 3) {
                            if (!ImmortalityStatus.isSemiImmortal(immortalPlayer)) {
                                ImmortalityStatus.setSemiImmortality(immortalPlayer, true);
                            }
                        }
                        immortalPlayer.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.bane_of_life, 60 * 20, 0, true, true));
                        if (!ImmortalityData.getLiverExtracted(ImmortalityStatus.getPlayerComponent(immortalPlayer))) {
                            ImmortalityData.setLiverExtracted(ImmortalityStatus.getPlayerComponent(immortalPlayer), true);
                            ImmortalityData.setLiverExtractionTime(ImmortalityStatus.getPlayerComponent(immortalPlayer), ImmortalityStatus.getCurrentTime(immortalPlayer));
                            ImmortalityStatus.addRegrowingLiver(immortalPlayer);
                            immortalPlayer.sendMessage(Text.translatable("immortality.status.liver_removed_forcefully"), true);
                            attackingPlayer.giveItemStack(new ItemStack(ImmortalityItems.LiverOfImmortality));
                        }
                    }
                }
            } else if (killedByImmortalWither) {
                ImmortalityStatus.setKilledByBaneOfLifeTime(immortalPlayer, ImmortalityStatus.getCurrentTime(immortalPlayer));
                ImmortalityStatus.incrementKilledByBaneOfLifeCount(immortalPlayer);
                if (ImmortalityStatus.getKilledByBaneOfLifeCount(immortalPlayer) >= 3) {
                    if (!ImmortalityStatus.isSemiImmortal(immortalPlayer)) {
                        ImmortalityStatus.setSemiImmortality(immortalPlayer, true);
                    }
                }
                immortalPlayer.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.bane_of_life, 60 * 20, 0, true, true));
            }
        }
        if (damageSource != soulBoundDamageSource && (ImmortalityData.getImmortalDeaths(ImmortalityStatus.getPlayerComponent(immortalPlayer)) + 1) % 5 == 0 && ImmortalityStatus.getAppliedBonusArmor(immortalPlayer) < ImmortalityStatus.immortalityBaseArmor * 10) {
            ImmortalityStatus.addImmortalityArmor(immortalPlayer);
            immortalPlayer.sendMessage(Text.translatable("immortality.status.skinHardened"), true);
        }
    }
}
