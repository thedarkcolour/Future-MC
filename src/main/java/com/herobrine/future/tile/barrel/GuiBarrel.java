package com.herobrine.future.tile.barrel;

import com.herobrine.future.init.Init;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiBarrel extends GuiContainer {
    public final InventoryPlayer playerInventory;
    public final TileBarrel te;
    private static final int WIDTH = 176;
    private static final int HEIGHT = 167;

    private static final ResourceLocation background = new ResourceLocation(Init.MODID, "textures/gui/gui.png");

    public GuiBarrel(ContainerBarrel container) {
        super(container);
        this.te = container.te;
        this.playerInventory = container.playerInventory;

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(I18n.format("container.Barrel"), 8, 6, 4210752);
        this.fontRenderer.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 92, 4210752);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}