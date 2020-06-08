package thedarkcolour.futuremc.client.gui

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.container.SmithingContainer

class SmithingGui(container: SmithingContainer) : FGui<SmithingContainer>(container) {
    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        mc.textureManager.bindTexture(BACKGROUND)
        val i = guiLeft
        val j = guiTop
        drawTexturedModalRect(i, j, 0, 0, 176, 166)
        drawTexturedModalRect(i + 59, j + 20, 0, 166 + (if (container.inventory[0].isEmpty) 0 else 16), 110, 16)

        if ((!container.inventory[0].isEmpty || !container.inventory[1].isEmpty) && container.inventory[2].isEmpty) {
            drawTexturedModalRect(i + 99, j + 45, 176, 0, 28, 21)
        }
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        val s = I18n.format("container.smithing")

        GlStateManager.disableBlend()
        fontRenderer.drawString(s, 60, 20, 4210752)
        fontRenderer.drawString(container.playerInv.displayName.formattedText, 8, 72, 4210752)
    }

    companion object {
        private val BACKGROUND = ResourceLocation(FutureMC.ID, "textures/gui/smithing.png")
    }
}