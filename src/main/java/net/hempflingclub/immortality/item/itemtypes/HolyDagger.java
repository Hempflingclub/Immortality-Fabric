package net.hempflingclub.immortality.item.itemtypes;

import net.hempflingclub.immortality.util.ImmortalityData.DataTypeBool;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

import static net.hempflingclub.immortality.util.ImmortalityStatus.getBool;
import static net.hempflingclub.immortality.util.ImmortalityStatus.toggleGeneric;
import static net.minecraft.util.TypedActionResult.consume;
import static net.minecraft.util.TypedActionResult.fail;

public class HolyDagger extends Item {
    public HolyDagger(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemStack = playerEntity.getStackInHand(hand);
        if (!(playerEntity instanceof ServerPlayerEntity serverPlayerEntity)) return consume(itemStack);
        boolean isGammaImmortal = getBool(serverPlayerEntity, DataTypeBool.GammaImmortality);
        boolean isBetaImmortal = getBool(serverPlayerEntity, DataTypeBool.BetaImmortality);
        boolean isAlphaImmortal = getBool(serverPlayerEntity, DataTypeBool.AlphaImmortality);
        boolean isExtracted = getBool(serverPlayerEntity, DataTypeBool.LiverCurrentlyExtracted);
        boolean neededImmortality = isGammaImmortal||isBetaImmortal||isAlphaImmortal;
        boolean extractable = !isExtracted && neededImmortality;
        if(!extractable) return fail(itemStack);
        //Toggle Liver Extraction
        //The Following logic should happen in ImmortalityStatus
        toggleGeneric(serverPlayerEntity,DataTypeBool.LiverCurrentlyExtracted);
        int damageToItem = 1;
        //Stop Alpha and higher types of Immortality from consuming Holy Dagger
        if(isAlphaImmortal) damageToItem=0;
        itemStack.damage(damageToItem,serverPlayerEntity,x -> {});
        return TypedActionResult.success(itemStack);
    }

            @Override
            public void appendTooltip (ItemStack itemStack, World world, List < Text > tooltip, TooltipContext context){
                tooltip.add(Text.translatable("immortality.tooltip.item.holy_dagger_1").formatted(Formatting.LIGHT_PURPLE));
                tooltip.add(Text.translatable("immortality.tooltip.item.holy_dagger_2").formatted(Formatting.LIGHT_PURPLE));
                tooltip.add(Text.translatable("immortality.tooltip.item.holy_dagger_3").formatted(Formatting.LIGHT_PURPLE));
                tooltip.add(Text.translatable("immortality.tooltip.item.holy_dagger_4").formatted(Formatting.DARK_PURPLE));
                tooltip.add(Text.translatable("immortality.tooltip.item.holy_dagger_5").formatted(Formatting.DARK_PURPLE));
                tooltip.add(Text.translatable("immortality.tooltip.item.holy_dagger_6").formatted(Formatting.DARK_PURPLE));
                tooltip.add(Text.translatable("immortality.tooltip.item.holy_dagger_7").formatted(Formatting.LIGHT_PURPLE));
                tooltip.add(Text.translatable("immortality.tooltip.item.holy_dagger_8").formatted(Formatting.LIGHT_PURPLE));
            }
        }
