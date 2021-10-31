package thedarkcolour.futuremc.entity.irongolem

import net.minecraft.client.renderer.entity.RenderIronGolem
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.entity.monster.EntityIronGolem
import net.minecraft.util.ResourceLocation
import thedarkcolour.futuremc.FutureMC

class LayerIronGolemCrack(val renderer: RenderIronGolem) : LayerRenderer<EntityIronGolem> {
    override fun doRenderLayer(
        entitylivingbaseIn: EntityIronGolem,
        limbSwing: Float,
        limbSwingAmount: Float,
        partialTicks: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float,
        scale: Float
    ) {
        if (!entitylivingbaseIn.isInvisible) {
            when {
                entitylivingbaseIn.health < 25 -> renderer.bindTexture(CRACK_TEXTURES[2])
                entitylivingbaseIn.health < 50 -> renderer.bindTexture(CRACK_TEXTURES[1])
                entitylivingbaseIn.health < 75 -> renderer.bindTexture(CRACK_TEXTURES[0])
                else -> return

            }

            renderer.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale)
        }
    }

    override fun shouldCombineTextures(): Boolean = true

    companion object {
        // Low, Medium, High crack textures
        private val CRACK_TEXTURES = arrayOf(
            ResourceLocation(FutureMC.ID, "textures/entity/iron_golem/iron_golem_crackiness_low.png"),
            ResourceLocation(FutureMC.ID, "textures/entity/iron_golem/iron_golem_crackiness_medium.png"),
            ResourceLocation(FutureMC.ID, "textures/entity/iron_golem/iron_golem_crackiness_high.png")
        )
    }
}