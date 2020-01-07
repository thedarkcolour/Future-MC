package thedarkcolour.futuremc.entity.fish.cod

import net.minecraft.client.model.ModelBase
import net.minecraft.client.model.ModelRenderer
import net.minecraft.entity.Entity
import kotlin.math.PI
import kotlin.math.sin

class ModelCod : ModelBase() {
    private val body: ModelRenderer
    private val finTop: ModelRenderer
    private val head: ModelRenderer
    private val headFront: ModelRenderer
    private val finRight: ModelRenderer
    private val finLeft: ModelRenderer
    private val tail: ModelRenderer

    init {
        textureWidth = 32
        body = ModelRenderer(this, 0, 0)
        body.addBox(-1.0F, -2.0F, 0.0F, 2, 4, 7)
        body.setRotationPoint(0.0F, 22.0F, 0.0F)
        head = ModelRenderer(this, 11, 0)
        head.addBox(-1.0F, -2.0F, -3.0F, 2, 4, 3)
        head.setRotationPoint(0.0F, 22.0F, 0.0F)
        headFront = ModelRenderer(this, 0, 0)
        headFront.addBox(-1.0F, -2.0F, -1.0F, 2, 3, 1)
        headFront.setRotationPoint(0.0F, 22.0F, -3.0F)
        finRight = ModelRenderer(this, 22, 1)
        finRight.addBox(-2.0F, 0.0F, -1.0F, 2, 0, 2)
        finRight.setRotationPoint(-1.0F, 23.0F, 0.0F)
        finRight.rotateAngleZ = (-PI).toFloat() / 4F
        finLeft = ModelRenderer(this, 22, 4)
        finLeft.addBox(0.0F, 0.0F, -1.0F, 2, 0, 2)
        finLeft.setRotationPoint(1.0F, 23.0F, 0.0F)
        finLeft.rotateAngleZ = PI.toFloat() / 4F
        tail = ModelRenderer(this, 22, 3)
        tail.addBox(0.0F, -2.0F, 0.0F, 0, 4, 4)
        tail.setRotationPoint(0.0F, 22.0F, 7.0F)
        finTop = ModelRenderer(this, 20, -6)
        finTop.addBox(0.0F, -1.0F, -1.0F, 0, 1, 6)
        finTop.setRotationPoint(0.0F, 20.0F, 0.0F)
    }

    override fun render(entityIn: Entity, limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn)
        body.render(scale)
        head.render(scale)
        headFront.render(scale)
        finRight.render(scale)
        finLeft.render(scale)
        tail.render(scale)
        finTop.render(scale)
    }

    override fun setRotationAngles(limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scaleFactor: Float, entityIn: Entity) {
        val f = if (entityIn.isInWater) {
            1F
        } else {
            1.5F
        }
        tail.rotateAngleY = -f * 0.45F * sin(0.6F * ageInTicks)
    }
}