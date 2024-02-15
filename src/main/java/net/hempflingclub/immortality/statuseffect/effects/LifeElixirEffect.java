package net.hempflingclub.immortality.statuseffect.effects;

import net.hempflingclub.immortality.statuseffect.ModStatusEffect;
import net.hempflingclub.immortality.util.ImmortalityAdvancementGiver;
import net.hempflingclub.immortality.util.ImmortalityStatus;
import net.minecraft.client.report.ReporterEnvironment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.network.ServerPlayerEntity;

public class LifeElixirEffect extends ModStatusEffect {
    public LifeElixirEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        // Specific Logic is now in ImmortalityStatus
        if (entity instanceof ServerPlayerEntity serverPlayerEntity)
            ImmortalityAdvancementGiver.giveImmortalityAchievements(serverPlayerEntity);
    }
}
