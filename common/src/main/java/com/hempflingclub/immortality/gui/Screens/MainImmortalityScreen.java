package com.hempflingclub.immortality.gui.Screens;

import com.hempflingclub.immortality.gui.BaseScreen;
import com.hempflingclub.immortality.gui.ScreenI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;

public class MainImmortalityScreen extends BaseScreen implements ScreenI {
    public MainImmortalityScreen() {
        super();
    }

    @Override
    public void init(Minecraft minecraft, BaseScreen callback) {
        super.init(minecraft, callback);
        Button buttonWidget = Button.builder(Component.literal("Hello World"), (btn) -> {
                    // When the button is clicked, we can display a toast to the screen.
                    minecraft.getToastManager().addToast(
                            new SystemToast(SystemToast.SystemToastId.NARRATOR_TOGGLE,
                                    Component.literal("Test Button"),
                                    Component.literal("Hello " + minecraft.level.random.nextIntBetweenInclusive(1, 10)))
                    );
                }).bounds(xStart + 10, (int) (yStart + (10 * ratio) * 2), 60, (int) (60 * ratio))
                .build();
        callback.addRenderableWidget(buttonWidget);
    }

    @Override
    public void renderForeground(Minecraft minecraft, BaseScreen callback, GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        String text = "Immortality Menu";
        Font font = minecraft.gui.getFont();
        int textWidth = font.width(text);
        guiGraphics.drawString(font, text, xStart + (xSize - textWidth) / 2, yStart + 8, FONT_COLOR);
    }
}
