package com.hempflingclub.immortality.mixin;

import com.hempflingclub.immortality.gui.ImmortalityScreen;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class OnKeyPressMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(at = @At("HEAD"), method = "keyPress")
    public void keyPress(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        // Minecraft Screen null means normal InGame Menu (walking around)
        if (key == 'Y') if (minecraft.level != null && minecraft.screen == null) {
            assert minecraft.player != null;
            minecraft.player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200));
            minecraft.setScreen(new ImmortalityScreen());
        }
    }
}