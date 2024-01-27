package net.hempflingclub.immortality.util;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.hempflingclub.immortality.Immortality;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public interface IImmortalityItemComponent extends ComponentV3, IImmortalityComponent {
    ComponentKey<IImmortalityItemComponent> KEY = ComponentRegistry.getOrCreate(new Identifier(Immortality.MOD_ID, "item-data"), IImmortalityItemComponent.class);

    void setItemData(NbtCompound nbt);

    NbtCompound getItemData();
}
