package thedarkcolour.futuremc.client.gui

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.inventory.Container
import org.lwjgl.opengl.GL11

/**
 * [T] should probably extend [thedarkcolour.core.gui.FContainer]
 */
abstract class FGui<T : Container>(private var container: Container) : GuiContainer(container) {
    final override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        render(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
    }

    fun getContainer(): T = container as T

    open fun render(mouseX: Int, mouseY: Int, partialTicks: Float) {

    }


    companion object {
        // Parity for newer versions

        fun blit(
            x: Int, y: Int, z: Float, uOffset: Float, vOffset: Float, uWidth: Int, vHeight: Int, textureHeight: Int, textureWidth: Int) {
            innerBlit(x, x + uWidth, y, y + vHeight, z.toInt(), uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight)
        }

        private fun innerBlit(
            x1: Int, x2: Int, y1: Int, y2: Int, z: Int, uWidth: Int, vHeight: Int, uOffset: Float, vOffset: Float, textureWidth: Int, textureHeight: Int) {
            innerBlit(x1, x2, y1, y2, z, uOffset / textureWidth, (uOffset + uWidth) / textureWidth, vOffset / textureHeight, (vOffset + vHeight) / textureHeight)
        }

        private fun innerBlit(x1: Int, x2: Int, y1: Int, y2: Int, z: Int, minU: Float, maxU: Float, minV: Float, maxV: Float) {
            val tessellator = Tessellator.getInstance()
            val builder = tessellator.buffer

            builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            builder.pos(x1.toDouble(), y2.toDouble(), z.toDouble()).tex(minU.toDouble(), maxV.toDouble()).endVertex()
            builder.pos(x2.toDouble(), y2.toDouble(), z.toDouble()).tex(maxU.toDouble(), maxV.toDouble()).endVertex()
            builder.pos(x2.toDouble(), y1.toDouble(), z.toDouble()).tex(maxU.toDouble(), minV.toDouble()).endVertex()
            builder.pos(x1.toDouble(), y1.toDouble(), z.toDouble()).tex(minU.toDouble(), minV.toDouble()).endVertex()
            tessellator.draw()
        }
    }
}