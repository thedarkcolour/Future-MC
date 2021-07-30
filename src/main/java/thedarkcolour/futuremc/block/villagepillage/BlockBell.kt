package thedarkcolour.futuremc.block.villagepillage

import net.minecraft.block.Block
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.command.InvalidBlockStateException
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import thedarkcolour.core.block.RotatableBlock
import thedarkcolour.futuremc.registry.FSounds
import thedarkcolour.futuremc.tile.BellTileEntity

class BlockBell(properties: Properties) : RotatableBlock(properties) {
    override fun hasTileEntity(state: IBlockState) = true

    override fun createTileEntity(world: World, state: IBlockState): TileEntity {
        return BellTileEntity()
    }

    override fun onBlockActivated(
        worldIn: World,
        pos: BlockPos,
        state: IBlockState,
        playerIn: EntityPlayer,
        hand: EnumHand,
        facing: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float
    ): Boolean {
        return ring(worldIn, state, worldIn.getTileEntity(pos), facing, hitY, pos, true)
    }

    private fun ring(
        worldIn: World,
        state: IBlockState,
        te: TileEntity?,
        facing: EnumFacing,
        hitY: Float,
        pos: BlockPos,
        checkFacing: Boolean
    ): Boolean {
        val shouldRing = !checkFacing || canRingFrom(state, facing, hitY - pos.y)
        if (!worldIn.isRemote && te is BellTileEntity && shouldRing) {
            te.ring(facing)
            playRingSound(worldIn, pos)
        }
        return true
    }

    private fun canRingFrom(state: IBlockState, facing: EnumFacing, v: Float): Boolean {
        return if (facing.axis != EnumFacing.Axis.Y && v <= 0.8124f) {
            val direction = state.getValue(FACING)
            when (state.getValue(ATTACHMENT)) {
                BellAttachment.FLOOR -> direction.axis == facing.axis
                BellAttachment.SINGLE_WALL, BellAttachment.DOUBLE_WALL -> direction.axis != facing.axis
                BellAttachment.CEILING -> true
                else -> false
            }
        } else {
            false
        }
    }

    private fun playRingSound(worldIn: World, pos: BlockPos) {
        worldIn.playSound(null, pos, FSounds.BELL_RING, SoundCategory.BLOCKS, 2.0f, 1.0f)
    }

    override fun isFullBlock(state: IBlockState): Boolean {
        return false
    }

    override fun isOpaqueCube(state: IBlockState): Boolean {
        return false
    }

    override fun isFullCube(state: IBlockState): Boolean {
        return false
    }

    override fun getBlockFaceShape(worldIn: IBlockAccess, state: IBlockState, pos: BlockPos, face: EnumFacing) = BlockFaceShape.UNDEFINED

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, ATTACHMENT, FACING)
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
        val axis = facing.axis
        if (axis == EnumFacing.Axis.Y) {
            val blockstate = this.defaultState.withProperty(
                ATTACHMENT,
                if (facing == EnumFacing.DOWN) BellAttachment.CEILING else BellAttachment.FLOOR
            ).withProperty(FACING, placer.horizontalFacing)
            if (blockstate.block.canPlaceBlockAt(worldIn, pos)) {
                return blockstate
            }
        } else {
            val west = pos.west()
            val east = pos.east()
            val north = pos.north()
            val south = pos.south()
            val down = pos.down()

            val flag =
                axis == EnumFacing.Axis.X &&
                        worldIn.getBlockState(west).getBlockFaceShape(worldIn, west, EnumFacing.EAST) == BlockFaceShape.SOLID &&
                        worldIn.getBlockState(east).getBlockFaceShape(worldIn, east, EnumFacing.WEST) == BlockFaceShape.SOLID ||
                axis == EnumFacing.Axis.Z &&
                        worldIn.getBlockState(north).getBlockFaceShape(worldIn, north, EnumFacing.SOUTH) == BlockFaceShape.SOLID &&
                        worldIn.getBlockState(south).getBlockFaceShape(worldIn, south, EnumFacing.NORTH) == BlockFaceShape.SOLID

            var blockstate1 = defaultState.withProperty(FACING, facing.opposite).withProperty(ATTACHMENT, if (flag) BellAttachment.DOUBLE_WALL else BellAttachment.SINGLE_WALL)

            if (blockstate1.block.canPlaceBlockAt(worldIn, pos)) {
                return blockstate1
            }

            val flag1 = worldIn.getBlockState(down).getBlockFaceShape(worldIn, down, EnumFacing.UP) == BlockFaceShape.SOLID
            blockstate1 = blockstate1.withProperty(ATTACHMENT, if (flag1) BellAttachment.FLOOR else BellAttachment.CEILING)

            if (blockstate1.block.canPlaceBlockAt(worldIn, pos)) {
                return blockstate1
            }
        }
        throw InvalidBlockStateException()
    }

    override fun eventReceived(state: IBlockState, worldIn: World, pos: BlockPos, id: Int, param: Int): Boolean {
        val bell = worldIn.getTileEntity(pos)
        return bell?.receiveClientEvent(id, param) ?: false
    }

    override fun getRenderType(state: IBlockState) = EnumBlockRenderType.MODEL

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(ATTACHMENT, BellAttachment.values()[meta / 4])
            .withProperty(FACING, EnumFacing.byHorizontalIndex(meta % 4))
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return (state.getValue(ATTACHMENT).ordinal shl 2) + state.getValue(FACING).horizontalIndex
    }

    override fun getBoundingBox(
        state: IBlockState,
        worldIn: IBlockAccess,
        pos: BlockPos
    ): AxisAlignedBB {
        val facing = state.getValue(FACING)
        val isXAxis = facing.axis == EnumFacing.Axis.X

        @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
        return when (state.getValue(ATTACHMENT)!!) {
            BellAttachment.FLOOR -> if (isXAxis) FLOOR_X else FLOOR_Z
            BellAttachment.DOUBLE_WALL -> if (isXAxis) DOUBLE_WALL_X else DOUBLE_WALL_Z
            BellAttachment.CEILING -> CEILING
            BellAttachment.SINGLE_WALL -> when (facing) {
                EnumFacing.NORTH -> SINGLE_WALL_N
                EnumFacing.SOUTH -> SINGLE_WALL_S
                EnumFacing.WEST -> SINGLE_WALL_W
                EnumFacing.EAST -> SINGLE_WALL_E
                else -> Block.FULL_BLOCK_AABB
            }
        }
    }

    enum class BellAttachment(private val string: String) : IStringSerializable {
        FLOOR("floor"), CEILING("ceiling"), SINGLE_WALL("single_wall"), DOUBLE_WALL("double_wall");

        override fun getName(): String {
            return string
        }
    }

    companion object {
        val ATTACHMENT: PropertyEnum<BellAttachment> = PropertyEnum.create("attachment", BellAttachment::class.java)
        val FLOOR_X = cube(4.0, 0.0, 0.0, 12.0, 16.0, 16.0)
        val FLOOR_Z = cube(0.0, 0.0, 4.0, 16.0, 16.0, 12.0)
        val DOUBLE_WALL_X = cube(0.0, 4.0, 4.0, 16.0, 16.0, 12.0)
        val DOUBLE_WALL_Z = cube(4.0, 4.0, 0.0, 12.0, 16.0, 16.0)
        val CEILING = cube(4.0, 4.0, 4.0, 12.0, 13.0, 12.0)
        val SINGLE_WALL_N = cube(4.0, 4.0, 0.0, 12.0, 15.0, 13.0)
        val SINGLE_WALL_S = cube(4.0, 4.0, 3.0, 12.0, 15.0, 16.0)
        val SINGLE_WALL_W = cube(0.0, 4.0, 4.0, 13.0, 15.0, 12.0)
        val SINGLE_WALL_E = cube(3.0, 4.0, 4.0, 16.0, 15.0, 12.0)
    }
}