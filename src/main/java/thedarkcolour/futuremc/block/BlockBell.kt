package thedarkcolour.futuremc.block

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.command.InvalidBlockStateException
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.block.RotatableBlock
import thedarkcolour.futuremc.registry.FSounds
import thedarkcolour.futuremc.tile.TileBell

class BlockBell : RotatableBlock("bell", Material.IRON, SoundType.ANVIL) {
    init {
        setHardness(5.0f)
    }

    override fun hasTileEntity(state: IBlockState) = true

    override fun createTileEntity(world: World, state: IBlockState): TileEntity {
        return TileBell()
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
        return ring(worldIn, state, worldIn.getTileEntity(pos), facing, hitY, playerIn, pos, true)
    }

    private fun ring(
        worldIn: World,
        state: IBlockState,
        te: TileEntity?,
        facing: EnumFacing,
        hitY: Float,
        playerIn: EntityPlayer?,
        pos: BlockPos,
        checkFacing: Boolean
    ): Boolean {
        val shouldRing = !checkFacing || canRingFrom(state, facing, hitY - pos.y)
        if (!worldIn.isRemote && te is TileBell && shouldRing) {
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

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, ATTACHMENT, FACING)
    }

    enum class BellAttachment(private val string: String) : IStringSerializable {
        FLOOR("floor"), CEILING("ceiling"), SINGLE_WALL("single_wall"), DOUBLE_WALL("double_wall");

        override fun getName(): String {
            return string
        }

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
            val flag = axis == EnumFacing.Axis.X && isSideSolid(
                worldIn.getBlockState(pos.west()),
                worldIn,
                pos.west(),
                EnumFacing.EAST
            ) && isSideSolid(
                worldIn.getBlockState(pos.east()),
                worldIn,
                pos.east(),
                EnumFacing.WEST
            ) || axis == EnumFacing.Axis.Z && isSideSolid(
                worldIn.getBlockState(pos.north()),
                worldIn,
                pos.north(),
                EnumFacing.SOUTH
            ) && isSideSolid(
                worldIn
                    .getBlockState(pos.south()), worldIn, pos.south(), EnumFacing.NORTH
            )
            var blockstate1 = this.defaultState.withProperty(FACING, facing.opposite)
                .withProperty(ATTACHMENT, if (flag) BellAttachment.DOUBLE_WALL else BellAttachment.SINGLE_WALL)
            if (blockstate1.block.canPlaceBlockAt(worldIn, pos)) {
                return blockstate1
            }
            val flag1 = isSideSolid(worldIn.getBlockState(pos.down()), worldIn, pos.down(), EnumFacing.UP)
            blockstate1 =
                blockstate1.withProperty(ATTACHMENT, if (flag1) BellAttachment.FLOOR else BellAttachment.CEILING)
            if (blockstate1.block.canPlaceBlockAt(worldIn, pos)) {
                return blockstate1
            }
        }
        throw InvalidBlockStateException()
    }

    override fun getRenderType(state: IBlockState): EnumBlockRenderType {
        return EnumBlockRenderType.MODEL
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(ATTACHMENT, BellAttachment.values()[meta / 4])
            .withProperty(FACING, EnumFacing.byHorizontalIndex(meta % 4))
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return state.getValue(ATTACHMENT).ordinal * 4 + state.getValue(ATTACHMENT).ordinal
    }

    companion object {
        val ATTACHMENT: PropertyEnum<BellAttachment> = PropertyEnum.create("attachment", BellAttachment::class.java)
    }
}