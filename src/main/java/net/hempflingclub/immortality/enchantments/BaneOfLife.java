package net.hempflingclub.immortality.enchantments;

import net.hempflingclub.immortality.util.ImmortalityData;
import net.hempflingclub.immortality.util.ImmortalityStatus;
import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.hempflingclub.immortality.util.ImmortalityStatus.getBool;

public class BaneOfLife extends DamageEnchantment {
    protected BaneOfLife(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type.ordinal(), slotTypes);
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return false;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public float getAttackDamage(int level, EntityGroup group) {
        if (group != EntityGroup.UNDEAD) {
            return Math.max(0, level * 3f);
        } else {
            return Math.max(0, level * 1.5f);
        }
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (!(target instanceof LivingEntity livingEntityTarget)) return;
        if (livingEntityTarget instanceof ServerPlayerEntity serverPlayerEntity)
            playerTargetDamaged(serverPlayerEntity, level);
        else if (livingEntityTarget.getGroup() != EntityGroup.UNDEAD) notUndeadTargetDamaged(livingEntityTarget, level);
    }

    private void playerTargetDamaged(ServerPlayerEntity serverPlayerEntity, int level) {
        boolean isDeltaImmortal = getBool(serverPlayerEntity, ImmortalityData.DataTypeBool.DeltaImmortality);
        boolean isGammaImmortal = getBool(serverPlayerEntity, ImmortalityData.DataTypeBool.GammaImmortality);
        boolean isBetaImmortal = getBool(serverPlayerEntity, ImmortalityData.DataTypeBool.BetaImmortality);
        boolean isAlphaImmortal = getBool(serverPlayerEntity, ImmortalityData.DataTypeBool.AlphaImmortality);
        boolean hasAnyUnaffectedImmortality = isDeltaImmortal ||
                isGammaImmortal ||
                isBetaImmortal ||
                isAlphaImmortal;
        if (hasAnyUnaffectedImmortality) return;
        //No Immortality
        serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 15 * 20, level - 1, true, true));
        serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 15 * 20, level - 1, true, true));
    }

    private void notUndeadTargetDamaged(LivingEntity livingEntity, int level) {
        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 15 * 20, level - 1, true, true));
        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 15 * 20, level - 1, true, true));
    }
}
