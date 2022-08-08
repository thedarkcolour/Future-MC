package thedarkcolour.futuremc.block.villagepillage

import net.minecraft.block.Block
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityFallingBlock
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockPos.MutableBlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import thedarkcolour.core.block.FBlock
import thedarkcolour.futuremc.registry.FBlocks
import java.util.*

class ScaffoldingBlock(properties: Properties) : FBlock(properties) {
    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, DISTANCE, BOTTOM)
    }

    // todo getShape
    override fun getBoundingBox(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        return super.getBoundingBox(state, worldIn, pos)
    }

    override fun addCollisionBoxToList(
        state: IBlockState,
        worldIn: World,
        pos: BlockPos,
        entityBox: AxisAlignedBB,
        collidingBoxes: List<AxisAlignedBB>,
        entityIn: Entity?,
        isActualState: Boolean
    ) {
        if (entityIn != null) {
            if (entityIn.posY > (pos.y + 1.0 - 1.0E-5) && !entityIn.isSneaking) {
                for (box in noBottomCollisionBoxes) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, box)
                }
                // todo fix jump ups in scaffolding above the ground
            } else if (entityIn.posY > (pos.y - 1.0E-5) && state.getValue(DISTANCE) != 0 && state.getValue(BOTTOM)) {
                if (entityIn.motionY <= 0) {
                    for (box in bottomCollisionBoxes) {
                        addCollisionBoxToList(pos, entityBox, collidingBoxes, box)
                    }
                } else {
                    //for (box in noBottomCollisionBoxes) {
                    //    addCollisionBoxToList(pos, entityBox, collidingBoxes, box)
                    //}
                }
            }
        }
    }

    override fun getRenderLayer(): BlockRenderLayer {
        return BlockRenderLayer.CUTOUT
    }

    override fun getStateForPlacement(
        worldIn: World, pos: BlockPos, facing: EnumFacing, hitX: Float,
        hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase
    ): IBlockState {
        val i = getHorizontalDistance(worldIn, pos)
        return this.defaultState.withProperty(DISTANCE, i).withProperty(BOTTOM, hasBottom(worldIn, pos, i))
    }

    private fun hasBottom(worldIn: World, pos: BlockPos, i: Int): Boolean {
        return i > 0 && worldIn.getBlockState(pos.down()).block != this
    }

    override fun onBlockAdded(worldIn: World, pos: BlockPos, state: IBlockState) {
        if (!worldIn.isRemote) {
            worldIn.scheduleUpdate(pos, this, 1)
        }
    }

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) {
        if (!worldIn.isRemote) {
            worldIn.scheduleUpdate(pos, this, 1)
        }
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        val i = getHorizontalDistance(worldIn, pos)
        val blockstate = state.withProperty(DISTANCE, i).withProperty(BOTTOM, hasBottom(worldIn, pos, i))
        if (blockstate.getValue(DISTANCE) == 7) {
            if (state.getValue(DISTANCE) == 7) {
                worldIn.spawnEntity(
                    EntityFallingBlock(
                        worldIn,
                        pos.x.toDouble() + 0.5,
                        pos.y.toDouble(),
                        pos.z.toDouble() + 0.5,
                        state
                    )
                )
            } else {
                worldIn.destroyBlock(pos, true)
            }
        } else if (state != blockstate) {
            worldIn.setBlockState(pos, blockstate, 3)
        }
    }

    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean {
        return getHorizontalDistance(worldIn, pos) < 7
    }

    override fun isLadder(state: IBlockState, world: IBlockAccess, pos: BlockPos, entity: EntityLivingBase): Boolean {
        return true
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        val bottom = meta > 7
        val distance = if (bottom) meta - 8 else meta
        return defaultState.withProperty(BOTTOM, bottom).withProperty(DISTANCE, distance)
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return if (state.getValue(BOTTOM)) state.getValue(DISTANCE) + 8 else state.getValue(DISTANCE)
    }

    override fun isFullBlock(state: IBlockState): Boolean {
        return false
    }

    override fun isFullCube(state: IBlockState): Boolean {
        return false
    }

    override fun isOpaqueCube(state: IBlockState): Boolean {
        return false
    }

    companion object {
        private val DISTANCE: PropertyInteger = PropertyInteger.create("distance", 0, 7)
        private val BOTTOM: PropertyBool = PropertyBool.create("bottom")

        private var bottomCollisionBoxes = arrayOf(
            cube(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
            cube(0.0, 0.0, 0.0, 2.0, 2.0, 16.0),
            cube(14.0, 0.0, 0.0, 16.0, 2.0, 16.0),
            cube(0.0, 0.0, 14.0, 16.0, 2.0, 16.0),
            cube(0.0, 0.0, 0.0, 16.0, 2.0, 2.0)
        )
        private var noBottomCollisionBoxes = arrayOf(
            cube(0.0, 14.0, 0.0, 16.0, 16.0, 16.0),
            cube(0.0, 0.0, 0.0, 2.0, 16.0, 2.0),
            cube(14.0, 0.0, 0.0, 16.0, 16.0, 2.0),
            cube(0.0, 0.0, 14.0, 2.0, 16.0, 16.0),
            cube(14.0, 0.0, 14.0, 16.0, 16.0, 16.0)
        )

        // Number of blocks away from pos
        fun getHorizontalDistance(worldIn: World, pos: BlockPos): Int {
            val blockPos = MutableBlockPos(pos).move(EnumFacing.DOWN)
            val blockstate = worldIn.getBlockState(blockPos)
            var i = 7
            if (blockstate.block == FBlocks.SCAFFOLDING) {
                i = blockstate.getValue(DISTANCE)
            } else if (blockstate.isSideSolid(worldIn, blockPos, EnumFacing.UP)) {
                return 0
            }
            for (direction in EnumFacing.Plane.HORIZONTAL) {
                val blockstate1 = worldIn.getBlockState(blockPos.setPos(pos).move(direction))
                if (blockstate1.block == FBlocks.SCAFFOLDING) {
                    i = i.coerceAtMost(blockstate1.getValue(DISTANCE) + 1)
                    if (i == 1) {
                        break
                    }
                }
            }
            return i
        }
    }
}