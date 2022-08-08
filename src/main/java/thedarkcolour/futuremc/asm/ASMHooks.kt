package thedarkcolour.futuremc.asm

import net.minecraft.block.state.IBlockState
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.ItemModelMesher
import net.minecraft.client.renderer.RenderItem
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.EnumRarity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraftforge.client.ForgeHooksClient
import thedarkcolour.futuremc.client.render.TridentBakedModel
import thedarkcolour.futuremc.registry.FBlocks

object ASMHooks {
    @JvmStatic
    private val BEACON_ITEM = Item.getItemFromBlock(Blocks.BEACON)
    @JvmStatic
    private val COMMAND_BLOCK_ITEM = Item.getItemFromBlock(Blocks.COMMAND_BLOCK)
    @JvmStatic
    private val CHAIN_COMMAND_BLOCK_ITEM = Item.getItemFromBlock(Blocks.CHAIN_COMMAND_BLOCK)
    @JvmStatic
    private val REPEATING_COMMAND_BLOCK_ITEM = Item.getItemFromBlock(Blocks.REPEATING_COMMAND_BLOCK)
    @JvmStatic
    private val DRAGON_EGG_ITEM = Item.getItemFromBlock(Blocks.DRAGON_EGG)
    @JvmStatic
    private val STRUCTURE_BLOCK_ITEM = Item.getItemFromBlock(Blocks.STRUCTURE_BLOCK)
    @JvmStatic
    private val STRUCTURE_VOID_ITEM = Item.getItemFromBlock(Blocks.STRUCTURE_VOID)
    @JvmStatic
    private val SPAWNER_ITEM = Item.getItemFromBlock(Blocks.MOB_SPAWNER)
    @JvmStatic
    private val BARRIER_ITEM = Item.getItemFromBlock(Blocks.BARRIER)

    @JvmStatic
    private val UNCOMMON_ITEMS = hashSetOf(Items.EXPERIENCE_BOTTLE, Items.DRAGON_BREATH, Items.ELYTRA, Items.SKULL, Items.NETHER_STAR, Items.TOTEM_OF_UNDYING)
    @JvmStatic
    private val RARE_ITEMS = hashSetOf(BEACON_ITEM, Items.END_CRYSTAL)
    @JvmStatic
    private val EPIC_ITEMS = hashSetOf(COMMAND_BLOCK_ITEM, CHAIN_COMMAND_BLOCK_ITEM, REPEATING_COMMAND_BLOCK_ITEM, DRAGON_EGG_ITEM, STRUCTURE_BLOCK_ITEM, STRUCTURE_VOID_ITEM, SPAWNER_ITEM, BARRIER_ITEM, Items.COMMAND_BLOCK_MINECART)

    /**
     * Prevents crouching in scaffold blocks to allow the player to fall through
     */
    @JvmStatic
    fun scaffoldFallThrough(flag: Boolean, entity: EntityLivingBase): Boolean {
        return flag && getBlockAtBase(entity).block != FBlocks.SCAFFOLDING
    }

    fun a(item: Item, stack: ItemStack): EnumRarity {
        return getEnchantmentRarity(item, stack)
    }

    @JvmStatic
    fun getEnchantmentRarity(item: Item, stack: ItemStack): EnumRarity {
        if (UNCOMMON_ITEMS.contains(item)) {
            return if (stack.isItemEnchanted) EnumRarity.RARE else EnumRarity.UNCOMMON
        } else if (RARE_ITEMS.contains(item)) {
            return if (stack.isItemEnchanted) EnumRarity.EPIC else EnumRarity.RARE
        } else if (EPIC_ITEMS.contains(item)) {
            return EnumRarity.EPIC
        } else {
            return if (stack.isItemEnchanted) EnumRarity.RARE else EnumRarity.COMMON
        }
    }

    @JvmStatic
    private fun getBlockAtBase(entity: EntityLivingBase): IBlockState {
        val pos = BlockPos(MathHelper.floor(entity.posX), MathHelper.floor(entity.entityBoundingBox.minY), MathHelper.floor(entity.posZ))
        return entity.world.getBlockState(pos)
    }

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
    /*@JvmStatic
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
    }*/

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

                if (action == TridentBakedModel.TRIDENT_USE_ACTION) {
                    return TridentBakedModel.TRIDENT_ARM_POSE
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

    // todo config option
    @JvmStatic
    fun creativeEat(player: EntityPlayer): Boolean {
        return player.isCreative
    }

    /**
     * Called in [net.minecraft.client.model.ModelBiped.setRotationAngles].
     * Part of the Trident (and eventually Crossbow) coremod.
     *
     * Rotates the player's arms if the poses of those arms are
     * custom Future MC arm poses.
     */
    /*@JvmStatic
    fun rotateByPose(model: ModelBiped) {
        val rightPose = model.rightArmPose
        val leftPose = model.leftArmPose

        if (rightPose == TridentBakedModel.TRIDENT_ARM_POSE) {
            model.bipedRightArm.rotateAngleX = model.bipedRightArm.rotateAngleX * 0.5f - PI.toFloat()
            model.bipedRightArm.rotateAngleY = 0.0f
        } else if (leftPose == TridentBakedModel.TRIDENT_ARM_POSE) {
            model.bipedLeftArm.rotateAngleX = model.bipedLeftArm.rotateAngleX * 0.5f - PI.toFloat()
            model.bipedLeftArm.rotateAngleY = 0.0f
        }
    }*/
}