package thedarkcolour.futuremc.block

import com.google.common.collect.ImmutableMap
import net.minecraft.block.*
import net.minecraft.fluid.Fluids
import net.minecraft.fluid.IFluidState
import net.minecraft.item.BlockItemUseContext
import net.minecraft.pathfinding.PathType
import net.minecraft.state.EnumProperty
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.tags.BlockTags
import net.minecraft.util.Direction
import net.minecraft.util.IStringSerializable
import net.minecraft.util.Mirror
import net.minecraft.util.Rotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.IBooleanFunction
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.math.shapes.VoxelShapes
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorld
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.util.makeCuboidShape

class FWallBlock(properties: Properties) : Block(properties), IWaterLoggable {
    private val shapeMap = makeShapes(4, 3, 16, 0, 14, 16)
    private val collisionShapeMap = makeShapes(4, 3, 24, 0, 24, 24)

    private fun combine(voxelShape: VoxelShape, wallShape: WallHeight, voxelShape2: VoxelShape, voxelShape3: VoxelShape): VoxelShape {
        return if (wallShape == WallHeight.TALL) {
            VoxelShapes.or(voxelShape, voxelShape3)
        } else {
            if (wallShape == WallHeight.LOW) VoxelShapes.or(voxelShape, voxelShape2) else voxelShape
        }
    }

    @Suppress("SameParameterValue", "DuplicatedCode")
    private fun makeShapes(f: Int, g: Int, h: Int, i: Int, j: Int, k: Int): Map<BlockState, VoxelShape> {
        val l = (8.0f - f).toInt()
        val m = (8.0f + f).toInt()
        val n = (8.0f - g).toInt()
        val o = (8.0f + g).toInt()
        // see Util.kt
        val voxelShape1 = makeCuboidShape(minX = l, minY = 0, minZ = l, maxX = m, maxY = h, maxZ = m )
        val voxelShape2 = makeCuboidShape(minX = n, minY = i, minZ = 0, maxX = o, maxY = j, maxZ = o )
        val voxelShape3 = makeCuboidShape(minX = n, minY = i, minZ = n, maxX = o, maxY = j, maxZ = 16)
        val voxelShape4 = makeCuboidShape(minX = 0, minY = i, minZ = n, maxX = o, maxY = j, maxZ = o )
        val voxelShape5 = makeCuboidShape(minX = n, minY = i, minZ = n, maxX = 16, maxY= j, maxZ = o )
        val voxelShape6 = makeCuboidShape(minX = n, minY = i, minZ = 0, maxX = o, maxY = k, maxZ = o )
        val voxelShape7 = makeCuboidShape(minX = n, minY = i, minZ = n, maxX = o, maxY = k, maxZ = 16)
        val voxelShape8 = makeCuboidShape(minX = 0, minY = i, minZ = n, maxX = o, maxY = k, maxZ = o )
        val voxelShape9 = makeCuboidShape(minX = n, minY = i, minZ = n, maxX = 16, maxY = k,maxZ = o )
        val builder = ImmutableMap.builder<BlockState, VoxelShape>()

        for (upValue in UP.allowedValues) {
            for (wallShape in EAST.allowedValues) {
                for (wallShape2 in NORTH.allowedValues) {
                    for (wallShape3 in WEST.allowedValues) {
                        for (wallShape4 in SOUTH.allowedValues) {
                            var voxelShape10 = VoxelShapes.empty()
                            voxelShape10 = combine(voxelShape10, wallShape, voxelShape5, voxelShape9)
                            voxelShape10 = combine(voxelShape10, wallShape3, voxelShape4, voxelShape8)
                            voxelShape10 = combine(voxelShape10, wallShape2, voxelShape2, voxelShape6)
                            voxelShape10 = combine(voxelShape10, wallShape4, voxelShape3, voxelShape7)
                            if (upValue) {
                                voxelShape10 = VoxelShapes.or(voxelShape10, voxelShape1)
                            }
                            val blockState = defaultState.with(UP, upValue).with(EAST, wallShape).with(WEST, wallShape3).with(NORTH, wallShape2).with(SOUTH, wallShape4)
                            builder.put(blockState.with(WATERLOGGED, false), voxelShape10)
                            builder.put(blockState.with(WATERLOGGED, true), voxelShape10)
                        }
                    }
                }
            }
        }

        return builder.build()
    }

    override fun getShape(
        state: BlockState,
        worldIn: IBlockReader,
        pos: BlockPos,
        ctx: ISelectionContext,
    ): VoxelShape {
        return shapeMap.getOrDefault(state, VoxelShapes.fullCube())
    }

    override fun getCollisionShape(
        state: BlockState,
        worldIn: IBlockReader,
        pos: BlockPos,
        ctx: ISelectionContext,
    ): VoxelShape {
        return collisionShapeMap.getOrDefault(state, VoxelShapes.fullCube())
    }

    override fun allowsMovement(
        state: BlockState,
        worldIn: IBlockReader,
        pos: BlockPos,
        pathType: PathType,
    ): Boolean {
        return false
    }

    private fun shouldConnectTo(state: BlockState, faceFullSquare: Boolean, side: Direction): Boolean {
        val block = state.block
        val bl = block is FenceGateBlock && FenceGateBlock.isParallel(state, side)
        return state.isIn(BlockTags.WALLS) || !cannotAttach(block) && faceFullSquare || block is PaneBlock || bl
    }

    override fun getStateForPlacement(ctx: BlockItemUseContext): BlockState? {
        val worldIn = ctx.world
        val pos = ctx.pos
        val state = worldIn.getFluidState(pos)
        val north = pos.north()
        val east = pos.east()
        val south = pos.south()
        val west = pos.west()
        val up = pos.up()
        val northState = worldIn.getBlockState(north)
        val eastState = worldIn.getBlockState(east)
        val southState = worldIn.getBlockState(south)
        val westState = worldIn.getBlockState(west)
        val upState = worldIn.getBlockState(up)
        val flag0 = shouldConnectTo(northState, northState.isSideSolidFullSquare(worldIn, north, Direction.NORTH), Direction.NORTH)
        val flag1 = shouldConnectTo(eastState, eastState.isSideSolidFullSquare(worldIn, east, Direction.EAST), Direction.EAST)
        val flag2 = shouldConnectTo(southState, southState.isSideSolidFullSquare(worldIn, south, Direction.SOUTH), Direction.SOUTH)
        val flag3 = shouldConnectTo(westState, westState.isSideSolidFullSquare(worldIn, west, Direction.WEST), Direction.WEST)
        val newState = defaultState.with(WATERLOGGED, state.fluid == Fluids.WATER)

        return pickShapes(worldIn, newState, up, upState, flag0, flag1, flag2, flag3)
    }

    override fun updatePostPlacement(
        state: BlockState,
        facing: Direction,
        facingState: BlockState,
        worldIn: IWorld,
        pos: BlockPos,
        facingPos: BlockPos
    ): BlockState {
        if (state.get(WATERLOGGED)) {
            worldIn.pendingFluidTicks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn))
        }

        return if (facing == Direction.DOWN) {
            state
        } else {
            if (facing == Direction.UP) {
                checkUpConnection(worldIn, state, pos, facingState)
            } else {
                checkSideConnections(worldIn, pos, state, facingPos, facingState, facing)
            }
        }
    }

    private fun isConnection(state: BlockState, property: EnumProperty<WallHeight>): Boolean =
        state.get(property) != WallHeight.NONE

    private fun compare(shape1: VoxelShape, shape2: VoxelShape): Boolean =
        !VoxelShapes.compare(shape2, shape1, IBooleanFunction.ONLY_FIRST)

    private fun checkUpConnection(worldIn: IWorld, state: BlockState, pos: BlockPos, facingState: BlockState): BlockState {
        val flag0 = isConnection(state, NORTH)
        val flag1 = isConnection(state, EAST)
        val flag2 = isConnection(state, SOUTH)
        val flag3 = isConnection(state, WEST)
        return pickShapes(worldIn, state, pos, facingState, flag0, flag1, flag2, flag3)
    }

    private fun checkSideConnections(
        worldIn: IWorld,
        pos: BlockPos,
        state: BlockState,
        facingPos: BlockPos,
        facingState: BlockState,
        facing: Direction,
    ): BlockState {
        val f = facing.opposite
        val flag0 = if (facing == Direction.NORTH) shouldConnectTo(facingState, facingState.isSideSolidFullSquare(worldIn, facingPos, f), f) else isConnection(state, NORTH)
        val flag1 = if (facing == Direction.EAST) shouldConnectTo(facingState, facingState.isSideSolidFullSquare(worldIn, facingPos, f), f) else isConnection(state, EAST)
        val flag2 = if (facing == Direction.SOUTH) shouldConnectTo(facingState, facingState.isSideSolidFullSquare(worldIn, facingPos, f), f) else isConnection(state, SOUTH)
        val flag3 = if (facing == Direction.WEST) shouldConnectTo(facingState, facingState.isSideSolidFullSquare(worldIn, facingPos, f), f) else isConnection(state, WEST)
        val up = pos.up()
        val upState = worldIn.getBlockState(up)
        return pickShapes(worldIn, state, up, upState, flag0, flag1, flag2, flag3)
    }

    private fun pickShapes(
        worldIn: IWorld,
        newState: BlockState,
        up: BlockPos,
        upState: BlockState,
        north: Boolean,
        east: Boolean,
        south: Boolean,
        west: Boolean,
    ): BlockState {
        val voxelShape = upState.getCollisionShape(worldIn, up).project(Direction.DOWN)
        val blockState3 = withConnections(newState, north, east, south, west, voxelShape)
        return blockState3.with(UP, shouldConnectUp(blockState3, upState, voxelShape))
    }

    private fun withConnections(
        newState: BlockState,
        north: Boolean,
        east: Boolean,
        south: Boolean,
        west: Boolean,
        voxelShape: VoxelShape
    ): BlockState {
        return newState
            .with(NORTH, pickConnectionShape(north, voxelShape, SIDE_NORTH))
            .with(EAST,  pickConnectionShape(east, voxelShape, SIDE_EAST))
            .with(SOUTH, pickConnectionShape(south, voxelShape, SIDE_SOUTH))
            .with(WEST,  pickConnectionShape(west, voxelShape, SIDE_WEST))
    }

    private fun pickConnectionShape(
        hasSolidNeighbor: Boolean,
        connectionShape: VoxelShape,
        connectionShape2: VoxelShape
    ): WallHeight {
        return if (hasSolidNeighbor) {
            if (compare(connectionShape, connectionShape2)) {
                WallHeight.TALL
            } else WallHeight.LOW
        } else WallHeight.NONE
    }

    private fun shouldConnectUp(blockState: BlockState, blockState2: BlockState, voxelShape: VoxelShape): Boolean {
        return if (blockState2.block is FWallBlock && blockState2[UP]) {
            true
        } else {
            val northShape = blockState.get(NORTH)
            val southShape = blockState.get(SOUTH)
            val eastShape = blockState.get(EAST)
            val westShape = blockState.get(WEST)
            val bl2 = southShape == WallHeight.NONE
            val bl3 = westShape == WallHeight.NONE
            val bl4 = eastShape == WallHeight.NONE
            val bl5 = northShape == WallHeight.NONE
            val bl6 = bl5 && bl2 && bl3 && bl4 || bl5 != bl2 || bl3 != bl4

            if (bl6) {
                true
            } else {
                val isFullWall = northShape == WallHeight.TALL && southShape == WallHeight.TALL ||
                            eastShape == WallHeight.TALL && westShape == WallHeight.TALL

                if (isFullWall) {
                    false
                } else {
                    blockState2.block.isIn(FBlocks.WALL_POST_OVERRIDE) || compare(voxelShape, SIDE_UP)
                }
            }
        }
    }

    override fun getFluidState(state: BlockState): IFluidState {
        return if (state.get(WATERLOGGED)) {
            Fluids.WATER.getStillFluidState(false)
        } else {
            Fluids.EMPTY.defaultState
        }
    }

    override fun propagatesSkylightDown(
        state: BlockState,
        worldIn: IBlockReader,
        pos: BlockPos
    ): Boolean {
        return !state.get(WATERLOGGED)
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>) {
        builder.add(UP, NORTH, EAST, WEST, SOUTH, WATERLOGGED)
    }

    override fun rotate(state: BlockState, world: IWorld?, pos: BlockPos?, rotation: Rotation): BlockState {
        return when (rotation) {
            Rotation.CLOCKWISE_180 -> state.with(NORTH, state[SOUTH]).with(EAST, state[WEST]).with(SOUTH, state[NORTH])
                .with(WEST, state[EAST])
            Rotation.COUNTERCLOCKWISE_90 -> state.with(NORTH, state[EAST]).with(EAST, state[SOUTH])
                .with(SOUTH, state[WEST]).with(WEST, state[NORTH])
            Rotation.CLOCKWISE_90 -> state.with(NORTH, state[WEST]).with(EAST, state[NORTH]).with(SOUTH, state[EAST])
                .with(WEST, state[SOUTH])
            else -> state
        }
    }

    override fun mirror(state: BlockState, mirror: Mirror): BlockState {
        return when (mirror) {
            Mirror.LEFT_RIGHT -> state
                .with(NORTH, state.get(SOUTH))
                .with(SOUTH, state.get(NORTH))
            Mirror.FRONT_BACK -> state
                .with(EAST, state.get(WEST))
                .with(WEST, state.get(EAST))
            else -> state
        }
    }

    companion object {
        private val UP = BlockStateProperties.UP
        private val EAST = EnumProperty.create("east", WallHeight::class.java)
        private val NORTH = EnumProperty.create("north", WallHeight::class.java)
        private val SOUTH = EnumProperty.create("south", WallHeight::class.java)
        private val WEST = EnumProperty.create("west", WallHeight::class.java)
        private val WATERLOGGED = BlockStateProperties.WATERLOGGED
        private val SIDE_UP    = makeCuboidShape(7, 0, 7, 9, 16, 9)
        private val SIDE_NORTH = makeCuboidShape(7, 0, 0, 9, 16, 9)
        private val SIDE_SOUTH = makeCuboidShape(7, 0, 7, 9, 16, 16)
        private val SIDE_WEST  = makeCuboidShape(0, 0, 7, 9, 16, 9)
        private val SIDE_EAST  = makeCuboidShape(7, 0, 7, 16, 16, 9)
    }

    enum class WallHeight(private val _name: String) : IStringSerializable {
        NONE("none"),
        LOW("low"),
        TALL("tall");

        override fun getName(): String {
            return _name
        }
    }
}