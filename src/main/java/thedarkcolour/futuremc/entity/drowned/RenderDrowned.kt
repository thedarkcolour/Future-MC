package thedarkcolour.futuremc.entity.drowned

import net.minecraft.client.renderer.GlStateManager.rotate
import net.minecraft.client.renderer.entity.RenderBiped
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.ResourceLocation
import thedarkcolour.core.util.lerp
import thedarkcolour.futuremc.FutureMC

class RenderDrowned(manager: RenderManager) : RenderBiped<EntityDrowned>(manager, ModelDrowned(), 0.5f) {
    init {
        addLayer(LayerDrowned(this))
    }

    override fun applyRotations(entityLiving: EntityDrowned, ageInTicks: Float, rotationYaw: Float, partialTicks: Float) {
        val swimAnimation = entityLiving.getSwimAnimation(partialTicks)
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks)
        if (swimAnimation > 0) {
            rotate(lerp(swimAnimation, entityLiving.rotationPitch, -10.0f - entityLiving.rotationPitch), 1.0f, 0.0f, 0.0f)
        }
    }

    override fun getEntityTexture(entity: EntityDrowned): ResourceLocation? {
        return TEXTURE
    }

    companion object {
        private val TEXTURE = ResourceLocation(FutureMC.ID, "textures/entity/drowned/drowned.png")
    }
}