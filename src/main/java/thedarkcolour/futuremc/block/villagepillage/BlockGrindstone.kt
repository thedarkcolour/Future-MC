package thedarkcolour.futuremc.block.villagepillage

import net.minecraft.block.BlockHorizontal
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.IStringSerializable
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import thedarkcolour.core.block.FBlock
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.client.gui.GuiType
import thedarkcolour.futuremc.config.FConfig

class BlockGrindstone(properties: Properties) : FBlock(properties) {
    init {
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.DECORATIONS else FutureMC.GROUP
        useNeighborBrightness = true
    }

    override fun onBlockActivated(
        worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer,
        hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float
    ): Boolean {
        return if (FConfig.villageAndPillage.grindstone.functionality) {
            GuiType.GRINDSTONE.open(playerIn, worldIn, pos)
        } else false
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        return createBoundingBox(state.getValue(ATTACHMENT), state.getValue(FACING))!!
    }

    enum class EnumAttachment(private var string: String) : IStringSerializable {
        WALL("wall"), FLOOR("floor"), CEILING("ceiling");

        override fun getName(): String {
            return string
        }

        companion object {
            fun getFromFacing(facing: EnumFacing): EnumAttachment {
                if (facing == EnumFacing.DOWN) {
                    return CEILING
                }
                return if (facing == EnumFacing.UP) {
                    FLOOR
                } else WALL
            }
        }

    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, ATTACHMENT, FACING)
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return when (meta) {
            1 -> {
                getBlockState().baseState.withProperty(ATTACHMENT, EnumAttachment.FLOOR)
                    .withProperty(FACING, EnumFacing.EAST)
            }
            2 -> {
                getBlockState().baseState.withProperty(ATTACHMENT, EnumAttachment.FLOOR)
                    .withProperty(FACING, EnumFacing.SOUTH)
            }
            3 -> {
                getBlockState().baseState.withProperty(ATTACHMENT, EnumAttachment.FLOOR)
                    .withProperty(FACING, EnumFacing.WEST)
            }
            4 -> {
                getBlockState().baseState.withProperty(ATTACHMENT, EnumAttachment.WALL)
                    .withProperty(FACING, EnumFacing.NORTH)
            }
            5 -> {
                getBlockState().baseState.withProperty(ATTACHMENT, EnumAttachment.WALL)
                    .withProperty(FACING, EnumFacing.EAST)
            }
            6 -> {
                getBlockState().baseState.withProperty(ATTACHMENT, EnumAttachment.WALL)
                    .withProperty(FACING, EnumFacing.SOUTH)
            }
            7 -> {
                getBlockState().baseState.withProperty(ATTACHMENT, EnumAttachment.WALL)
                    .withProperty(FACING, EnumFacing.WEST)
            }
            8 -> {
                getBlockState().baseState.withProperty(ATTACHMENT, EnumAttachment.CEILING)
                    .withProperty(FACING, EnumFacing.NORTH)
            }
            9 -> {
                getBlockState().baseState.withProperty(ATTACHMENT, EnumAttachment.CEILING)
                    .withProperty(FACING, EnumFacing.EAST)
            }
            10 -> {
                getBlockState().baseState.withProperty(ATTACHMENT, EnumAttachment.CEILING)
                    .withProperty(FACING, EnumFacing.SOUTH)
            }
            11 -> {
                getBlockState().baseState.withProperty(ATTACHMENT, EnumAttachment.CEILING)
                    .withProperty(FACING, EnumFacing.WEST)
            }
            else -> {
                getBlockState().baseState.withProperty(ATTACHMENT, EnumAttachment.FLOOR)
                    .withProperty(FACING, EnumFacing.NORTH)
            }
        }
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return when (state.getValue(ATTACHMENT)) {
            EnumAttachment.WALL -> {
                when (state.getValue(FACING)) {
                    EnumFacing.EAST -> 5
                    EnumFacing.SOUTH -> 6
                    EnumFacing.WEST -> 7
                    else -> 4
                }
            }
            EnumAttachment.CEILING -> {
                when (state.getValue(FACING)) {
                    EnumFacing.EAST -> 9
                    EnumFacing.SOUTH -> 10
                    EnumFacing.WEST -> 11
                    else -> 8
                }
            }
            else -> {
                when (state.getValue(FACING)) {
                    EnumFacing.EAST -> 1
                    EnumFacing.SOUTH -> 2
                    EnumFacing.WEST -> 3
                    else -> 0
                }
            }
        }
    }

    override fun getStateForPlacement(
        worldIn: World, pos: BlockPos, facing: EnumFacing, hitX: Float,
        hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase
    ): IBlockState {
        val attachment = EnumAttachment.getFromFacing(facing)
        var finalFacing = placer.horizontalFacing
        if (attachment == EnumAttachment.WALL) {
            finalFacing = facing
        }
        return getBlockState().baseState.withProperty(ATTACHMENT, attachment).withProperty(FACING, finalFacing)
    }

    // todo see if this can be pulled into FBlock and as a property in the constructor
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
        private val ATTACHMENT = PropertyEnum.create("face", EnumAttachment::class.java)
        private val FACING = BlockHorizontal.FACING

        fun createBoundingBox(attachment: EnumAttachment, facing: EnumFacing): AxisAlignedBB? {
            val floorX = cube(2.0, 4.0, 4.0, 14.0, 16.0, 12.0)
            val floorZ = cube(4.0, 4.0, 2.0, 12.0, 16.0, 14.0)
            val ceilingX = cube(2.0, 0.0, 4.0, 14.0, 12.0, 12.0)
            val ceilingZ = cube(4.0, 0.0, 2.0, 12.0, 12.0, 14.0)
            val wallNorth = cube(4.0, 2.0, 0.0, 12.0, 14.0, 12.0)
            val wallWest = cube(0.0, 2.0, 4.0, 12.0, 14.0, 12.0)
            val wallSouth = cube(4.0, 2.0, 4.0, 12.0, 14.0, 16.0)
            val wallEast = cube(4.0, 2.0, 4.0, 16.0, 14.0, 12.0)

            return when (attachment) {
                EnumAttachment.FLOOR -> {
                    when (facing) {
                        EnumFacing.NORTH, EnumFacing.SOUTH -> floorZ
                        EnumFacing.EAST, EnumFacing.WEST -> floorX
                        else -> FULL_BLOCK_AABB
                    }
                }
                EnumAttachment.WALL -> {
                    when (facing) {
                        EnumFacing.EAST -> wallEast
                        EnumFacing.WEST -> wallWest
                        EnumFacing.NORTH -> wallNorth
                        EnumFacing.SOUTH -> wallSouth
                        else -> FULL_BLOCK_AABB
                    }

                }
                EnumAttachment.CEILING -> {
                    when (facing) {
                        EnumFacing.NORTH, EnumFacing.SOUTH -> ceilingZ
                        EnumFacing.EAST, EnumFacing.WEST -> ceilingX
                        else -> FULL_BLOCK_AABB
                    }
                }
            }
        }
    }
}