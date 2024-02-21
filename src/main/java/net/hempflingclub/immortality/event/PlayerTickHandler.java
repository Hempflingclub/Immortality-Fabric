package net.hempflingclub.immortality.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.hempflingclub.immortality.util.ImmortalityData;
import net.hempflingclub.immortality.util.ImmortalityData.DataTypeInt;
import net.hempflingclub.immortality.util.ImmortalityStatus;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.include.com.google.common.collect.Iterables;

import java.util.ArrayList;

public class PlayerTickHandler implements ServerTickEvents.StartTick {
    public static MinecraftServer minecraftServer;
    long counter = 0;

    @Override
    public void onStartTick(MinecraftServer server) {
        if (++counter % 20 == 0) {
            //int currentTime = ImmortalityStatus.getCurrentTime(server);
            minecraftServer = server;
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                //Every 5sec
                if (counter % 100 == 0) {
                    //Reducing All every Cooldown by 5sec (except at 0)
                    {
                        ArrayList<DataTypeInt> dataTypesCooldown = new ArrayList<>();
                        dataTypesCooldown.add(DataTypeInt.GammaImmortalityHeartCooldownSeconds);
                        dataTypesCooldown.add(DataTypeInt.LiverExtractionCooldownSeconds);
                        dataTypesCooldown.add(DataTypeInt.LifeElixirDropCooldownSeconds);
                        dataTypesCooldown.add(DataTypeInt.LifeElixirCooldownSeconds);
                        for (DataTypeInt dataTypeCooldown : dataTypesCooldown) {
                            int secondsToReduce = 5;
                            int currentCooldownSeconds = ImmortalityStatus.getInt(player, dataTypeCooldown);
                            //Not do anything on 0 sec Cooldown
                            if (currentCooldownSeconds == 0) continue;
                            //Making sure to not hit 0 by itself
                            if (currentCooldownSeconds - secondsToReduce == 0) secondsToReduce--;
                            //Reduce by 5 (6) seconds
                            ImmortalityStatus.addGeneric(player, dataTypeCooldown, -secondsToReduce);
                        }
                    }
                    //Alpha Immortal Item Healing Effect
                    if (ImmortalityStatus.getBool(player, ImmortalityData.DataTypeBool.AlphaImmortality)) {
                        Iterable<ItemStack> inventory = Iterables.concat(player.getArmorItems(), player.getHandItems());
                        for (ItemStack item : inventory)
                            if (item.isDamaged()) {
                                //Don't give more than full repair
                                int newDamage = Math.max(0, item.getDamage() - 5);
                                item.setDamage(newDamage);
                                //Only repair 1 Damage/sec, and for at most 1 Item
                                break;
                            }
                    }

                }
                //Every 30 sec
                if (counter % 600 == 0) {
                    //Check every state of general Logic for Player
                    ImmortalityStatus.specificAllLogicApplier(player);
                    //Apply Void Heart + boost per Immortality Stage
                    if (ImmortalityStatus.getBool(player, ImmortalityData.DataTypeBool.VoidHeart)) {
                        int foodRegen = 1;
                        if (ImmortalityStatus.getBool(player, ImmortalityData.DataTypeBool.DeltaImmortality))
                            foodRegen += 1;
                        else if (ImmortalityStatus.getBool(player, ImmortalityData.DataTypeBool.GammaImmortality))
                            foodRegen += 2;
                        else if (ImmortalityStatus.getBool(player, ImmortalityData.DataTypeBool.BetaImmortality))
                            foodRegen += 3;
                        else if (ImmortalityStatus.getBool(player, ImmortalityData.DataTypeBool.AlphaImmortality))
                            foodRegen += 4;
                        player.getHungerManager().add(foodRegen, foodRegen);
                    }
                }
            }
        }
    }
}

