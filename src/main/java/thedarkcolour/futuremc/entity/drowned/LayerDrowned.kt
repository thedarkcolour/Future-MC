package thedarkcolour.futuremc.entity.drowned

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.util.ResourceLocation
import thedarkcolour.futuremc.FutureMC

class LayerDrowned(private val renderer: RenderDrowned) : LayerRenderer<EntityDrowned> {
    private val model = ModelDrowned()

    override fun doRenderLayer(
        entity: EntityDrowned,
        limbSwing: Float,
        limbSwingAmount: Float,
        partialTicks: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float,
        scale: Float
    ) {
        if (!entity.isInvisible) {
            renderer.mainModel.setModelAttributes(model)
            model.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks)
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
            renderer.bindTexture(OUTER_LAYER)
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale)
        }
    }

    override fun shouldCombineTextures(): Boolean = true

    companion object {
        private val OUTER_LAYER = ResourceLocation(FutureMC.ID, "textures/entity/drowned/drowned_outer.png")
    }
}