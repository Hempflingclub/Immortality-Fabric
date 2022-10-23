package net.hempflingclub.immortality.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.hempflingclub.immortality.Immortality;
import net.hempflingclub.immortality.item.itemtypes.*;
import net.hempflingclub.immortality.statuseffect.ModEffectRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class ImmortalityItems {
    public static final Item HeartOfImmortality = registerItem("heart_of_immortality",
            new HeartOfImmortality(
                    new FabricItemSettings()
                            .group(net.hempflingclub.immortality.item.ItemGroup.Immortality)
                            .maxCount(1)
                            .fireproof()
                            .food(
                                    new FoodComponent.Builder()
                                            .hunger(0)
                                            .saturationModifier(0)
                                            .alwaysEdible()
                                            .meat()
                                            .build())
                            .rarity(Rarity.EPIC)));
    public static final Item VoidHeart = registerItem("void_heart",
            new VoidHeart(
                    new FabricItemSettings()
                            .group(ItemGroup.Immortality)
                            .maxCount(1)
                            .fireproof()
                            .food(
                                    new FoodComponent.Builder()
                                            .hunger(0)
                                            .saturationModifier(0)
                                            .alwaysEdible()
                                            .meat()
                                            .build())
                            .rarity(Rarity.EPIC)));

    public static final Item LiverOfImmortality = registerItem("liver_of_immortality"
            , new LiverOfImmortality(
                    new FabricItemSettings()
                            .group(ItemGroup.Immortality)
                            .maxCount(1)
                            .fireproof()
                            .food(
                                    new FoodComponent.Builder()
                                            .hunger(0)
                                            .saturationModifier(0)
                                            .alwaysEdible()
                                            .meat()
                                            .build())
                            .rarity(Rarity.RARE)));
    public static final Item HolyDagger = registerItem("holy_dagger",
            new HolyDagger(
                    new FabricItemSettings()
                            .group(ItemGroup.Immortality)
                            .maxCount(1)
                            .fireproof()
                            .maxDamage(1)
                            .rarity(Rarity.UNCOMMON)));
    public static final Item Trilogy = registerItem("trilogy", new Trilogy(new FabricItemSettings()
            .group(ItemGroup.Immortality)
            .maxCount(1)));
    public static final Item DoubleHearted = registerItem("double_hearted", new DoubleHearted(new FabricItemSettings()
            .group(ItemGroup.Immortality)
            .maxCount(1)));
    public static final Item LifeElixir = registerItem("life_elixir", new LifeElixir(new FabricItemSettings()
            .group(ItemGroup.Immortality)
            .rarity(Rarity.RARE)
            .maxCount(1)));
    public static final Item SemiImmortality = registerItem("semi_immortality", new SemiImmortality(new FabricItemSettings()
            .group(ItemGroup.Immortality)
            .maxCount(1)));
    public static final Potion LifeElixirPotion = registerPotion("life_elixir_potion", new Potion("life_elixir_potion", new StatusEffectInstance(ModEffectRegistry.life_elixir, 0)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Immortality.MOD_ID, name), item);
    }

    private static Potion registerPotion(String name, Potion potion) {
        return Registry.register(Registry.POTION, new Identifier(Immortality.MOD_ID, name), potion);
    }

    public static void registerModItems() {
        Immortality.LOGGER.debug("Registering Useable Items for " + Immortality.MOD_ID);
    }
}
