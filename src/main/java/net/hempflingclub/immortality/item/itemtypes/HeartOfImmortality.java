package net.hempflingclub.immortality.item.itemtypes;

import net.hempflingclub.immortality.Immortality;
import net.hempflingclub.immortality.item.ImmortalityItems;
import net.hempflingclub.immortality.util.ImmortalityAdvancementGiver;
import net.hempflingclub.immortality.util.ImmortalityData;
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
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

import static net.hempflingclub.immortality.util.ImmortalityStatus.toggleGeneric;

public class HeartOfImmortality extends Item {
    public HeartOfImmortality(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity player) {
        if (player instanceof ServerPlayerEntity serverPlayerEntity) {
            //Unlock Recipe Debug
            Identifier[] recipes = new Identifier[4];
            recipes[0] = new Identifier(Immortality.MOD_ID, "immortal_essence");
            recipes[1] = new Identifier(Immortality.MOD_ID, "liver_of_immortality");
            recipes[2] = new Identifier(Immortality.MOD_ID, "summoning_sigil");
            recipes[3] = new Identifier(Immortality.MOD_ID, "holy_dagger");
            serverPlayerEntity.unlockRecipes(recipes);
            serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 0, false, false));
            serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 50, 0, false, false));
            //Give Immortal Heart
            toggleGeneric(serverPlayerEntity, ImmortalityData.DataTypeBool.ImmortalHeart);
            //Give Achievements
            ImmortalityAdvancementGiver.giveImmortalityAchievements(serverPlayerEntity);
        } else if (player instanceof PlayerEntity playerEntity && playerEntity.world.isClient) {
            //Client
            //Give Feedback if worked
            if (ImmortalityStatus.getBool(playerEntity, ImmortalityData.DataTypeBool.ImmortalHeart))
                MinecraftClient.getInstance().gameRenderer.showFloatingItem(new ItemStack(ImmortalityItems.HeartOfImmortality));
            return new ItemStack(stack.getItem());
        }
        return super.finishUsing(stack, world, player);
    }
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("immortality.tooltip.item.heart_of_immortality_1").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.heart_of_immortality_2").formatted(Formatting.DARK_PURPLE));
    }
}

