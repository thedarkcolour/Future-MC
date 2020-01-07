package thedarkcolour.futuremc.entity.fish.tropical

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.layers.LayerRenderer

class LayerTropicalFishPattern(private val fishRenderer: RenderTropicalFish) : LayerRenderer<EntityTropicalFish> {
    private val modelA = ModelTropicalFishA(0.008F)
    private val modelB = ModelTropicalFishB(0.008F)

    override fun doRenderLayer(entity: EntityTropicalFish, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float) {
        if (!entity.isInvisible) {
            val model = if (entity.getSize() == 0) modelA else modelB
            fishRenderer.bindTexture(entity.getPatternTexture())
            val colors = entity.getPatternColorComponentValues()
            GlStateManager.color(colors[0], colors[1], colors[2])
            fishRenderer.mainModel.setModelAttributes(model)
            model.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks)
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale)
        }
    }

    override fun shouldCombineTextures(): Boolean = true
}