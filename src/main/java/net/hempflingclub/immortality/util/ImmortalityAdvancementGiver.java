package net.hempflingclub.immortality.util;

import net.hempflingclub.immortality.Immortality;
import net.hempflingclub.immortality.util.ImmortalityData.DataTypeBool;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Objects;

import static net.hempflingclub.immortality.util.ImmortalityStatus.getBool;

public final class ImmortalityAdvancementGiver {
    public static final Identifier root = new Identifier(Immortality.MOD_ID, "immortality/root");
    public static final Identifier doubleHearted = new Identifier(Immortality.MOD_ID, "immortality/double_hearted");
    public static final Identifier immortality = new Identifier(Immortality.MOD_ID, "immortality/heart_of_immortality");
    public static final Identifier holyDagger = new Identifier(Immortality.MOD_ID, "immortality/holy_dagger");
    public static final Identifier falseImmortality = new Identifier(Immortality.MOD_ID, "immortality/liver_of_immortality");
    public static final Identifier semi_immortality = new Identifier(Immortality.MOD_ID, "immortality/semi_immortality");
    public static final Identifier trueImmortality = new Identifier(Immortality.MOD_ID, "immortality/trilogy");
    public static final Identifier voidHeart = new Identifier(Immortality.MOD_ID, "immortality/void_heart");
    public static final Identifier soulBound = new Identifier(Immortality.MOD_ID, "immortality/soul_bound");
    public static final Identifier lifeElixir = new Identifier(Immortality.MOD_ID, "immortality/life_elixir");
    public static final Identifier immortalWitherSlayer = new Identifier(Immortality.MOD_ID, "immortality/immortal_wither_slayer");

    public static void giveImmortalityAchievements(PlayerEntity playerEntity) {
        boolean isDeltaImmortal = getBool(playerEntity, DataTypeBool.DeltaImmortality);
        boolean isGammaImmortal = getBool(playerEntity, DataTypeBool.GammaImmortality);
        boolean isBetaImmortal = getBool(playerEntity, DataTypeBool.BetaImmortality);
        boolean isAlphaImmortal = getBool(playerEntity, DataTypeBool.AlphaImmortality);
        boolean isVoidHearted = getBool(playerEntity, DataTypeBool.VoidHeart);
        //Needs any of those
        if (!(isDeltaImmortal ||
                isGammaImmortal ||
                isBetaImmortal ||
                isAlphaImmortal ||
                isVoidHearted)) return;
        achievementGranter(playerEntity, root);
        if (isVoidHearted) achievementGranter(playerEntity, voidHeart);
        if (isDeltaImmortal) achievementGranter(playerEntity, falseImmortality);
        if (isGammaImmortal) achievementGranter(playerEntity, semi_immortality);
        if (isBetaImmortal) {
            achievementGranter(playerEntity, immortality);
            if (isVoidHearted)
                achievementGranter(playerEntity, doubleHearted);
        }
        if (isAlphaImmortal) achievementGranter(playerEntity, trueImmortality);
    }

    public static void giveHolyDaggerAchievement(PlayerEntity playerEntity) {
        achievementGranter(playerEntity, holyDagger);
    }

    public static void giveSoulBoundAchievement(PlayerEntity playerEntity) {
        achievementGranter(playerEntity, soulBound);
    }

    public static void giveLifeElixirAchievement(PlayerEntity playerEntity) {
        achievementGranter(playerEntity, root);
        achievementGranter(playerEntity, lifeElixir);
    }

    public static void giveImmortalWitherSlayer(PlayerEntity playerEntity) {
        achievementGranter(playerEntity, root);
        achievementGranter(playerEntity, immortalWitherSlayer);
    }

    private static void achievementGranter(PlayerEntity playerEntity, Identifier achievement) {
        Objects.requireNonNull(playerEntity.getServer()).getPlayerManager().getAdvancementTracker((ServerPlayerEntity) playerEntity).grantCriterion(playerEntity.getServer().getAdvancementLoader().get(achievement), "placeholder");
    }


}
