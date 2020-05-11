package thedarkcolour.futuremc.block

import net.minecraft.block.Block
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
        } else {
            defaultState.withProperty(HANGING, false)
        }
    }

    /**
     * Checks if a lantern should not be place-able here.
     *
     * @param facing the offset of the block to check
     */
    private fun isBlockInvalid(world: World, blockPos: BlockPos, facing: EnumFacing): Boolean {
        val pos = blockPos.offset(facing)
        val state = world.getBlockState(pos)
        val block = state.block
        val faceShape = state.getBlockFaceShape(world, pos, facing.opposite)
        return isExceptBlockForAttachWithPiston(block)
                || arrayOf(BlockFaceShape.BOWL, BlockFaceShape.UNDEFINED).any(faceShape::equals)
    }

    /**
     * Returns true if:
     *      The block at the position is replaceable
     *      If there is a valid block below  OR
     *      If there is a valid block above
     */
    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean {
        return super.canPlaceBlockAt(worldIn, pos) && !(isBlockInvalid(worldIn, pos, EnumFacing.DOWN) && isBlockInvalid(worldIn, pos, EnumFacing.UP))
    }

    /**
     * Shortcut method to use [IBlockState] instead of [EnumFacing]
     */
    private fun isInvalidPosition(worldIn: World, pos: BlockPos, state: IBlockState): Boolean {
        return isBlockInvalid(worldIn, pos, if (state.getValue(HANGING)) EnumFacing.UP else EnumFacing.DOWN)
    }

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) {
        if (isInvalidPosition(worldIn, pos, state)) {
            dropBlockAsItem(worldIn, pos, state, 0)
            worldIn.setBlockToAir(pos)
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

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        return if (state.getValue(HANGING)) HANGING_AABB else SITTING_AABB
    }

    override fun getRenderLayer() = BlockRenderLayer.CUTOUT

    override fun isBlockNormalCube(state: IBlockState): Boolean = false
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
    }
}