package thedarkcolour.futuremc.client.tesr.bell

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import thedarkcolour.futuremc.tile.BellTileEntity
import kotlin.math.sin

class BellRenderer : TileEntitySpecialRenderer<BellTileEntity>() {
    private val model = BellModel()

    @Suppress("NON_EXHAUSTIVE_WHEN")
    override fun render(
        te: BellTileEntity,
        x: Double,
        y: Double,
        z: Double,
        partialTicks: Float,
        destroyStage: Int,
        alpha: Float
    ) {
        GlStateManager.pushMatrix()
        GlStateManager.enableRescaleNormal()
        bindTexture(TEXTURE)
        GlStateManager.translate(x, y, z)
        val f = te.ringingTicks + partialTicks
        var f1 = 0f
        var f2 = 0f
        if (te.isRinging) {
            val f3 = (sin(f / Math.PI) / (4f + f / 3f)).toFloat()
            when (te.ringFacing) {
                EnumFacing.NORTH -> f1 = -f3
                EnumFacing.SOUTH -> f1 = f3
                EnumFacing.EAST -> f2 = -f3
                EnumFacing.WEST -> f2 = f3
            }
        }
        model.renderBell(f1, f2, 0.0625f)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.popMatrix()
    }

    companion object {
        private val TEXTURE = ResourceLocation("futuremc:textures/entity/bell/bell_body.png")
    }
}