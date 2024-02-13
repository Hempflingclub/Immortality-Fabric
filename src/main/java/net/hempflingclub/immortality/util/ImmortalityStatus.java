package net.hempflingclub.immortality.util;

import net.hempflingclub.immortality.entitys.ImmortalWither.ImmortalWither;
import net.hempflingclub.immortality.event.PlayerTickHandler;
import net.hempflingclub.immortality.item.ImmortalityItems;
import net.hempflingclub.immortality.statuseffect.ModEffectRegistry;
import net.hempflingclub.immortality.util.ImmortalityData.DataTypeBool;
import net.hempflingclub.immortality.util.ImmortalityData.DataTypeInt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class ImmortalityStatus {
    public static final MinecraftServer minecraftServer = PlayerTickHandler.minecraftServer;
    public static final int immortalityHeartsHealthAddition = 2;
    public static final int negativeImmortalityHeartsHealthAddition = -1 * immortalityHeartsHealthAddition;
    public static final int regrowingImmortalityLiverHealthAddition = -10; //TODO: Maybe remove or implement? Not sure
    public static final int immortalityHardeningArmorToughnessAddition = 1;
    public static final int immortalityBaseArmorAddition = 1;
    public static final int REQ_BONUS_HEARTS_FOR_IMMORTALITY = 10;
    public static final int REQ_DEATHS_FOR_ALPHA_IMMORTALITY = 50;
    public static final int REQ_BANE_OF_LIFE_DEATHS_FOR_TEMP_GAMMA_IMMORTALITY = 3;
    public static final int REQ_SECONDS_COOLDOWN_TO_CLEAR_SEMI_IMMORTALITY_DEATHS = 30;
    public static final int BASE_SEMI_IMMORTALITY_HEART_COOLDOWN_BASE_SECONDS = 300;
    public static final int BASE_BONUS_HEART_CHANCE_PERCENT = 50;
    public static final int IMMORTAL_BONUS_HEART_CHANCE_PERCENT = 75;
    public static final int TRUE_IMMORTAL_BONUS_HEART_CHANCE_PERCENT = 100;
    public static final int LIFE_ELIXIR_SECONDS_TO_FINISH = 150;
    public static final String BONUS_HEALTH_KEY = "bonus-health";
    public static final String NEGATIVE_HEALTH_KEY = "negative-health";
    public static final String BONUS_ARMOR_KEY = "bonus-armor";
    public static final String BONUS_ARMOR_TOUGHNESS_KEY = "bonus-armor-toughness";
    public static final int DEATHS_FOR_BONUS_ARMOR = 5;
    public static final int DEATHS_FOR_BONUS_ARMOR_SCALING = 2;
    public static final int DEATHS_FOR_BONUS_ARMOR_TOUGHNESS = 20;
    public static final int DEATHS_FOR_BONUS_ARMOR_TOUGHNESS_SCALING = 2;
    public static final int COOLDOWN_SECONDS_FOR_LIVER_REGROWTH = 600;

    public static int incrementGeneric(ServerPlayerEntity serverPlayerEntity, DataTypeInt dataTypeInt) {
        IImmortalityPlayerComponent iImmortalityPlayerComponent = getComponent(serverPlayerEntity);
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(iImmortalityPlayerComponent, dataTypeInt);
        int newValue = incrementGeneric(dataTypes);
        specificSpecializedLogicApplier(serverPlayerEntity,dataTypeInt);
        return newValue;
    }

    public static int incrementGeneric(LivingEntity livingEntity, DataTypeInt dataTypeInt) {
        IImmortalityLivingEntityComponent iImmortalityLivingEntityComponent = getComponent(livingEntity);
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(iImmortalityLivingEntityComponent, dataTypeInt);
        int newValue = incrementGeneric(dataTypes);
        specificSpecializedLogicApplier(livingEntity,dataTypeInt);
        return newValue;
    }

    public static int incrementGeneric(ItemStack itemStack, DataTypeInt dataTypeInt) {
        IImmortalityItemComponent iImmortalityItemComponent = getComponent(itemStack);
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(iImmortalityItemComponent, dataTypeInt);
        int newValue = incrementGeneric(dataTypes);
        specificSpecializedLogicApplier(itemStack,dataTypeInt);
        return newValue;
    }

    public static int incrementGeneric(IImmortalityComponent something, DataTypeInt dataTypeInt) {
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(something, dataTypeInt);
        int newValue = incrementGeneric(dataTypes);
        logicApplier(something, dataTypeInt);
        return newValue;
    }

    private static int incrementGeneric(ImmortalityData.DataTypes dataTypes) {
        return addGeneric(dataTypes, 1);
    }

    public static boolean toggleGeneric(ServerPlayerEntity serverPlayerEntity, DataTypeBool dataTypeBool) {
        IImmortalityPlayerComponent iImmortalityPlayerComponent = getComponent(serverPlayerEntity);
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(iImmortalityPlayerComponent, dataTypeBool);
        boolean newValue = toggleGeneric(dataTypes);
        specificSpecializedLogicApplier(serverPlayerEntity,dataTypeBool);
        return newValue;
    }

    public static boolean toggleGeneric(LivingEntity livingEntity, DataTypeBool dataTypeBool) {
        IImmortalityLivingEntityComponent iImmortalityLivingEntityComponent = getComponent(livingEntity);
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(iImmortalityLivingEntityComponent, dataTypeBool);
        boolean newValue = toggleGeneric(dataTypes);
        specificSpecializedLogicApplier(livingEntity,dataTypeBool);
        return newValue;
    }

    public static boolean toggleGeneric(ItemStack itemStack, DataTypeBool dataTypeBool) {
        IImmortalityItemComponent iImmortalityItemComponent = getComponent(itemStack);
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(iImmortalityItemComponent, dataTypeBool);
        boolean newValue = toggleGeneric(dataTypes);
        specificSpecializedLogicApplier(itemStack,dataTypeBool);
        return newValue;
    }

    public static boolean toggleGeneric(IImmortalityComponent something, DataTypeBool dataTypeBool) {
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(something, dataTypeBool);
        boolean newValue = toggleGeneric(dataTypes);
        logicApplier(something, dataTypeBool);
        return newValue;
    }

    private static boolean toggleGeneric(ImmortalityData.DataTypes dataTypes) {
        boolean curValue = getBool(dataTypes);
        return dataTypes.set(!curValue);
    }

    public static int getInt(ServerPlayerEntity serverPlayerEntity, DataTypeInt dataTypeInt) {
        IImmortalityComponent immortalityComponent = getComponent(serverPlayerEntity);
        return getInt(immortalityComponent, dataTypeInt);
    }

    public static int getInt(LivingEntity livingEntity, DataTypeInt dataTypeInt) {
        IImmortalityComponent immortalityComponent = getComponent(livingEntity);
        return getInt(immortalityComponent, dataTypeInt);
    }

    public static int getInt(ItemStack itemStack, DataTypeInt dataTypeInt) {
        IImmortalityComponent immortalityComponent = getComponent(itemStack);
        return getInt(immortalityComponent, dataTypeInt);
    }

    public static int getInt(IImmortalityComponent something, DataTypeInt dataTypeInt) {
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(something, dataTypeInt);
        return getInt(dataTypes);
    }

    private static int getInt(ImmortalityData.DataTypes dataTypes) {
        return dataTypes.readInt();
    }

    public static boolean getBool(ServerPlayerEntity serverPlayerEntity, DataTypeBool dataTypeBool) {
        IImmortalityComponent immortalityComponent = getComponent(serverPlayerEntity);
        return getBool(immortalityComponent, dataTypeBool);
    }

    public static boolean getBool(LivingEntity livingEntity, DataTypeBool dataTypeBool) {
        IImmortalityComponent immortalityComponent = getComponent(livingEntity);
        return getBool(immortalityComponent, dataTypeBool);
    }

    public static boolean getBool(ItemStack itemStack, DataTypeBool dataTypeBool) {
        IImmortalityComponent immortalityComponent = getComponent(itemStack);
        return getBool(immortalityComponent, dataTypeBool);
    }

    public static boolean getBool(IImmortalityComponent something, DataTypeBool dataTypeBool) {
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(something, dataTypeBool);
        return getBool(dataTypes);
    }

    private static boolean getBool(ImmortalityData.DataTypes dataTypes) {
        return dataTypes.readBool();
    }

    public static int addGeneric(ServerPlayerEntity serverPlayerEntity, DataTypeInt dataTypeInt, int addition) {
        IImmortalityPlayerComponent iImmortalityPlayerComponent = getComponent(serverPlayerEntity);
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(iImmortalityPlayerComponent, dataTypeInt);
        int newValue = addGeneric(dataTypes, addition);
        specificSpecializedLogicApplier(serverPlayerEntity,dataTypeInt);
        return newValue;
    }

    public static int addGeneric(LivingEntity livingEntity, DataTypeInt dataTypeInt, int addition) {
        IImmortalityLivingEntityComponent iImmortalityLivingEntityComponent = getComponent(livingEntity);
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(iImmortalityLivingEntityComponent, dataTypeInt);
        int newValue = addGeneric(dataTypes, addition);
        specificSpecializedLogicApplier(livingEntity,dataTypeInt);
        return newValue;
    }

    public static int addGeneric(ItemStack itemStack, DataTypeInt dataTypeInt, int addition) {
        IImmortalityItemComponent iImmortalityItemComponent = getComponent(itemStack);
        ImmortalityData.DataTypes dataTypes = new ImmortalityData.DataTypes(iImmortalityItemComponent, dataTypeInt);
        int newValue = addGeneric(dataTypes, addition);
        specificSpecializedLogicApplier(itemStack,dataTypeInt);
        return newValue;
    }

    public static int addGeneric(IImmortalityComponent something, DataTypeInt dataTypeInt, int addition) {
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
        logicApplier(dataTypes,dataType);
    }

    public static void logicApplier(ImmortalityData.DataTypes dataTypes, ImmortalityData.DataType dataType) {
        //Loop over all Entities until found matching component, and then run logic to reapply fitting status
        IImmortalityComponent immortalityComponent = dataTypes.getIImmortalityComponent();
        if (immortalityComponent instanceof IImmortalityPlayerComponent iImmortalityPlayerComponent) {
            ServerPlayerEntity serverPlayerEntity = findPlayer(iImmortalityPlayerComponent);
            specificSpecializedLogicApplier(serverPlayerEntity,dataType);
        } else if (immortalityComponent instanceof IImmortalityLivingEntityComponent iImmortalityLivingEntityComponent) {
            LivingEntity livingEntity = findEntity(iImmortalityLivingEntityComponent);
            specificSpecializedLogicApplier(livingEntity,dataType);
        } else if (immortalityComponent instanceof IImmortalityItemComponent iImmortalityItemComponent) {
            ItemStack itemStack = findItemStack(iImmortalityItemComponent); //TODO: Probably also search in BlockEntity of Soul Urn
            specificSpecializedLogicApplier(itemStack,dataType);
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

    public static void specificSpecializedLogicApplier(ServerPlayerEntity serverPlayerEntity, ImmortalityData.DataType dataType){
        serverPlayerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
        new SpecificLogicApplier(serverPlayerEntity, dataType);
    }
    public static void specificSpecializedLogicApplier(LivingEntity livingEntity, ImmortalityData.DataType dataType){
        livingEntity.syncComponent(IImmortalityLivingEntityComponent.KEY);
        new SpecificLogicApplier(livingEntity, dataType);
    }
    public static void specificSpecializedLogicApplier(ItemStack itemStack, ImmortalityData.DataType dataType){
        new SpecificLogicApplier(itemStack, dataType);
    }
    public static void specificAllLogicApplier(ServerPlayerEntity serverPlayerEntity) {
        serverPlayerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
        for (DataTypeInt dataTypeInt : DataTypeInt.values())
            new SpecificLogicApplier(serverPlayerEntity, dataTypeInt);
        for (DataTypeBool dataTypeBool : DataTypeBool.values())
            new SpecificLogicApplier(serverPlayerEntity, dataTypeBool);

    }

    public static void specificAllLogicApplier(LivingEntity livingEntity) {
        livingEntity.syncComponent(IImmortalityLivingEntityComponent.KEY);
        for (DataTypeInt dataTypeInt : DataTypeInt.values())
            new SpecificLogicApplier(livingEntity, dataTypeInt);
        for (DataTypeBool dataTypeBool : DataTypeBool.values())
            new SpecificLogicApplier(livingEntity, dataTypeBool);
    }

    public static void specificAllLogicApplier(ItemStack itemStack) {
        //itemStack.syncComponent(IImmortalityItemComponent.KEY); // Sadly this cannot be done, but should probably be fine
        for (DataTypeInt dataTypeInt : DataTypeInt.values())
            new SpecificLogicApplier(itemStack, dataTypeInt);
        for (DataTypeBool dataTypeBool : DataTypeBool.values())
            new SpecificLogicApplier(itemStack, dataTypeBool);
    }

    /**
     * Removes Immortality / False Immortality / Void Heart / Immortality Hearts / regrowingImmortalityLiver / negativeImmortalityHearts and resets Immortality Deaths
     *
     * @param serverPlayerEntity the player to lose it all
     */
    public static void removeEverything(ServerPlayerEntity serverPlayerEntity) {
        IImmortalityPlayerComponent iImmortalityPlayerComponent = getComponent(serverPlayerEntity);
        for (DataTypeBool dataTypeBool : DataTypeBool.values())
            new ImmortalityData.DataTypes(iImmortalityPlayerComponent, dataTypeBool, false); //Will set all possible states to false
        for (DataTypeInt dataTypeInt : DataTypeInt.values())
            new ImmortalityData.DataTypes(iImmortalityPlayerComponent, dataTypeInt, 0); //Will set all possible states to 0
        serverPlayerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void removeEverythingExcept(ServerPlayerEntity serverPlayerEntity, ImmortalityData.DataType... dataType) {
        IImmortalityPlayerComponent iImmortalityPlayerComponent = getComponent(serverPlayerEntity);
        ArrayList<ImmortalityData.DataType> dataTypesExceptions = new ArrayList<ImmortalityData.DataType>(Arrays.asList(dataType));
        for (DataTypeBool dataTypeBool : DataTypeBool.values())
            if (!dataTypesExceptions.contains(dataTypeBool))
                new ImmortalityData.DataTypes(iImmortalityPlayerComponent, dataTypeBool, false); //Will set all possible states except provided exceptions to false

        for (DataTypeInt dataTypeInt : DataTypeInt.values())
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

    /**
     * TODO: HIGH PRIORITY
     * TODO: DONE: fixed Timer
     * TODO: TBD: Death, Dimension Hopping, Using specific Items
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
            if (dataType instanceof DataTypeInt) valueInt = dataTypes.readInt();
            if (dataType instanceof DataTypeBool) valueBool = dataTypes.readBool();
            if (this.isType == ImmortalityStatus.isType.ServerPlayerEntity)
                doPlayerLogic();
            else if (this.isType == ImmortalityStatus.isType.LivingEntity)
                doLivingEntityLogic();
            else if (this.isType == ImmortalityStatus.isType.ItemStack)
                doItemStackLogic();
        }

        private void doPlayerLogic() {
            if (dataType instanceof DataTypeInt) doPlayerLogicInt();
            else if (dataType instanceof DataTypeBool) doPlayerLogicBool();
        }

        private void doPlayerLogicInt() {
            if (dataType == DataTypeInt.ImmortalDeaths) {
                checkLifeElixir();
                checkTemporaryNegativeHearts();
                checkEveryImmortality();
            } else if (dataType == DataTypeInt.GammaImmortalityHeartCooldownSeconds) {
                checkBaneOfLife();
                checkGammaImmortal();
            } else if (dataType == DataTypeInt.KilledByBaneOfLifeTime) {
                checkBaneOfLife();
                checkGammaImmortal();
            } else if (dataType == DataTypeInt.KilledByBaneOfLifeCurrentAmount) {
                checkBaneOfLife();
                checkEveryImmortality();
            } else if (dataType == DataTypeInt.LiverExtractionAmount) {
                checkLiverExtraction();
            } else if (dataType == DataTypeInt.LiverExtractionCooldownSeconds) {
                checkLiverExtraction();
            } else if (dataType == DataTypeInt.LifeElixirTime) {
                checkLifeElixir();
            } else if (dataType == DataTypeInt.LifeElixirDropCooldownSeconds) {
                checkLifeElixir();
            } else if (dataType == DataTypeInt.BonusHearts) {
                checkBonusHearts();
                checkEveryImmortality();
            } else if (dataType == DataTypeInt.TemporaryNegativeHearts)
                checkTemporaryNegativeHearts();
            else if (dataType == DataTypeInt.BonusArmor) {
                checkBonusArmor();
            } else if (dataType == DataTypeInt.BonusArmorToughness) {
                checkBonusArmorToughness();
            } else if (dataType == DataTypeInt.SoulEnergy) {
                checkSoulEnergy();
            }
        }

        private void doPlayerLogicBool() {
            if (dataType == DataTypeBool.AlphaImmortality)
                checkEveryImmortality();
            else if (dataType == DataTypeBool.BetaImmortality)
                checkEveryImmortality();
            else if (dataType == DataTypeBool.GammaImmortality)
                checkEveryImmortality();
            else if (dataType == DataTypeBool.DeltaImmortality)
                checkEveryImmortality();
            else if (dataType == DataTypeBool.ImmortalHeart) {
                checkImmortalHeart(); // Ensure Player should actually be able to earn one, if not refund it
                checkEveryImmortality();
            } else if (dataType == DataTypeBool.VoidHeart)
                checkEveryImmortality();
            else if (dataType == DataTypeBool.ExistingSoulVial) {
            } else if (dataType == DataTypeBool.LiverExtractedEver)
                checkEveryImmortality();
            else if (dataType == DataTypeBool.LiverCurrentlyExtracted)
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
            checkAlphaImmortal();
            checkBetaImmortal();
            checkGammaImmortal();
            checkDeltaImmortal();
        }

        /**
         * Checks if Player should rank up to Alpha Immortality
         */
        private void checkAlphaImmortal() {
            {
                //Checking if should instead be AlphaImmortality, can be also Gamma Immortal simultaneously

                boolean isAlphaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.AlphaImmortality);
                if (isAlphaImmortal)
                    return; //Not unsetting True Immortality, just because Target already is True Immortal and some false Immortality value is set
                boolean voidHeart = getBool(this.serverPlayerEntity, DataTypeBool.VoidHeart);
                if (!voidHeart) return;
                boolean liverExtractedEver = getBool(this.serverPlayerEntity, DataTypeBool.LiverExtractedEver);
                if (!liverExtractedEver) return;
                int immortalDeaths = getInt(this.serverPlayerEntity, DataTypeInt.ImmortalDeaths);
                if (immortalDeaths < ImmortalityStatus.REQ_DEATHS_FOR_ALPHA_IMMORTALITY) return;
                boolean isBetaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.BetaImmortality);
                if (!isBetaImmortal) return; //At least needs to be BetaImmortal
            }
            toggleGeneric(this.serverPlayerEntity, DataTypeBool.AlphaImmortality);
            //Is Final Form, cannot be removed except for all reset
        }

        /**
         * Checks if Player should rank up to BetaImmortality, or Lose it if they already are Alpha Immortal
         */
        private void checkBetaImmortal() {
            {
                //Check if Immortal or should unset Immortality, can be both Beta Immortal and Gamma Immortal
                boolean isAlphaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.AlphaImmortality);
                boolean isBetaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.BetaImmortality);
                if (isAlphaImmortal && isBetaImmortal) {
                    //Illegal state, should only be either or
                    toggleGeneric(this.serverPlayerEntity, DataTypeBool.BetaImmortality);
                    return;
                }
                if (isBetaImmortal)
                    return; // If not Alpha Immortal no need to check other states, as Gamma Immortality is allowed
                boolean isGammaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.GammaImmortality);
                if (!isGammaImmortal) return; //At least needs to be Gamma Immortal
                int bonusHearts = getInt(this.serverPlayerEntity, DataTypeInt.BonusHearts);
                if (bonusHearts < ImmortalityStatus.REQ_BONUS_HEARTS_FOR_IMMORTALITY) return;
                boolean immortalHeart = getBool(this.serverPlayerEntity, DataTypeBool.ImmortalHeart);
                if (!immortalHeart) return;
            }
            toggleGeneric(this.serverPlayerEntity, DataTypeBool.BetaImmortality);
        }

        /**
         * Checks if Should rank up from Delta Immortality, or lose when Ranking up to Immortality
         * Also Checks if it should be kept as a Temporary State for higher types of Immortality
         */
        private void checkGammaImmortal() {
            boolean isGammaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.GammaImmortality);

            if (isGammaImmortal) {
                //Checking if Player should Lose Gamma Immortality
                boolean isBetaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.BetaImmortality);
                boolean isAlphaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.AlphaImmortality);
                //Needs Higher Immortality
                if (!(isBetaImmortal || isAlphaImmortal)) return; // Not removing when Player is only Semi Immortal
                int killedByBaneOfLifeCurrentAmount = getInt(this.serverPlayerEntity, DataTypeInt.KilledByBaneOfLifeCurrentAmount);
                // Should keep it, as it's temporary
                if (killedByBaneOfLifeCurrentAmount >= REQ_BANE_OF_LIFE_DEATHS_FOR_TEMP_GAMMA_IMMORTALITY) return;
                //Will also clear, when just promoted to Beta Immortality
            } else {
                //Check if should become Gamma Immortality // Rank Up from Delta Immortality
                boolean isDeltaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.DeltaImmortality);
                if (isDeltaImmortal) {
                    //Promotion Logic
                    int bonusHearts = getInt(this.serverPlayerEntity, DataTypeInt.BonusHearts);
                    if (bonusHearts < ImmortalityStatus.REQ_BONUS_HEARTS_FOR_IMMORTALITY) return;
                    // That's about it
                } else {
                    //Temporary State Logic
                    int killedByBaneOfLifeCurrentAmount = getInt(this.serverPlayerEntity, DataTypeInt.KilledByBaneOfLifeCurrentAmount);
                    if (killedByBaneOfLifeCurrentAmount < REQ_BANE_OF_LIFE_DEATHS_FOR_TEMP_GAMMA_IMMORTALITY) return;

                    boolean isBetaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.BetaImmortality);
                    boolean isAlphaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.AlphaImmortality);
                    //Needs Higher Immortality
                    if (!(isBetaImmortal || isAlphaImmortal)) return; // Not removing when Player is only Semi Immortal
                }
            }
            toggleGeneric(this.serverPlayerEntity, DataTypeBool.GammaImmortality);
        }

        /**
         * Checks if Player has at least 1 Heart to keep Delta Immortality, and also clears if conflicting with other type of Immortality
         */
        private void checkDeltaImmortal() {
            {
                boolean isDeltaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.DeltaImmortality);
                if (!isDeltaImmortal) return;

                //Check if Should still be Delta Immortal
                boolean isGammaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.GammaImmortality);
                boolean isBetaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.BetaImmortality);
                boolean isAlphaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.AlphaImmortality);
                // Will check if other has other Immortality
                if (isGammaImmortal || isBetaImmortal || isAlphaImmortal) {
                    //Turn off Delta Immortality, conflicting with other Immortality
                    toggleGeneric(this.serverPlayerEntity, DataTypeBool.DeltaImmortality);
                    return;
                }
                //Check if at least 1 Heart is left
                int maxHealth = (int) this.serverPlayerEntity.getMaxHealth();
                if (maxHealth >= 2) return;
            }
            toggleGeneric(this.serverPlayerEntity, DataTypeBool.DeltaImmortality);
        }

        /**
         * Checks if Player able to rank up from Gamma Immortality to BetaImmortality with Heart, otherwise refund it
         */
        private void checkImmortalHeart() {
            //Checking if Player should earn one, otherwise refund it
            if (shouldEarnOrKeepImmortalHeart()) return; //He won't get it removed
            //Refunding and removing State
            toggleGeneric(this.serverPlayerEntity, DataTypeBool.ImmortalHeart);
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
            boolean isAlphaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.AlphaImmortality);
            if (isAlphaImmortal) return true;
            boolean isBetaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.BetaImmortality);
            if (isBetaImmortal) return true;
            boolean isGammaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.GammaImmortality);
            int bonusHearts = getInt(this.serverPlayerEntity, DataTypeInt.BonusHearts);
            if (isGammaImmortal)
                if (bonusHearts >= ImmortalityStatus.REQ_BONUS_HEARTS_FOR_IMMORTALITY)
                    return true;
            return false;
        }

        /**
         * Will clear Negative Hearts,
         * if not killed within ImmortalityStatus.REQ_SECONDS_COOLDOWN_TO_CLEAR_GAMMA_IMMORTALITY_DEATHS for higher Type of Immortality than Gamma Immortality
         */
        private void checkBaneOfLife() {
            int killedByBaneOfLifeCurrentAmount = getInt(this.serverPlayerEntity, DataTypeInt.KilledByBaneOfLifeCurrentAmount);
            if (killedByBaneOfLifeCurrentAmount == 0) return; //Nothing to do
            int currentTimeSeconds = getCurrentTime(this.serverPlayerEntity) / 20;
            int killedByBaneOfLifeTimeSeconds = getInt(this.serverPlayerEntity, DataTypeInt.KilledByBaneOfLifeTime) / 20;
            //Time left until clear in secs
            int diffTimeSeconds = (killedByBaneOfLifeTimeSeconds + ImmortalityStatus.REQ_SECONDS_COOLDOWN_TO_CLEAR_SEMI_IMMORTALITY_DEATHS)-currentTimeSeconds;
            // Give Bane Of Life effect, to indicate Temporary Status
            if(killedByBaneOfLifeCurrentAmount>=ImmortalityStatus.REQ_BANE_OF_LIFE_DEATHS_FOR_TEMP_GAMMA_IMMORTALITY)
                if (!this.serverPlayerEntity.hasStatusEffect(ModEffectRegistry.bane_of_life))
                    this.serverPlayerEntity.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.bane_of_life, 5 * 20, 0, true, true));
            //Should be cleared
            if (diffTimeSeconds<0) {
                //Clear Bane Of Life Deaths
                ImmortalityStatus.addGeneric(this.serverPlayerEntity, DataTypeInt.KilledByBaneOfLifeCurrentAmount, -killedByBaneOfLifeCurrentAmount);
                //If higher Immortality than Gamma Immortality, instantly regain all lost Hearts
                boolean isBetaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.BetaImmortality);
                boolean isAlphaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.AlphaImmortality);
                if (!(isBetaImmortal || isAlphaImmortal)) return;
                int temporaryNegativeHearts = getInt(this.serverPlayerEntity, DataTypeInt.TemporaryNegativeHearts);
                ImmortalityStatus.addGeneric(this.serverPlayerEntity, DataTypeInt.TemporaryNegativeHearts, -temporaryNegativeHearts);
            }
        }

        /**
         * Applies Negative Hearts
         * Will regen Negative Hearts for Gamma Immortality
         * Will give Negative Hearts to Delta/Gamma Immortality on Death
         */
        private void checkTemporaryNegativeHearts() {
            //Applying Negative Hearts
            {
                EntityAttributeInstance healthModifier = this.serverPlayerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                int negativeHearts = getInt(this.serverPlayerEntity, DataTypeInt.TemporaryNegativeHearts);
                boolean isDeltaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.DeltaImmortality);
                boolean isGammaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.GammaImmortality);
                if (!(isDeltaImmortal || isGammaImmortal)) {
                    //When not False/Semi Immortal clear Negative Hearts
                    negativeHearts = 0;
                }
                assert healthModifier != null;
                healthModifier.clearModifiers();
                healthModifier.addPersistentModifier(new EntityAttributeModifier(ImmortalityStatus.NEGATIVE_HEALTH_KEY, (negativeHearts * ImmortalityStatus.negativeImmortalityHeartsHealthAddition), EntityAttributeModifier.Operation.ADDITION));
            }
            //Giving Negative Hearts on Death to Delta/Gamma Immortality
            if (this.dataType instanceof DataTypeInt)
                if (this.dataType == DataTypeInt.ImmortalDeaths) {
                    boolean isDeltaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.DeltaImmortality);
                    boolean isGammaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.GammaImmortality);
                    //Needs to be either or to get Negative Hearts, or for any underlying Logic as Negative Hearts are applied first anyway
                    if (!(isDeltaImmortal || isGammaImmortal)) return;
                    //This will result in this function being called again, and applying the first part, of giving negative Hearts
                    incrementGeneric(this.serverPlayerEntity, DataTypeInt.TemporaryNegativeHearts);
                }
            {
                //Needs to be Semi Immortal to allow possibility of regenerating his Negative Hearts
                boolean isGammaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.GammaImmortality);
                if (!isGammaImmortal) return;
                int currentTimeSeconds = getCurrentTime(this.serverPlayerEntity) / 20;
                int timeLostHeartSeconds = getInt(this.serverPlayerEntity, DataTypeInt.KilledByBaneOfLifeTime) / 20;
                int heartRegenCooldown = getInt(this.serverPlayerEntity, DataTypeInt.GammaImmortalityHeartCooldownSeconds);
                int diffCooldown = (timeLostHeartSeconds + ImmortalityStatus.BASE_SEMI_IMMORTALITY_HEART_COOLDOWN_BASE_SECONDS) - currentTimeSeconds;
                heartRegenCooldown -= diffCooldown;
                if (heartRegenCooldown > 0) return;
                //Set Last Regenerated Heart Time
                new ImmortalityData.DataTypes(getComponent(this.serverPlayerEntity), DataTypeInt.KilledByBaneOfLifeTime, currentTimeSeconds * 20);
                //Give new Cooldown to Heart Regen
                addGeneric(this.serverPlayerEntity, DataTypeInt.GammaImmortalityHeartCooldownSeconds, ImmortalityStatus.BASE_SEMI_IMMORTALITY_HEART_COOLDOWN_BASE_SECONDS);
                //Regen a Heart
                addGeneric(this.serverPlayerEntity, DataTypeInt.TemporaryNegativeHearts, -1);
                //Give Feedback Message
                this.serverPlayerEntity.sendMessage(Text.translatable("immortality.status.heart_restored"), true);
            }
        }

        /**
         * Will give appropriate Bonus Hearts
         * If not any type of Immortality, clear Bonus Hearts
         */
        private void checkBonusHearts() {
            {
                //Applying Bonus Hearts
                int bonusHearts = getInt(this.serverPlayerEntity, DataTypeInt.BonusHearts);

                boolean isDeltaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.DeltaImmortality);
                boolean isGammaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.GammaImmortality);
                boolean isBetaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.BetaImmortality);
                boolean isAlphaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.AlphaImmortality);
                //Needs to be any Immortality, to sustain/have bonus hearts
                if (!(isDeltaImmortal || isGammaImmortal || isBetaImmortal || isAlphaImmortal)) {
                    //If not any Immortality remove Bonus Hearts
                    addGeneric(this.serverPlayerEntity, DataTypeInt.BonusHearts, -bonusHearts);
                    bonusHearts = 0;
                }
                EntityAttributeInstance maxHealthInstance = this.serverPlayerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                assert maxHealthInstance != null;
                {
                    //Removing old Modifier
                    Set<EntityAttributeModifier> entityAttributeModifiers = maxHealthInstance.getModifiers(EntityAttributeModifier.Operation.ADDITION);
                    EntityAttributeModifier oldHealthBonus = null;
                    for (EntityAttributeModifier entityAttributeModifier : entityAttributeModifiers) {
                        if (!entityAttributeModifier.getName().equals(ImmortalityStatus.BONUS_HEALTH_KEY))
                            continue;
                        oldHealthBonus = entityAttributeModifier;
                        break;
                    }
                    //Could possibly not yet have one, so null check
                    if (oldHealthBonus != null) maxHealthInstance.removeModifier(oldHealthBonus);
                }
                maxHealthInstance.addPersistentModifier(new EntityAttributeModifier(ImmortalityStatus.BONUS_HEALTH_KEY, (bonusHearts * ImmortalityStatus.immortalityHeartsHealthAddition), EntityAttributeModifier.Operation.ADDITION));
            }
        }

        /**
         * Will handle Life Elixir
         * giving Bonus Health on life elixir drinking, after effect wears off time
         * enforcing Immortality tier based percentages for chance of it working
         * With Audio/Visual Effects
         * Life Elixir Dropping on Final Killing Semi Immortal or higher
         */
        private void checkLifeElixir() {
            int lifeElixirTime = getInt(this.serverPlayerEntity, DataTypeInt.LifeElixirTime);
            final int[] lifeElixirChances = {ImmortalityStatus.BASE_BONUS_HEART_CHANCE_PERCENT, ImmortalityStatus.IMMORTAL_BONUS_HEART_CHANCE_PERCENT, ImmortalityStatus.TRUE_IMMORTAL_BONUS_HEART_CHANCE_PERCENT};
            //lifeElixirChances[0] = normal
            //lifeElixirChances[1] = for Immortal
            //lifeElixirChances[2] = for True Immortal
            World world = this.serverPlayerEntity.world;
            int x = (int) this.serverPlayerEntity.getX();
            int y = (int) this.serverPlayerEntity.getY();
            int z = (int) this.serverPlayerEntity.getZ();
            if (lifeElixirTime > 0) {
                //Check if Required Time has been reached
                int currentTimeSeconds = getCurrentTime(this.serverPlayerEntity);
                int lifeElixirTimeSeconds = lifeElixirTime / 20;
                int secondsPassed = currentTimeSeconds - lifeElixirTimeSeconds;
                if (secondsPassed < ImmortalityStatus.LIFE_ELIXIR_SECONDS_TO_FINISH) return;

                boolean isBetaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.BetaImmortality);
                boolean isAlphaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.AlphaImmortality);
                int lifeElixirChance = lifeElixirChances[0];
                if (isBetaImmortal) lifeElixirChance = lifeElixirChances[1];
                else if (isAlphaImmortal) lifeElixirChance = lifeElixirChances[2];
                int randomInt = ThreadLocalRandom.current().nextInt(0, 100);
                //Apply RNG
                boolean success = false;
                if (lifeElixirChance > randomInt) success = true;
                //Check if they even have an Immortality
                boolean isDeltaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.DeltaImmortality);
                boolean isGammaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.GammaImmortality);
                if (!(isDeltaImmortal || isGammaImmortal || isBetaImmortal || isAlphaImmortal)) success = false;
                //Reset LifeElixirTime
                addGeneric(this.serverPlayerEntity, DataTypeInt.LifeElixirTime, -lifeElixirTime);
                if (success) {
                    //Add Bonus Heart
                    incrementGeneric(this.serverPlayerEntity, DataTypeInt.BonusHearts);
                    //Positive Audio/Visual Feedback
                    {
                        //Particles
                        ParticleEffect particleEffect = ParticleTypes.HEART;
                        world.addParticle(particleEffect, x, y, z, 0, 1, 0);
                        //Audio
                        SoundEvent soundEvent = SoundEvents.ENTITY_BEE_POLLINATE;
                        SoundCategory soundCategory = SoundCategory.PLAYERS;
                        world.playSound(x, y, z, soundEvent, soundCategory, 1, 1, true);
                    }
                } else {
                    //Fail Audio/Visual Feedback
                    {
                        //Particles
                        ParticleEffect particleEffect = ParticleTypes.SCULK_CHARGE_POP;
                        world.addParticle(particleEffect, x, y, z, 0, 1, 0);
                        //Audio
                        SoundEvent soundEvent = SoundEvents.BLOCK_CHAIN_HIT;
                        SoundCategory soundCategory = SoundCategory.PLAYERS;
                        world.playSound(x, y, z, soundEvent, soundCategory, 1, 1, true);
                    }
                }
            }
            int lifeElixirDropCooldown = getInt(this.serverPlayerEntity, DataTypeInt.LifeElixirDropCooldownSeconds);
            if (lifeElixirDropCooldown < 0) {
                //Logic to Handle Life Elixir Drop on Final Killing a Gamma Immortal or higher
                boolean isGammaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.GammaImmortality);
                boolean isBetaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.BetaImmortality);
                boolean isAlphaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.AlphaImmortality);
                if (!(isGammaImmortal || isBetaImmortal | isAlphaImmortal)) return;

                //Dropping item
                ItemEntity lifeElixirItemEntity = new ItemEntity(world, x, y, z, new ItemStack(ImmortalityItems.LifeElixir));
                world.spawnEntity(lifeElixirItemEntity);

                //Giving implicit 0, to indicate to no longer reduce it
                addGeneric(this.serverPlayerEntity, DataTypeInt.LifeElixirDropCooldownSeconds, -lifeElixirDropCooldown);
            }
        }

        /**
         * Checking if Liver should be extractable (not Delta Immortality) (Cooldown is cool)
         * If then drop Liver and set cooldown
         * When not then if problem wasn't cooldown, reset cooldown to deactivate function call
         */
        private void checkLiverExtraction() {
            boolean isLiverCurrentlyExtracted = getBool(this.serverPlayerEntity, DataTypeBool.LiverCurrentlyExtracted);
            int liverExtractionCooldown = getInt(this.serverPlayerEntity, DataTypeInt.LiverExtractionCooldownSeconds);
            boolean isDeltaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.DeltaImmortality);
            //Reset liverExtraction status after Cooldown
            if (liverExtractionCooldown < 0) {
                addGeneric(this.serverPlayerEntity, DataTypeInt.LiverExtractionCooldownSeconds, -liverExtractionCooldown);
                assert isLiverCurrentlyExtracted;
                toggleGeneric(this.serverPlayerEntity, DataTypeBool.LiverCurrentlyExtracted);
                this.serverPlayerEntity.sendMessage(Text.translatable("immortality.status.liver_regrown"), true);
            } else if (isLiverCurrentlyExtracted && liverExtractionCooldown == 0) {
                //Just Extracted Liver, no Cooldown active yet
                boolean shouldExtract = true;
                if (isDeltaImmortal) shouldExtract = false;
                //If not extracting reset all
                if (!shouldExtract) {
                    toggleGeneric(this.serverPlayerEntity, DataTypeBool.LiverCurrentlyExtracted);
                    this.serverPlayerEntity.sendMessage(Text.literal("Liver not regrown"),true);
                    return;
                }
                //If extracting increment cooldown, and drop Liver
                //Increment Cooldown
                addGeneric(this.serverPlayerEntity, DataTypeInt.LiverExtractionCooldownSeconds, ImmortalityStatus.COOLDOWN_SECONDS_FOR_LIVER_REGROWTH);
                //Drop Liver
                World world = this.serverPlayerEntity.world;
                int x = (int) this.serverPlayerEntity.getX();
                int y = (int) this.serverPlayerEntity.getY();
                int z = (int) this.serverPlayerEntity.getZ();
                ItemEntity lifeElixirItemEntity = new ItemEntity(world, x, y, z, new ItemStack(ImmortalityItems.LifeElixir));
                world.spawnEntity(lifeElixirItemEntity);
            }
        }

        /**
         * Applying Bonus Armor
         * Also clears if no type of Immortality
         */
        private void checkBonusArmor() {
            //Check Immortality, and Immortal Deaths to find the right Bonus Armor to apply
            //Apply Bonus Armor accordingly / clear if no Immortality
            {
                //Determining bonus Armor
                boolean isDeltaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.DeltaImmortality);
                boolean isGammaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.GammaImmortality);
                boolean isBetaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.BetaImmortality);
                boolean isAlphaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.AlphaImmortality);
                int bonusArmor = getInt(this.serverPlayerEntity, DataTypeInt.BonusArmor);
                //Needs any type of Immortality to be allowed bonus Armor
                if (!(isDeltaImmortal || isGammaImmortal || isBetaImmortal || isAlphaImmortal)) {
                    addGeneric(this.serverPlayerEntity, DataTypeInt.BonusArmor, -bonusArmor);
                    bonusArmor = 0;
                }
                //Scale Armor
                {
                    int immortalDeaths = getInt(this.serverPlayerEntity, DataTypeInt.ImmortalDeaths);
                    int deathsForNextBoni = ImmortalityStatus.DEATHS_FOR_BONUS_ARMOR;
                    int boniToBeApplied = 0;
                    while (immortalDeaths >= deathsForNextBoni) {
                        //Increase Bonus Armor
                        boniToBeApplied++;
                        //Scale until, no longer enough Deaths
                        deathsForNextBoni *= ImmortalityStatus.DEATHS_FOR_BONUS_ARMOR_SCALING;
                    }
                    //Apply Scaling to applied Armor
                    bonusArmor *= boniToBeApplied;
                }


                int appliedBonusArmor = ImmortalityStatus.immortalityBaseArmorAddition * bonusArmor;

                //Actually Applying
                EntityAttributeInstance armorInstance = this.serverPlayerEntity.getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
                assert armorInstance != null;
                {
                    //Removing old Modifier
                    Set<EntityAttributeModifier> entityAttributeModifiers = armorInstance.getModifiers(EntityAttributeModifier.Operation.ADDITION);
                    EntityAttributeModifier oldArmorBonus = null;
                    for (EntityAttributeModifier entityAttributeModifier : entityAttributeModifiers) {
                        if (!entityAttributeModifier.getName().equals(ImmortalityStatus.BONUS_ARMOR_KEY))
                            continue;
                        oldArmorBonus = entityAttributeModifier;
                        break;
                    }
                    //Could possibly not yet have one, so null check
                    if (oldArmorBonus != null) armorInstance.removeModifier(oldArmorBonus);
                }
                armorInstance.addPersistentModifier(new EntityAttributeModifier(ImmortalityStatus.BONUS_HEALTH_KEY, appliedBonusArmor, EntityAttributeModifier.Operation.ADDITION));
            }
        }

        /**
         * Applying Bonus Armor Toughness
         * Also clears if no type of Immortality
         */
        private void checkBonusArmorToughness() {
            //Check Immortality, and Immortal Deaths to find the right Bonus Armor Toughness to apply
            //Apply Bonus Armor Toughness accordingly / clear if no Immortality
            {
                //Determining bonus Armor Toughness
                boolean isDeltaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.DeltaImmortality);
                boolean isGammaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.GammaImmortality);
                boolean isBetaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.BetaImmortality);
                boolean isAlphaImmortal = getBool(this.serverPlayerEntity, DataTypeBool.AlphaImmortality);
                int bonusArmorToughness = getInt(this.serverPlayerEntity, DataTypeInt.BonusArmorToughness);
                //Needs any type of Immortality to be allowed bonus Armor
                if (!(isDeltaImmortal || isGammaImmortal || isBetaImmortal || isAlphaImmortal)) {
                    addGeneric(this.serverPlayerEntity, DataTypeInt.BonusArmor, -bonusArmorToughness);
                    bonusArmorToughness = 0;
                }
                //Scale Armor Toughness
                {
                    int immortalDeaths = getInt(this.serverPlayerEntity, DataTypeInt.ImmortalDeaths);
                    int deathsForNextBoni = ImmortalityStatus.DEATHS_FOR_BONUS_ARMOR_TOUGHNESS;
                    int boniToBeApplied = 0;
                    while (immortalDeaths >= deathsForNextBoni) {
                        //Increase Bonus Armor Toughness
                        boniToBeApplied++;
                        //Scale until, no longer enough Deaths
                        deathsForNextBoni *= ImmortalityStatus.DEATHS_FOR_BONUS_ARMOR_TOUGHNESS_SCALING;
                    }
                    //Apply Scaling to applied Armor Toughness
                    bonusArmorToughness *= boniToBeApplied;
                }


                int appliedBonusArmorToughness = ImmortalityStatus.immortalityHardeningArmorToughnessAddition * bonusArmorToughness;

                //Actually Applying
                EntityAttributeInstance armorToughnessInstance = this.serverPlayerEntity.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
                assert armorToughnessInstance != null;
                {
                    //Removing old Modifier
                    Set<EntityAttributeModifier> entityAttributeModifiers = armorToughnessInstance.getModifiers(EntityAttributeModifier.Operation.ADDITION);
                    EntityAttributeModifier oldArmorToughnessBonus = null;
                    for (EntityAttributeModifier entityAttributeModifier : entityAttributeModifiers) {
                        if (!entityAttributeModifier.getName().equals(ImmortalityStatus.BONUS_ARMOR_TOUGHNESS_KEY))
                            continue;
                        oldArmorToughnessBonus = entityAttributeModifier;
                        break;
                    }
                    //Could possibly not yet have one, so null check
                    if (oldArmorToughnessBonus != null) armorToughnessInstance.removeModifier(oldArmorToughnessBonus);
                }
                armorToughnessInstance.addPersistentModifier(new EntityAttributeModifier(ImmortalityStatus.BONUS_HEALTH_KEY, appliedBonusArmorToughness, EntityAttributeModifier.Operation.ADDITION));
            }
        }

        /**
         * TODO
         */
        private void checkSoulEnergy() {
            //TODO: This is a planned feature, using the new Item, and Soul Urn, so not yet to be implemented before Refactor is done
        }
/* Specific Player logic checks END
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------------------------------------------------------
*/

        private void doLivingEntityLogic() {
            if (livingEntity instanceof ServerPlayerEntity serverPlayer) {
                this.serverPlayerEntity = serverPlayer;
                doPlayerLogic();
                return;
            } else if (livingEntity instanceof ImmortalWither immortalWither) {
                if (!(dataType instanceof DataTypeInt)) return;
                EntityAttributeInstance maxHealthInstance = immortalWither.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                if (dataType == DataTypeInt.ImmortalDeaths) {
                    //Applying Health based on Heads left
                    int health_diff = (int) -((valueInt / 3f) * immortalWither.getMaxHealth());
                    assert maxHealthInstance != null;
                    {
                        //Removing old Modifier
                        Set<EntityAttributeModifier> entityAttributeModifiers = maxHealthInstance.getModifiers(EntityAttributeModifier.Operation.ADDITION);
                        EntityAttributeModifier oldHealthDeficit = null;
                        for (EntityAttributeModifier entityAttributeModifier : entityAttributeModifiers) {
                            if (!entityAttributeModifier.getName().equals(ImmortalityStatus.NEGATIVE_HEALTH_KEY))
                                continue;
                            oldHealthDeficit = entityAttributeModifier;
                            break;
                        }
                        if (oldHealthDeficit != null) maxHealthInstance.removeModifier(oldHealthDeficit);
                    }
                    maxHealthInstance.addPersistentModifier(new EntityAttributeModifier(ImmortalityStatus.NEGATIVE_HEALTH_KEY, health_diff, EntityAttributeModifier.Operation.ADDITION));
                }
            }
        }

        private void doItemStackLogic() {
            //TODO: not yet necessary but to be adjusted as needed
        }
    }
}
