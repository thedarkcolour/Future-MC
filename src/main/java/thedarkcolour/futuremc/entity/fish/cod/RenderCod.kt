package thedarkcolour.futuremc.entity.fish.cod

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.ResourceLocation
import thedarkcolour.futuremc.FutureMC
import kotlin.math.sin

class RenderCod(manager: RenderManager) : RenderLiving<EntityCod>(manager, ModelCod(), 0.3F) {
    override fun applyRotations(entityLiving: EntityCod, ageInTicks: Float, rotationYaw: Float, partialTicks: Float) {
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks)
        val f = 4.3F * sin(0.6F * ageInTicks)
        GlStateManager.rotate(f, 0.0F, 1.0F, 0.0F)
        if (!entityLiving.isInWater) {
            GlStateManager.translate(0.1F, 0.1F, -0.1F)
            GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F)
        }
    }

    override fun getEntityTexture(entity: EntityCod): ResourceLocation = TEXTURE_LOCATION

    companion object {
        private val TEXTURE_LOCATION = ResourceLocation(FutureMC.ID, "textures/entity/cod/cod.png")
    }
}