package thedarkcolour.futuremc.asm

import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.ItemModelMesher
import net.minecraft.client.renderer.RenderItem
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumHandSide
import net.minecraftforge.client.ForgeHooksClient
import thedarkcolour.futuremc.item.TridentItem
import kotlin.math.PI

object ASMHooks {
    /**
     * Called in [RenderItem.renderItem]. Part of the Trident coremod.
     *
     * Applies perspective changes BEFORE applying model overrides.
     * This allows the Trident to override itself in hand, but not in gui.
     */
    @JvmStatic
    fun getPerspectiveAndOverride(
        mesher: ItemModelMesher,
        stack: ItemStack,
        transform: TransformType
    ): IBakedModel {
        var model = mesher.getItemModel(stack)
        model = ForgeHooksClient.handleCameraTransforms(model, transform, false)
        return model.overrides.handleItemState(model, stack, null, null)
    }

    /**
     * Called in [net.minecraft.client.renderer.entity.RenderPlayer.setModelVisibilities].
     * Part of the Trident (and eventually Crossbow) coremod.
     *
     * Adds functionality for the custom Arm Poses that Future MC adds.
     */
    @JvmStatic
    fun setCustomArmPoses(player: AbstractClientPlayer, model: ModelBiped) {
        val mainItem = player.heldItemMainhand
        val offItem = player.heldItemMainhand

        val a = rotateHand(player, mainItem, offItem, EnumHand.MAIN_HAND) ?: return
        val b = rotateHand(player, mainItem, offItem, EnumHand.OFF_HAND) ?: return

        if (player.primaryHand == EnumHandSide.RIGHT) {
            model.rightArmPose = a
            model.leftArmPose = b
        } else {
            model.leftArmPose = a
            model.rightArmPose = b
        }
    }

    /**
     * If the player has a Trident (or eventually a Crossbow)
     * this sets the correct pose for the player's arms.
     */
    private fun rotateHand(
        playerIn: EntityPlayer,
        mainItem: ItemStack,
        offItem: ItemStack,
        hand: EnumHand
    ): ModelBiped.ArmPose? {
        val stack = if (hand == EnumHand.MAIN_HAND) mainItem else offItem

        if (!stack.isEmpty) {
            if (playerIn.itemInUseCount > 0) {
                val action = stack.itemUseAction

                if (action == TridentItem.TRIDENT_USE_ACTION) {
                    return TridentItem.TRIDENT_ARM_POSE
                }/* else if (action == CrossbowItem.CROSSBOW_USE_ACTION) {
                    pose = CrossbowItem.CROSSBOW_ARM_POSE
                }
            } else {
                val flag3 = mainItem.item == FItems.CROSSBOW
                val flag = CrossbowItem.isCharged(mainItem)
                val flag1 = offItem.item == FItems.CROSSBOW
                val flag2 = CrossbowItem.isCharged(offItem)

                if (flag3 && flag) {
                    pose = CrossbowItem.CROSSBOW_ARM_POSE
                }

                if (flag1 && flag2) {
                    pose = CrossbowItem.CROSSBOW_ARM_POSE
                }*/
            }
        }

        return null
    }

    /**
     * Called in [net.minecraft.client.model.ModelBiped.setRotationAngles].
     * Part of the Trident (and eventually Crossbow) coremod.
     *
     * Rotates the player's arms if the poses of those arms are
     * custom Future MC arm poses.
     */
    @JvmStatic
    fun rotateByPose(model: ModelBiped) {
        val rightPose = model.rightArmPose
        val leftPose = model.leftArmPose

        if (rightPose == TridentItem.TRIDENT_ARM_POSE) {
            model.bipedRightArm.rotateAngleX = model.bipedRightArm.rotateAngleX * 0.5f - PI.toFloat()
            model.bipedRightArm.rotateAngleY = 0.0f
        } else if (leftPose == TridentItem.TRIDENT_ARM_POSE) {
            model.bipedLeftArm.rotateAngleX = model.bipedLeftArm.rotateAngleX * 0.5f - PI.toFloat()
            model.bipedLeftArm.rotateAngleY = 0.0f
        }
    }
}