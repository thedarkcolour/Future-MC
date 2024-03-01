package thedarkcolour.futuremc.asm

import net.minecraft.block.state.IBlockState
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.model.ModelBiped
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.monster.EntitySnowman
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.EnumRarity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.management.PlayerInteractionManager
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumHandSide
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.GameType
import thedarkcolour.futuremc.client.ClientEvents
import thedarkcolour.futuremc.client.render.TridentBakedModel
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.network.GameModeSwitchPacket
import thedarkcolour.futuremc.network.NetworkHandler
import thedarkcolour.futuremc.registry.FBlocks
import java.util.*

@Suppress("ReplacePutWithAssignment", "unused")
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

    // one map access is faster than three set contains
    @JvmStatic
    private val RARITY_MAP = IdentityHashMap<Item, EnumRarity>()

    init {
        RARITY_MAP.put(Items.EXPERIENCE_BOTTLE, EnumRarity.UNCOMMON)
        RARITY_MAP.put(Items.DRAGON_BREATH, EnumRarity.UNCOMMON)
        RARITY_MAP.put(Items.ELYTRA, EnumRarity.UNCOMMON)
        RARITY_MAP.put(Items.SKULL, EnumRarity.UNCOMMON)
        RARITY_MAP.put(Items.NETHER_STAR, EnumRarity.UNCOMMON)
        RARITY_MAP.put(Items.TOTEM_OF_UNDYING, EnumRarity.UNCOMMON)

        RARITY_MAP.put(BEACON_ITEM, EnumRarity.RARE)
        RARITY_MAP.put(Items.END_CRYSTAL, EnumRarity.RARE)

        RARITY_MAP.put(COMMAND_BLOCK_ITEM, EnumRarity.EPIC)
        RARITY_MAP.put(CHAIN_COMMAND_BLOCK_ITEM, EnumRarity.EPIC)
        RARITY_MAP.put(REPEATING_COMMAND_BLOCK_ITEM, EnumRarity.EPIC)
        RARITY_MAP.put(DRAGON_EGG_ITEM, EnumRarity.EPIC)
        RARITY_MAP.put(STRUCTURE_BLOCK_ITEM, EnumRarity.EPIC)
        RARITY_MAP.put(STRUCTURE_VOID_ITEM, EnumRarity.EPIC)
        RARITY_MAP.put(SPAWNER_ITEM, EnumRarity.EPIC)
        RARITY_MAP.put(BARRIER_ITEM, EnumRarity.EPIC)
        RARITY_MAP.put(Items.COMMAND_BLOCK_MINECART, EnumRarity.EPIC)
    }

    /**
     * Prevents crouching in scaffold blocks to allow the player to fall through
     */
    @JvmStatic
    fun scaffoldFallThrough(flag: Boolean, entity: EntityLivingBase): Boolean {
        return flag && getBlockAtBase(entity).block != FBlocks.SCAFFOLDING
    }

    // i don't know if this is unused, but i'm scared to touch it
    fun a(item: Item, stack: ItemStack): EnumRarity {
        return getEnchantmentRarity(item, stack)
    }

    @JvmStatic
    fun getEnchantmentRarity(item: Item, stack: ItemStack): EnumRarity {
        val rarity = RARITY_MAP.get(item) ?: EnumRarity.COMMON

        return if (stack.isItemEnchanted) {
            when (rarity) {
                EnumRarity.COMMON, EnumRarity.UNCOMMON -> EnumRarity.RARE
                else -> EnumRarity.EPIC
            }
        } else {
            rarity
        }
    }

    @JvmStatic
    private fun getBlockAtBase(entity: EntityLivingBase): IBlockState {
        val pos = BlockPos(MathHelper.floor(entity.posX), MathHelper.floor(entity.entityBoundingBox.minY), MathHelper.floor(entity.posZ))
        return entity.world.getBlockState(pos)
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

        val a = ClientEvents.getCustomArmPose(player, mainItem, offItem, EnumHand.MAIN_HAND) ?: return
        val b = ClientEvents.getCustomArmPose(player, mainItem, offItem, EnumHand.OFF_HAND) ?: return

        if (player.primaryHand == EnumHandSide.RIGHT) {
            model.rightArmPose = a
            model.leftArmPose = b
        } else {
            model.leftArmPose = a
            model.rightArmPose = b
        }
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
    @JvmStatic
    fun rotateByPose(model: ModelBiped) {
        val rightPose = model.rightArmPose
        val leftPose = model.leftArmPose

        if (rightPose == TridentBakedModel.TRIDENT_ARM_POSE) {
            model.bipedRightArm.rotateAngleX = model.bipedRightArm.rotateAngleX * 0.5f - Math.PI.toFloat()
            model.bipedRightArm.rotateAngleY = 0.0f
        } else if (leftPose == TridentBakedModel.TRIDENT_ARM_POSE) {
            model.bipedLeftArm.rotateAngleX = model.bipedLeftArm.rotateAngleX * 0.5f - Math.PI.toFloat()
            model.bipedLeftArm.rotateAngleY = 0.0f
        }
    }

    @JvmStatic
    fun onSnowmanSheared(snowman: EntitySnowman) {
        if (FConfig.netherUpdate.snowGolemShearing && snowman.isPumpkinEquipped) {
            snowman.dropItemWithOffset(Item.getItemFromBlock(Blocks.PUMPKIN), 1, 1.7f)
        }
    }

    @JvmStatic
    fun setPrevGameType(interactionManager: PlayerInteractionManager) {
        if (interactionManager.gameType != GameType.NOT_SET) {
            NetworkHandler.sendSetPrevGameMode(interactionManager.player, interactionManager.gameType)
        }
    }
}