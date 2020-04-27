package thedarkcolour.futuremc.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder
import net.minecraft.world.lighting.LightEngine
import net.minecraft.world.server.ServerWorld
import java.util.*

class NyliumBlock(properties: Properties) : Block(properties) {
    override fun scheduledTick(state: BlockState, worldIn: ServerWorld, pos: BlockPos, rand: Random) {
        if (!stayAlive(state, worldIn, pos)) {
            worldIn.setBlockState(pos, SurfaceBuilder.NETHERRACK)
        }
    }

    private fun stayAlive(state: BlockState, worldIn: ServerWorld, pos: BlockPos): Boolean {
        val up = pos.up()
        val upState = worldIn.getBlockState(up)
        val i = LightEngine.func_215613_a(worldIn, state, pos, upState, up, Direction.UP, upState.getOpacity(worldIn, up))
        return i < worldIn.maxLightLevel
    }
}