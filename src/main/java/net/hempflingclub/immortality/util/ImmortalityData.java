package net.hempflingclub.immortality.util;

import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class ImmortalityData {
    public enum DataTypeBool {
        TrueImmortality,
        Immortality,
        SemiImmortality,
        FalseImmortality,
        VoidHeart,
        ExistingSoulVial,
        LiverExtractedEver,
        LiverCurrentlyExtracted,


        
    }
    /*Unneeded as SoulBound mechanic will be discontinued for a SoulVial esc one
    public enum DataTypeUUID {

    }*/
    public enum DataTypeInt{
        ImmortalDeaths,
        SemiImmortalityHeartCooldown,
        KilledByBaneOfLifeTime,
        KilledByBaneOfLifeCurrentAmount,
        LiverExtractionAmount,
        LiverExtractionTime,
        HeartExtractionAmount,
        LifeElixirTime,
        LifeElixirDropCooldown,
        BonusHearts,
        TemporaryNegativeHearts, //Logically decide based on if Semi/HeartCooldown or False Immortal if to regen or keep
        BonusArmor,
        BonusArmorToughness,
        SoulEnergy

    }
    public enum DataTypeDouble{

    }
    public static class DataTypes {
        private IImmortalityPlayerComponent player;
        private DataTypeBool dataType;
        private boolean state; // need atleast boolean UUID int double

        /**
         * Will set the provided state
         * @param dataType
         * @param state
         */
        public DataTypes(@NotNull IImmortalityPlayerComponent playerData, DataTypeBool dataType, boolean state) {
            this.player = playerData;
            this.dataType = dataType;
            this.state = state;
            //TODO: code to actually set the provided state
        }

        /**
         * will get the current state
         * @param dataType
         */
        public DataTypes(@NotNull IImmortalityPlayerComponent playerData, DataTypeBool dataType) {
            this.player = playerData;
            this.dataType = dataType;
            state = refreshState();
        }

        public DataTypeBool getDataType() {
            return dataType;
        }

        public boolean isState() {
            return state;
        }
        public boolean refreshState(){
            //TODO: refresh state
            state=false; //TODO: New State
            return this.state;
        }
    }

/*
TODO: Rework to run with Immortal Deaths
 */
    public static void setImmortalWitherDeaths(@NotNull IImmortalityLivingEntityComponent livingEntityComponent, int deaths) {
        NbtCompound nbt = livingEntityComponent.getLivingEntityData();
        nbt.putInt("immortalWitherDeaths", deaths);
        livingEntityComponent.setLivingEntityData(nbt);
    }
}
