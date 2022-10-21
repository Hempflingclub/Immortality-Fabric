package net.hempflingclub.immortality.util;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.hempflingclub.immortality.enchantments.ImmortalityEnchants;
import net.hempflingclub.immortality.item.ImmortalityItems;
import net.hempflingclub.immortality.statuseffect.ModEffectRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

import java.util.Objects;

public class ImmortalityInvokeImmortality {
    public static float damageManager(LivingEntity entity, DamageSource dmgSource, float damageAmount) {
        if (entity.isPlayer()) {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            if (!entity.world.isClient
                    && (ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.getLiverImmortality(playerEntity) || ImmortalityStatus.isSemiImmortal(playerEntity) || ImmortalityStatus.isSemiImmortal(playerEntity))
                    && (entity.getHealth() - damageAmount) <= 0) {
                // This is Server, Player is Immortal and would've Died
                playerEntity.getWorld().playSoundFromEntity(null, playerEntity, SoundEvents.BLOCK_AMETHYST_CLUSTER_FALL, SoundCategory.PLAYERS, 5, 1);
                ((ServerWorld) playerEntity.getWorld()).spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), 64, 0, 5, 0, 1);
                if (playerEntity.getY() <= playerEntity.world.getBottomY() && dmgSource == DamageSource.OUT_OF_WORLD) {
                    //If in Void taking damage then Teleport to Spawnpoint/Bed of Player, When no Bed is found then yeet them to Overworld Spawn
                    FabricDimensions.teleport(playerEntity
                            , Objects.requireNonNull(playerEntity.world.getServer()).getWorld(((ServerPlayerEntity) playerEntity).getSpawnPointDimension())
                            , new TeleportTarget(
                                    Vec3d.of((((ServerPlayerEntity) playerEntity).getSpawnPointPosition()) == null ?
                                            Objects.requireNonNull(playerEntity.getWorld().getServer()).getOverworld().getSpawnPos() :
                                            (((ServerPlayerEntity) playerEntity).getSpawnPointPosition()))
                                    , Vec3d.ZERO, playerEntity.headYaw, playerEntity.getPitch()
                            ));
                    playerEntity.fallDistance = 0;
                } else if (dmgSource != DamageSource.OUT_OF_WORLD) {
                    //Extinguish and refill Air
                    playerEntity.setAir(playerEntity.getMaxAir());
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20, 0, false, false));
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1, false, false));
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 15 * 20, 2, false, false));
                    //Increase Immortals Death Counter, if DamageType is not Void Damage
                    if (!((ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.isTrueImmortal(playerEntity)) && ImmortalityStatus.isSemiImmortal(playerEntity))) {
                        ImmortalityStatus.incrementImmortalityDeath(playerEntity);
                    }
                    //Increase Death Counter in Statistics
                    playerEntity.incrementStat(Stats.DEATHS);
                    if (ImmortalityStatus.getImmortality(playerEntity) && !ImmortalityStatus.isSemiImmortal(playerEntity)) {
                        //If real Immortality not LiverImmortality then use Leveling Mechanic
                        //Add 1 Heart per Immortal Death
                        ImmortalityStatus.addImmortalityArmorT(playerEntity);
                        playerEntity.setHealth(playerEntity.getMaxHealth());
                        if (dmgSource.getSource() != null && dmgSource.getSource() != playerEntity && dmgSource.getSource().isPlayer()) {
                            PlayerEntity attackingPlayer = (PlayerEntity) dmgSource.getSource();
                            if (attackingPlayer.getMainHandStack().hasEnchantments()) {
                                if (EnchantmentHelper.getLevel(ImmortalityEnchants.Bane_Of_Life, attackingPlayer.getMainHandStack()) > 0) {
                                    //Killed By Bane Of Life
                                    ImmortalityStatus.setKilledByBaneOfLifeTime(playerEntity, ImmortalityStatus.getCurrentTime(playerEntity));
                                    ImmortalityStatus.incrementKilledByBaneOfLifeCount(playerEntity);
                                    if (ImmortalityStatus.getKilledByBaneOfLifeCount(playerEntity) >= 3) {
                                        if (!ImmortalityStatus.isSemiImmortal(playerEntity)) {
                                            ImmortalityStatus.setSemiImmortality(playerEntity, true);
                                        }
                                    }
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
                        }
                        if (ImmortalityData.getImmortalDeaths(ImmortalityStatus.getPlayerComponent(playerEntity)) == 25) {
                            playerEntity.giveItemStack(new ItemStack(ImmortalityItems.VoidHeart));
                            playerEntity.sendMessage(Text.translatable("immortality.status.trainedVoidHeart"), true);
                        }
                        if ((ImmortalityData.getImmortalDeaths(ImmortalityStatus.getPlayerComponent(playerEntity)) + 1) % 5 == 0 && ImmortalityData.getImmortalDeaths(ImmortalityStatus.getPlayerComponent(playerEntity)) < 50) {
                            ImmortalityStatus.addImmortalityArmor(playerEntity);
                            playerEntity.sendMessage(Text.translatable("immortality.status.skinHardened"), true);
                        }
                    } else if (ImmortalityStatus.getLiverImmortality(playerEntity) || ImmortalityStatus.isSemiImmortal(playerEntity)) {
                        //If LiverImmortality then use Degrading Mechanic
                        //Remove 1 Heart per Death
                        if (dmgSource.getSource() != null && dmgSource.getSource() != playerEntity && dmgSource.getSource().isPlayer()) {
                            PlayerEntity attackingPlayer = (PlayerEntity) dmgSource.getSource();
                            if (attackingPlayer.getMainHandStack().hasEnchantments()) {
                                if (EnchantmentHelper.getLevel(ImmortalityEnchants.Bane_Of_Life, attackingPlayer.getMainHandStack()) > 0) {
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
                            }
                        }
                        ImmortalityStatus.addNegativeHearts(playerEntity);
                        playerEntity.setHealth(playerEntity.getMaxHealth());
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
                            if (ImmortalityStatus.getLifeElixirDropTime(playerEntity) == 0 || ImmortalityStatus.getCurrentTime(playerEntity) >= (ImmortalityStatus.getLifeElixirDropTime(playerEntity) + 300 * 20)) {
                                ImmortalityStatus.setLifeElixirDropTime(playerEntity, ImmortalityStatus.getCurrentTime(playerEntity));
                                ItemEntity itemEntity = new ItemEntity(EntityType.ITEM, playerEntity.getWorld());
                                itemEntity.setPosition(playerEntity.getPos());
                                itemEntity.setStack(new ItemStack(ImmortalityItems.LifeElixir));
                                playerEntity.getWorld().spawnEntity(itemEntity);
                            }
                            if (!ImmortalityStatus.isSemiImmortal(playerEntity)) {
                                for (PlayerEntity player : Objects.requireNonNull(playerEntity.getServer()).getPlayerManager().getPlayerList()) {
                                    if (dmgSource.getSource() != null && dmgSource.getSource() != playerEntity) {
                                        player.sendMessage(Text.translatable("immortality.last.death.player", playerEntity.getName().getString(), dmgSource.getSource().getName().getString()));
                                    } else {
                                        player.sendMessage(Text.translatable("immortality.last.death", playerEntity.getName().getString()));
                                    }
                                }
                            } else {
                                for (PlayerEntity player : Objects.requireNonNull(playerEntity.getServer()).getPlayerManager().getPlayerList()) {
                                    if (dmgSource.getSource() != null && dmgSource.getSource() != playerEntity) {
                                        player.sendMessage(Text.translatable("immortality.immortal_slayed.death.player", playerEntity.getName().getString(), dmgSource.getSource().getName().getString()));
                                    } else {
                                        player.sendMessage(Text.translatable("immortality.immortal_slayed.death", playerEntity.getName().getString()));
                                    }
                                }
                                ImmortalityStatus.resetKilledByBaneOfLifeTime(playerEntity);
                                ImmortalityStatus.resetKilledByBaneOfLifeCount(playerEntity);
                                ImmortalityStatus.convertSemiImmortalityIntoOtherImmortality(playerEntity);
                            }
                            return damageAmount;
                        }
                    }
                }
                //Prevent Death
                playerEntity.setHealth(playerEntity.getMaxHealth());
                return 0;
            }
            //Can Survive Damage or ain't immortal
        }
        return damageAmount;
    }
}
