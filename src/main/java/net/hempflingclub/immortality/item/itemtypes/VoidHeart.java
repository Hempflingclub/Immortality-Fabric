package net.hempflingclub.immortality.item.itemtypes;

import net.hempflingclub.immortality.Immortality;
import net.hempflingclub.immortality.item.ImmortalityItems;
import net.hempflingclub.immortality.util.ImmortalityAdvancementGiver;
import net.hempflingclub.immortality.util.ImmortalityData;
import net.hempflingclub.immortality.util.ImmortalityData.DataTypeBool;
import net.hempflingclub.immortality.util.ImmortalityStatus;
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
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

import static net.hempflingclub.immortality.util.ImmortalityStatus.*;

public class VoidHeart extends Item {
    public VoidHeart(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity player) {
        //Client Display of VoidHeart
        if (player instanceof PlayerEntity playerEntity && playerEntity.world.isClient)
            if (getBool(playerEntity, DataTypeBool.VoidHeart))
                MinecraftClient.getInstance().gameRenderer.showFloatingItem(new ItemStack(ImmortalityItems.VoidHeart));
        if (!(player instanceof ServerPlayerEntity serverPlayerEntity)) return stack;
        //Server
        boolean hasVoidHeart = getBool(serverPlayerEntity, DataTypeBool.VoidHeart);
        if (hasVoidHeart) return stack;
        //Toggle Void Heart
        toggleGeneric(serverPlayerEntity, DataTypeBool.VoidHeart);
        //Debug Crafting Feedback
        serverPlayerEntity.sendMessage(Text.literal("You consume the Void."), true);
        world.playSoundFromEntity(null, serverPlayerEntity, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 1, 1);
        Identifier[] recipes = new Identifier[4];
        recipes[0] = new Identifier(Immortality.MOD_ID, "immortal_essence");
        recipes[1] = new Identifier(Immortality.MOD_ID, "liver_of_immortality");
        recipes[2] = new Identifier(Immortality.MOD_ID, "summoning_sigil");
        recipes[3] = new Identifier(Immortality.MOD_ID, "holy_dagger");
        serverPlayerEntity.unlockRecipes(recipes);
        //Feedback Effects
        serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 0, false, false));
        serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 50, 0, false, false));
        ImmortalityAdvancementGiver.giveImmortalityAchievements(serverPlayerEntity);
        //Consume Item
        return super.finishUsing(stack, world, serverPlayerEntity);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("immortality.tooltip.item.void_heart_1").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.void_heart_2").formatted(Formatting.LIGHT_PURPLE));
    }
}
