package com.herobrine.future.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

@SuppressWarnings("all")
public class RenderTrident extends Render<EntityTrident> {
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("minecraftfuture:textures/entity/trident.png");
    private final ModelTrident modelTrident = new ModelTrident();

    public RenderTrident(RenderManager renderManager) {
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
        this.renderIn(entity, x, y, z, entityYaw, partialTicks);
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
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();

        byte b0 = 0;
        float f2 = 0.0F;
        float f3 = 0.5F;
        float f4 = (float) (b0 * 10) / 32.0F;
        float f5 = (float) (5 + b0 * 10) / 32.0F;
        float f6 = 0.0F;
        float f7 = 0.15625F;
        float f8 = (float) (5 + b0 * 10) / 32.0F;
        float f9 = (float) (10 + b0 * 10) / 32.0F;
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

        buffer.begin(0x07, DefaultVertexFormats.POSITION_TEX_NORMAL);
        buffer.pos(-7.0D, -2.0D, -2.0D).tex(f6, f8).normal(f10, 0.0F, 0.0F).endVertex();
        buffer.pos(-7.0D, -2.0D, 2.0D).tex(f7, f8).normal(f10, 0.0F, 0.0F).endVertex();
        buffer.pos(-7.0D, 2.0D, 2.0D).tex(f7, f9).normal(f10, 0.0F, 0.0F).endVertex();
        buffer.pos(-7.0D, 2.0D, -2.0D).tex(f6, f9).normal(f10, 0.0F, 0.0F).endVertex();
        tess.draw();

        buffer.begin(0x07, DefaultVertexFormats.POSITION_TEX_NORMAL);
        buffer.pos(-7.0D, 2.0D, -2.0D).tex(f6, f8).normal(-f10, 0.0F, 0.0F).endVertex();
        buffer.pos(-7.0D, 2.0D, 2.0D).tex(f7, f8).normal(-f10, 0.0F, 0.0F).endVertex();
        buffer.pos(-7.0D, -2.0D, 2.0D).tex(f7, f9).normal(-f10, 0.0F, 0.0F).endVertex();
        buffer.pos(-7.0D, -2.0D, -2.0D).tex(f6, f9).normal(-f10, 0.0F, 0.0F).endVertex();
        tess.draw();

        for (int i = 0; i < 4; ++i) {

            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);

            buffer.begin(0x07, DefaultVertexFormats.POSITION_TEX_NORMAL);
            buffer.pos(-8.0D, -2.0D, 0.0D).tex(f2, f4).normal(0.0F, 0.0F, f10).endVertex();
            buffer.pos(8.0D, -2.0D, 0.0D).tex(f3, f4).normal(0.0F, 0.0F, f10).endVertex();
            buffer.pos(8.0D, 2.0D, 0.0D).tex(f3, f5).normal(0.0F, 0.0F, f10).endVertex();
            buffer.pos(-8.0D, 2.0D, 0.0D).tex(f2, f5).normal(0.0F, 0.0F, f10).endVertex();
            tess.draw();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        GlStateManager.enableLighting();
        er.enableLightmap();
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityTrident entity) {
        return TEXTURE_LOCATION;
    }

    private double render(double d0, double d1, double d2) {
        return d0 + (d1 - d0) * d2;
    }

    protected void renderIn(EntityTrident entity, double x, double y, double z, float entityYaw, float partialTicks) {
        Entity entityIn = entity.getShooter();
        if (entityIn != null && entity.canCrit()) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufBuilder = tessellator.getBuffer();
            double d0 = this.render((double)entityIn.prevRotationYaw, (double)entityIn.rotationYaw, (double)(partialTicks * 0.5F)) * 0.01745329238474369D;
            double d1 = Math.cos(d0);
            double d2 = Math.sin(d0);
            double d3 = this.render(entityIn.prevPosX, entityIn.posX, (double)partialTicks);
            double d4 = this.render(entityIn.prevPosY + (double)entityIn.getEyeHeight() * 0.8D, entityIn.posY + (double)entityIn.getEyeHeight() * 0.8D, (double)partialTicks);
            double d5 = this.render(entityIn.prevPosZ, entityIn.posZ, (double)partialTicks);
            double d6 = d1 - d2;
            double d7 = d2 + d1;
            double d8 = this.render(entity.prevPosX, entity.posX, (double)partialTicks);
            double d9 = this.render(entity.prevPosY, entity.posY, (double)partialTicks);
            double d10 = this.render(entity.prevPosZ, entity.posZ, (double)partialTicks);
            double d11 = (double)((float)(d3 - d8));
            double d12 = (double)((float)(d4 - d9));
            double d13 = (double)((float)(d5 - d10));
            double d14 = Math.sqrt(d11 * d11 + d12 * d12 + d13 * d13);
            int i = entity.getEntityId() + entity.ticksExisted;
            double d15 = (double)((float)i + partialTicks) * -0.1D;
            double d16 = Math.min(0.5D, d14 / 30.0D);
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 255.0F, 255.0F);
            bufBuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
            //int lvt_48_1_ = true;
            int j = 7 - i % 7;
            //double lvt_50_1_ = 0.1D;

            float f0;
            float f1;
            float f2;
            int k;
            double d17;
            float f3;
            double d18;
            double d19;
            double d20;
            double d21;
            for(k = 0; k <= 37; ++k) {
                d17 = (double)k / 37.0D;
                f3 = 1.0F - (float)((k + j) % 7) / 7.0F;
                d18 = d17 * 2.0D - 1.0D;
                d18 = (1.0D - d18 * d18) * d16;
                d19 = x + d11 * d17 + Math.sin(d17 * 3.141592653589793D * 8.0D + d15) * d6 * d18;
                d20 = y + d12 * d17 + Math.cos(d17 * 3.141592653589793D * 8.0D + d15) * 0.02D + (0.1D + d18) * 1.0D;
                d21 = z + d13 * d17 + Math.sin(d17 * 3.141592653589793D * 8.0D + d15) * d7 * d18;
                f0 = 0.87F * f3 + 0.3F * (1.0F - f3);
                f1 = 0.91F * f3 + 0.6F * (1.0F - f3);
                f2 = 0.85F * f3 + 0.5F * (1.0F - f3);
                bufBuilder.pos(d19, d20, d21).color(f0, f1, f2, 1.0F).endVertex();
                bufBuilder.pos(d19 + 0.1D * d18, d20 + 0.1D * d18, d21).color(f0, f1, f2, 1.0F).endVertex();
                //if (k > entity.returningTicks * 2) {
                  //  break;
                //}
            }

            tessellator.draw();
            bufBuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);

            for(k = 0; k <= 37; ++k) {
                d17 = (double)k / 37.0D;
                f3 = 1.0F - (float)((k + j) % 7) / 7.0F;
                d18 = d17 * 2.0D - 1.0D;
                d18 = (1.0D - d18 * d18) * d16;
                d19 = x + d11 * d17 + Math.sin(d17 * 3.141592653589793D * 8.0D + d15) * d6 * d18;
                d20 = y + d12 * d17 + Math.cos(d17 * 3.141592653589793D * 8.0D + d15) * 0.01D + (0.1D + d18) * 1.0D;
                d21 = z + d13 * d17 + Math.sin(d17 * 3.141592653589793D * 8.0D + d15) * d7 * d18;
                f0 = 0.87F * f3 + 0.3F * (1.0F - f3);
                f1 = 0.91F * f3 + 0.6F * (1.0F - f3);
                f2 = 0.85F * f3 + 0.5F * (1.0F - f3);
                bufBuilder.pos(d19, d20, d21).color(f0, f1, f2, 1.0F).endVertex();
                bufBuilder.pos(d19 + 0.1D * d18, d20, d21 + 0.1D * d18).color(f0, f1, f2, 1.0F).endVertex();
                if (k > entity.returningTicks * 2) {
                    break;
                }
            }

            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.enableCull();
        }
    }
}