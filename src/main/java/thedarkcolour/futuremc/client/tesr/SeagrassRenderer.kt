package thedarkcolour.futuremc.client.tesr

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BufferBuilder
import net.minecraftforge.client.model.animation.FastTESR
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.tile.TileSeagrassRenderer

object SeagrassRenderer : FastTESR<TileSeagrassRenderer>() {
    private val SEAGRASS = FBlocks.SEAGRASS.defaultState
    private val MODEL = Minecraft.getMinecraft().blockRendererDispatcher.getModelForState(SEAGRASS)

    override fun renderTileEntityFast(
        te: TileSeagrassRenderer,
        x: Double,
        y: Double,
        z: Double,
        partialTicks: Float,
        destroyStage: Int,
        partial: Float,
        buffer: BufferBuilder
    ) {
        val pos = te.pos
        buffer.setTranslation(x - pos.x, y - pos.y, z - pos.z)
        Minecraft.getMinecraft().blockRendererDispatcher.blockModelRenderer.renderModel(
            world,
            MODEL,
            SEAGRASS,
            pos,
            buffer,
            false
        )
    }
}