package com.hempflingclub.immortality.gui;

import com.hempflingclub.immortality.CommonClass;
import com.hempflingclub.immortality.Constants;
import com.mojang.blaze3d.buffers.BufferUsage;
import com.mojang.blaze3d.opengl.VertexArrayCache;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public abstract class BaseScreen extends Screen implements ScreenI {
    public static final Component TITLE = Component.literal("Immortality Menu");

    public static final int BACKGROUND_COLOR = 0xFF474747; //Actually 474747 (but needs to be left padded for some reason)
    public static final int FONT_COLOR = 0xFFb00b69; //Actually b00b69
    public static final int WHITE_COLOR = 0xFFc6c6c6; //Actually c6c6c6 (default menu white)

    protected int xStart; //Will be at topLeft Corner
    protected int yStart; //Will be at topLeft Corner
    protected int xSize;
    protected int ySize;
    protected double ratio;

    public BaseScreen() {
        this(TITLE);
    }

    protected BaseScreen(Component title) {
        super(title);
    }

    @Override
    public void tick() {
        this.tick(minecraft, this);
    }

    @Override
    public void tick(Minecraft minecraft, BaseScreen callback) {
        super.tick();
    }

    @Override
    protected void init() {
        this.init(minecraft, this);
    }

    @Override
    public void init(Minecraft minecraft, BaseScreen callback) {
        clearWidgets();
        super.init();

        double offsetPercent = 0.25;

        xSize = (int) (minecraft.getWindow().getGuiScaledWidth() * (1 - offsetPercent * 2));
        ySize = (int) (minecraft.getWindow().getGuiScaledHeight() * (1 - offsetPercent * 2));

        xStart = (int) (minecraft.getWindow().getGuiScaledWidth() * offsetPercent);
        yStart = (int) (minecraft.getWindow().getGuiScaledHeight() * offsetPercent);

        ratio = minecraft.getWindow().getGuiScaledHeight() / (double) minecraft.getWindow().getGuiScaledWidth();
    }

    protected boolean isIngame() {
        assert minecraft != null;
        return minecraft.level != null;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float deltaPartialTickTime) {
        renderTransparentBackground(guiGraphics);

        this.renderBackground(guiGraphics, mouseX, mouseY, deltaPartialTickTime);
        super.render(guiGraphics, mouseX, mouseY, deltaPartialTickTime);
        this.renderForeground(guiGraphics, mouseX, mouseY, deltaPartialTickTime);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(minecraft, this, guiGraphics, mouseX, mouseY, delta);
    }

    public void renderBackground(Minecraft minecraft, BaseScreen callback, GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        guiGraphics.fill(xStart, yStart, xStart + xSize, yStart + ySize, WHITE_COLOR);
        guiGraphics.renderOutline(xStart, yStart, xSize, ySize, BACKGROUND_COLOR);
    }


    public void renderForeground(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderForeground(minecraft, this, guiGraphics, mouseX, mouseY, delta);
    }

    public void renderForeground(Minecraft minecraft, BaseScreen callback, GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
    }

    @Override
    public <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget) {
        return super.addRenderableWidget(widget);
    }

}
