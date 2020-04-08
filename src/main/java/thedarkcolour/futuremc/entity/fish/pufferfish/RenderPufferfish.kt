package thedarkcolour.futuremc.entity.fish.pufferfish

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.ResourceLocation
import thedarkcolour.futuremc.FutureMC
import kotlin.math.cos

class RenderPufferfish(manager: RenderManager) : RenderLiving<EntityPufferfish>(manager, ModelLargePufferfish(), 0.3F) {
    private var puffState: Int = 3
    private val smallModel = ModelSmallPufferfish()
    private val mediumModel = ModelMediumPufferfish()
    private val largeModel = ModelLargePufferfish()

    override fun doRender(
        entity: EntityPufferfish,
        x: Double,
        y: Double,
        z: Double,
        entityYaw: Float,
        partialTicks: Float
    ) {
        val i = entity.puffState
        if (i != puffState) {
            mainModel = when (i) {
                0 -> smallModel
                1 -> mediumModel
                else -> largeModel
            }
        }

        puffState = i
        shadowSize = 0.1F + 0.1F * i

        super.doRender(entity, x, y, z, entityYaw, partialTicks)
    }

    override fun applyRotations(
        entityLiving: EntityPufferfish,
        ageInTicks: Float,
        rotationYaw: Float,
        partialTicks: Float
    ) {
        GlStateManager.translate(0.0F, cos(ageInTicks * 0.05F) * 0.08F, 0.0F)
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks)
    }

    override fun getEntityTexture(entity: EntityPufferfish): ResourceLocation = TEXTURE_LOCATION

    companion object {
        private val TEXTURE_LOCATION = ResourceLocation(FutureMC.ID, "textures/entity/pufferfish/pufferfish.png")
    }
}