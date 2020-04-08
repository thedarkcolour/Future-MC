package thedarkcolour.futuremc.asm

import net.minecraft.block.BlockPistonBase.canPush
import net.minecraft.block.material.EnumPushReaction
import net.minecraft.block.state.BlockPistonStructureHelper
import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.futuremc.api.IStickyBlock

@Suppress("unused")
class BlockPistonStructureHelper(
    world: World,
    pistonPos: BlockPos,
    private val facing: EnumFacing,
    private val extending: Boolean
) : BlockPistonStructureHelper(world, pistonPos, facing, extending) {
    override fun canMove(): Boolean {
        toMove.clear()
        toDestroy.clear()
        val state = world.getBlockState(blockToMove)
        // Changes last parameter to facing instead of moveDirection
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
        // remove block variable

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
                // add oldState variable
                var oldState: IBlockState

                while (state.block.isStickyBlock(state)) {
                    val pos = origin.offset(moveDirection.opposite, i)
                    // save to oldState before updating state
                    oldState = state
                    state = world.getBlockState(pos)
                    if (state.block.isAir(state, world, pos) || !canBlocksStick(oldState, state) || !canPush(
                            state,
                            world,
                            pos,
                            moveDirection,
                            false,
                            moveDirection.opposite
                        ) || pos == pistonPos
                    ) {
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
                    val pos1 = origin.offset(moveDirection, j1)
                    val j = toMove.indexOf(pos1)

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
                    state = world.getBlockState(pos1)
                    if (state.block.isAir(state, world, pos1)) {
                        return true
                    }
                    if (!canPush(state, world, pos1, moveDirection, true, moveDirection) || pos1 == pistonPos) {
                        return false
                    }
                    if (state.pushReaction == EnumPushReaction.DESTROY) {
                        toDestroy.add(pos1)
                        return true
                    }
                    if (toMove.size >= 12) {
                        return false
                    }
                    toMove.add(pos1)
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
        // add state variable
        val state = world.getBlockState(fromPos)
        for (facing in EnumFacing.values()) {
            // remove !addBlockLine
            if (facing.axis != moveDirection.axis) {
                // add pos variable
                val pos = fromPos.offset(facing)
                // add canBlocksStick using pos and state
                // add back !addBlockLine
                if (canBlocksStick(world.getBlockState(pos), state) && !addBlockLine(pos, facing)) {
                    return false
                }
            }
        }

        return true
    }

    // sticky check
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
}