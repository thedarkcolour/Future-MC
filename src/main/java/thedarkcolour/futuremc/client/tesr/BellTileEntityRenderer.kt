package thedarkcolour.futuremc.client.tesr

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import thedarkcolour.futuremc.tile.BellTileEntity
import kotlin.math.PI
import kotlin.math.sin

class BellTileEntityRenderer : TileEntitySpecialRenderer<BellTileEntity>() {
    private val model = BellModel()

    @Suppress("NON_EXHAUSTIVE_WHEN")
    override fun render(
        bell: BellTileEntity, x: Double, y: Double, z: Double,
        partialTicks: Float, destroyStage: Int, alpha: Float
    ) {
        GlStateManager.pushMatrix()
        //GlStateManager.enableRescaleNormal()
        bindTexture(TEXTURE)
        GlStateManager.translate(x, y, z)
        val f = bell.ringingTicks + partialTicks
        var f1 = 0.0f
        var f2 = 0.0f

        if (bell.isRinging) {
            // sin wave
            val f3 = sin(f / PI).toFloat() / (4.0f + f / 3.0f)

            when (bell.ringFacing) {
                EnumFacing.NORTH -> f1 = -f3
                EnumFacing.SOUTH -> f1 = +f3
                EnumFacing.EAST -> f2 = -f3
                EnumFacing.WEST -> f2 = +f3
            }

            GlStateManager.rotate(f3, f1, 0.0f, f2)
        }

        model.renderBell(f1, f2)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.popMatrix()
    }

    companion object {
        private val TEXTURE = ResourceLocation("futuremc:textures/entity/bell/bell_body.png")
    }
}