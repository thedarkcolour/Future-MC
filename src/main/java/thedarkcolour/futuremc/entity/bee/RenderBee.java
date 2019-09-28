package thedarkcolour.futuremc.entity.bee;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import thedarkcolour.futuremc.FutureMC;

public class RenderBee extends RenderLiving<EntityLiving> {
    private static final ResourceLocation PASSIVE = new ResourceLocation(FutureMC.ID, "textures/entity/bee/passive.png");
    private static final ResourceLocation PASSIVE_NECTAR = new ResourceLocation(FutureMC.ID, "textures/entity/bee/passive_nectar.png");
    private static final ResourceLocation ANGRY = new ResourceLocation(FutureMC.ID, "textures/entity/bee/angry.png");
    private static final ResourceLocation ANGRY_NECTAR = new ResourceLocation(FutureMC.ID, "textures/entity/bee/angry_nectar.png");

    public RenderBee(RenderManager manager) {
        super(manager, new ModelBee(), 0.4F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityLiving entity) {
        EntityBee bee = (EntityBee) entity;
        if (bee.isAngry()) {
            return bee.hasNectar() ? ANGRY_NECTAR : ANGRY;
        } else {
            return bee.hasNectar() ? PASSIVE_NECTAR : PASSIVE;
        }
    }
}
