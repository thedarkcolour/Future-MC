package thedarkcolour.futuremc.entity.bee

import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.ResourceLocation
import thedarkcolour.futuremc.FutureMC

class RenderBee(manager: RenderManager) : RenderLiving<EntityBee>(manager, ModelBee(), 0.4f) {
    override fun getEntityTexture(bee: EntityBee): ResourceLocation? {
        return if (bee.isAngry()) {
            if (bee.hasNectar()) ANGRY_NECTAR else ANGRY
        } else {
            if (bee.hasNectar()) PASSIVE_NECTAR else PASSIVE
        }
    }

    companion object {
        private val PASSIVE = ResourceLocation(FutureMC.ID, "textures/entity/bee/passive.png")
        private val PASSIVE_NECTAR = ResourceLocation(FutureMC.ID, "textures/entity/bee/passive_nectar.png")
        private val ANGRY = ResourceLocation(FutureMC.ID, "textures/entity/bee/angry.png")
        private val ANGRY_NECTAR = ResourceLocation(FutureMC.ID, "textures/entity/bee/angry_nectar.png")
    }
}
