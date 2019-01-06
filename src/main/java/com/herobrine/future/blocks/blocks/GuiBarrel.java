package com.herobrine.future.blocks.blocks;

import com.herobrine.future.utils.init;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiBarrel extends GuiContainer {
    public static final int WIDTH = 177;
    public static final int HEIGHT = 166;

    private static final ResourceLocation background = new ResourceLocation(init.MODID, "textures/gui/gui.png");

    public GuiBarrel(TileEntityBarrel tileEntity, ContainerBarrel container) {
        super(container);

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString("Barrel", 8, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 92, 4210752);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
