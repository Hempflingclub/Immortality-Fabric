package net.hempflingclub.immortality.util;

import net.hempflingclub.immortality.entitys.ImmortalWither.ImmortalWither;
import net.hempflingclub.immortality.event.PlayerTickHandler;
import net.hempflingclub.immortality.item.ImmortalityItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.*;

public final class ImmortalityStatus {
    public static final MinecraftServer minecraftServer = PlayerTickHandler.minecraftServer;
    public static final int immortalityHeartsHealthAddition = 2;
    public static final int negativeImmortalityHeartsHealthAddition = -1 * immortalityHeartsHealthAddition;
    public static final int regrowingImmortalityLiverHealthAddition = -10;
    public static final int immortalityHardeningArmorToughnessAddition = 1;
    public static final int immortalityBaseArmorAddition = 1;
    public static final int lifeElixirHealthAddition = 2;
    public static final int REQ_BONUS_HEARTS_FOR_IMMORTALITY = 10;
    public static final int REQ_DEATHS_FOR_TRUE_IMMORTALITY = 50;
    public static final int REQ_BANE_OF_LIFE_DEATHS_FOR_TEMP_SEMI_IMMORTALITY = 3;
    public static final int REQ_SECONDS_COOLDOWN_TO_CLEAR_SEMI_IMMORTALITY_DEATHS = 30;
    public static final int BASE_SEMI_IMMORTALITY_HEART_COOLDOWN_BASE_SECONDS = 300;

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

            /*
            ▗██████████████████▙▖                                                               ▗▟███████████████████████▌
            █████████████████████▄                                                           ▗▄██████████████████████████▌
           ▟███████████████████████▄                █▙▙▙▄▄▄▖▖                              ▗▟████████████████████████████▌
          ▗█████████████████████████▙▖              ▜██████████▟▄▖▖                       ▟███████████████████████████████
          ▟███████████████████████████▙▖            ▝██████████████▙▙▖                  ▗█████████████████████████████████▘
          ██████████████████████████████▄            ▐█████████████████▙▄              ▗██████████████████████████████████▌
         ▐████████████████████████████████▄           ▜███████████████████▙▖          ▗███████████████████████████████████▌  ▖
         ▜█████████████████████████████████▙▖          ▜█████████████████████▄▖       █████████████████████████████████████
         ████████████████████████████████████▙          ▀██████████████████████▄▖    ▟█████████████████████████████████████
        ▗██████████████████████████████████████▖          ▝▟█████████████████████▙▖  ▜████████████████████████████████████▙
        ▐██████████████████████████████████████▛▌   ▗▗▄▟▟▄█████████████████████████▙▖  ▜███████████████████████████████████
        ▟███████████████████████████████████▛▀   ▄▟▟██████████████████████████████████▟▄▐█████████████████████████████████▙
        ▟█████████████████████████████████▀▘ ▗▄██████▜▛█▜▛▛▛███████████████████████████████████████████████████████████████
        ▟████████████████████████████████                       ▐█████████████████████████████████████████████████████████▙
        ▟█████████████████████████████████▙█▟▙█▟██████████████████████████████████████████████████████████████████████████▌
        ▐█████████████████████████████████████████████████████████████████████████████████████████████████████████████████▌
        ▝█████████████████████████████████████████████████████████████████████████████████████████████████████████████████▘
         █████████████████████████████████████████████████████████████████████████████████████████████████████████████████▘
         ▜████████████████████████████████████████████████████████████████████████████████████████████████████████████████
          ███████████████████████████████████████████████████████████████████████████████████████████████████████████████▌
          ▜█████████████████████████████████████████████████████████████████████████████████████████████████████████████▛
           ▜████████████████████████████████████████████████████████████████████████████████████████████████████████████▘
           ▝████████████████████████████████████████████████████████████████▛▛▛▀▀▀▀▀▀▝▝▝▝▝▝▝▝▝   ▝▝▝▝▝▀████████████████▘
            ▝███████████▛▛▛▛█████████████████████▜█▜██████████████████████▛▘            ▄▄▄▄▄▄▟▙  ▗▄▄▙████████████████▘
             ▀██████████ ▖                          ▐█████████████████████▘             ▐███████▙  ▜█████████████████▘
              ▀████████████▘ ▗████████▘             ▐████████████████████▛              ▐████████▌  ███████████████▀
               ▀██████████▘ ▗█████████              ▐████████████████████▛              ▐█████████▖ ▐████████████▛
                ▝████████▛  ▟█████████▖             ▐████████████████████▛              ▐█████████▙  ██████████▀▘
                  ▜██████▘ ▗██████████              ▜████████████████████▛              ▐█████████▙  ▐█████▛▘     ▖▖▖▖▖▖▖▖  ▖
    ▄▖▖▖      ▗    ▀█████  ▟██████████              ██████████████████████              ▟██████████  ▜██████▙████████████▘
    ▀█████▙▙▙▟▄▄▄▄▄▟████▙  ███████████▌            ▗██████████████████████▖             ▟██████████  ▟█████████████████▀
     ▝██████████████████▌ ▗███████████▌            ▐██████████████████████▙▖           ▟███████████  ▟███████████████▛▘
       ▜████████████████▙  ▜███████████▖           ████████████████████████▙▖         ▟████████████  ▟█████████████▛▘
        ▝▀███████████████▖ ▝████████████▖         ▟██████████████████████████▙▗    ▗▄█████████████▙▖▗████████████▛▘  ▖
           ▝▀█████████████▖ ▝████████████▙▖     ▄▟█████▛▀▝▝▝▝▀▀▜███████████████████████████████████████████████▛▘   ▘
               ▀███████████▄▟███████████████▟▟▟█████████▄▄▄▄▄▙████████████████████████████████▛▀█████▜▜███████▌
               ▟████▀▚▘▖▖▖█████▜▛███████████████████████████████████████████████████████████▜▗▐▝▖█▛▀▖▚▜████████▙▖
              ▗████▄▚▙█▘▌▚▛▛▀▝▖▚▟██████████████████████████████████████████████████████████▞▖▙▌▌▞▚▘▙▙████████████▙▖
             ▗████████▛▞▝▖▚▐▟█▟█████████████████████████████████████████████▀▜████████████████▛▖▚▚████████████████▙▖
            ▗██████████▙█▟██████████████████████████████████▀▘▘▘▀▀▀▜▜▜▜▜▀▀▘  ▄██████████████████████████████████████▄
           ▗█████████████████████████████████████▛▘▝▀▜▜▛▛▀   ▄▄▄▄▖▖▗   ▗▗▗▄▟█████████████████████████████████████████▄
          ▗███████████████████████████████████████▙▄▗    ▗▄▟██████████████████████████████████████████████████████████▙
         ▗██████████▛▛▛▛███████████████████████████████████████████████████████████████████████████▛▀▘  ▀▀▀▀▀▀▀▀▘▀       ▖
         ▝▝▀▀▀▀▘▘▘       ▝▀████████████████████████████████████████████████████████████████████▛▀▘
                             ▀▀▛██████████████████████████████████████████████████████████▛▛▀▘
                                 ▝▝▀▛██████████████████████████████████████████████▀▘▀▝▘
                                       ▘▀▀▀▛██████████████████████████████████████▙▟▄▖
                                     ▙▙▄▄▗       ▘▀▀██████████████████████████████████▌
                                      ▀██████▙█▟▙██████████████████████████████████████▌ ▝
                                       ▝▜███████████████████████████████████████████████▌
                                          ▀▛█████████████████████████████████████████████▌
                                             ▀████████████████████████████████████████████▌
                                            ▄▟█████████████████████████████████████████████▙ ▝
                                          ▄█████████████████████████████████████████████████▄
                                        ▗▙███████████████████████████████████████████████████▖
                                       ▗▛▛▛▀▀▀▀███████████████████████████████████████████████▖
                                               ▟███████████████████████████████████████████████
                                            ▘ ▐████████████████████████████████████████████████▙
                                             ▗██████████████████████████████████████████████████▌
                                             ▟███████████████████████████████████████████████████
                                            ▟████████████████████████████████████████████████████▌
                                         ▝ ▗██████████████████████████████████████████████████████  ▘
                                           ▟██████████████████████████████████████████████████████▖
                                        ▝  ████████████████████████████████████████████████████████
     */

    private enum isType {
        ServerPlayerEntity,
        LivingEntity,
        ItemStack
    }

    /**TODO:
     * Should be used on a fixed timer, to actively apply logic
     * and also run on specific Events (Death, Dimension Hopping, Using specific Items)
     */
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

        //TODO: Cooldowns with Time Support where is wasnt
        private void doPlayerLogic() {
            if (dataType instanceof ImmortalityData.DataTypeInt) doPlayerLogicInt();
            else if (dataType instanceof ImmortalityData.DataTypeBool) doPlayerLogicBool();
        }

        private void doPlayerLogicInt() {
            //TODO:
            if (dataType == ImmortalityData.DataTypeInt.ImmortalDeaths) {
                checkTemporaryNegativeHearts();
                checkEveryImmortality();
            } else if (dataType == ImmortalityData.DataTypeInt.SemiImmortalityHeartCooldownSeconds) {
                checkBaneOfLife();
                checkSemiImmortal();
            } else if (dataType == ImmortalityData.DataTypeInt.KilledByBaneOfLifeTime) {
                checkBaneOfLife();
                checkSemiImmortal();
            } else if (dataType == ImmortalityData.DataTypeInt.KilledByBaneOfLifeCurrentAmount) {
                checkBaneOfLife();
                checkEveryImmortality();
            } else if (dataType == ImmortalityData.DataTypeInt.LiverExtractionAmount) {
            } else if (dataType == ImmortalityData.DataTypeInt.LiverExtractionTime) {
            } else if (dataType == ImmortalityData.DataTypeInt.HeartExtractionAmount) {
            } else if (dataType == ImmortalityData.DataTypeInt.LifeElixirTime) {
                checkLifeElixir();
            } else if (dataType == ImmortalityData.DataTypeInt.LifeElixirDropCooldown) {
            } else if (dataType == ImmortalityData.DataTypeInt.BonusHearts)
                checkEveryImmortality();
            else if (dataType == ImmortalityData.DataTypeInt.TemporaryNegativeHearts)
                checkTemporaryNegativeHearts();
            else if (dataType == ImmortalityData.DataTypeInt.BonusArmor) {
            } else if (dataType == ImmortalityData.DataTypeInt.BonusArmorToughness) {
            } else if (dataType == ImmortalityData.DataTypeInt.SoulEnergy) {
            }
        }

        private void doPlayerLogicBool() {
            //Todo:
            if (dataType == ImmortalityData.DataTypeBool.TrueImmortality)
                checkEveryImmortality();
            else if (dataType == ImmortalityData.DataTypeBool.Immortality)
                checkEveryImmortality();
            else if (dataType == ImmortalityData.DataTypeBool.SemiImmortality)
                checkEveryImmortality();
            else if (dataType == ImmortalityData.DataTypeBool.FalseImmortality)
                checkEveryImmortality();
            else if (dataType == ImmortalityData.DataTypeBool.ImmortalHeart) {
                checkImmortalHeart(); // Ensure Player should actually be able to earn one, if not refund it
                checkEveryImmortality();
            } else if (dataType == ImmortalityData.DataTypeBool.VoidHeart)
                checkEveryImmortality();
            else if (dataType == ImmortalityData.DataTypeBool.ExistingSoulVial) {
            } else if (dataType == ImmortalityData.DataTypeBool.LiverExtractedEver)
                checkEveryImmortality();
            else if (dataType == ImmortalityData.DataTypeBool.LiverCurrentlyExtracted)
                checkEveryImmortality();

        }

/* Specific Player logic checks
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
*/

        /**
         * Checking Which Immortality to apply or clear
         */
        private void checkEveryImmortality() {
            checkTrueImmortal();
            checkImmortal();
            checkSemiImmortal();
            checkFalseImmortal();
        }

        /**
         * Checks if Player should rank up to True Immortality
         */
        private void checkTrueImmortal() {
            {
                //Checking if should instead be TrueImmortality, can be also Semi Immortal

                boolean isTrueImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.TrueImmortality);
                if (isTrueImmortal)
                    return; //Not unsetting True Immortality, just because Target already is True Immortal and some false Immortality value is set
                boolean voidHeart = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.VoidHeart);
                if (!voidHeart) return;
                boolean liverExtractedEver = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.LiverExtractedEver);
                if (!liverExtractedEver) return;
                int immortalDeaths = getInt(this.serverPlayerEntity, ImmortalityData.DataTypeInt.ImmortalDeaths);
                if (immortalDeaths < ImmortalityStatus.REQ_DEATHS_FOR_TRUE_IMMORTALITY) return;
                boolean isImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.Immortality);
                if (!isImmortal) return; //At least needs to be Immortal
            }
            toggleGeneric(this.serverPlayerEntity, ImmortalityData.DataTypeBool.TrueImmortality);
            //Is Final Form, cannot be removed except for all reset
        }

        /**
         * Checks if Player should rank up to Immortality, or Lose it if they already have True Immortality
         */
        private void checkImmortal() {
            {
                //Check if Immortal or should unset Immortality, can be both Immortal and Semi Immortal
                boolean isTrueImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.TrueImmortality);
                boolean isImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.Immortality);
                if (isTrueImmortal && isImmortal) {
                    //Illegal state, should only be either or
                    toggleGeneric(this.serverPlayerEntity, ImmortalityData.DataTypeBool.Immortality);
                    return;
                }
                if (isImmortal)
                    return; // If not True Immortal no need to check other states, as Semi Immortality is allowed
                boolean isSemiImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.SemiImmortality);
                if (!isSemiImmortal) return; //At least needs to be Semi Immortal
                int bonusHearts = getInt(this.serverPlayerEntity, ImmortalityData.DataTypeInt.BonusHearts);
                if (bonusHearts < ImmortalityStatus.REQ_BONUS_HEARTS_FOR_IMMORTALITY) return;
                boolean immortalHeart = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.ImmortalHeart);
                if (!immortalHeart) return;
            }
            toggleGeneric(this.serverPlayerEntity, ImmortalityData.DataTypeBool.Immortality);
        }

        /**
         * Checks if Should rank up from False Immortality, or lose when Ranking up to Immortality
         * Also Checks if it should be kept as a Temporary State for higher types of Immortality
         */
        private void checkSemiImmortal() {
            boolean isSemiImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.SemiImmortality);

            if (isSemiImmortal) {
                //Checking if Player should Lose Semi Immortality
                boolean isImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.Immortality);
                boolean isTrueImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.TrueImmortality);
                //Needs Higher Immortality
                if (!(isImmortal || isTrueImmortal)) return; // Not removing when Player is only Semi Immortal
                int killedByBaneOfLifeCurrentAmount = getInt(this.serverPlayerEntity, ImmortalityData.DataTypeInt.KilledByBaneOfLifeCurrentAmount);
                // Should keep it, as it's temporary
                if (killedByBaneOfLifeCurrentAmount >= REQ_BANE_OF_LIFE_DEATHS_FOR_TEMP_SEMI_IMMORTALITY) return;
                //Will also clear, when just promoted to normal Immortality
            } else {
                //Check if should become Semi Immortality // Rank Up from False Immortality
                boolean isFalseImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.FalseImmortality);
                if (isFalseImmortal) {
                    //Promotion Logic
                    int bonusHearts = getInt(this.serverPlayerEntity, ImmortalityData.DataTypeInt.BonusHearts);
                    if (bonusHearts < ImmortalityStatus.REQ_BONUS_HEARTS_FOR_IMMORTALITY) return;
                    // That's about it
                } else {
                    //Temporary State Logic
                    int killedByBaneOfLifeCurrentAmount = getInt(this.serverPlayerEntity, ImmortalityData.DataTypeInt.KilledByBaneOfLifeCurrentAmount);
                    if (killedByBaneOfLifeCurrentAmount < REQ_BANE_OF_LIFE_DEATHS_FOR_TEMP_SEMI_IMMORTALITY) return;

                    boolean isImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.Immortality);
                    boolean isTrueImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.TrueImmortality);
                    //Needs Higher Immortality
                    if (!(isImmortal || isTrueImmortal)) return; // Not removing when Player is only Semi Immortal
                }
            }
            toggleGeneric(this.serverPlayerEntity, ImmortalityData.DataTypeBool.SemiImmortality);
        }

        /**
         * Checks if Player has at least 1 Heart to keep False Immortality, and also clears if conflicting with other type of Immortality
         */
        private void checkFalseImmortal() {
            {
                boolean isFalseImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.FalseImmortality);
                if (!isFalseImmortal) return;

                //Check if Should still be False Immortal
                boolean isSemiImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.SemiImmortality);
                boolean isImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.Immortality);
                boolean isTrueImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.TrueImmortality);
                // Will check if other has other Immortality
                if (isSemiImmortal || isImmortal || isTrueImmortal) {
                    //Turn off False Immortality, conflicting with other Immortality
                    toggleGeneric(this.serverPlayerEntity, ImmortalityData.DataTypeBool.FalseImmortality);
                    return;
                }
                //Check if at least 1 Heart is left
                int maxHealth = (int) this.serverPlayerEntity.getMaxHealth();
                if (maxHealth >= 2) return;
            }
            toggleGeneric(this.serverPlayerEntity, ImmortalityData.DataTypeBool.FalseImmortality);
        }

        /**
         * Checks if Player able to rank up from Semi Immortality to Immortality with Heart, otherwise refund it
         */
        private void checkImmortalHeart() {
            //Checking if Player should earn one, otherwise refund it
            if (shouldEarnOrKeepImmortalHeart()) return; //He won't get it removed
            //Refunding and removing State
            toggleGeneric(this.serverPlayerEntity, ImmortalityData.DataTypeBool.ImmortalHeart);
            World world = this.serverPlayerEntity.world;
            double x = this.serverPlayerEntity.getX();
            double y = this.serverPlayerEntity.getY();
            double z = this.serverPlayerEntity.getZ();

            world.spawnEntity(new ItemEntity(world, x, y, z, new ItemStack(ImmortalityItems.HeartOfImmortality)));
        }

        /**
         * The determining Logic for checkImmortalHeart()
         *
         * @return should earn or keep Immortal Heart
         */
        private boolean shouldEarnOrKeepImmortalHeart() {
            boolean isTrueImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.TrueImmortality);
            if (isTrueImmortal) return true;
            boolean isImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.Immortality);
            if (isImmortal) return true;
            boolean isSemiImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.SemiImmortality);
            int bonusHearts = getInt(this.serverPlayerEntity, ImmortalityData.DataTypeInt.BonusHearts);
            if (isSemiImmortal)
                if (bonusHearts >= ImmortalityStatus.REQ_BONUS_HEARTS_FOR_IMMORTALITY)
                    return true;
            return false;
        }

        /**
         * Will clear Negative Hearts, if not killed within ImmortalityStatus.REQ_SECONDS_COOLDOWN_TO_CLEAR_SEMI_IMMORTALITY_DEATHS for higher Type of Immortality than Semi Immortality
         */
        private void checkBaneOfLife() {
            int killedByBaneOfLifeCurrentAmount = getInt(this.serverPlayerEntity, ImmortalityData.DataTypeInt.KilledByBaneOfLifeCurrentAmount);
            if (killedByBaneOfLifeCurrentAmount == 0) return; //Nothing to do
            int currentTimeSeconds = getCurrentTime(this.serverPlayerEntity) / 20;
            int killedByBaneOfLifeTimeSeconds = getInt(this.serverPlayerEntity, ImmortalityData.DataTypeInt.KilledByBaneOfLifeTime) / 20;
            if (currentTimeSeconds > killedByBaneOfLifeTimeSeconds + ImmortalityStatus.REQ_SECONDS_COOLDOWN_TO_CLEAR_SEMI_IMMORTALITY_DEATHS) {
                //Clear Bane Of Life Deaths
                ImmortalityStatus.addGeneric(this.serverPlayerEntity, ImmortalityData.DataTypeInt.KilledByBaneOfLifeCurrentAmount, -killedByBaneOfLifeCurrentAmount);
                //If higher Immortality than Semi Immortality, instantly regain all lost Hearts
                boolean isImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.Immortality);
                boolean isTrueImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.TrueImmortality);
                if (!(isImmortal || isTrueImmortal)) return;
                int temporaryNegativeHearts = getInt(this.serverPlayerEntity, ImmortalityData.DataTypeInt.TemporaryNegativeHearts);
                ImmortalityStatus.addGeneric(this.serverPlayerEntity, ImmortalityData.DataTypeInt.TemporaryNegativeHearts, -temporaryNegativeHearts);
            }
        }

        /**
         * Applies Negative Hearts
         * Will regen Negative Hearts for Semi Immortality
         * Will give Negative Hearts to False/Semi Immortality on Death
         */
        private void checkTemporaryNegativeHearts() {
            //Applying Negative Hearts
            {
                EntityAttributeInstance healthModifier = this.serverPlayerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                int negativeHearts = getInt(this.serverPlayerEntity, ImmortalityData.DataTypeInt.TemporaryNegativeHearts);
                boolean isFalseImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.FalseImmortality);
                boolean isSemiImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.SemiImmortality);
                if (!(isFalseImmortal || isSemiImmortal)) {
                    //When not False/Semi Immortal clear Negative Hearts
                    negativeHearts = 0;
                }
                assert healthModifier != null;
                healthModifier.clearModifiers();
                healthModifier.addPersistentModifier(new EntityAttributeModifier("negative-hearts", (negativeHearts * ImmortalityStatus.negativeImmortalityHeartsHealthAddition), EntityAttributeModifier.Operation.ADDITION));
            }
            //Giving Negative Hearts on Death to False/Semi Immortality
            if (this.dataType instanceof ImmortalityData.DataTypeInt)
                if (this.dataType == ImmortalityData.DataTypeInt.ImmortalDeaths) {
                    boolean isFalseImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.FalseImmortality);
                    boolean isSemiImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.SemiImmortality);
                    //Needs to be either or to get Negative Hearts, or for any underlying Logic as Negative Hearts are applied first anyway
                    if (!(isFalseImmortal || isSemiImmortal)) return;
                    //This will result in this function being called again, and applying the first part, of giving negative Hearts
                    incrementGeneric(this.serverPlayerEntity, ImmortalityData.DataTypeInt.TemporaryNegativeHearts);
                }
            {
                //Needs to be Semi Immortal to allow possibility of regenerating his Negative Hearts
                boolean isSemiImmortal = getBool(this.serverPlayerEntity, ImmortalityData.DataTypeBool.SemiImmortality);
                if (!isSemiImmortal) return;
                int currentTimeSeconds = getCurrentTime(this.serverPlayerEntity) / 20;
                int timeLostHeartSeconds = getInt(this.serverPlayerEntity, ImmortalityData.DataTypeInt.KilledByBaneOfLifeTime) / 20;
                int heartRegenCooldown = getInt(this.serverPlayerEntity, ImmortalityData.DataTypeInt.SemiImmortalityHeartCooldownSeconds);
                int diffCooldown = (timeLostHeartSeconds + ImmortalityStatus.BASE_SEMI_IMMORTALITY_HEART_COOLDOWN_BASE_SECONDS) - currentTimeSeconds;
                heartRegenCooldown -= diffCooldown;
                if (heartRegenCooldown > 0) return;
                //Set Last Regenerated Heart Time
                new ImmortalityData.DataTypes(getComponent(this.serverPlayerEntity), ImmortalityData.DataTypeInt.KilledByBaneOfLifeTime, currentTimeSeconds * 20);
                //Give new Cooldown to Heart Regen
                addGeneric(this.serverPlayerEntity, ImmortalityData.DataTypeInt.SemiImmortalityHeartCooldownSeconds, ImmortalityStatus.BASE_SEMI_IMMORTALITY_HEART_COOLDOWN_BASE_SECONDS);
                //Regen a Heart
                addGeneric(this.serverPlayerEntity, ImmortalityData.DataTypeInt.TemporaryNegativeHearts, -1);
            }
        }
        private void checkLifeElixir(){
            //TODO:
        }
/* Specific Player logic checks END
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
*/

        private void doLivingEntityLogic() {
            //TODO:
            if (livingEntity instanceof ServerPlayerEntity serverPlayer) {
                this.serverPlayerEntity = serverPlayer;
                doPlayerLogic();
                return;
            } else if (livingEntity instanceof ImmortalWither immortalWither) {
                if (!(dataType instanceof ImmortalityData.DataTypeInt)) return;
                EntityAttributeInstance maxHealthInstance = immortalWither.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                if (dataType == ImmortalityData.DataTypeInt.ImmortalDeaths) {
                    //Applying Health based on Heads left
                    int health_diff = (int) -((valueInt / 3f) * immortalWither.getMaxHealth());
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
