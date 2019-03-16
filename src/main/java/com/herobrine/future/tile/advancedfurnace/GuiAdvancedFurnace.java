package com.herobrine.future.tile.advancedfurnace;

import com.herobrine.future.blocks.FurnaceAdvanced;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiAdvancedFurnace extends GuiContainer {
    private static final int WIDTH = 176;
    private static final int HEIGHT = 166;
    private static TileEntityAdvancedFurnace te;

    private static final ResourceLocation background = new ResourceLocation("textures/gui/container/furnace.png");

    public GuiAdvancedFurnace(ContainerAdvancedFurnace container) {
        super(container);
        te = container.te;

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(background);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, WIDTH, HEIGHT);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if(te.getType() == FurnaceAdvanced.FurnaceType.SMOKER) {
            this.fontRenderer.drawString("Smoker", 8, 6, 4210752);
            this.fontRenderer.drawString("Inventory", 8, this.ySize - 40, 4210752);
        }
        if(te.getType() == FurnaceAdvanced.FurnaceType.BLAST_FURNACE) {
            this.fontRenderer.drawString("Blast Furnace", 8, 6, 4210752);
            this.fontRenderer.drawString("Inventory", 8, this.ySize -40, 4210752);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
