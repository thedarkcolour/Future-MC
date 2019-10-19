package thedarkcolour.futuremc.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.container.ContainerBarrel;
import thedarkcolour.futuremc.tile.TileBarrel;

public class GuiBarrel extends GuiContainer {
    public final InventoryPlayer playerInventory;
    public final TileBarrel te;
    private static final int WIDTH = 176;
    private static final int HEIGHT = 167;

    private static final ResourceLocation background = new ResourceLocation(FutureMC.ID, "textures/gui/gui.png");

    public GuiBarrel(ContainerBarrel container) {
        super(container);
        this.te = container.te;
        this.playerInventory = container.playerInventory;

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(I18n.format("container.Barrel"), 8, 6, 4210752);
        fontRenderer.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 92, 4210752);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }
}