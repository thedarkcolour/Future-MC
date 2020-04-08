package thedarkcolour.futuremc.entity.fish.pufferfish

import net.minecraft.client.model.ModelBase
import net.minecraft.client.model.ModelRenderer
import net.minecraft.entity.Entity
import kotlin.math.sin

class ModelSmallPufferfish : ModelBase() {
    private val body: ModelRenderer
    private val field_203755_b: ModelRenderer
    private val field_203756_c: ModelRenderer
    private val rightFin: ModelRenderer
    private val leftFin: ModelRenderer
    private val field_203759_f: ModelRenderer

    init {
        textureWidth = 32
        body = ModelRenderer(this, 0, 27)
        body.addBox(-1.5F, -2.0F, -1.5F, 3, 2, 3)
        body.setRotationPoint(0.0F, 23.0F, 0.0F)
        field_203755_b = ModelRenderer(this, 24, 6)
        field_203755_b.addBox(-1.5F, 0.0F, -1.5F, 1, 1, 1)
        field_203755_b.setRotationPoint(0.0F, 20.0F, 0.0F)
        field_203756_c = ModelRenderer(this, 28, 6)
        field_203756_c.addBox(0.5F, 0.0F, -1.5F, 1, 1, 1)
        field_203756_c.setRotationPoint(0.0F, 20.0F, 0.0F)
        field_203759_f = ModelRenderer(this, -3, 0)
        field_203759_f.addBox(-1.5F, 0.0F, 0.0F, 3, 0, 3)
        field_203759_f.setRotationPoint(0.0F, 22.0F, 1.5F)
        rightFin = ModelRenderer(this, 25, 0)
        rightFin.addBox(-1.0F, 0.0F, 0.0F, 1, 0, 2)
        rightFin.setRotationPoint(-1.5F, 22.0F, -1.5F)
        leftFin = ModelRenderer(this, 25, 0)
        leftFin.addBox(0.0F, 0.0F, 0.0F, 1, 0, 2)
        leftFin.setRotationPoint(1.5F, 22.0F, -1.5F)
    }

    override fun render(
        entityIn: Entity,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float,
        scale: Float
    ) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn)
        body.render(scale)
        field_203755_b.render(scale)
        field_203756_c.render(scale)
        field_203759_f.render(scale)
        rightFin.render(scale)
        leftFin.render(scale)
    }

    override fun setRotationAngles(
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float,
        scaleFactor: Float,
        entityIn: Entity
    ) {
        rightFin.rotateAngleZ = -0.2F + 0.4F * sin(ageInTicks * 0.2F)
        leftFin.rotateAngleZ = 0.2F - 0.4F * sin(ageInTicks * 0.2F)
    }
}