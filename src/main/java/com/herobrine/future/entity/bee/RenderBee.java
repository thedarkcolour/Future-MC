package com.herobrine.future.entity.bee;

import com.herobrine.future.FutureMC;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderBee extends RenderLiving<EntityLiving> {
    private static final ResourceLocation PASSIVE = new ResourceLocation(FutureMC.ID, "textures/entity/bee/passive.png");
    private static final ResourceLocation PASSIVE_NECTAR = new ResourceLocation(FutureMC.ID, "textures/entity/bee/passive_nectar.png");
    private static final ResourceLocation ANGRY = new ResourceLocation(FutureMC.ID, "textures/entity/bee/angry.png");
    private static final ResourceLocation ANGRY_NECTAR = new ResourceLocation(FutureMC.ID, "textures/entity/bee/angry_nectar.png");

    public RenderBee(RenderManager manager, ModelBase model) {
        super(manager, model, 0.4F);
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
