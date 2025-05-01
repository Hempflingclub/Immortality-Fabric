package com.hempflingclub.immortality.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

public interface ScreenI {
    void init(Minecraft minecraft, BaseScreen callback);
    void tick(Minecraft minecraft, BaseScreen callback);
    void renderBackground(Minecraft minecraft, BaseScreen callback, GuiGraphics guiGraphics, int mouseX, int mouseY, float delta);
    void renderForeground(Minecraft minecraft, BaseScreen callback,GuiGraphics guiGraphics, int mouseX, int mouseY, float delta);
    <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget);
}
