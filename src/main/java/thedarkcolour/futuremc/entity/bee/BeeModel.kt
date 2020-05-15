package thedarkcolour.futuremc.entity.bee

import net.minecraft.client.model.ModelRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.math.Vec3d
import thedarkcolour.futuremc.entity.FModel
import kotlin.math.cos

class BeeModel : FModel<EntityBee>() {
    private val body: ModelRenderer
    private val leftWing: ModelRenderer
    private val rightWing: ModelRenderer
    private val frontLeg: ModelRenderer
    private val midLeg: ModelRenderer
    private val backLeg: ModelRenderer
    private val stinger: ModelRenderer
    private val leftAntenna: ModelRenderer
    private val rightAntenna: ModelRenderer
    private var bodyPitch = 0.0f

    init {
        textureHeight = 64
        body = ModelRenderer(this)
        body.setRotationPoint(0.0f, 19.0f, 0.0f)
        val mainBody = ModelRenderer(this, 0, 0)
        mainBody.setRotationPoint(0.0f, 0.0f, 0.0f)
        body.addChild(mainBody)
        mainBody.addBox(-3.5f, -4.0f, -5.0f, 7, 7, 10, 0.0f)
        stinger = ModelRenderer(this, 26, 7)
        stinger.addBox(0.0f, -1.0f, 5.0f, 0, 1, 2, 0.0f)
        mainBody.addChild(stinger)
        leftAntenna = ModelRenderer(this, 2, 0)
        leftAntenna.setRotationPoint(0.0f, -2.0f, -5.0f)
        leftAntenna.addBox(1.5f, -2.0f, -3.0f, 1, 2, 3, 0.0f)
        rightAntenna = ModelRenderer(this, 2, 3)
        rightAntenna.setRotationPoint(0.0f, -2.0f, -5.0f)
        rightAntenna.addBox(-2.5f, -2.0f, -3.0f, 1, 2, 3, 0.0f)
        mainBody.addChild(leftAntenna)
        mainBody.addChild(rightAntenna)
        leftWing = ModelRenderer(this, 0, 18)
        leftWing.setRotationPoint(-1.5f, -4.0f, -3.0f)
        leftWing.rotateAngleX = 0.0f
        leftWing.rotateAngleY = -0.2618f
        leftWing.rotateAngleZ = 0.0f
        body.addChild(leftWing)
        leftWing.addBox(-9.0f, 0.0f, 0.0f, 9, 0, 6, 0.001f)
        rightWing = ModelRenderer(this, 0, 18)
        rightWing.setRotationPoint(1.5f, -4.0f, -3.0f)
        rightWing.rotateAngleX = 0.0f
        rightWing.rotateAngleY = 0.2618f
        rightWing.rotateAngleZ = 0.0f
        rightWing.mirror = true
        body.addChild(rightWing)
        rightWing.addBox(0.0f, 0.0f, 0.0f, 9, 0, 6, 0.001f)
        frontLeg = ModelRenderer(this, 26, 1)
        frontLeg.setRotationPoint(1.5f, 3.0f, -2.0f)
        body.addChild(frontLeg)
        frontLeg.addBox(-5.0f, 0.0f, 0.0f, 7, 2, 0)
        midLeg = ModelRenderer(this, 26, 3)
        midLeg.setRotationPoint(1.5f, 3.0f, 0.0f)
        body.addChild(midLeg)
        midLeg.addBox(-5.0f, 0.0f, 0.0f, 7, 2, 0)
        backLeg = ModelRenderer(this, 26, 5)
        backLeg.setRotationPoint(1.5f, 3.0f, 2.0f)
        body.addChild(backLeg)
        backLeg.addBox(-5.0f, 0.0f, 0.0f, 7, 2, 0)
    }


    override fun setEntityLivingAnimations(
        entity: EntityBee,
        limbSwing: Float,
        limbSwingAmount: Float,
        partialTicks: Float
    ) {
        bodyPitch = if (entity.isChild) 0.0f else entity.getBodyPitch(partialTicks)
        stinger.showModel = !entity.hasStung()
    }

    override fun setRotationAngles(
        entity: EntityBee,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float,
        scaleFactor: Float
    ) {
        leftWing.rotateAngleX = 0.0f
        leftAntenna.rotateAngleX = 0.0f
        rightAntenna.rotateAngleX = 0.0f
        body.rotateAngleX = 0.0f
        body.rotationPointY = 19.0f
        val flag = Vec3d(entity.motionX, entity.motionY, entity.motionZ).lengthSquared() < 1.0E-7
        var f: Float
        if (flag) {
            leftWing.rotateAngleY = -0.2618f
            leftWing.rotateAngleZ = 0.0f
            rightWing.rotateAngleX = 0.0f
            rightWing.rotateAngleY = 0.2618f
            rightWing.rotateAngleZ = 0.0f
            frontLeg.rotateAngleX = 0.0f
            midLeg.rotateAngleX = 0.0f
            backLeg.rotateAngleX = 0.0f
        } else {
            f = ageInTicks * 2.1f
            leftWing.rotateAngleY = 0.0f
            leftWing.rotateAngleZ = cos(f) * 3.1415927f * 0.15f
            rightWing.rotateAngleX = leftWing.rotateAngleX
            rightWing.rotateAngleY = leftWing.rotateAngleY
            rightWing.rotateAngleZ = -leftWing.rotateAngleZ
            frontLeg.rotateAngleX = 0.7853982f
            midLeg.rotateAngleX = 0.7853982f
            backLeg.rotateAngleX = 0.7853982f
            body.rotateAngleX = 0.0f
            body.rotateAngleY = 0.0f
            body.rotateAngleZ = 0.0f
        }

        if (!entity.isAngry) {
            body.rotateAngleX = 0.0f
            body.rotateAngleY = 0.0f
            body.rotateAngleZ = 0.0f
            if (!flag) {
                f = cos(ageInTicks * 0.18f)
                body.rotateAngleX = 0.1f + f * 3.1415927f * 0.025f
                leftAntenna.rotateAngleX = f * 3.1415927f * 0.03f
                rightAntenna.rotateAngleX = f * 3.1415927f * 0.03f
                frontLeg.rotateAngleX = -f * 3.1415927f * 0.1f + 0.3926991f
                backLeg.rotateAngleX = -f * 3.1415927f * 0.05f + 0.7853982f
                body.rotationPointY = 19.0f - cos(ageInTicks * 0.18f) * 0.9f
            }
        }

        if (bodyPitch > 0.0f) {
            body.rotateAngleX = interpolateAngle(body.rotateAngleX, bodyPitch)
        }
    }

    override fun render(
        entity: EntityBee,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float,
        scale: Float
    ) {
        if (isChild) {
            GlStateManager.pushMatrix()
            GlStateManager.scale(0.5f, 0.5f, 0.5f)
            GlStateManager.translate(0.0f, 1.5f, 0.0f)
            body.render(scale)
            GlStateManager.popMatrix()
        } else {
            body.render(scale)
        }
    }

    private fun interpolateAngle(headPitch: Float, bodyPitch: Float): Float {
        var var3 = 3.0915928f - headPitch

        while (var3 < -3.1415927f) {
            var3 += 6.2831855f
        }

        while (var3 >= 3.1415927f) {
            var3 -= 6.2831855f
        }

        return headPitch + bodyPitch * var3
    }
}