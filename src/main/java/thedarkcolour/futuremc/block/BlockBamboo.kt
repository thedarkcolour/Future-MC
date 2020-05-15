package thedarkcolour.futuremc.block

import net.minecraft.block.Block
import net.minecraft.block.BlockDirt
import net.minecraft.block.IGrowable
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.Blocks
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.IStringSerializable
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import thedarkcolour.core.block.FBlock
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.registry.FBlocks.BAMBOO
import java.util.*

open class BlockBamboo(properties: Properties) : FBlock(properties), IGrowable {
    init {
        defaultState = defaultState.withProperty(THICK, false).withProperty(LEAVES, EnumLeaves.NO_LEAVES).withProperty(MATURE, false)
    }

    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
        if (worldIn.getBlockState(pos.up()).block == BAMBOO) {
            worldIn.destroyBlock(pos.up(), true)
        }
    }

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) {
        if (!canPlaceBlockAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true)
        }
        if (worldIn.getBlockState(pos.up()).block == BAMBOO) {
            if (worldIn.getBlockState(pos.up()).getValue(THICK) && !worldIn.getBlockState(pos).getValue(THICK)) {
                worldIn.setBlockState(
                    pos, defaultState
                        .withProperty(THICK, true)
                        .withProperty(MATURE, worldIn.getBlockState(pos).getValue(MATURE))
                        .withProperty(LEAVES, worldIn.getBlockState(pos).getValue(LEAVES))
                )
            }
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
        return if (worldIn.getBlockState(pos.offset(facing, -1)).block == BAMBOO) {
            val thick = worldIn.getBlockState(pos.offset(facing, -1)).getValue(THICK)
            defaultState.withProperty(THICK, thick)
        } else {
            getStateFromMeta(meta)
        }
    }

    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean {
        return isBlockValidForPlacement(worldIn.getBlockState(pos.down()).block)
    }

    private fun isBlockValidForPlacement(block: Block): Boolean {
        return block == Blocks.GRASS || block is BlockDirt || block == Blocks.SAND || block == Blocks.GRAVEL || block == BAMBOO || block == Blocks.MYCELIUM
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return if (meta > 5) {
            if (meta > 8) {
                defaultState.withProperty(LEAVES, EnumLeaves.values()[meta - 9]).withProperty(MATURE, true)
                    .withProperty(THICK, true)
            } else {
                defaultState.withProperty(LEAVES, EnumLeaves.values()[meta - 6]).withProperty(MATURE, true)
                    .withProperty(THICK, false)
            }
        } else {
            if (meta > 2) {
                defaultState.withProperty(LEAVES, EnumLeaves.values()[meta - 3]).withProperty(MATURE, false)
                    .withProperty(THICK, true)
            } else {
                defaultState.withProperty(LEAVES, EnumLeaves.values()[meta]).withProperty(MATURE, false)
                    .withProperty(THICK, false)
            }
        }
    }

    override fun getMetaFromState(state: IBlockState): Int {
        var meta = 0
        if (state.getValue(MATURE)) {
            meta += 6
        }
        when (state.getValue(LEAVES)) {
            EnumLeaves.NO_LEAVES -> {
            }
            EnumLeaves.SMALL_LEAVES -> meta += 1
            EnumLeaves.LARGE_LEAVES -> meta += 2
            null -> {
            }
        }
        if (state.getValue(THICK)) {
            meta += 3
        }
        return meta
    }

    override fun isFullBlock(state: IBlockState): Boolean = false

    override fun isOpaqueCube(state: IBlockState): Boolean = false

    override fun isFullCube(state: IBlockState): Boolean = false

    override fun canEntitySpawn(state: IBlockState, entityIn: Entity): Boolean = false

    override fun isSideSolid(base_state: IBlockState, worldIn: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean =
        false

    override fun isTopSolid(state: IBlockState): Boolean = false

    override fun getBlockFaceShape(
        worldIn: IBlockAccess,
        state: IBlockState,
        pos: BlockPos,
        face: EnumFacing
    ): BlockFaceShape = BlockFaceShape.UNDEFINED

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, THICK, LEAVES, MATURE)
    }

    override fun getRenderLayer(): BlockRenderLayer = BlockRenderLayer.CUTOUT

    override fun canGrow(worldIn: World, pos: BlockPos, state: IBlockState?, isClient: Boolean): Boolean {
        val i = numOfAboveBamboo(worldIn, pos)
        val j = numOfBelowBamboo(worldIn, pos)
        return (i + j + 1 < 16 && !worldIn.getBlockState(pos.up(i)).getValue(MATURE)
                && worldIn.isAirBlock(pos.up(i + 1)))
    }

    override fun canUseBonemeal(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState): Boolean {
        return true
    }

    override fun grow(worldIn: World, random: Random, pos: BlockPos, state: IBlockState) {
        var i = numOfAboveBamboo(worldIn, pos)
        val j = numOfBelowBamboo(worldIn, pos)
        var k = i + j + 1
        val l = 1 + random.nextInt(2)
        for (i1 in 0 until l) {
            val blockpos = pos.up(i)
            val blockstate = worldIn.getBlockState(blockpos)
            if (k >= 16 || blockstate.getValue(MATURE) || !worldIn.isAirBlock(blockpos.up())) {
                return
            }
            updateStalk(worldIn, blockpos, random, k)
            ++i
            ++k
        }
    }

    private fun updateStalk(worldIn: World, pos: BlockPos, random: Random, blocks: Int) {
        val down = worldIn.getBlockState(pos.down())
        val down2 = worldIn.getBlockState(pos.down(2))
        var leaves = EnumLeaves.NO_LEAVES
        if (blocks >= 1) {
            if (down.block == BAMBOO && down.getValue(LEAVES) != EnumLeaves.NO_LEAVES) {
                if (down.block == BAMBOO && down.getValue(LEAVES) != EnumLeaves.NO_LEAVES) {
                    leaves = EnumLeaves.LARGE_LEAVES
                    if (down2.block == BAMBOO) {
                        worldIn.setBlockState(pos.down(), down.withProperty(LEAVES, EnumLeaves.SMALL_LEAVES), 3)
                        worldIn.setBlockState(pos.down(2), down2.withProperty(LEAVES, EnumLeaves.NO_LEAVES), 3)
                    }
                }
            } else {
                leaves = EnumLeaves.SMALL_LEAVES
            }
        }
        var thick = numOfBelowBamboo(worldIn, pos) > 2
        if (down.block == BAMBOO) {
            thick = numOfBelowBamboo(worldIn, pos) > 2 || down.getValue(THICK)
        }
        val mature = (blocks < 11 || random.nextFloat() >= 0.25f) && blocks != 15
        worldIn.setBlockState(
            pos.up(),
            defaultState.withProperty(THICK, thick).withProperty(LEAVES, leaves).withProperty(MATURE, !mature),
            3
        )
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, random: Random) {
        if (!canPlaceBlockAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true)
        } else if (!state.getValue(MATURE)) {
            if (random.nextInt(3) == 0 && worldIn.isAirBlock(pos.up()) && worldIn.getLightFromNeighbors(pos.up()) >= 9) {
                val i = numOfBelowBamboo(worldIn, pos) + 1
                if (i < 16) {
                    updateStalk(worldIn, pos, random, i)
                }
            }
        }
    }

    override fun getCollisionBoundingBox(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        val vec3d = state.getOffset(worldIn, pos)
        return if (state.getValue(THICK)) {
            TIGHT_THICK_AABB.offset(vec3d)
        } else {
            TIGHT_THIN_AABB.offset(vec3d)
        }
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        val vec3d = state.getOffset(source, pos)

        return if (FConfig.villageAndPillage.bamboo.tightBoundingBox) {
            getCollisionBoundingBox(state, source, pos)
        } else {
            if (state.getValue(LEAVES) == EnumLeaves.LARGE_LEAVES) {
                LEAVES_AABB.offset(vec3d)
            } else {
                NORMAL_AABB.offset(vec3d)
            }
        }
    }

    override fun getOffset(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): Vec3d {
        val i = MathHelper.getCoordinateRandom(pos.x, 0, pos.z)
        return Vec3d(((i shr 16 and 15L) / 15.0f - 0.5) * 0.5, 0.0, ((i shr 24 and 15L) / 15.0f - 0.5) * 0.5)
    }

    private fun numOfAboveBamboo(worldIn: World, pos: BlockPos): Int {
        var i = 0
        while (i < 16 && worldIn.getBlockState(pos.up(i + 1)).block == BAMBOO) {
            ++i
        }
        return i
    }

    private fun numOfBelowBamboo(worldIn: World, pos: BlockPos): Int {
        var i = 0
        while (i < 16 && worldIn.getBlockState(pos.down(i + 1)).block == BAMBOO) {
            ++i
        }
        return i
    }

    enum class EnumLeaves(var string: String) : IStringSerializable {
        NO_LEAVES("none"), SMALL_LEAVES("small"), LARGE_LEAVES("large");

        override fun getName(): String {
            return string
        }
    }

    companion object {
        val THICK: PropertyBool = PropertyBool.create("thick")
        val LEAVES: PropertyEnum<EnumLeaves> = PropertyEnum.create("leaves", EnumLeaves::class.java)
        val MATURE: PropertyBool = PropertyBool.create("mature")
        private val TIGHT_THICK_AABB = makeCube(6.5, 0.0, 6.5, 9.5, 16.0, 9.5)
        private val TIGHT_THIN_AABB = makeCube(7.0, 0.0, 7.0, 9.0, 16.0, 9.0)
        private val NORMAL_AABB = makeCube(5.0, 0.0, 5.0, 11.0, 16.0, 11.0)
        private val LEAVES_AABB = makeCube(3.0, 0.0, 3.0, 13.0, 16.0, 13.0)
    }
}