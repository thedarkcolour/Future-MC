package thedarkcolour.futuremc.block

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.FireBlock
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.IWorldReader
import thedarkcolour.futuremc.registry.FBlocks

class SoulFireBlock(properties: Properties) : FireBlock(properties) {
    override fun updatePostPlacement(state: BlockState, direction: Direction, neighborState: BlockState, worldIn: IWorld, pos: BlockPos, neighborPos: BlockPos): BlockState {
        return if (state.isValidPosition(worldIn, pos)) {
            defaultState
        } else {
            Blocks.AIR.defaultState
        }
    }

    override fun isValidPosition(state: BlockState, worldIn: IWorldReader, pos: BlockPos): Boolean {
        return worldIn.getBlockState(pos.down()).block == FBlocks.SOUL_SOIL
    }
}