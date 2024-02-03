package net.hempflingclub.immortality.util;

import net.minecraft.nbt.NbtCompound;

public final class ImmortalityData {
    public interface DataType {
    }

    public enum DataTypeBool implements DataType {
        TrueImmortality,
        Immortality,
        SemiImmortality,
        FalseImmortality,
        ImmortalHeart,
        VoidHeart,
        ExistingSoulVial,
        LiverExtractedEver,
        LiverCurrentlyExtracted,


    }

    /*Unneeded as SoulBound mechanic will be discontinued for a SoulVial esc one
    public enum DataTypeUUID {

    }*/
    public enum DataTypeInt implements DataType {
        ImmortalDeaths,
        SemiImmortalityHeartCooldownSeconds,
        KilledByBaneOfLifeTime,
        KilledByBaneOfLifeCurrentAmount,
        LiverExtractionAmount,
        LiverExtractionCooldown,
        LifeElixirTime,
        LifeElixirDropCooldown,
        BonusHearts,
        TemporaryNegativeHearts, //Logically decide based on if Semi/HeartCooldown or False Immortal if to regen or keep
        BonusArmor,
        BonusArmorToughness,
        SoulEnergy

    }

    public static class DataTypes implements DataType {
        private final IImmortalityComponent something; // Player, Entity, Item
        private final DataType dataType;
        private boolean stateBool;
        private int stateInt;

        /**
         * Will set it's state to the current state
         *
         * @param something Player, Entity, Item
         * @param dataType  ImmortalityData.DataType (or Boolean/Int variant)
         */
        public DataTypes(IImmortalityComponent something, DataType dataType) {
            this.something = something;
            this.dataType = dataType;
            refresh();
        }

        /**
         * Will Use Parameters to override state
         *
         * @param something Player, Entity, Item
         * @param dataType  ImmortalityData.DataType (or Boolean variant)
         * @param stateBool new State
         */
        public DataTypes(IImmortalityComponent something, DataType dataType, boolean stateBool) {
            this.something = something;
            this.dataType = dataType;
            this.stateBool = stateBool;
        }

        /**
         * Will Use Parameters to override state
         *
         * @param something Player, Entity, Item
         * @param dataType  ImmortalityData.DataType (or Int variant)
         * @param stateInt  new State
         */
        public DataTypes(IImmortalityComponent something, DataType dataType, int stateInt) {
            this.something = something;
            this.dataType = dataType;
            this.stateInt = stateInt;
        }

        private NbtCompound getNBTCompound() {
            NbtCompound nbt = null;
            if (something instanceof IImmortalityPlayerComponent iiImmortalityPlayerComponent)
                nbt = iiImmortalityPlayerComponent.getPlayerData();
            else if (something instanceof IImmortalityLivingEntityComponent iiImmortalityLivingEntityComponent)
                nbt = iiImmortalityLivingEntityComponent.getLivingEntityData();
            else if (something instanceof IImmortalityItemComponent iiImmortalityItemComponent)
                nbt = iiImmortalityItemComponent.getItemData();
            return nbt;
        }

        public void refresh() {
            NbtCompound nbt = getNBTCompound();
            if (dataType instanceof DataTypeBool dataTypeBool)
                this.stateBool = nbt.getBoolean(ImmortalityData.getKey(dataTypeBool));
            else if (dataType instanceof DataTypeInt dataTypeInt)  // Not needed but good for future additions
                this.stateInt = nbt.getInt(ImmortalityData.getKey(dataTypeInt));
        }

        /**
         * Will only set if the Enum is DataTypeInt
         *
         * @param toSet
         * @return will return the int the game has saved, or -1 if invalid Enum
         */
        public int set(int toSet) {
            if (!(dataType instanceof DataTypeInt)) return -1;
            DataTypeInt dataType = (DataTypeInt) this.dataType;
            NbtCompound nbtCompound = getNBTCompound();
            String key = ImmortalityData.getKey(dataType);
            nbtCompound.putInt(key, toSet);
            return readInt();
        }

        /**
         * Will only set if the Enum is DataTypeBool
         *
         * @param toSet
         * @return will return the bool the game has saved, or false if invalid Enum
         */
        public boolean set(boolean toSet) {
            if (!(dataType instanceof DataTypeBool)) return false;
            DataTypeBool dataType = (DataTypeBool) this.dataType;
            NbtCompound nbtCompound = getNBTCompound();
            String key = ImmortalityData.getKey(dataType);
            nbtCompound.putBoolean(key, toSet);
            return readBool();
        }

        public int readInt() {
            refresh();
            return this.stateInt;
        }

        public boolean readBool() {
            refresh();
            return this.stateBool;
        }

        public IImmortalityComponent getIImmortalityComponent() {
            return something;
        }
    }

    static String getKey(DataType dataType) {
        return dataType instanceof DataTypeBool dataTypeBool
                ? dataTypeBool.toString()
                : dataType instanceof DataTypeInt dataTypeInt
                ? dataTypeInt.toString()
                : "";
    }

    static String getKey(DataTypeBool dataTypeBool) {
        return getKey((DataType) dataTypeBool);
    }

    static String getKey(DataTypeInt dataTypeInt) {
        return getKey((DataType) dataTypeInt);
    }
}
