package com.herobrine.future.entity.bee;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ModelBee extends ModelBase {
    private final ModelRenderer body;
    private final ModelRenderer mainBody;
    private final ModelRenderer leftWing;
    private final ModelRenderer rightWing;
    private final ModelRenderer frontLeg;
    private final ModelRenderer midLeg;
    private final ModelRenderer backLeg;
    private final ModelRenderer stinger;
    private final ModelRenderer leftAntenna;
    private final ModelRenderer rightAntenna;
    private float bodyPitch;

    public ModelBee() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.mainBody = new ModelRenderer(this, 0, 0);
        this.mainBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addChild(this.mainBody);
        this.mainBody.addBox(-3.5F, -4.0F, -5.0F, 7, 7, 10, 0.0F);
        this.stinger = new ModelRenderer(this, 26, 7);
        this.stinger.addBox(0.0F, -1.0F, 5.0F, 0, 1, 2, 0.0F);
        this.mainBody.addChild(this.stinger);
        this.leftAntenna = new ModelRenderer(this, 2, 0);
        this.leftAntenna.setRotationPoint(0.0F, -2.0F, -5.0F);
        this.leftAntenna.addBox(1.5F, -2.0F, -3.0F, 1, 2, 3, 0.0F);
        this.rightAntenna = new ModelRenderer(this, 2, 3);
        this.rightAntenna.setRotationPoint(0.0F, -2.0F, -5.0F);
        this.rightAntenna.addBox(-2.5F, -2.0F, -3.0F, 1, 2, 3, 0.0F);
        this.mainBody.addChild(this.leftAntenna);
        this.mainBody.addChild(this.rightAntenna);
        this.leftWing = new ModelRenderer(this, 0, 18);
        this.leftWing.setRotationPoint(-1.5F, -4.0F, -3.0F);
        this.leftWing.rotateAngleX = 0.0F;
        this.leftWing.rotateAngleY = -0.2618F;
        this.leftWing.rotateAngleZ = 0.0F;
        this.body.addChild(this.leftWing);
        this.leftWing.addBox(-9.0F, 0.0F, 0.0F, 9, 0, 6, 0.001F);
        this.rightWing = new ModelRenderer(this, 0, 18);
        this.rightWing.setRotationPoint(1.5F, -4.0F, -3.0F);
        this.rightWing.rotateAngleX = 0.0F;
        this.rightWing.rotateAngleY = 0.2618F;
        this.rightWing.rotateAngleZ = 0.0F;
        this.rightWing.mirror = true;
        this.body.addChild(this.rightWing);
        this.rightWing.addBox(0.0F, 0.0F, 0.0F, 9, 0, 6, 0.001F);
        this.frontLeg = new ModelRenderer(this);
        this.frontLeg.setRotationPoint(1.5F, 3.0F, -2.0F);
        this.body.addChild(this.frontLeg);
        this.frontLeg.addBox("frontLegBox", -5.0F, 0.0F, 0.0F, 7, 2, 0).setTextureOffset(26, 1);
        this.midLeg = new ModelRenderer(this);
        this.midLeg.setRotationPoint(1.5F, 3.0F, 0.0F);
        this.body.addChild(this.midLeg);
        this.midLeg.addBox("midLegBox", -5.0F, 0.0F, 0.0F, 7, 2, 0).setTextureOffset(26, 3);
        this.backLeg = new ModelRenderer(this);
        this.backLeg.setRotationPoint(1.5F, 3.0F, 2.0F);
        this.body.addChild(this.backLeg);
        this.backLeg.addBox("backLegBox", -5.0F, 0.0F, 0.0F, 7, 2, 0).setTextureOffset(26, 5);
    }


    @Override
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        EntityBee bee = (EntityBee) entitylivingbaseIn;
        this.bodyPitch = bee.isChild() ? 0.0F : bee.getBodyPitch(partialTickTime);
        this.stinger.showModel = !bee.hasStung();
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        EntityBee bee = (EntityBee) entityIn;

        this.leftWing.rotateAngleX = 0.0F;
        this.leftAntenna.rotateAngleX = 0.0F;
        this.rightAntenna.rotateAngleX = 0.0F;
        this.body.rotateAngleX = 0.0F;
        this.body.rotationPointY = 19.0F;
        boolean flag = new Vec3d(bee.motionX, bee.motionY, bee.motionZ).lengthSquared() < 1.0E-7D;
        float f;
        if (flag) {
            this.leftWing.rotateAngleY = -0.2618F;
            this.leftWing.rotateAngleZ = 0.0F;
            this.rightWing.rotateAngleX = 0.0F;
            this.rightWing.rotateAngleY = 0.2618F;
            this.rightWing.rotateAngleZ = 0.0F;
            this.frontLeg.rotateAngleX = 0.0F;
            this.midLeg.rotateAngleX = 0.0F;
            this.backLeg.rotateAngleX = 0.0F;
        } else {
            f = ageInTicks * 2.1F;
            this.leftWing.rotateAngleY = 0.0F;
            this.leftWing.rotateAngleZ = MathHelper.cos(f) * 3.1415927F * 0.15F;
            this.rightWing.rotateAngleX = this.leftWing.rotateAngleX;
            this.rightWing.rotateAngleY = this.leftWing.rotateAngleY;
            this.rightWing.rotateAngleZ = -this.leftWing.rotateAngleZ;
            this.frontLeg.rotateAngleX = 0.7853982F;
            this.midLeg.rotateAngleX = 0.7853982F;
            this.backLeg.rotateAngleX = 0.7853982F;
            this.body.rotateAngleX = 0.0F;
            this.body.rotateAngleY = 0.0F;
            this.body.rotateAngleZ = 0.0F;
        }

        if (!bee.isAngry()) {
            this.body.rotateAngleX = 0.0F;
            this.body.rotateAngleY = 0.0F;
            this.body.rotateAngleZ = 0.0F;
            if (!flag) {
                f = MathHelper.cos(ageInTicks * 0.18F);
                this.body.rotateAngleX = 0.1F + f * 3.1415927F * 0.025F;
                this.leftAntenna.rotateAngleX = f * 3.1415927F * 0.03F;
                this.rightAntenna.rotateAngleX = f * 3.1415927F * 0.03F;
                this.frontLeg.rotateAngleX = -f * 3.1415927F * 0.1F + 0.3926991F;
                this.backLeg.rotateAngleX = -f * 3.1415927F * 0.05F + 0.7853982F;
                this.body.rotationPointY = 19.0F - MathHelper.cos(ageInTicks * 0.18F) * 0.9F;
            }
        }

        if (this.bodyPitch > 0.0F) {
            this.body.rotateAngleX = interpolateAngle(this.body.rotateAngleX, this.bodyPitch);
        }
    }


    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        if (this.isChild) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 1.5F, 0.0F);
            this.body.render(scale);
            GlStateManager.popMatrix();
        } else {
            this.body.render(scale);
        }
    }

    private static float interpolateAngle(float headPitch, float bodyPitch) {
        float var3;
        //noinspection StatementWithEmptyBody
        for(var3 = 3.0915928F - headPitch; var3 < -3.1415927F; var3 += 6.2831855F) {
        }

        while(var3 >= 3.1415927F) {
            var3 -= 6.2831855F;
        }

        return headPitch + bodyPitch * var3;
    }
}