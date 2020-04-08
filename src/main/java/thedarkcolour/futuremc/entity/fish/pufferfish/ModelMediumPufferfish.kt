package thedarkcolour.futuremc.entity.fish.pufferfish

import net.minecraft.client.model.ModelBase
import net.minecraft.client.model.ModelRenderer
import net.minecraft.entity.Entity
import kotlin.math.PI
import kotlin.math.sin

class ModelMediumPufferfish : ModelBase() {
    private val body: ModelRenderer
    private val rightFin: ModelRenderer
    private val leftFin: ModelRenderer
    private val field_203733_d: ModelRenderer
    private val field_203734_e: ModelRenderer
    private val field_203735_f: ModelRenderer
    private val field_203736_g: ModelRenderer
    private val field_203737_h: ModelRenderer
    private val field_203738_i: ModelRenderer
    private val field_203739_j: ModelRenderer
    private val field_203740_k: ModelRenderer

    init {
        textureWidth = 32

        body = ModelRenderer(this, 12, 22)
        body.addBox(-2.5F, -5.0F, -2.5F, 5, 5, 5)
        body.setRotationPoint(0.0F, 22.0F, 0.0F)
        rightFin = ModelRenderer(this, 24, 0)
        rightFin.addBox(-2.0F, 0.0F, 0.0F, 2, 0, 2)
        rightFin.setRotationPoint(-2.5F, 17.0F, -1.5F)
        leftFin = ModelRenderer(this, 24, 3)
        leftFin.addBox(0.0F, 0.0F, 0.0F, 2, 0, 2)
        leftFin.setRotationPoint(2.5F, 17.0F, -1.5F)
        field_203733_d = ModelRenderer(this, 15, 16)
        field_203733_d.addBox(-2.5F, -1.0F, 0.0F, 5, 1, 1)
        field_203733_d.setRotationPoint(0.0F, 17.0F, -2.5F)
        field_203733_d.rotateAngleX = (PI.toFloat() / 4F)
        field_203734_e = ModelRenderer(this, 10, 16)
        field_203734_e.addBox(-2.5F, -1.0F, -1.0F, 5, 1, 1)
        field_203734_e.setRotationPoint(0.0F, 17.0F, 2.5F)
        field_203734_e.rotateAngleX = (-PI.toFloat() / 4F)
        field_203735_f = ModelRenderer(this, 8, 16)
        field_203735_f.addBox(-1.0F, -5.0F, 0.0F, 1, 5, 1)
        field_203735_f.setRotationPoint(-2.5F, 22.0F, -2.5F)
        field_203735_f.rotateAngleY = (-PI.toFloat() / 4F)
        field_203736_g = ModelRenderer(this, 8, 16)
        field_203736_g.addBox(-1.0F, -5.0F, 0.0F, 1, 5, 1)
        field_203736_g.setRotationPoint(-2.5F, 22.0F, 2.5F)
        field_203736_g.rotateAngleY = (PI.toFloat() / 4F)
        field_203737_h = ModelRenderer(this, 4, 16)
        field_203737_h.addBox(0.0F, -5.0F, 0.0F, 1, 5, 1)
        field_203737_h.setRotationPoint(2.5F, 22.0F, 2.5F)
        field_203737_h.rotateAngleY = (-PI.toFloat() / 4F)
        field_203738_i = ModelRenderer(this, 0, 16)
        field_203738_i.addBox(0.0F, -5.0F, 0.0F, 1, 5, 1)
        field_203738_i.setRotationPoint(2.5F, 22.0F, -2.5F)
        field_203738_i.rotateAngleY = (PI.toFloat() / 4F)
        field_203739_j = ModelRenderer(this, 8, 22)
        field_203739_j.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1)
        field_203739_j.setRotationPoint(0.5F, 22.0F, 2.5F)
        field_203739_j.rotateAngleX = (PI.toFloat() / 4F)
        field_203740_k = ModelRenderer(this, 17, 21)
        field_203740_k.addBox(-2.5F, 0.0F, 0.0F, 5, 1, 1)
        field_203740_k.setRotationPoint(0.0F, 22.0F, -2.5F)
        field_203740_k.rotateAngleX = (-PI.toFloat() / 4F)
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
        rightFin.render(scale)
        leftFin.render(scale)
        field_203733_d.render(scale)
        field_203734_e.render(scale)
        field_203735_f.render(scale)
        field_203736_g.render(scale)
        field_203737_h.render(scale)
        field_203738_i.render(scale)
        field_203739_j.render(scale)
        field_203740_k.render(scale)
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