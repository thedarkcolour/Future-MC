package com.herobrine.future.entity.trident;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderTrident extends Render<EntityTrident> {
    private final ModelTrident modelTrident = new ModelTrident();

    public static final Factory FACTORY = new Factory();

    private RenderTrident(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityTrident entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.bindEntityTexture(entity);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks + 90.0F, 0.0F, 0.0F, 1.0F);
        this.modelTrident.renderer();
        GlStateManager.popMatrix();
        //this.renderIn(entity, x, y, z, partialTicks);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.enableLighting();

        EntityRenderer er = Minecraft.getMinecraft().entityRenderer;
        er.disableLightmap();
        GlStateManager.disableLighting();

        bindEntityTexture(entity);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);

        float f10 = 0.05625F;
        GlStateManager.enableRescaleNormal();
        float f11 = (float) entity.arrowShake - partialTicks;

        if (f11 > 0.0F) {
            float f12 = -MathHelper.sin(f11 * 3.0F) * f11;
            GlStateManager.rotate(f12, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(f10, f10, f10);
        GlStateManager.translate(-4.0F, 0.0F, 0.0F);

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        GlStateManager.enableLighting();
        er.enableLightmap();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(EntityTrident entity) {
        return ModelTrident.TEXTURE_LOCATION;
    }

    public static class Factory implements IRenderFactory<EntityTrident> {
        @Override
        public Render<? super EntityTrident> createRenderFor(RenderManager manager) {
            return new RenderTrident(manager);
        }
    }


}