package thedarkcolour.futuremc.client.gui

import net.minecraft.client.gui.inventory.GuiContainer
import thedarkcolour.core.gui.FContainer

abstract class FGui<T : FContainer>(@JvmField protected var container: T) : GuiContainer(container) {
    final override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
    }
}