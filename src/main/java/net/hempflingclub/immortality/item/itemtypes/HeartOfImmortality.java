package net.hempflingclub.immortality.item.itemtypes;

import net.hempflingclub.immortality.item.UsableItems;
import net.hempflingclub.immortality.util.IPlayerDataSaver;
import net.hempflingclub.immortality.util.ImmortalityData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class HeartOfImmortality extends Item {
    public HeartOfImmortality(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity player) {
        if (!world.isClient()) {
            //Server
            ImmortalityData.setImmortality(((IPlayerDataSaver) player), !ImmortalityData.getImmortality((IPlayerDataSaver) player));
            boolean status = ImmortalityData.getImmortality((IPlayerDataSaver) player);
            if (status) {
                ((PlayerEntity) player).sendMessage(Text.literal("You have achieved Immortality."), true);
                world.playSoundFromEntity(null, player, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 1, 1);
                player.setHealth(player.getMaxHealth());
            } else {
                world.playSoundFromEntity(null, player, SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.PLAYERS, 1, 1);
                player.setHealth(1);
                player.damage(new DamageSource(Text.translatable("immortality", player.getName()).getString()).setBypassesArmor().setBypassesProtection().setUnblockable(),
                        2000000000);
                ImmortalityData.setImmortalDeaths((IPlayerDataSaver) player, 0);
            }
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 0, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 50, 0, false, false));
        } else {
            //Client
            MinecraftClient.getInstance().gameRenderer.showFloatingItem(new ItemStack(UsableItems.HeartOfImmortality));
        }
        return super.finishUsing(stack, world, player);
    }
}