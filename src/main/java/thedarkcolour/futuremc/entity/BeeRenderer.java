package thedarkcolour.futuremc.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import thedarkcolour.futuremc.FutureMC;

public class BeeRenderer extends MobRenderer<BeeEntity, BeeModel> {
    private static final ResourceLocation PASSIVE = new ResourceLocation(FutureMC.ID, "textures/entity/bee/passive.png");
    private static final ResourceLocation PASSIVE_NECTAR = new ResourceLocation(FutureMC.ID, "textures/entity/bee/passive_nectar.png");
    private static final ResourceLocation ANGRY = new ResourceLocation(FutureMC.ID, "textures/entity/bee/angry.png");
    private static final ResourceLocation ANGRY_NECTAR = new ResourceLocation(FutureMC.ID, "textures/entity/bee/angry_nectar.png");

    public BeeRenderer(EntityRendererManager manager) {
        super(manager, new BeeModel(), 0.4F);
    }

    @Override
    protected ResourceLocation getEntityTexture(BeeEntity entity) {
        if (entity.isAngry()) {
            return entity.hasNectar() ? ANGRY_NECTAR : ANGRY;
        } else {
            return entity.hasNectar() ? PASSIVE_NECTAR : PASSIVE;
        }
    }
}
