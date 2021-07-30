package thedarkcolour.futuremc.entity.panda;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import thedarkcolour.core.util.UtilKt;

import java.util.Map;

public class RenderPanda extends RenderLiving<EntityPanda> {
    private static final Map<EntityPanda.PandaType, ResourceLocation> textures = UtilKt.make(Maps.newHashMap(), map -> {
        map.put(EntityPanda.PandaType.NORMAL, new ResourceLocation("futuremc:textures/entity/panda/panda.png"));
        map.put(EntityPanda.PandaType.LAZY, new ResourceLocation("futuremc:textures/entity/panda/lazy_panda.png"));
        map.put(EntityPanda.PandaType.WORRIED, new ResourceLocation("futuremc:textures/entity/panda/worried_panda.png"));
        map.put(EntityPanda.PandaType.PLAYFUL, new ResourceLocation("futuremc:textures/entity/panda/playful_panda.png"));
        map.put(EntityPanda.PandaType.BROWN, new ResourceLocation("futuremc:textures/entity/panda/brown_panda.png"));
        map.put(EntityPanda.PandaType.WEAK, new ResourceLocation("futuremc:textures/entity/panda/weak_panda.png"));
        map.put(EntityPanda.PandaType.AGGRESSIVE, new ResourceLocation("futuremc:textures/entity/panda/aggressive_panda.png"));
    });

    public RenderPanda(RenderManager manager) {
        super(manager, new ModelPanda(9, 0.0F), 0.9F);
        this.addLayer(new LayerPandaHeldItem());
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityPanda entity) {
        return textures.getOrDefault(entity.getPandaType(), textures.get(EntityPanda.PandaType.NORMAL));
    }

    @Override
    protected void applyRotations(EntityPanda entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);

        if (entityLiving.rolls > 0) {
            int i = entityLiving.rolls;
            int j = i + 1;
            float f1 = entityLiving.isChild() ? 0.3F : 0.8F;
            if (i < 8) {
                float f3 = (float) (90 * i) / 7.0F;
                float f4 = (float) (90 * j) / 7.0F;
                float f2 = this.func_217775_a(f3, f4, j, partialTicks, 8.0F);
                GlStateManager.translate(0.0F, (f1 + 0.2F) * (f2 / 90.0F), 0.0F);
                GlStateManager.rotate(-f2, 1.0F, 0.0F, 0.0F);
            } else if (i < 16) {
                float f13 = ((float) i - 8.0F) / 7.0F;
                float f16 = 90.0F + 90.0F * f13;
                float f5 = 90.0F + 90.0F * ((float) j - 8.0F) / 7.0F;
                float f10 = this.func_217775_a(f16, f5, j, partialTicks, 16.0F);
                GlStateManager.translate(0.0F, f1 + 0.2F + (f1 - 0.2F) * (f10 - 90.0F) / 90.0F, 0.0F);
                GlStateManager.rotate(-f10, 1.0F, 0.0F, 0.0F);
            } else if ((float) i < 24.0F) {
                float f14 = ((float) i - 16.0F) / 7.0F;
                float f17 = 180.0F + 90.0F * f14;
                float f19 = 180.0F + 90.0F * ((float) j - 16.0F) / 7.0F;
                float f11 = this.func_217775_a(f17, f19, j, partialTicks, 24.0F);
                GlStateManager.translate(0.0F, f1 + f1 * (270.0F - f11) / 90.0F, 0.0F);
                GlStateManager.rotate(-f11, 1.0F, 0.0F, 0.0F);
            } else if (i < 32) {
                float f15 = ((float) i - 24.0F) / 7.0F;
                float f18 = 270.0F + 90.0F * f15;
                float f20 = 270.0F + 90.0F * ((float) j - 24.0F) / 7.0F;
                float f12 = this.func_217775_a(f18, f20, j, partialTicks, 32.0F);
                GlStateManager.translate(0.0F, f1 * ((360.0F - f12) / 90.0F), 0.0F);
                GlStateManager.rotate(-f12, 1.0F, 0.0F, 0.0F);
            }
        } else {
            GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
        }

        float f6 = entityLiving.getScaredAnimation(partialTicks);
        if (f6 > 0.0F) {
            GlStateManager.translate(0.0F, 0.8F * f6, 0.0F);
            GlStateManager.rotate(UtilKt.lerp(f6, entityLiving.rotationPitch, entityLiving.rotationPitch + 90.0F), 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(0.0F, -1.0F * f6, 0.0F);
            if (entityLiving.isFrightened()) {
                float f7 = (float) (Math.cos((double) entityLiving.ticksExisted * 1.25D) * Math.PI * (double) 0.05F);
                GlStateManager.rotate(f7, 0.0F, 1.0F, 0.0F);
                if (entityLiving.isChild()) {
                    GlStateManager.translate(0.0F, 0.8F, 0.55F);
                }
            }
        }

        float f8 = entityLiving.getLazyAnimation(partialTicks);
        if (f8 > 0.0F) {
            float f9 = entityLiving.isChild() ? 0.5F : 1.3F;
            GlStateManager.translate(0.0F, f9 * f8, 0.0F);
            GlStateManager.rotate(UtilKt.lerp(f8, entityLiving.rotationPitch, entityLiving.rotationPitch + 180.0F), 1.0F, 0.0F, 0.0F);
        }
    }

    private float func_217775_a(float p_217775_1_, float p_217775_2_, int p_217775_3_, float partialTicks, float p_217775_5_) {
        return (float) p_217775_3_ < p_217775_5_ ? UtilKt.lerp(partialTicks, p_217775_1_, p_217775_2_) : p_217775_1_;
    }
}