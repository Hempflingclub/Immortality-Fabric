package net.hempflingclub.immortality.item.itemtypes;

import net.hempflingclub.immortality.item.ImmortalityItems;
import net.hempflingclub.immortality.util.ImmortalityAdvancementGiver;
import net.hempflingclub.immortality.util.ImmortalityData.DataTypeBool;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

import static net.hempflingclub.immortality.util.ImmortalityStatus.getBool;
import static net.hempflingclub.immortality.util.ImmortalityStatus.toggleGeneric;

public class LiverOfImmortality extends Item {
    public LiverOfImmortality(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity player) {
        if (player instanceof PlayerEntity playerEntity && playerEntity.world.isClient) {
            boolean isDeltaImmortal = getBool(playerEntity, DataTypeBool.DeltaImmortality);
            //Client
            if (isDeltaImmortal)
                MinecraftClient.getInstance().gameRenderer.showFloatingItem(new ItemStack(ImmortalityItems.LiverOfImmortality));
        }
        if (!(player instanceof ServerPlayerEntity serverPlayerEntity)) return stack;
        //Server
        boolean isDeltaImmortal = getBool(player, DataTypeBool.DeltaImmortality);
        //Don't do shit if already Delta Immortal
        if (isDeltaImmortal) return stack;
        //Use Stack
        super.finishUsing(stack, world, serverPlayerEntity);
        //Give Delta Immortality (edge cases will be handled in ImmortalityStatus
        toggleGeneric(serverPlayerEntity, DataTypeBool.DeltaImmortality);
        //Give Feedback Effects
        //Sounds + Heal
        world.playSoundFromEntity(null, serverPlayerEntity, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 1, 1);
        serverPlayerEntity.setHealth(serverPlayerEntity.getMaxHealth());
        //Effects + Achievements
        serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 0, false, false));
        serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 50, 0, false, false));
        ImmortalityAdvancementGiver.giveImmortalityAchievements(serverPlayerEntity);
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("immortality.tooltip.item.liver_of_immortality_1").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.liver_of_immortality_2").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.liver_of_immortality_3").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.liver_of_immortality_4").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.liver_of_immortality_5").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.liver_of_immortality_6").formatted(Formatting.DARK_PURPLE));
    }
}
