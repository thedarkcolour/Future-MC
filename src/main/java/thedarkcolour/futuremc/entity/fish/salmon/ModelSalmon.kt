package thedarkcolour.futuremc.entity.fish.salmon

import net.minecraft.client.model.ModelBase
import net.minecraft.client.model.ModelRenderer
import net.minecraft.entity.Entity
import kotlin.math.PI
import kotlin.math.sin

class ModelSalmon : ModelBase() {
    private val bodyFront: ModelRenderer
    private val bodyRear: ModelRenderer
    private val head: ModelRenderer
    private val finTopFront: ModelRenderer
    private val finTopRear: ModelRenderer
    private val tail: ModelRenderer
    private val finRight: ModelRenderer
    private val finLeft: ModelRenderer

    init {
        textureWidth = 32

        bodyFront = ModelRenderer(this, 0, 0)
        bodyFront.addBox(-1.5f, -2.5f, 0.0f, 3, 5, 8)
        bodyFront.setRotationPoint(0.0f, 20.0f, 0.0f)
        bodyRear = ModelRenderer(this, 0, 13)
        bodyRear.addBox(-1.5f, -2.5f, 0.0f, 3, 5, 8)
        bodyRear.setRotationPoint(0.0f, 20.0f, 8.0f)
        head = ModelRenderer(this, 22, 0)
        head.addBox(-1.0f, -2.0f, -3.0f, 2, 4, 3)
        head.setRotationPoint(0.0f, 20.0f, 0.0f)
        tail = ModelRenderer(this, 20, 10)
        tail.addBox(0.0f, -2.5f, 0.0f, 0, 5, 6)
        tail.setRotationPoint(0.0f, 0.0f, 8.0f)
        bodyRear.addChild(tail)
        finTopFront = ModelRenderer(this, 2, 1)
        finTopFront.addBox(0.0f, 0.0f, 0.0f, 0, 2, 3)
        finTopFront.setRotationPoint(0.0f, -4.5f, 5.0f)
        bodyFront.addChild(finTopFront)
        finTopRear = ModelRenderer(this, 0, 2)
        finTopRear.addBox(0.0f, 0.0f, 0.0f, 0, 2, 4)
        finTopRear.setRotationPoint(0.0f, -4.5f, -1.0f)
        bodyRear.addChild(finTopRear)
        finRight = ModelRenderer(this, -4, 0)
        finRight.addBox(-2.0f, 0.0f, 0.0f, 2, 0, 2)
        finRight.setRotationPoint(-1.5f, 21.5f, 0.0f)
        finRight.rotateAngleZ = (-PI.toFloat() / 4F)
        finLeft = ModelRenderer(this, 0, 0)
        finLeft.addBox(0.0f, 0.0f, 0.0f, 2, 0, 2)
        finLeft.setRotationPoint(1.5f, 21.5f, 0.0f)
    }

    override fun render(entityIn: Entity, limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn)
        bodyFront.render(scale)
        bodyRear.render(scale)
        head.render(scale)
        finRight.render(scale)
        finLeft.render(scale)
    }

    override fun setRotationAngles(limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scaleFactor: Float, entityIn: Entity) {
        var i = 1F
        var j = 1F

        if (!entityIn.isInWater) {
            i = 1.3F
            j = 1.7F
        }

        bodyRear.rotateAngleY = -i * 0.25f * sin(j * 0.6F * ageInTicks)
    }
}