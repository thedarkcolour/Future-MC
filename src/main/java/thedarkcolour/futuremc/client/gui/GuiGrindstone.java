package thedarkcolour.futuremc.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.container.ContainerGrindstone;

public class GuiGrindstone extends FGui<ContainerGrindstone> {
    private static final int WIDTH = 176;
    private static final int HEIGHT = 166;

    private static final ResourceLocation background = new ResourceLocation(FutureMC.ID, "textures/gui/grindstone.png");

    public GuiGrindstone(ContainerGrindstone container) {
        super(container);

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = I18n.format("container.grindstone");
        fontRenderer.drawString(s, 8, 6, 4210752);
        fontRenderer.drawString(container.getPlayerInv().getDisplayName().getUnformattedText(), 8, ySize - 93, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(background);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;

        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);

        if (container.isRecipeInvalid()) {
            drawTexturedModalRect(i + 92, j + 31, 176, 0, 28, 21);
        }
    }
}
