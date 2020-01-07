package thedarkcolour.futuremc.entity.fish.salmon

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.ResourceLocation
import thedarkcolour.futuremc.FutureMC
import kotlin.math.sin

class RenderSalmon(manager: RenderManager) : RenderLiving<EntitySalmon>(manager, ModelSalmon(), 0.3f) {
    override fun applyRotations(entityLiving: EntitySalmon, ageInTicks: Float, rotationYaw: Float, partialTicks: Float) {
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks)
        val i: Float
        val j: Float

        if (entityLiving.isInWater) {
            i = 1f
            j = 1f
        } else {
            i = 1.3f
            j = 1.7f
        }

        val k = i * 4.3f * sin(j * 0.6f * ageInTicks)
        GlStateManager.rotate(k, 0.0f, 1.0f, 0.0f)
        GlStateManager.translate(0.0f, 0.0f, -0.4f)
        if (!entityLiving.isInWater) {
            GlStateManager.translate(0.2f, 0.1f, 0.0f)
            GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f)
        }
    }

    override fun getEntityTexture(entity: EntitySalmon): ResourceLocation = TEXTURE_LOCATION

    companion object {
        private val TEXTURE_LOCATION = ResourceLocation(FutureMC.ID, "textures/entity/salmon/salmon.png")
    }
}