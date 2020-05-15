package thedarkcolour.futuremc.entity

import net.minecraft.client.model.ModelBase
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase

/**
 * Adds generic type to [ModelBase] and adds a call to [setRotationAngles] to render
 */
@Suppress("UNCHECKED_CAST")
abstract class FModel<T : EntityLivingBase> : ModelBase() {
    final override fun render(
        entity: Entity,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float,
        scale: Float
    ) {
        setRotationAngles(entity as T, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale)
        render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale)
    }

    /**
     * By this point, [setRotationAngles] has already been called.
     */
    open fun render(
        entity: T,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float,
        scale: Float
    ) = Unit

    final override fun setLivingAnimations(
        entity: EntityLivingBase,
        limbSwing: Float,
        limbSwingAmount: Float,
        partialTicks: Float
    ) = setEntityLivingAnimations(entity as T, limbSwing, limbSwingAmount, partialTicks)

    open fun setEntityLivingAnimations(
        entity: T,
        limbSwing: Float,
        limbSwingAmount: Float,
        partialTicks: Float
    ) = Unit

    final override fun setRotationAngles(
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float,
        scale: Float,
        entity: Entity
    ) {
        setRotationAngles(entity as T, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale)
    }

    open fun setRotationAngles(
        entity: T,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float,
        scaleFactor: Float
    ) = Unit
}