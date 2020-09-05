package thedarkcolour.futuremc.block.buzzybees

import net.minecraft.block.BlockHorizontal
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.block.InteractionBlock
import thedarkcolour.futuremc.registry.FBlocks.BEEHIVE
import thedarkcolour.futuremc.tile.BeeHiveTile
import java.util.*

class BeeHiveBlock(properties: Properties) : InteractionBlock(properties) {
    init {
        defaultState = defaultState.withProperty(FACING, EnumFacing.NORTH).withProperty(IS_FULL, false)
    }

    override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item {
        return if (state.block == BEEHIVE) super.getItemDropped(
            state,
            rand,
            fortune
        ) else Item.getItemFromBlock(Blocks.AIR)
    }

    override fun canSilkHarvest(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer): Boolean {
        return true
    }

    override fun getSilkTouchDrop(state: IBlockState): ItemStack {
        return ItemStack.EMPTY
    }

    override fun getHarvestTool(state: IBlockState): String {
        return "axe"
    }

    override fun createTileEntity(worldIn: World?, state: IBlockState?): TileEntity {
        return BeeHiveTile()
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, FACING, IS_FULL)
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(IS_FULL, meta and 4 != 0)
            .withProperty(FACING, EnumFacing.byHorizontalIndex(meta and -5))
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return (if (state.getValue(IS_FULL)) 4 else 0) or state.getValue(FACING).horizontalIndex
    }

    override fun hasComparatorInputOverride(state: IBlockState): Boolean {
        return true
    }

    override fun getComparatorInputOverride(blockState: IBlockState, worldIn: World, pos: BlockPos): Int {
        return if (worldIn.getTileEntity(pos) is BeeHiveTile) {
            (worldIn.getTileEntity(pos) as BeeHiveTile).honeyLevel
        } else 0
    }

    override fun getStateForPlacement(
        worldIn: World,
        pos: BlockPos,
        facing: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float,
        meta: Int,
        placer: EntityLivingBase
    ): IBlockState {
        return defaultState.withProperty(FACING, placer.horizontalFacing.opposite)
    }

    override fun setHardness(hardness: Float): BeeHiveBlock {
        super.setHardness(hardness)
        return this
    }

    companion object {
        val IS_FULL = PropertyBool.create("full")
        @kotlin.jvm.JvmField
        val FACING: PropertyDirection = BlockHorizontal.FACING
    }
}