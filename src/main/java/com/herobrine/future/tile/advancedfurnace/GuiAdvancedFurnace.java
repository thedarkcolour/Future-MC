package com.herobrine.future.tile.advancedfurnace;

import com.herobrine.future.blocks.BlockFurnaceAdvanced;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiAdvancedFurnace extends GuiContainer {
    private static final int WIDTH = 176;
    private static final int HEIGHT = 166;
    private final TileAdvancedFurnace te;
    private final ContainerAdvancedFurnace container;
    private final InventoryPlayer playerInventory;

    private static final ResourceLocation background = new ResourceLocation("textures/gui/container/furnace.png");

    public GuiAdvancedFurnace(ContainerAdvancedFurnace container) {
        super(container);
        this.container = container;
        this.te = container.te;
        this.playerInventory = container.playerInventory;

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if(te.getType() == BlockFurnaceAdvanced.FurnaceType.SMOKER) {
            String s = I18n.format("container.Smoker");
            this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
            this.fontRenderer.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 93, 4210752);
        }
        if(te.getType() == BlockFurnaceAdvanced.FurnaceType.BLAST_FURNACE) {
            String s = I18n.format("container.BlastFurnace");
            this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
            this.fontRenderer.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 93, 4210752);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(background);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(i, j, 0, 0, WIDTH, HEIGHT);

        if(container.isBurning()) {
            int k = container.getScaledFire();
            this.drawTexturedModalRect(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);

            int l = container.getScaledArrow();
            this.drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}