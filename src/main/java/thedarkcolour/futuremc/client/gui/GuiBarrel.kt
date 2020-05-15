package thedarkcolour.futuremc.client.gui

import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import thedarkcolour.futuremc.container.ContainerBarrel

class GuiBarrel(container: ContainerBarrel) : FGui<ContainerBarrel>(container) {
    val te = container.te

    init {
        ySize = 168
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        mc.textureManager.bindTexture(BACKGROUND)
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, 71)
        drawTexturedModalRect(guiLeft, guiTop + 71, 0, 127, xSize, 96)
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        fontRenderer.drawString(I18n.format("container.barrel"), 8, 6, 4210752)
        fontRenderer.drawString(container.playerInv.displayName.unformattedText, 8, ySize - 95, 4210752)
    }

    companion object {
        private val BACKGROUND = ResourceLocation("minecraft:textures/gui/container/generic_54.png")
    }
}