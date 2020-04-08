package thedarkcolour.futuremc.entity.bee

import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.ResourceLocation
import thedarkcolour.futuremc.FutureMC

class BeeRenderer(manager: RenderManager) : RenderLiving<BeeEntity>(manager, BeeModel(), 0.4f) {
    override fun getEntityTexture(bee: BeeEntity): ResourceLocation? {
        return if (bee.isAngry()) {
            if (bee.hasPollen()) ANGRY_NECTAR else ANGRY
        } else {
            if (bee.hasPollen()) PASSIVE_NECTAR else PASSIVE
        }
    }

    companion object {
        private val PASSIVE = ResourceLocation(FutureMC.ID, "textures/entity/bee/passive.png")
        private val PASSIVE_NECTAR = ResourceLocation(FutureMC.ID, "textures/entity/bee/passive_nectar.png")
        private val ANGRY = ResourceLocation(FutureMC.ID, "textures/entity/bee/angry.png")
        private val ANGRY_NECTAR = ResourceLocation(FutureMC.ID, "textures/entity/bee/angry_nectar.png")
    }
}
