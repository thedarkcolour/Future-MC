package thedarkcolour.futuremc.entity.bee;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ModelBee extends ModelBase {
    private final ModelRenderer body;
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
        textureHeight = 64;
        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 19.0F, 0.0F);
        ModelRenderer mainBody = new ModelRenderer(this, 0, 0);
        mainBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        body.addChild(mainBody);
        mainBody.addBox(-3.5F, -4.0F, -5.0F, 7, 7, 10, 0.0F);
        stinger = new ModelRenderer(this, 26, 7);
        stinger.addBox(0.0F, -1.0F, 5.0F, 0, 1, 2, 0.0F);
        mainBody.addChild(stinger);
        leftAntenna = new ModelRenderer(this, 2, 0);
        leftAntenna.setRotationPoint(0.0F, -2.0F, -5.0F);
        leftAntenna.addBox(1.5F, -2.0F, -3.0F, 1, 2, 3, 0.0F);
        rightAntenna = new ModelRenderer(this, 2, 3);
        rightAntenna.setRotationPoint(0.0F, -2.0F, -5.0F);
        rightAntenna.addBox(-2.5F, -2.0F, -3.0F, 1, 2, 3, 0.0F);
        mainBody.addChild(leftAntenna);
        mainBody.addChild(rightAntenna);
        leftWing = new ModelRenderer(this, 0, 18);
        leftWing.setRotationPoint(-1.5F, -4.0F, -3.0F);
        leftWing.rotateAngleX = 0.0F;
        leftWing.rotateAngleY = -0.2618F;
        leftWing.rotateAngleZ = 0.0F;
        body.addChild(leftWing);
        leftWing.addBox(-9.0F, 0.0F, 0.0F, 9, 0, 6, 0.001F);
        rightWing = new ModelRenderer(this, 0, 18);
        rightWing.setRotationPoint(1.5F, -4.0F, -3.0F);
        rightWing.rotateAngleX = 0.0F;
        rightWing.rotateAngleY = 0.2618F;
        rightWing.rotateAngleZ = 0.0F;
        rightWing.mirror = true;
        body.addChild(rightWing);
        rightWing.addBox(0.0F, 0.0F, 0.0F, 9, 0, 6, 0.001F);
        frontLeg = new ModelRenderer(this, 26, 1);
        frontLeg.setRotationPoint(1.5F, 3.0F, -2.0F);
        body.addChild(frontLeg);
        frontLeg.addBox(-5.0F, 0.0F, 0.0F, 7, 2, 0);
        midLeg = new ModelRenderer(this, 26, 3);
        midLeg.setRotationPoint(1.5F, 3.0F, 0.0F);
        body.addChild(midLeg);
        midLeg.addBox(-5.0F, 0.0F, 0.0F, 7, 2, 0);
        backLeg = new ModelRenderer(this, 26, 5);
        backLeg.setRotationPoint(1.5F, 3.0F, 2.0F);
        body.addChild(backLeg);
        backLeg.addBox(-5.0F, 0.0F, 0.0F, 7, 2, 0);
    }


    @Override
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        EntityBee bee = (EntityBee) entitylivingbaseIn;
        bodyPitch = bee.isChild() ? 0.0F : bee.getBodyPitch(partialTickTime);
        stinger.showModel = !bee.hasStung();
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        EntityBee bee = (EntityBee) entityIn;

        leftWing.rotateAngleX = 0.0F;
        leftAntenna.rotateAngleX = 0.0F;
        rightAntenna.rotateAngleX = 0.0F;
        body.rotateAngleX = 0.0F;
        body.rotationPointY = 19.0F;
        boolean flag = new Vec3d(bee.motionX, bee.motionY, bee.motionZ).lengthSquared() < 1.0E-7D;
        float f;
        if (flag) {
            leftWing.rotateAngleY = -0.2618F;
            leftWing.rotateAngleZ = 0.0F;
            rightWing.rotateAngleX = 0.0F;
            rightWing.rotateAngleY = 0.2618F;
            rightWing.rotateAngleZ = 0.0F;
            frontLeg.rotateAngleX = 0.0F;
            midLeg.rotateAngleX = 0.0F;
            backLeg.rotateAngleX = 0.0F;
        } else {
            f = ageInTicks * 2.1F;
            leftWing.rotateAngleY = 0.0F;
            leftWing.rotateAngleZ = MathHelper.cos(f) * 3.1415927F * 0.15F;
            rightWing.rotateAngleX = leftWing.rotateAngleX;
            rightWing.rotateAngleY = leftWing.rotateAngleY;
            rightWing.rotateAngleZ = -leftWing.rotateAngleZ;
            frontLeg.rotateAngleX = 0.7853982F;
            midLeg.rotateAngleX = 0.7853982F;
            backLeg.rotateAngleX = 0.7853982F;
            body.rotateAngleX = 0.0F;
            body.rotateAngleY = 0.0F;
            body.rotateAngleZ = 0.0F;
        }

        if (!bee.isAngry()) {
            body.rotateAngleX = 0.0F;
            body.rotateAngleY = 0.0F;
            body.rotateAngleZ = 0.0F;
            if (!flag) {
                f = MathHelper.cos(ageInTicks * 0.18F);
                body.rotateAngleX = 0.1F + f * 3.1415927F * 0.025F;
                leftAntenna.rotateAngleX = f * 3.1415927F * 0.03F;
                rightAntenna.rotateAngleX = f * 3.1415927F * 0.03F;
                frontLeg.rotateAngleX = -f * 3.1415927F * 0.1F + 0.3926991F;
                backLeg.rotateAngleX = -f * 3.1415927F * 0.05F + 0.7853982F;
                body.rotationPointY = 19.0F - MathHelper.cos(ageInTicks * 0.18F) * 0.9F;
            }
        }

        if (bodyPitch > 0.0F) {
            body.rotateAngleX = interpolateAngle(body.rotateAngleX, bodyPitch);
        }
    }


    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        if (isChild) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 1.5F, 0.0F);
            body.render(scale);
            GlStateManager.popMatrix();
        } else {
            body.render(scale);
        }
    }

    private static float interpolateAngle(float headPitch, float bodyPitch) {
        float var3;
        //noinspection StatementWithEmptyBody
        for (var3 = 3.0915928F - headPitch; var3 < -3.1415927F; var3 += 6.2831855F) {
        }

        while (var3 >= 3.1415927F) {
            var3 -= 6.2831855F;
        }

        return headPitch + bodyPitch * var3;
    }
}