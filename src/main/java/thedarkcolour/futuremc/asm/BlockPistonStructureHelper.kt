package thedarkcolour.futuremc.asm

import net.minecraft.block.BlockPistonBase.canPush
import net.minecraft.block.material.EnumPushReaction
import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.futuremc.api.IStickyBlock

class BlockPistonStructureHelper(private val world: World, private val pistonPos: BlockPos, private val facing: EnumFacing, private val extending: Boolean) {
    private val toDestroy = ArrayList<BlockPos>()
    private val toMove = ArrayList<BlockPos>()
    private val moveDirection: EnumFacing
    private val blockToMove: BlockPos

    init {
        if (extending) {
            moveDirection = facing
            blockToMove = pistonPos.offset(facing)
        } else {
            moveDirection = facing.opposite
            blockToMove = pistonPos.offset(facing, 2)
        }
    }

    fun canMove(): Boolean {
        toMove.clear()
        toDestroy.clear()
        val state = world.getBlockState(blockToMove)
        if (!canPush(state, world, blockToMove, moveDirection, false, facing)) {
            return if (extending && state.pushReaction == EnumPushReaction.DESTROY) {
                toDestroy.add(blockToMove)
                true
            } else {
                false
            }
        } else if (!addBlockLine(blockToMove, moveDirection)) {
            return false
        } else {
            for (i in toMove.indices) {
                val pos = toMove[i]
                val state1 = world.getBlockState(pos)
                if (state1.block.isStickyBlock(state1) && !addBranchingBlocks(pos)) {
                    return false
                }
            }

            return true
        }
    }

    private fun addBlockLine(origin: BlockPos, facing: EnumFacing): Boolean {
        var state = world.getBlockState(origin)
        if (world.isAirBlock(origin)) {
            return true
        } else if (!canPush(state, world, origin, moveDirection, false, facing)) {
            return true
        } else if (origin == pistonPos) {
            return true
        } else if (toMove.contains(origin)) {
            return true
        } else {
            var i = 1
            if (i + toMove.size > 12) {
                return false
            } else {
                var oldState: IBlockState
                while (state.block.isStickyBlock(state)) {
                    val pos = origin.offset(moveDirection.opposite, i)
                    oldState = state
                    state = world.getBlockState(pos)
                    if (state.block.isAir(state, world, pos) || !canBlocksStick(oldState, state) || !canPush(state, world, pos, moveDirection, false, moveDirection.opposite) || pos == pistonPos) {
                        break
                    }
                    ++i
                    if (i + toMove.size > 12) {
                        return false
                    }
                }
                var l = 0
                for (i1 in i - 1 downTo 0) {
                    toMove.add(origin.offset(moveDirection.opposite, i1))
                    ++l
                }
                var j1 = 1
                while (true) {
                    val blockpos1 = origin.offset(moveDirection, j1)
                    val j = toMove.indexOf(blockpos1)
                    if (j > -1) {
                        reorderListAtCollision(l, j)
                        for (k in 0..j + l) {
                            val blockpos2 = toMove[k]
                            val state1 = world.getBlockState(blockpos2)
                            if (state1.block.isStickyBlock(state1) && !addBranchingBlocks(blockpos2)) {
                                return false
                            }
                        }
                        return true
                    }
                    state = world.getBlockState(blockpos1)
                    if (state.block.isAir(state, world, blockpos1)) {
                        return true
                    }
                    if (!canPush(state, world, blockpos1, moveDirection, true, moveDirection) || blockpos1 == pistonPos) {
                        return false
                    }
                    if (state.pushReaction == EnumPushReaction.DESTROY) {
                        toDestroy.add(blockpos1)
                        return true
                    }
                    if (toMove.size >= 12) {
                        return false
                    }
                    toMove.add(blockpos1)
                    ++l
                    ++j1
                }
            }
        }
    }

    private fun reorderListAtCollision(a: Int, b: Int) {
        val list = ArrayList<BlockPos>()
        val list1 = ArrayList<BlockPos>()
        val list2 = ArrayList<BlockPos>()
        list.addAll(toMove.subList(0, b))
        list1.addAll(toMove.subList(toMove.size - a, toMove.size))
        list2.addAll(toMove.subList(b, toMove.size - a))
        toMove.clear()
        toMove.addAll(list)
        toMove.addAll(list1)
        toMove.addAll(list2)
    }

    private fun addBranchingBlocks(fromPos: BlockPos): Boolean {
        val state = world.getBlockState(fromPos)
        for (facing in EnumFacing.values()) {
            if (facing.axis != moveDirection.axis) {
                val pos = fromPos.offset(facing)
                if (canBlocksStick(world.getBlockState(pos), state) && !addBlockLine(pos, facing)) {
                    return false
                }
            }
        }

        return true
    }

    private fun canBlocksStick(state: IBlockState, other: IBlockState): Boolean {
        val block = state.block
        val otherBlock = other.block
        return when {
            block is IStickyBlock -> {
                block.canStickTo(state, other)
            }
            otherBlock is IStickyBlock -> {
                otherBlock.canStickTo(other, state)
            }
            else -> true
        }
    }

    fun getBlocksToMove(): List<BlockPos> {
        return toMove
    }

    fun getBlocksToDestroy(): List<BlockPos> {
        return toDestroy
    }
}