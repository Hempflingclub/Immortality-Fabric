package net.hempflingclub.immortality.item.itemtypes;

import net.hempflingclub.immortality.util.ImmortalityData;
import net.hempflingclub.immortality.util.ImmortalityStatus;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

import static net.hempflingclub.immortality.util.ImmortalityStatus.addGeneric;
import static net.hempflingclub.immortality.util.ImmortalityStatus.incrementGeneric;

public class SoulStone extends Item {
    public static int MAX_CAPACITY = 0xFF;

    public SoulStone(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        //Testing only, just test if increasing works
        int soulEnergy = incrementGeneric(itemStack, ImmortalityData.DataTypeInt.SoulEnergy);
        if (soulEnergy > MAX_CAPACITY) addGeneric(itemStack, ImmortalityData.DataTypeInt.SoulEnergy, -1);
        return TypedActionResult.success(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("immortality.tooltip.item.soul_stone_1",
                        (ImmortalityStatus.getInt(itemStack, ImmortalityData.DataTypeInt.SoulEnergy)))
                .formatted(Formatting.RED));
    }
}
