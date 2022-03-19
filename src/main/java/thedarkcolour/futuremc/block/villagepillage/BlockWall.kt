package thedarkcolour.futuremc.block.villagepillage

import git.jbredwards.fluidlogged_api.api.block.IFluidloggable
import net.minecraft.block.Block
import net.minecraft.block.BlockFenceGate
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.init.Blocks
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.common.Optional
import thedarkcolour.core.block.FBlock

@Optional.Interface(iface = "git.jbredwards.fluidlogged_api.api.block.IFluidloggable", modid = "fluidlogged_api")
class BlockWall(properties: Properties) : FBlock(properties), IFluidloggable {
    init {
        defaultState = defaultState.withProperty(UP, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(
            SOUTH, false).withProperty(WEST, false)
    }

    override fun getBoundingBox(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        val actualState = getActualState(state, worldIn, pos)
        return AABB_BY_INDEX[getAABBIndex(actualState)]
    }

    override fun addCollisionBoxToList(
        state: IBlockState, worldIn: World, pos: BlockPos, entityBox: AxisAlignedBB, 
        collidingBoxes: List<AxisAlignedBB>, entityIn: Entity?, isActualState: Boolean
    ) {
        var actualState = state
        if (!isActualState) {
            actualState = getActualState(state, worldIn, pos)
        }
        addCollisionBoxToList(pos, entityBox, collidingBoxes, CLIP_AABB_BY_INDEX[getAABBIndex(actualState)])
    }

    override fun getCollisionBoundingBox(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        val actualState = getActualState(state, worldIn, pos)
        return CLIP_AABB_BY_INDEX[getAABBIndex(actualState)]
    }

    override fun isFullCube(state: IBlockState) = false

    override fun isPassable(worldIn: IBlockAccess, pos: BlockPos) = false

    override fun isOpaqueCube(state: IBlockState) = false

    private fun canConnectTo(
        worldIn: IBlockAccess,
        pos: BlockPos,
        facing: EnumFacing
    ): Boolean {
        val state = worldIn.getBlockState(pos)
        val block = state.block
        val shape = state.getBlockFaceShape(worldIn, pos, facing)
        val flag = shape == BlockFaceShape.MIDDLE_POLE_THICK || shape == BlockFaceShape.MIDDLE_POLE && block is BlockFenceGate
        return !isExceptBlockForAttachWithPiston(block) && shape == BlockFaceShape.SOLID || flag
    }

    override fun shouldSideBeRendered(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean {
        return if (side == EnumFacing.DOWN) super.shouldSideBeRendered(state, worldIn, pos, side) else true
    }
    
    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState {
        val flag = canWallConnectTo(worldIn, pos, EnumFacing.NORTH)
        val flag1 = canWallConnectTo(worldIn, pos, EnumFacing.EAST)
        val flag2 = canWallConnectTo(worldIn, pos, EnumFacing.SOUTH)
        val flag3 = canWallConnectTo(worldIn, pos, EnumFacing.WEST)
        val flag4 = flag && !flag1 && flag2 && !flag3 || !flag && flag1 && !flag2 && flag3
        return state.withProperty(UP, (!flag4 || !worldIn.isAirBlock(pos.up()))).withProperty(NORTH, (flag)).withProperty(
            EAST, (flag1)).withProperty(SOUTH, (flag2)).withProperty(WEST, (flag3))
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, UP, NORTH, EAST, WEST, SOUTH)
    }

    override fun getMetaFromState(state: IBlockState) = 0

    override fun getBlockFaceShape(
        worldIn: IBlockAccess,
        state: IBlockState,
        pos: BlockPos,
        face: EnumFacing
    ): BlockFaceShape {
        return if (face != EnumFacing.UP && face != EnumFacing.DOWN) BlockFaceShape.MIDDLE_POLE_THICK else BlockFaceShape.CENTER_BIG
    }

    override fun canBeConnectedTo(
        world: IBlockAccess,
        pos: BlockPos,
        facing: EnumFacing
    ): Boolean {
        return canConnectTo(world, pos.offset(facing), facing.opposite)
    }

    private fun canWallConnectTo(
        world: IBlockAccess,
        pos: BlockPos,
        facing: EnumFacing
    ): Boolean {
        val other = pos.offset(facing)
        val block = world.getBlockState(other).block
        return block.canBeConnectedTo(world, other, facing.opposite) || canConnectTo(
            world,
            other,
            facing.opposite
        )
    }

    enum class Variant(
        val hardness: Float,
        val resistance: Float = 10.0f,
        val soundType: SoundType = SoundType.STONE,
        val material: Material = Material.ROCK
    ) {
        BRICK(2.0f),
        GRANITE(1.5f),
        ANDESITE(1.5f),
        DIORITE(1.5f),
        SANDSTONE(0.8f, 4.0f / 3.0f),
        RED_SANDSTONE(0.8f, 4.0f / 3.0f),
        STONE_BRICK(1.5f),
        MOSSY_STONE(1.5f),
        NETHER_BRICK(2.0f),
        RED_NETHER_BRICK(2.0f),
        END_STONE(0.8f, 4.0f / 3.0f),
        PRISMARINE(1.5f),
    }

    companion object {
        private val UP = PropertyBool.create("up")
        private val NORTH = PropertyBool.create("north")
        private val EAST = PropertyBool.create("east")
        private val SOUTH = PropertyBool.create("south")
        private val WEST = PropertyBool.create("west")
        private val AABB_BY_INDEX = arrayOf(
            AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 1.0, 0.75),
            AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 1.0, 1.0),
            AxisAlignedBB(0.0, 0.0, 0.25, 0.75, 1.0, 0.75),
            AxisAlignedBB(0.0, 0.0, 0.25, 0.75, 1.0, 1.0),
            AxisAlignedBB(0.25, 0.0, 0.0, 0.75, 1.0, 0.75),
            AxisAlignedBB(0.3125, 0.0, 0.0, 0.6875, 0.875, 1.0),
            AxisAlignedBB(0.0, 0.0, 0.0, 0.75, 1.0, 0.75),
            AxisAlignedBB(0.0, 0.0, 0.0, 0.75, 1.0, 1.0),
            AxisAlignedBB(0.25, 0.0, 0.25, 1.0, 1.0, 0.75),
            AxisAlignedBB(0.25, 0.0, 0.25, 1.0, 1.0, 1.0),
            AxisAlignedBB(0.0, 0.0, 0.3125, 1.0, 0.875, 0.6875),
            AxisAlignedBB(0.0, 0.0, 0.25, 1.0, 1.0, 1.0),
            AxisAlignedBB(0.25, 0.0, 0.0, 1.0, 1.0, 0.75),
            AxisAlignedBB(0.25, 0.0, 0.0, 1.0, 1.0, 1.0),
            AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.75),
            AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
        )
        private val CLIP_AABB_BY_INDEX = arrayOf(
            AABB_BY_INDEX[0].setMaxY(1.5),
            AABB_BY_INDEX[1].setMaxY(1.5),
            AABB_BY_INDEX[2].setMaxY(1.5),
            AABB_BY_INDEX[3].setMaxY(1.5),
            AABB_BY_INDEX[4].setMaxY(1.5),
            AABB_BY_INDEX[5].setMaxY(1.5),
            AABB_BY_INDEX[6].setMaxY(1.5),
            AABB_BY_INDEX[7].setMaxY(1.5),
            AABB_BY_INDEX[8].setMaxY(1.5),
            AABB_BY_INDEX[9].setMaxY(1.5),
            AABB_BY_INDEX[10].setMaxY(1.5),
            AABB_BY_INDEX[11].setMaxY(1.5),
            AABB_BY_INDEX[12].setMaxY(1.5),
            AABB_BY_INDEX[13].setMaxY(1.5),
            AABB_BY_INDEX[14].setMaxY(1.5),
            AABB_BY_INDEX[15].setMaxY(1.5)
        )

        private fun getAABBIndex(state: IBlockState): Int {
            var i = 0
            if (state.getValue(NORTH)) {
                i = i or (1 shl EnumFacing.NORTH.horizontalIndex)
            }
            if (state.getValue(EAST)) {
                i = i or (1 shl EnumFacing.EAST.horizontalIndex)
            }
            if (state.getValue(SOUTH)) {
                i = i or (1 shl EnumFacing.SOUTH.horizontalIndex)
            }
            if (state.getValue(WEST)) {
                i = i or (1 shl EnumFacing.WEST.horizontalIndex)
            }
            return i
        }

        private fun isExceptBlockForAttachWithPiston(block: Block): Boolean {
            return Block.isExceptBlockForAttachWithPiston(block) || block == Blocks.BARRIER || block == Blocks.MELON_BLOCK || block == Blocks.PUMPKIN || block == Blocks.LIT_PUMPKIN
        }
    }
}