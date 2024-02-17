package net.hempflingclub.immortality.item.itemtypes;

import net.hempflingclub.immortality.statuseffect.ModEffectRegistry;
import net.hempflingclub.immortality.util.ImmortalityData.DataTypeInt;
import net.hempflingclub.immortality.util.ImmortalityStatus;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.List;

import static net.hempflingclub.immortality.util.ImmortalityStatus.addGeneric;
import static net.hempflingclub.immortality.util.ImmortalityStatus.getInt;

public class LifeElixir extends Item {
    private static final int MAX_USE_TIME = 96;

    public LifeElixir(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!(user instanceof ServerPlayerEntity serverPlayerEntity)) return stack;
        //Check if Life Elixir is active
        int lifeElixirCooldown = getInt(serverPlayerEntity, DataTypeInt.LifeElixirCooldown);
        if (lifeElixirCooldown > 0) return stack;
        //Consume Bottle and Update Stats
        Items.POTION.finishUsing(stack, world, serverPlayerEntity);
        Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
        serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        //Set Life Elixir Cooldown
        addGeneric(serverPlayerEntity, DataTypeInt.LifeElixirCooldown, ImmortalityStatus.LIFE_ELIXIR_SECONDS_TO_FINISH);
        //Give Approximate Effect Time
        serverPlayerEntity.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.life_elixir, ImmortalityStatus.LIFE_ELIXIR_SECONDS_TO_FINISH * 20));
        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        }
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return MAX_USE_TIME;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("immortality.tooltip.item.life_elixir_1").formatted(Formatting.RED));
        tooltip.add(Text.translatable("immortality.tooltip.item.life_elixir_2").formatted(Formatting.RED));
        tooltip.add(Text.translatable("immortality.tooltip.item.life_elixir_3").formatted(Formatting.RED));
        tooltip.add(Text.translatable("immortality.tooltip.item.life_elixir_4").formatted(Formatting.RED));
    }
}
