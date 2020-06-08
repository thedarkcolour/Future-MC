package thedarkcolour.futuremc.block

import net.minecraft.block.Block
import net.minecraft.block.BlockTrapDoor
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import thedarkcolour.core.block.FBlock
import java.util.*

class LanternBlock(properties: Properties) : FBlock(properties) {
    init {
        // todo check to see if i should add to FBlock
        setHarvestLevel("pickaxe", 0)
    }

    override fun getStateForPlacement(
        worldIn: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float,
        hitZ: Float, meta: Int, placer: EntityLivingBase, hand: EnumHand
    ): IBlockState {
        return if (facing == EnumFacing.DOWN && !isBlockInvalid(worldIn, pos, EnumFacing.UP)) {
            defaultState.withProperty(HANGING, true)
        } else if (isBlockInvalid(worldIn, pos, EnumFacing.DOWN)) {
            defaultState.withProperty(HANGING, true)
        } else {
            defaultState.withProperty(HANGING, false)
        }
    }

    /**
     * Returns true if:
     *      The block at the position is replaceable
     *      If there is a valid block below  OR
     *      If there is a valid block above
     */
    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean {
        return super.canPlaceBlockAt(worldIn, pos) && isValidPosition(worldIn, pos)
    }

    private fun isValidPosition(worldIn: World, pos: BlockPos): Boolean {
        return !(isBlockInvalid(worldIn, pos, EnumFacing.DOWN) && isBlockInvalid(worldIn, pos, EnumFacing.UP))
    }

    /**
     * Checks if a lantern should not be place-able here.
     *
     * @param facing the offset of the block to check
     */
    private fun isBlockInvalid(world: World, pos: BlockPos, facing: EnumFacing): Boolean {
        val pos1 = pos.offset(facing)
        val state = world.getBlockState(pos1)
        val faceShape = state.getBlockFaceShape(world, pos1, facing.opposite)
        return faceShape in INVALID_FACE_SHAPES || (state.block is BlockTrapDoor && isTrapdoorValid(state, facing))
    }

    private fun isTrapdoorValid(state: IBlockState, facing: EnumFacing): Boolean {
        return (facing == EnumFacing.UP && state.getValue(BlockTrapDoor.HALF) == BlockTrapDoor.DoorHalf.TOP || facing == EnumFacing.DOWN && state.getValue(BlockTrapDoor.HALF) == BlockTrapDoor.DoorHalf.BOTTOM) && !state.getValue(BlockTrapDoor.OPEN)
    }

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) {
        if (!worldIn.isRemote) {
            if (!isValidPosition(worldIn, pos)) {
                dropBlockAsItem(worldIn, pos, state, 0)
                worldIn.setBlockToAir(pos)
            }
        }
    }

    override fun observedNeighborChange(
        observerState: IBlockState,
        worldIn: World,
        observerPos: BlockPos,
        changedBlock: Block,
        changedBlockPos: BlockPos
    ) {
        if (changedBlock is BlockTrapDoor) {
            val state = worldIn.getBlockState(changedBlockPos)
            val facing = if (observerState.getValue(HANGING)) EnumFacing.UP else EnumFacing.DOWN
            if (!isTrapdoorValid(state, facing)) {
                dropBlockAsItem(worldIn, observerPos, observerState, 0)
                worldIn.setBlockToAir(observerPos)
            }
        }
    }

    override fun createBlockState() = BlockStateContainer(this, HANGING)

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(HANGING, meta != 1)
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return if (state.getValue(HANGING)) 1 else 0
    }

    override fun hasComparatorInputOverride(state: IBlockState) = true

    override fun getComparatorInputOverride(blockState: IBlockState, worldIn: World, pos: BlockPos): Int {
        return if (blockState.getValue(HANGING)) 15 else 0
    }

    override fun getBoundingBox(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        return if (state.getValue(HANGING)) HANGING_AABB else SITTING_AABB
    }

    override fun getRenderLayer() = BlockRenderLayer.CUTOUT

    override fun isBlockNormalCube(state: IBlockState) = false
    override fun isNormalCube(state: IBlockState, world: IBlockAccess, pos: BlockPos) = false
    override fun isFullBlock(state: IBlockState) = false
    override fun isOpaqueCube(state: IBlockState) = false
    override fun isNormalCube(state: IBlockState) = false
    override fun isFullCube(state: IBlockState) = false
    override fun canPlaceTorchOnTop(state: IBlockState, world: IBlockAccess, pos: BlockPos) = false
    override fun isTopSolid(state: IBlockState) = false
    override fun getBlockFaceShape(worldIn: IBlockAccess, state: IBlockState, pos: BlockPos, face: EnumFacing) = BlockFaceShape.UNDEFINED

    companion object {
        private val HANGING = PropertyBool.create("hanging")
        private val SITTING_AABB = makeCube(5.0, 0.0, 5.0, 11.0, 9.0, 11.0)
        private val HANGING_AABB = makeCube(5.0, 1.0, 5.0, 11.0, 10.0, 11.0)
        private val INVALID_FACE_SHAPES = EnumSet.of(BlockFaceShape.BOWL, BlockFaceShape.UNDEFINED)
    }
}