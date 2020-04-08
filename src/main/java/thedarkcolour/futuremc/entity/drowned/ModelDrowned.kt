package thedarkcolour.futuremc.entity.drowned

import net.minecraft.client.model.ModelBiped
import net.minecraft.client.model.ModelRenderer
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.EnumHandSide
import thedarkcolour.futuremc.registry.FItems
import kotlin.math.PI

class ModelDrowned : ModelBiped() {
    init {
        bipedRightArm = ModelRenderer(this, 32, 48)
        bipedRightArm.addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4)
        bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F)

        bipedRightLeg = ModelRenderer(this, 16, 48)
        bipedRightLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4)
        bipedRightLeg.setRotationPoint(-1.9f, 12.0f, 0.0f)
    }

    override fun setLivingAnimations(
        entityLiving: EntityLivingBase,
        limbSwing: Float,
        limbSwingAmount: Float,
        partialTickTime: Float
    ) {

    }

    override fun setRotationAngles(
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float,
        scaleFactor: Float,
        entityLiving: Entity
    ) {
        super.setRotationAngles(
            limbSwing,
            limbSwingAmount,
            ageInTicks,
            netHeadYaw,
            headPitch,
            scaleFactor,
            entityLiving
        )
        entityLiving as EntityDrowned
        rightArmPose = ArmPose.EMPTY
        leftArmPose = ArmPose.EMPTY
        val stack = entityLiving.heldItemMainhand

        if (stack.item == FItems.TRIDENT && entityLiving.isSwingingArms()) {
            if (entityLiving.primaryHand == EnumHandSide.RIGHT) {
                bipedRightArm.rotateAngleX *= 0.5f - PI.toFloat()
                bipedRightArm.rotateAngleY = 0.0f
            } else {
                if (rightArmPose == ArmPose.ITEM || rightArmPose == ArmPose.EMPTY) {
                    bipedLeftArm.rotateAngleX *= 0.5f - PI.toFloat()
                    bipedLeftArm.rotateAngleY = 0.0f
                }
            }
        }
    }
}