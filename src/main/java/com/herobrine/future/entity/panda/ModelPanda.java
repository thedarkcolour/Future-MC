package com.herobrine.future.entity.panda;

import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;


public class ModelPanda extends ModelQuadruped {
    private float field_217164_l;
    private float field_217165_m;
    private float field_217166_n;

    public ModelPanda(int height, float scale) {
        super(height, scale);

        this.textureWidth = 64;
        this.textureHeight = 64;
        this.head = new ModelRenderer(this, 0, 6);
        this.head.addBox(-6.5F, -5.0F, -4.0F, 13, 10, 9);
        this.head.setRotationPoint(0.0F, 11.5F, -17.0F);
        this.head.setTextureOffset(45, 16).addBox(-3.5F, 0.0F, -6.0F, 7, 5, 2);
        this.head.setTextureOffset(52, 25).addBox(-8.5F, -8.0F, -1.0F, 5, 4, 1);
        this.head.setTextureOffset(52, 25).addBox(3.5F, -8.0F, -1.0F, 5, 4, 1);

        this.body = new ModelRenderer(this, 0, 25);
        this.body.addBox(-9.5F, -13.0F, -6.5F, 19, 26, 13);
        this.body.setRotationPoint(0.0F, 10.0F, 0.0F);

        this.leg1 = new ModelRenderer(this, 40, 0);
        this.leg1.addBox(-3.0F, 0.0F, -3.0F, 6, 9, 6);
        this.leg1.setRotationPoint(-5.5F, 15.0F, 9.0F);

        this.leg2 = new ModelRenderer(this, 40, 0);
        this.leg2.addBox(-3.0F, 0.0F, -3.0F, 6, 9, 6);
        this.leg2.setRotationPoint(5.5F, 15.0F, 9.0F);

        this.leg3 = new ModelRenderer(this, 40, 0);
        this.leg3.addBox(-3.0F, 0.0F, -3.0F, 6, 9, 6);
        this.leg3.setRotationPoint(-5.5F, 15.0F, -9.0F);

        this.leg4 = new ModelRenderer(this, 40, 0);
        this.leg4.addBox(-3.0F, 0.0F, -3.0F, 6, 9, 6);
        this.leg4.setRotationPoint(5.5F, 15.0F, -9.0F);
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entityLiving, float limbSwing, float limbSwingAmount, float partialTickTime) {
        super.setLivingAnimations(entityLiving, limbSwing, limbSwingAmount, partialTickTime);
        this.field_217164_l = ((EntityPanda)entityLiving).func_213561_v(partialTickTime);
        this.field_217165_m = ((EntityPanda)entityLiving).func_213583_w(partialTickTime);
        this.field_217166_n = entityLiving.isChild() ? 0.0F : ((EntityPanda)entityLiving).func_213591_x(partialTickTime);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        EntityPanda entityPanda = (EntityPanda) entityIn;

        boolean flag = entityPanda.func_213544_dV() > 0;
        boolean flag1 = entityPanda.func_213539_dW();
        int i = entityPanda.func_213585_ee();
        boolean flag2 = entityPanda.func_213578_dZ();
        boolean flag3 = entityPanda.func_213566_eo();
        if (flag) {
            this.head.rotateAngleY = 0.35F * MathHelper.sin(0.6F * ageInTicks);
            this.head.rotateAngleZ = 0.35F * MathHelper.sin(0.6F * ageInTicks);
            this.leg3.rotateAngleX = -0.75F * MathHelper.sin(0.3F * ageInTicks); //
            this.leg4.rotateAngleX = 0.75F * MathHelper.sin(0.3F * ageInTicks); //
        } else {
            this.head.rotateAngleZ = 0.0F;
        }

        if (flag1) {
            if (i < 15) {
                this.head.rotateAngleX = (-(float)Math.PI / 4F) * (float)i / 14.0F;
            } else if (i < 20) {
                float f = (float)((i - 15) / 5);
                this.head.rotateAngleX = (-(float)Math.PI / 4F) + ((float)Math.PI / 4F) * f;
            }
        }

        if (this.field_217164_l > 0.0F) {
            this.body.rotateAngleX = this.func_217163_a(this.body.rotateAngleX, 1.7407963F, this.field_217164_l);
            this.head.rotateAngleX = this.func_217163_a(this.head.rotateAngleX, ((float)Math.PI / 2F), this.field_217164_l);
            this.leg1.rotateAngleZ = -0.27079642F;
            this.leg2.rotateAngleZ = 0.27079642F;
            this.leg3.rotateAngleZ = 0.5707964F;
            this.leg4.rotateAngleZ = -0.5707964F;
            if (flag2) {
                this.head.rotateAngleX = ((float)Math.PI / 2F) + 0.2F * MathHelper.sin(ageInTicks * 0.6F);
                this.leg3.rotateAngleX = -0.4F - 0.2F * MathHelper.sin(ageInTicks * 0.6F);
                this.leg4.rotateAngleX = -0.4F - 0.2F * MathHelper.sin(ageInTicks * 0.6F);
            }

            if (flag3) {
                this.head.rotateAngleX = 2.1707964F;
                this.leg3.rotateAngleX = -0.9F;
                this.leg4.rotateAngleX = -0.9F;
            }
        } else {
            this.leg1.rotateAngleZ = 0.0F;
            this.leg2.rotateAngleZ = 0.0F;
            this.leg3.rotateAngleZ = 0.0F;
            this.leg4.rotateAngleZ = 0.0F;
        }

        if (this.field_217165_m > 0.0F) {
            this.leg1.rotateAngleX = -0.6F * MathHelper.sin(ageInTicks * 0.15F);
            this.leg2.rotateAngleX = 0.6F * MathHelper.sin(ageInTicks * 0.15F);
            this.leg3.rotateAngleX = 0.3F * MathHelper.sin(ageInTicks * 0.25F);
            this.leg4.rotateAngleX = -0.3F * MathHelper.sin(ageInTicks * 0.25F);
            this.head.rotateAngleX = this.func_217163_a(this.head.rotateAngleX, ((float)Math.PI / 2F), this.field_217165_m);
        }

        if (this.field_217166_n > 0.0F) {
            this.head.rotateAngleX = this.func_217163_a(this.head.rotateAngleX, 2.0561945F, this.field_217166_n);
            this.leg1.rotateAngleX = -0.5F * MathHelper.sin(ageInTicks * 0.5F);
            this.leg2.rotateAngleX = 0.5F * MathHelper.sin(ageInTicks * 0.5F);
            this.leg3.rotateAngleX = 0.5F * MathHelper.sin(ageInTicks * 0.5F);
            this.leg4.rotateAngleX = -0.5F * MathHelper.sin(ageInTicks * 0.5F);
        }
    }

    protected float func_217163_a(float p_217163_1_, float p_217163_2_, float p_217163_3_) {
        float f;
        f = p_217163_2_ - p_217163_1_;
        while (f < -(float)Math.PI) {
            f += ((float) Math.PI * 2F);
        }

        while(f >= (float)Math.PI) {
            f -= ((float)Math.PI * 2F);
        }

        return p_217163_1_ + p_217163_3_ * f;
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

        if (this.isChild) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, this.childYOffset * scale, this.childZOffset * scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5555555F, 0.5555555F, 0.5555555F);
            GlStateManager.translate(0.0F, 23.0F * scale, 0.3F);
            this.head.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.33333334F, 0.33333334F, 0.33333334F);
            GlStateManager.translate(0.0F, 49.0F * scale, 0.0F);
            this.body.render(scale);
            this.leg1.render(scale);
            this.leg2.render(scale);
            this.leg3.render(scale);
            this.leg4.render(scale);
            GlStateManager.popMatrix();
        } else {
            this.head.render(scale);
            this.body.render(scale);
            this.leg1.render(scale);
            this.leg2.render(scale);
            this.leg3.render(scale);
            this.leg4.render(scale);
        }
    }
}
