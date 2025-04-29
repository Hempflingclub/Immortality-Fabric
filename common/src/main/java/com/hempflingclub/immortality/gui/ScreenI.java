package com.hempflingclub.immortality.gui;

import net.minecraft.client.gui.GuiGraphics;

public interface ScreenI {
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta);
    public void tick();
}
