package com.hempflingclub.immortality.gui;

import com.hempflingclub.immortality.gui.Screens.MainImmortalityScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

public class ImmortalityScreen extends BaseScreen {
    private BaseScreen currentScreen;

    public ImmortalityScreen() {
        super();
        this.currentScreen = new MainImmortalityScreen(); // Will also call its init
    }

    @Override
    public void init() {
        this.currentScreen.init(minecraft, this);
    }

    @Override
    public void tick() {
        this.currentScreen.tick(minecraft, this);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.currentScreen.renderBackground(minecraft, this, guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public void renderForeground(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.currentScreen.renderForeground(minecraft, this, guiGraphics, mouseX, mouseY, delta);
    }

}
