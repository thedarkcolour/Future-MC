package thedarkcolour.futuremc.block

import net.minecraft.block.Block
import net.minecraft.block.BlockFenceGate
import net.minecraft.block.SoundType
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.init.Blocks
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import thedarkcolour.core.block.BlockBase
import thedarkcolour.futuremc.FutureMC.TAB
import thedarkcolour.futuremc.config.FConfig

class BlockWall(variant: String) : BlockBase(variant + "_wall") {
    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        var state = state
        state = getActualState(state, source, pos)
        return AABB_BY_INDEX[getAABBIndex(state)]
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, UP, NORTH, EAST, WEST, SOUTH)
    }

    override fun isOpaqueCube(state: IBlockState): Boolean {
        return false
    }

    override fun isFullCube(state: IBlockState): Boolean {
        return false
    }

    @SideOnly(Side.CLIENT)
    override fun shouldSideBeRendered(blockState: IBlockState, blockAccess: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean {
        return side != EnumFacing.DOWN || super.shouldSideBeRendered(blockState, blockAccess, pos, side)
    }

    override fun addCollisionBoxToList(state: IBlockState, worldIn: World, pos: BlockPos, entityBox: AxisAlignedBB, collidingBoxes: List<AxisAlignedBB>, entityIn: Entity?, isActualState: Boolean) {
        var state = state
        if (!isActualState) {
            state = getActualState(state, worldIn, pos)
        }
        addCollisionBoxToList(pos, entityBox, collidingBoxes, CLIP_AABB_BY_INDEX[getAABBIndex(state)])
    }

    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState {
        val flag = canWallConnectTo(worldIn, pos, EnumFacing.NORTH)
        val flag1 = canWallConnectTo(worldIn, pos, EnumFacing.EAST)
        val flag2 = canWallConnectTo(worldIn, pos, EnumFacing.SOUTH)
        val flag3 = canWallConnectTo(worldIn, pos, EnumFacing.WEST)
        val flag4 = flag && !flag1 && flag2 && !flag3 || !flag && flag1 && !flag2 && flag3
        return state.withProperty(UP, !flag4 || !worldIn.isAirBlock(pos.up())).withProperty(NORTH, flag).withProperty(EAST, flag1).withProperty(SOUTH, flag2).withProperty(WEST, flag3)
    }

    override fun canBeConnectedTo(world: IBlockAccess, pos: BlockPos, facing: EnumFacing): Boolean {
        return canConnectTo(world, pos.offset(facing), facing.opposite)
    }

    private fun canWallConnectTo(world: IBlockAccess, pos: BlockPos, facing: EnumFacing): Boolean {
        val other = pos.offset(facing)
        val block = world.getBlockState(other).block
        return block.canBeConnectedTo(world, other, facing.opposite) || canConnectTo(world, other, facing.opposite)
    }

    private fun canConnectTo(worldIn: IBlockAccess, pos: BlockPos, p_176253_3_: EnumFacing): Boolean {
        val iblockstate = worldIn.getBlockState(pos)
        val block = iblockstate.block
        val blockfaceshape = iblockstate.getBlockFaceShape(worldIn, pos, p_176253_3_)
        val flag = blockfaceshape == BlockFaceShape.MIDDLE_POLE_THICK || blockfaceshape == BlockFaceShape.MIDDLE_POLE && block is BlockFenceGate
        return !isExceptBlockForAttachWithPiston(block) && blockfaceshape == BlockFaceShape.SOLID || flag
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return this.defaultState
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return 0
    }

    companion object {
        private val UP = PropertyBool.create("up")
        private val NORTH = PropertyBool.create("north")
        private val EAST = PropertyBool.create("east")
        private val SOUTH = PropertyBool.create("south")
        private val WEST = PropertyBool.create("west")
        private val AABB_BY_INDEX = arrayOf(AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 1.0, 0.75), AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 1.0, 1.0), AxisAlignedBB(0.0, 0.0, 0.25, 0.75, 1.0, 0.75), AxisAlignedBB(0.0, 0.0, 0.25, 0.75, 1.0, 1.0), AxisAlignedBB(0.25, 0.0, 0.0, 0.75, 1.0, 0.75), AxisAlignedBB(0.3125, 0.0, 0.0, 0.6875, 0.875, 1.0), AxisAlignedBB(0.0, 0.0, 0.0, 0.75, 1.0, 0.75), AxisAlignedBB(0.0, 0.0, 0.0, 0.75, 1.0, 1.0), AxisAlignedBB(0.25, 0.0, 0.25, 1.0, 1.0, 0.75), AxisAlignedBB(0.25, 0.0, 0.25, 1.0, 1.0, 1.0), AxisAlignedBB(0.0, 0.0, 0.3125, 1.0, 0.875, 0.6875), AxisAlignedBB(0.0, 0.0, 0.25, 1.0, 1.0, 1.0), AxisAlignedBB(0.25, 0.0, 0.0, 1.0, 1.0, 0.75), AxisAlignedBB(0.25, 0.0, 0.0, 1.0, 1.0, 1.0), AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.75), AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0))
        private val CLIP_AABB_BY_INDEX = arrayOf(AABB_BY_INDEX[0].setMaxY(1.5), AABB_BY_INDEX[1].setMaxY(1.5), AABB_BY_INDEX[2].setMaxY(1.5), AABB_BY_INDEX[3].setMaxY(1.5), AABB_BY_INDEX[4].setMaxY(1.5), AABB_BY_INDEX[5].setMaxY(1.5), AABB_BY_INDEX[6].setMaxY(1.5), AABB_BY_INDEX[7].setMaxY(1.5), AABB_BY_INDEX[8].setMaxY(1.5), AABB_BY_INDEX[9].setMaxY(1.5), AABB_BY_INDEX[10].setMaxY(1.5), AABB_BY_INDEX[11].setMaxY(1.5), AABB_BY_INDEX[12].setMaxY(1.5), AABB_BY_INDEX[13].setMaxY(1.5), AABB_BY_INDEX[14].setMaxY(1.5), AABB_BY_INDEX[15].setMaxY(1.5))

        private fun getAABBIndex(state: IBlockState): Int {
            var i = 0
            if (state.getValue(NORTH)) {
                i = i or 1 shl EnumFacing.NORTH.horizontalIndex
            }
            if (state.getValue(EAST)) {
                i = i or 1 shl EnumFacing.EAST.horizontalIndex
            }
            if (state.getValue(SOUTH)) {
                i = i or 1 shl EnumFacing.SOUTH.horizontalIndex
            }
            if (state.getValue(WEST)) {
                i = i or 1 shl EnumFacing.WEST.horizontalIndex
            }
            return i
        }

        protected fun isExceptBlockForAttachWithPiston(block: Block): Boolean {
            return Block.isExceptBlockForAttachWithPiston(block) || block == Blocks.BARRIER || block == Blocks.MELON_BLOCK || block == Blocks.PUMPKIN || block == Blocks.LIT_PUMPKIN
        }
    }

    init {
        soundType = SoundType.STONE
        setHardness(3.0f)
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.BUILDING_BLOCKS else TAB
    }
}