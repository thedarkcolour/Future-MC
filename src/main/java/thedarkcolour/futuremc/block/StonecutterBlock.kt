package thedarkcolour.futuremc.block

import net.minecraft.block.BlockHorizontal
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import thedarkcolour.futuremc.client.gui.Gui

class StonecutterBlock(properties: Properties, gui: Gui?, functionalityOption: Boolean) : VillageStationBlock(properties, gui, functionalityOption) {
    init {
        defaultState = defaultState.withProperty(FACING, EnumFacing.NORTH)
        useNeighborBrightness = true
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, FACING)
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(FACING, EnumFacing.byHorizontalIndex(meta))
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return state.getValue(FACING).horizontalIndex
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

    override fun isBlockNormalCube(state: IBlockState) = false
    override fun isNormalCube(state: IBlockState, world: IBlockAccess, pos: BlockPos) = false
    override fun isFullBlock(state: IBlockState) = false
    override fun isOpaqueCube(state: IBlockState) = false
    override fun isNormalCube(state: IBlockState) = false
    override fun isFullCube(state: IBlockState) = false
    override fun canPlaceTorchOnTop(state: IBlockState, world: IBlockAccess, pos: BlockPos) = false
    override fun isTopSolid(state: IBlockState) = false

    override fun getRenderLayer(): BlockRenderLayer {
        return BlockRenderLayer.CUTOUT
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        return if (getMetaFromState(state) < 4) boundingBox else FULL_BLOCK_AABB
    }

    override fun hasCustomBreakingProgress(state: IBlockState): Boolean {
        return true
    }

    override fun getBlockFaceShape(
        worldIn: IBlockAccess,
        state: IBlockState,
        pos: BlockPos,
        face: EnumFacing
    ): BlockFaceShape {
        return if (face == EnumFacing.DOWN) BlockFaceShape.SOLID else BlockFaceShape.UNDEFINED
    }

    companion object {
        private val FACING = BlockHorizontal.FACING
        private val boundingBox = AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5625, 1.0)
    }
}