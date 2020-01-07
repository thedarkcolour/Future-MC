package thedarkcolour.futuremc.client.tesr.waterloggable

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import thedarkcolour.futuremc.tile.TileWaterlogged

class TESRWaterlogged : TileEntitySpecialRenderer<TileWaterlogged>() {
    override fun render(te: TileWaterlogged, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha)
    }


}