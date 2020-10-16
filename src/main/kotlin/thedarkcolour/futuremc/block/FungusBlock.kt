package thedarkcolour.futuremc.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.BushBlock
import net.minecraft.block.IGrowable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.server.ServerWorld
import thedarkcolour.futuremc.feature.HugeFungusFeatureConfig
import thedarkcolour.futuremc.registry.FBlocks
import java.util.*

class FungusBlock(properties: Properties, private val supplier: () -> ConfiguredFeature<HugeFungusFeatureConfig, *>) : BushBlock(properties), IGrowable {
    override fun getShape(state: BlockState?, worldIn: IBlockReader?, pos: BlockPos?, ctx: ISelectionContext?): VoxelShape {
        return SHAPE
    }

    override fun isValidGround(state: BlockState, worldIn: IBlockReader, pos: BlockPos): Boolean {
        val flag = super.isValidGround(state, worldIn, pos)
        return state.isIn(FBlocks.NYLIUM) || state.block == FBlocks.SOUL_SOIL || flag
    }

    override fun canGrow(worldIn: IBlockReader, pos: BlockPos, state: BlockState?, isClient: Boolean): Boolean {
        val stem = supplier().config.base
        val down = worldIn.getBlockState(pos.down())
        return stem == down
    }

    override fun canUseBonemeal(worldIn: World?, rand: Random, pos: BlockPos?, state: BlockState?): Boolean {
        return rand.nextFloat() < 0.4
    }

    override fun grow(worldIn: ServerWorld, rand: Random, pos: BlockPos, state: BlockState?) {
        supplier().place(worldIn, worldIn.chunkProvider.chunkGenerator, rand, pos)
    }

    companion object {
        private val SHAPE = Block.makeCuboidShape(4.0, 0.0, 4.0, 12.0, 9.0, 12.0)
    }
}