package thedarkcolour.futuremc.entity.fish.tropical

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.ResourceLocation
import kotlin.math.sin

class RenderTropicalFish(manager: RenderManager) :
    RenderLiving<EntityTropicalFish>(manager, ModelTropicalFishA(), 0.15F) {
    private val modelA = ModelTropicalFishA()
    private val modelB = ModelTropicalFishB()

    init {
        addLayer(LayerTropicalFishPattern(this))
    }

    override fun doRender(
        entity: EntityTropicalFish,
        x: Double,
        y: Double,
        z: Double,
        entityYaw: Float,
        partialTicks: Float
    ) {
        mainModel = if (entity.getSize() == 0) modelA else modelB
        val colors = entity.getBodyColorComponentValues()
        GlStateManager.color(colors[0], colors[1], colors[2])
        super.doRender(entity, x, y, z, entityYaw, partialTicks)
    }

    override fun applyRotations(
        entityLiving: EntityTropicalFish,
        ageInTicks: Float,
        rotationYaw: Float,
        partialTicks: Float
    ) {
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks)
        val f = 4.3F * sin(0.6F * ageInTicks)
        GlStateManager.rotate(f, 0.0F, 1.0F, 0.0F)
        if (!entityLiving.isInWater) {
            GlStateManager.translate(0.2F, 0.1F, 0.0F)
            GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F)
        }
    }

    override fun getEntityTexture(entity: EntityTropicalFish): ResourceLocation = entity.getBodyTexture()
}