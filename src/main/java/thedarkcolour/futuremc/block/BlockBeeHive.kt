package thedarkcolour.futuremc.block

import net.minecraft.block.BlockHorizontal
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Enchantments
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.block.InteractionBlock
import thedarkcolour.futuremc.FutureMC.TAB
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.entity.bee.EntityBee
import thedarkcolour.futuremc.init.FBlocks.BEEHIVE
import thedarkcolour.futuremc.tile.TileBeeHive
import java.util.*

class BlockBeeHive(regName: String?) : InteractionBlock(regName, Material.WOOD, SoundType.WOOD) {
    override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item {
        return if (state.block == BEEHIVE) super.getItemDropped(state, rand, fortune) else Item.getItemFromBlock(Blocks.AIR)
    }

    override fun canSilkHarvest(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer): Boolean {
        return true
    }

    override fun getSilkTouchDrop(state: IBlockState): ItemStack {
        return ItemStack.EMPTY
    }

    override fun getHarvestTool(state: IBlockState): String? {
        return "axe"
    }

    override fun createTileEntity(world: World, state: IBlockState): TileEntity? {
        return TileBeeHive()
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, FACING, IS_FULL)
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(IS_FULL, meta and 4 != 0).withProperty(FACING, EnumFacing.byHorizontalIndex(meta and -5))
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return (if (state.getValue(IS_FULL)) 4 else 0) or state.getValue(FACING).horizontalIndex
    }

    override fun hasComparatorInputOverride(state: IBlockState): Boolean {
        return true
    }

    override fun getComparatorInputOverride(blockState: IBlockState, worldIn: World, pos: BlockPos): Int {
        return if (worldIn.getTileEntity(pos) is TileBeeHive) {
            (worldIn.getTileEntity(pos) as TileBeeHive?)!!.honeyLevel
        } else 0
    }

    override fun removedByPlayer(state: IBlockState, worldIn: World, pos: BlockPos, player: EntityPlayer, willHarvest: Boolean): Boolean {
        if (!worldIn.isRemote && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.heldItemMainhand) == 0) {
            if (worldIn.getTileEntity(pos) is TileBeeHive) {
                (worldIn.getTileEntity(pos) as TileBeeHive?)!!.angerBees(player, TileBeeHive.BeeState.DELIVERED)
                worldIn.updateComparatorOutputLevel(pos, this)
            }
            val nearbyBees = worldIn.getEntitiesWithinAABB(EntityBee::class.java, AxisAlignedBB(pos).expand(8.0, 6.0, 8.0))
            if (!nearbyBees.isEmpty()) {
                val nearbyPlayers = worldIn.getEntitiesWithinAABB(EntityPlayer::class.java, AxisAlignedBB(pos).expand(8.0, 6.0, 8.0))
                val players = nearbyPlayers.size
                if (players > 0) {
                    for (bee in nearbyBees) {
                        if (bee.attackTarget == null) {
                            bee.setBeeAttacker(nearbyPlayers[worldIn.rand.nextInt(players)])
                        }
                    }
                }
            }
        }
        return super.removedByPlayer(state, worldIn, pos, player, willHarvest)
    }

    override fun getStateForPlacement(worldIn: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase): IBlockState {
        return defaultState.withProperty(FACING, placer.horizontalFacing.opposite)
    }

    override fun setHardness(hardness: Float): BlockBeeHive {
        super.setHardness(hardness)
        return this
    }

    companion object {
        val IS_FULL = PropertyBool.create("full")
        @kotlin.jvm.JvmField
        val FACING = BlockHorizontal.FACING
    }

    init {
        defaultState = defaultState.withProperty(FACING, EnumFacing.NORTH).withProperty(IS_FULL, false)
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.DECORATIONS else TAB
    }
}