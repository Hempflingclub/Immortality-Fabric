package net.hempflingclub.immortality.util;

import net.hempflingclub.immortality.entitys.ImmortalWither.ImmortalWither;
import net.hempflingclub.immortality.event.PlayerTickHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
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

    public static int incrementGeneric(ServerPlayerEntity serverPlayerEntity, ImmortalityData.DataTypeInt dataTypeInt) {
        IImmortalityPlayerComponent iImmortalityPlayerComponent = getComponent(serverPlayerEntity);
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(iImmortalityPlayerComponent, dataTypeInt);
        int newValue = incrementGeneric(dataTypes);
        specificLogicApplier(serverPlayerEntity);
        return newValue;
    }

    public static int incrementGeneric(LivingEntity livingEntity, ImmortalityData.DataTypeInt dataTypeInt) {
        IImmortalityLivingEntityComponent iImmortalityLivingEntityComponent = getComponent(livingEntity);
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(iImmortalityLivingEntityComponent, dataTypeInt);
        int newValue = incrementGeneric(dataTypes);
        specificLogicApplier(livingEntity);
        return newValue;
    }

    public static int incrementGeneric(ItemStack itemStack, ImmortalityData.DataTypeInt dataTypeInt) {
        IImmortalityItemComponent iImmortalityItemComponent = getComponent(itemStack);
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(iImmortalityItemComponent, dataTypeInt);
        int newValue = incrementGeneric(dataTypes);
        specificLogicApplier(itemStack);
        return newValue;
    }

    public static int incrementGeneric(IImmortalityComponent something, ImmortalityData.DataTypeInt dataTypeInt) {
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(something, dataTypeInt);
        int newValue = incrementGeneric(dataTypes);
        logicApplier(something, dataTypeInt);
        return newValue;
    }

    private static int incrementGeneric(ImmortalityData.DataTypes dataTypes) {
        return addGeneric(dataTypes, 1);
    }

    public static boolean toggleGeneric(ServerPlayerEntity serverPlayerEntity, ImmortalityData.DataTypeBool dataTypeBool) {
        IImmortalityPlayerComponent iImmortalityPlayerComponent = getComponent(serverPlayerEntity);
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(iImmortalityPlayerComponent, dataTypeBool);
        boolean newValue = toggleGeneric(dataTypes);
        specificLogicApplier(serverPlayerEntity);
        return newValue;
    }

    public static boolean toggleGeneric(LivingEntity livingEntity, ImmortalityData.DataTypeBool dataTypeBool) {
        IImmortalityLivingEntityComponent iImmortalityLivingEntityComponent = getComponent(livingEntity);
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(iImmortalityLivingEntityComponent, dataTypeBool);
        boolean newValue = toggleGeneric(dataTypes);
        specificLogicApplier(livingEntity);
        return newValue;
    }

    public static boolean toggleGeneric(ItemStack itemStack, ImmortalityData.DataTypeBool dataTypeBool) {
        IImmortalityItemComponent iImmortalityItemComponent = getComponent(itemStack);
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(iImmortalityItemComponent, dataTypeBool);
        boolean newValue = toggleGeneric(dataTypes);
        specificLogicApplier(itemStack);
        return newValue;
    }

    public static boolean toggleGeneric(IImmortalityComponent something, ImmortalityData.DataTypeBool dataTypeBool) {
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(something, dataTypeBool);
        boolean newValue = toggleGeneric(dataTypes);
        logicApplier(something, dataTypeBool);
        return newValue;
    }

    private static boolean toggleGeneric(ImmortalityData.DataTypes dataTypes) {
        boolean curValue = getBool(dataTypes);
        return dataTypes.set(!curValue);
    }

    public static int getInt(ServerPlayerEntity serverPlayerEntity, ImmortalityData.DataTypeInt dataTypeInt) {
        IImmortalityComponent immortalityComponent = getComponent(serverPlayerEntity);
        return getInt(immortalityComponent, dataTypeInt);
    }

    public static int getInt(LivingEntity livingEntity, ImmortalityData.DataTypeInt dataTypeInt) {
        IImmortalityComponent immortalityComponent = getComponent(livingEntity);
        return getInt(immortalityComponent, dataTypeInt);
    }

    public static int getInt(ItemStack itemStack, ImmortalityData.DataTypeInt dataTypeInt) {
        IImmortalityComponent immortalityComponent = getComponent(itemStack);
        return getInt(immortalityComponent, dataTypeInt);
    }

    public static int getInt(IImmortalityComponent something, ImmortalityData.DataTypeInt dataTypeInt) {
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(something, dataTypeInt);
        return getInt(dataTypes);
    }

    private static int getInt(ImmortalityData.DataTypes dataTypes) {
        return dataTypes.readInt();
    }

    public static boolean getBool(ServerPlayerEntity serverPlayerEntity, ImmortalityData.DataTypeBool dataTypeBool) {
        IImmortalityComponent immortalityComponent = getComponent(serverPlayerEntity);
        return getBool(immortalityComponent, dataTypeBool);
    }

    public static boolean getBool(LivingEntity livingEntity, ImmortalityData.DataTypeBool dataTypeBool) {
        IImmortalityComponent immortalityComponent = getComponent(livingEntity);
        return getBool(immortalityComponent, dataTypeBool);
    }

    public static boolean getBool(ItemStack itemStack, ImmortalityData.DataTypeBool dataTypeBool) {
        IImmortalityComponent immortalityComponent = getComponent(itemStack);
        return getBool(immortalityComponent, dataTypeBool);
    }

    public static boolean getBool(IImmortalityComponent something, ImmortalityData.DataTypeBool dataTypeBool) {
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(something, dataTypeBool);
        return getBool(dataTypes);
    }

    private static boolean getBool(ImmortalityData.DataTypes dataTypes) {
        return dataTypes.readBool();
    }

    public static int addGeneric(ServerPlayerEntity serverPlayerEntity, ImmortalityData.DataTypeInt dataTypeInt, int addition) {
        IImmortalityPlayerComponent iImmortalityPlayerComponent = getComponent(serverPlayerEntity);
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(iImmortalityPlayerComponent, dataTypeInt);
        int newValue = addGeneric(dataTypes, addition);
        specificLogicApplier(serverPlayerEntity);
        return newValue;
    }

    public static int addGeneric(LivingEntity livingEntity, ImmortalityData.DataTypeInt dataTypeInt, int addition) {
        IImmortalityLivingEntityComponent iImmortalityLivingEntityComponent = getComponent(livingEntity);
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(iImmortalityLivingEntityComponent, dataTypeInt);
        int newValue = addGeneric(dataTypes, addition);
        specificLogicApplier(livingEntity);
        return newValue;
    }

    public static int addGeneric(ItemStack itemStack, ImmortalityData.DataTypeInt dataTypeInt, int addition) {
        IImmortalityItemComponent iImmortalityItemComponent = getComponent(itemStack);
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(iImmortalityItemComponent, dataTypeInt);
        int newValue = addGeneric(dataTypes, addition);
        specificLogicApplier(itemStack);
        return newValue;
    }

    public static int addGeneric(IImmortalityComponent something, ImmortalityData.DataTypeInt dataTypeInt, int addition) {
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(something, dataTypeInt);
        int newValue = addGeneric(dataTypes, addition);
        logicApplier(something, dataTypeInt);
        return newValue;
    }

    private static int addGeneric(ImmortalityData.DataTypes dataTypes, int addition) {
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
                IImmortalityPlayerComponent newComponent = getComponent(serverPlayerEntity);
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
                IImmortalityLivingEntityComponent newComponent = getComponent(livingEntity);
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
                    IImmortalityItemComponent newComponent = getComponent(itemStack);
                    if (iImmortalityItemComponent.equals(newComponent)) {
                        target = itemStack;
                        return target;
                    }
                }
            }
        return target;
    }

    public static void specificLogicApplier(ServerPlayerEntity serverPlayerEntity) {
        serverPlayerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
        IImmortalityPlayerComponent iImmortalityPlayerComponent = getComponent(serverPlayerEntity);
        for (ImmortalityData.DataTypeInt dataTypeInt : ImmortalityData.DataTypeInt.values())
            new SpecificLogicApplier(serverPlayerEntity, dataTypeInt);
        for (ImmortalityData.DataTypeBool dataTypeBool : ImmortalityData.DataTypeBool.values())
            new SpecificLogicApplier(serverPlayerEntity, dataTypeBool);
    }

    public static void specificLogicApplier(LivingEntity livingEntity) {
        livingEntity.syncComponent(IImmortalityLivingEntityComponent.KEY);
        IImmortalityLivingEntityComponent iImmortalityLivingEntityComponent = getComponent(livingEntity);
        for (ImmortalityData.DataTypeInt dataTypeInt : ImmortalityData.DataTypeInt.values())
            new SpecificLogicApplier(livingEntity, dataTypeInt);
        for (ImmortalityData.DataTypeBool dataTypeBool : ImmortalityData.DataTypeBool.values())
            new SpecificLogicApplier(livingEntity, dataTypeBool);
    }

    public static void specificLogicApplier(ItemStack itemStack) {
        //itemStack.syncComponent(IImmortalityItemComponent.KEY); // Sadly this cannot be done, but should probably be fine
        IImmortalityItemComponent iImmortalityItemComponent = getComponent(itemStack);
        for (ImmortalityData.DataTypeInt dataTypeInt : ImmortalityData.DataTypeInt.values())
            new SpecificLogicApplier(itemStack, dataTypeInt);
        for (ImmortalityData.DataTypeBool dataTypeBool : ImmortalityData.DataTypeBool.values())
            new SpecificLogicApplier(itemStack, dataTypeBool);
    }

    /**
     * Removes Immortality / False Immortality / Void Heart / Immortality Hearts / regrowingImmortalityLiver / negativeImmortalityHearts and resets Immortality Deaths
     *
     * @param serverPlayerEntity the player to lose it all
     */
    public static void removeEverything(ServerPlayerEntity serverPlayerEntity) {
        IImmortalityPlayerComponent iImmortalityPlayerComponent = getComponent(serverPlayerEntity);
        for (ImmortalityData.DataTypeBool dataTypeBool : ImmortalityData.DataTypeBool.values())
            new ImmortalityData.DataTypes(iImmortalityPlayerComponent, dataTypeBool, false); //Will set all possible states to false
        for (ImmortalityData.DataTypeInt dataTypeInt : ImmortalityData.DataTypeInt.values())
            new ImmortalityData.DataTypes(iImmortalityPlayerComponent, dataTypeInt, 0); //Will set all possible states to 0
        serverPlayerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void removeEverythingExcept(ServerPlayerEntity serverPlayerEntity, ImmortalityData.DataType... dataType) {
        IImmortalityPlayerComponent iImmortalityPlayerComponent = getComponent(serverPlayerEntity);
        ArrayList<ImmortalityData.DataType> dataTypesExceptions = new ArrayList<ImmortalityData.DataType>(Arrays.asList(dataType));
        for (ImmortalityData.DataTypeBool dataTypeBool : ImmortalityData.DataTypeBool.values())
            if (!dataTypesExceptions.contains(dataTypeBool))
                new ImmortalityData.DataTypes(iImmortalityPlayerComponent, dataTypeBool, false); //Will set all possible states except provided exceptions to false

        for (ImmortalityData.DataTypeInt dataTypeInt : ImmortalityData.DataTypeInt.values())
            if (!dataTypesExceptions.contains(dataTypeInt))
                new ImmortalityData.DataTypes(iImmortalityPlayerComponent, dataTypeInt, 0); //Will set all possible states except provided exceptions to 0
        serverPlayerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static IImmortalityPlayerComponent getComponent(PlayerEntity playerEntity) {
        return IImmortalityPlayerComponent.KEY.get(playerEntity);
    }

    public static int getCurrentTime(PlayerEntity playerEntity) {
        return (int) Objects.requireNonNull(playerEntity.getServer()).getOverworld().getTime();
    }

    public static int getCurrentTime(MinecraftServer server) {
        return (int) server.getOverworld().getTime();
    }

    public static IImmortalityLivingEntityComponent getComponent(LivingEntity livingEntity) {
        return IImmortalityLivingEntityComponent.KEY.get(livingEntity);
    }

    public static IImmortalityItemComponent getComponent(ItemStack itemStack) {
        return IImmortalityItemComponent.KEY.get(itemStack);
    }

    private enum isType {
        ServerPlayerEntity,
        LivingEntity,
        ItemStack
    }

    private static class SpecificLogicApplier {
        isType isType;
        ServerPlayerEntity serverPlayerEntity;
        LivingEntity livingEntity;
        ItemStack itemStack;
        IImmortalityComponent immortalityComponent;
        ImmortalityData.DataType dataType;
        ImmortalityData.DataTypes dataTypes;
        int valueInt;
        boolean valueBool;

        private SpecificLogicApplier(ServerPlayerEntity serverPlayerEntity, ImmortalityData.DataType dataType) {
            this.serverPlayerEntity = serverPlayerEntity;
            this.dataType = dataType;
            this.immortalityComponent = getComponent(serverPlayerEntity);
            this.isType = ImmortalityStatus.isType.ServerPlayerEntity;
            doLogic();
        }

        private SpecificLogicApplier(LivingEntity livingEntity, ImmortalityData.DataType dataType) {
            this.livingEntity = livingEntity;
            this.dataType = dataType;
            this.immortalityComponent = getComponent(livingEntity);
            this.isType = ImmortalityStatus.isType.LivingEntity;
            doLogic();
        }

        private SpecificLogicApplier(ItemStack itemStack, ImmortalityData.DataType dataType) {
            this.itemStack = itemStack;
            this.dataType = dataType;
            this.immortalityComponent = getComponent(itemStack);
            this.isType = ImmortalityStatus.isType.ItemStack;
            doLogic();
        }

        private void doLogic() {
            this.dataTypes = new ImmortalityData.DataTypes(this.immortalityComponent, this.dataType);
            if (dataType instanceof ImmortalityData.DataTypeInt) valueInt = dataTypes.readInt();
            if (dataType instanceof ImmortalityData.DataTypeBool) valueBool = dataTypes.readBool();
            if (this.isType == ImmortalityStatus.isType.ServerPlayerEntity)
                doPlayerLogic();
            else if (this.isType == ImmortalityStatus.isType.LivingEntity)
                doLivingEntityLogic();
            else if (this.isType == ImmortalityStatus.isType.ItemStack)
                doItemStackLogic();
        }

        //TODO: Cooldowns with Time Support where is wasnt | Immortal Wither using Immortality Deaths
        private void doPlayerLogic() {
            //TODO:
            if(dataType == ImmortalityData.DataTypeInt.ImmortalDeaths){}
            else if(dataType == ImmortalityData.DataTypeInt.BonusArmor){}//TODO: for all dataTypes
        }

        private void doLivingEntityLogic() {
            //TODO:
            if (livingEntity instanceof ServerPlayerEntity serverPlayer) {
                this.serverPlayerEntity = serverPlayer;
                doPlayerLogic();
                return;
            }
            else if (livingEntity instanceof ImmortalWither immortalWither) {
                EntityAttributeInstance maxHealthInstance = immortalWither.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                if (dataType == ImmortalityData.DataTypeInt.ImmortalDeaths) {
                    //Applying Health based on Heads left
                    int health_diff = (int) (-1f * ((valueInt / 3f) * immortalWither.getMaxHealth()));
                    assert maxHealthInstance != null;
                    maxHealthInstance.clearModifiers();
                    maxHealthInstance.addPersistentModifier(new EntityAttributeModifier("health-deficit", health_diff, EntityAttributeModifier.Operation.ADDITION));
                }
            }
        }

        private void doItemStackLogic() {
            //TODO: not yet necessary but to be adjusted as needed
        }
    }
}
