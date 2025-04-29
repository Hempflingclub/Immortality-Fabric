package com.hempflingclub.immortality.gui;

import com.hempflingclub.immortality.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.Items;

public class ImmortalityScreen extends BaseScreen implements ScreenI {
    public ImmortalityScreen() {
        super();
    }

    @Override
    protected void init() {
        super.init();
        Button buttonWidget = Button.builder(Component.literal("Hello World"), (btn) -> {
                    // When the button is clicked, we can display a toast to the screen.
                    minecraft.getToastManager().addToast(
                            new SystemToast(SystemToast.SystemToastId.NARRATOR_TOGGLE,
                                    Component.literal("Test Button"),
                                    Component.literal("Hello " + minecraft.level.random.nextIntBetweenInclusive(1, 10)))
                    );
                }).bounds(xStart + 10, (int) (yStart + (10 * ratio)*2), 60, (int) (60 * ratio))
                .build();
        this.addRenderableWidget(buttonWidget);
    }

    @Override
    public void renderForeground(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        String text = "Immortality Menu";
        int textWidth = font.width(text);
        guiGraphics.drawString(font, text, xStart + (xSize - textWidth) / 2, yStart + 8, FONT_COLOR);
    }
}
