package net.hempflingclub.immortality.util;

import net.hempflingclub.immortality.event.PlayerTickHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.*;

public final class ImmortalityStatus {
    public static final MinecraftServer minecraftServer = PlayerTickHandler.minecraftServer;
    public static final int immortalityHeartsHealthAddition = 2;
    public static final int negativeImmortalityHeartsHealthAddition = -1 * immortalityHeartsHealthAddition;
    public static final int regrowingImmortalityLiverHealthAddition = -10;
    public static final int immortalityHardeningArmorToughnessAddition = 1;
    public static final int immortalityBaseArmorAddition = 1;
    public static final int lifeElixirHealthAddition = 2;

    public static int incrementGeneric(IImmortalityComponent something, ImmortalityData.DataTypeInt dataTypeInt) {
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(something, dataTypeInt);
        return incrementGeneric(dataTypes);
    }

    public static int incrementGeneric(ImmortalityData.DataTypes dataTypes) {
        return addGeneric(dataTypes, 1);
    }

    public static boolean toggleGeneric(IImmortalityComponent something, ImmortalityData.DataTypeBool dataTypeBool) {
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(something, dataTypeBool);
        return toggleGeneric(dataTypes);
    }

    public static boolean toggleGeneric(ImmortalityData.DataTypes dataTypes) {
        boolean curValue = getBool(dataTypes);
        return dataTypes.set(!curValue);
    }

    public static int getInt(IImmortalityComponent something, ImmortalityData.DataTypeInt dataTypeInt) {
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(something, dataTypeInt);
        return getInt(dataTypes);
    }

    public static int getInt(ImmortalityData.DataTypes dataTypes) {
        return dataTypes.readInt();
    }

    public static boolean getBool(IImmortalityComponent something, ImmortalityData.DataTypeBool dataTypeBool) {
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(something, dataTypeBool);
        return getBool(dataTypes);
    }

    public static boolean getBool(ImmortalityData.DataTypes dataTypes) {
        return dataTypes.readBool();
    }

    public static int addGeneric(IImmortalityComponent something, ImmortalityData.DataTypeInt dataTypeInt, int addition) {
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(something, dataTypeInt);
        return addGeneric(dataTypes, addition);
    }

    public static int addGeneric(ImmortalityData.DataTypes dataTypes, int addition) {
        int curValue = getInt(dataTypes);
        return dataTypes.set(curValue + addition);
    }

    public static void logicApplier(IImmortalityComponent something, ImmortalityData.DataType dataType) {
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(something, dataType);
        logicApplier(dataTypes);
    }

    public static void logicApplier(ImmortalityData.DataTypes dataTypes) {
        //Loop over all Entities until found matching component, and then run logic to reapply fitting status
        IImmortalityComponent immortalityComponent = dataTypes.getIImmortalityComponent();
        if (immortalityComponent instanceof IImmortalityPlayerComponent iImmortalityPlayerComponent) {
            ServerPlayerEntity serverPlayerEntity = findPlayer(iImmortalityPlayerComponent);
            specificLogicApplier(serverPlayerEntity);
        } else if (immortalityComponent instanceof IImmortalityLivingEntityComponent iImmortalityLivingEntityComponent) {
            LivingEntity livingEntity = findEntity(iImmortalityLivingEntityComponent);
            specificLogicApplier(livingEntity);
        } else if (immortalityComponent instanceof IImmortalityItemComponent iImmortalityItemComponent) {
            ItemStack itemStack = findItemStack(iImmortalityItemComponent); //TODO: Probably also search in BlockEntity of Soul Urn
            specificLogicApplier(itemStack);
        }
    }

    private static ServerPlayerEntity findPlayer(IImmortalityPlayerComponent iImmortalityPlayerComponent) {
        Iterable<ServerWorld> serverWorlds = minecraftServer.getWorlds();
        ServerPlayerEntity target = null;
        for (ServerWorld serverWorld : serverWorlds)
            for (ServerPlayerEntity serverPlayerEntity : serverWorld.getPlayers()) {
                IImmortalityPlayerComponent newComponent = getPlayerComponent(serverPlayerEntity);
                if (iImmortalityPlayerComponent.equals(newComponent)) {
                    target = serverPlayerEntity;
                    return target;
                }
            }
        return target;
    }

    private static LivingEntity findEntity(IImmortalityLivingEntityComponent iImmortalityLivingEntityComponent) {
        Iterable<ServerWorld> serverWorlds = minecraftServer.getWorlds();
        LivingEntity target = null;
        for (ServerWorld serverWorld : serverWorlds)
            for (Entity entity : serverWorld.iterateEntities()) {
                if (!(entity instanceof LivingEntity livingEntity)) continue;
                IImmortalityLivingEntityComponent newComponent = getLivingEntityComponent(livingEntity);
                if (iImmortalityLivingEntityComponent.equals(newComponent)) {
                    target = livingEntity;
                    return target;
                }
            }
        return target;
    }

    private static ItemStack findItemStack(IImmortalityItemComponent iImmortalityItemComponent) {
        Iterable<ServerWorld> serverWorlds = minecraftServer.getWorlds();
        ItemStack target = null;
        final int MAX_INVENTORY_SLOT = 45;
        for (ServerWorld serverWorld : serverWorlds)
            for (ServerPlayerEntity serverPlayerEntity : serverWorld.getPlayers()) {
                Inventory inventory = serverPlayerEntity.getInventory();
                for (int slot = 0; slot <= MAX_INVENTORY_SLOT; slot++) {
                    ItemStack itemStack = inventory.getStack(slot);
                    IImmortalityItemComponent newComponent = getItemComponent(itemStack);
                    if (iImmortalityItemComponent.equals(newComponent)) {
                        target = itemStack;
                        return target;
                    }
                }
            }
        return target;
    }

    private static void specificLogicApplier(ServerPlayerEntity serverPlayerEntity) {
        //TODO:
    }

    private static void specificLogicApplier(LivingEntity livingEntity) {
        //TODO:
    }

    private static void specificLogicApplier(ItemStack itemStack) {
        //TODO:
    }

    /**
     * Removes Immortality / False Immortality / Void Heart / Immortality Hearts / regrowingImmortalityLiver / negativeImmortalityHearts and resets Immortality Deaths
     *
     * @param playerEntity you can Typecast to this using (playerEntity) however please check if player by playerEntity.isPlayer() before
     */
    public static void removeEverything(PlayerEntity playerEntity) {//TODO:
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        ImmortalityData.setImmortality(getPlayerComponent(playerEntity), false);
        ImmortalityData.setLiverImmortality(getPlayerComponent(playerEntity), false);
        ImmortalityData.setVoidHeart(getPlayerComponent(playerEntity), false);
        setSemiImmortality(playerEntity, false);
        resetImmortalityDeath(playerEntity);
        resetExtractedLivers(playerEntity);
        removeImmortalityArmorT(playerEntity);
        removeImmortalityArmor(playerEntity);
        for (EntityAttributeModifier entityModifier : maxHealth.getModifiers()) {
            if (entityModifier.getName().equals("immortalityHearts")) {
                maxHealth.removeModifier(entityModifier);
                setLiverHearts(playerEntity, getLiverHearts(playerEntity) - immortalityHearts);
            } else if (entityModifier.getName().equals("regrowingImmortalityLiver")) {
                maxHealth.removeModifier(entityModifier);
            } else if (entityModifier.getName().equals("negativeImmortalityHearts")) {
                maxHealth.removeModifier(entityModifier);
                setLostHearts(playerEntity, getLostHearts(playerEntity) - immortalityHearts);
            }
        }
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static IImmortalityPlayerComponent getPlayerComponent(PlayerEntity playerEntity) {
        return IImmortalityPlayerComponent.KEY.get(playerEntity);
    }

    public static int getCurrentTime(PlayerEntity playerEntity) {
        return (int) Objects.requireNonNull(playerEntity.getServer()).getOverworld().getTime();
    }

    public static int getCurrentTime(MinecraftServer server) {
        return (int) server.getOverworld().getTime();
    }

    public static IImmortalityLivingEntityComponent getLivingEntityComponent(LivingEntity livingEntity) {
        return IImmortalityLivingEntityComponent.KEY.get(livingEntity);
    }

    public static IImmortalityItemComponent getItemComponent(ItemStack itemStack) {
        return IImmortalityItemComponent.KEY.get(itemStack);
    }

}
