package net.hempflingclub.immortality.util;

import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class IImmortalityItemComponentImpl extends ItemComponent implements IImmortalityItemComponent {
    private NbtCompound nbtData;

    public IImmortalityItemComponentImpl(ItemStack stack) {
        super(stack);
    }

    @Override
    public void setItemData(NbtCompound nbt) {
        this.nbtData = nbt;
    }

    @Override
    public NbtCompound getItemData() {
        if (this.nbtData == null) {
            this.nbtData = new NbtCompound();
        }
        return this.nbtData;
    }
}
