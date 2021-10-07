package thedarkcolour.futuremc.block.villagepillage

import git.jbredwards.fluidlogged_api.common.block.IFluidloggable
import net.minecraft.block.Block
import net.minecraft.block.BlockHopper
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
import net.minecraftforge.fml.common.Optional
import thedarkcolour.core.block.FBlock
import java.util.*

@Optional.Interface(iface = "git.jbredwards.fluidlogged_api.common.block.IFluidloggable", modid = "fluidlogged_api")
class LanternBlock(properties: Properties) : FBlock(properties), IFluidloggable {
    init {
        // todo check to see if i should add to FBlock
        setHarvestLevel("pickaxe", 0)
    }

    override fun getStateForPlacement(
        worldIn: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float,
        hitZ: Float, meta: Int, placer: EntityLivingBase, hand: EnumHand
    ): IBlockState {
        return if (facing == EnumFacing.DOWN && isValidPos(worldIn, pos, EnumFacing.UP)) {
            defaultState.withProperty(HANGING, true)
        } else if (!isValidPos(worldIn, pos, EnumFacing.DOWN)) {
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
        return isValidPos(worldIn, pos, EnumFacing.DOWN) || isValidPos(worldIn, pos, EnumFacing.UP)
    }

    private fun isValidPos(worldIn: World, pos: BlockPos, facing: EnumFacing): Boolean {
        // position of the clicked block
        val pos1 = pos.offset(facing)
        // clicked block
        val state = worldIn.getBlockState(pos1)

        val bounds = arrayListOf<AxisAlignedBB>()
        state.addCollisionBoxToList(worldIn, BlockPos.ORIGIN, AxisAlignedBB(BlockPos.ORIGIN), bounds, null, false)

        // whether this was meant to be a hanging lantern
        val up = facing == EnumFacing.UP
        val offsetState = worldIn.getBlockState(pos.offset(if (up) facing else EnumFacing.DOWN))

        if (offsetState.block is LanternBlock) {
            return up && !offsetState.getValue(HANGING)
        }

        // whether to use the top one or the bottom one
        val validBound = if (up) validBoundsUp else validBoundsUp
        val x = validBound.minX
        val y = validBound.minY
        val z = validBound.minZ

        for (box in bounds) {
            val a = (x  in box.minX..box.maxX)
            val b = (y  in box.minY..box.maxY)
            val c = (z  in box.minZ..box.maxZ)

            if (
                a && b && c
            ) {
                return true
            }
        }

        // hoppers are dumb
        if (facing == EnumFacing.UP && state.block is BlockHopper) {
            if (state.getValue(BlockHopper.FACING) == EnumFacing.DOWN) {
                return true
            }
        }

        // trapdoors are also dumb
        if (state.block is BlockTrapDoor && isTrapdoorValid(state, facing.opposite)) {
            return true
        }

        return false
    }

    private fun isTrapdoorValid(state: IBlockState, facing: EnumFacing): Boolean {
        return state.properties.containsKey(BlockTrapDoor.HALF) && (facing == EnumFacing.UP && state.getValue(BlockTrapDoor.HALF) == BlockTrapDoor.DoorHalf.TOP || facing == EnumFacing.DOWN && state.getValue(BlockTrapDoor.HALF) == BlockTrapDoor.DoorHalf.BOTTOM) && !state.getValue(BlockTrapDoor.OPEN)
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
            val facing = if (observerState.getValue(HANGING)) EnumFacing.UP else EnumFacing.DOWN

            if (!isValidPos(worldIn, observerPos, facing)) {
                dropBlockAsItem(worldIn, observerPos, observerState, 0)
                worldIn.setBlockToAir(observerPos)
            }
        }
    }

    override fun createBlockState() = BlockStateContainer(this, HANGING)

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(HANGING, meta == 1)
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
        private val SITTING_AABB = cube(5.0, 0.0, 5.0, 11.0, 9.0, 11.0)
        private val HANGING_AABB = cube(5.0, 1.0, 5.0, 11.0, 10.0, 11.0)
        private val INVALID_FACE_SHAPES = EnumSet.of(BlockFaceShape.BOWL, BlockFaceShape.UNDEFINED)
        private val validBoundsDown = AxisAlignedBB(0.4375, 0.0, 0.4375, 0.5625, 0.0, 0.5625)
        private val validBoundsUp = AxisAlignedBB(0.4375, 0.0, 0.4375, 0.5625, 0.0, 0.5625)
    }
}