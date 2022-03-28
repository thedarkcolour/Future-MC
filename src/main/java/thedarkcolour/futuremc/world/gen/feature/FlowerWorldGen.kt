package thedarkcolour.futuremc.world.gen.feature

import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldType
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import thedarkcolour.futuremc.block.villagepillage.BlockFlower
import thedarkcolour.futuremc.block.villagepillage.SweetBerryBushBlock
import thedarkcolour.futuremc.config.FConfig
import java.util.*

class FlowerWorldGen(private val flower: BlockFlower) : FWorldGen {
    private val state: IBlockState

    init {
        if (flower is SweetBerryBushBlock && FConfig.villageAndPillage.sweetBerryBush.spawnWithBerries) {
            this.state = flower.defaultState.withProperty(SweetBerryBushBlock.AGE, 3)
        } else {
            this.state = flower.defaultState
        }
    }

    private fun generate(worldIn: World, random: Random, targetPos: BlockPos) {
        for (i in 0..63) {
            val pos = targetPos.add(
                random.nextInt(8) - random.nextInt(8),
                random.nextInt(4) - random.nextInt(4),
                random.nextInt(8) - random.nextInt(8)
            )
            // check if loaded to prevent cascading worldgen
            if (worldIn.isBlockLoaded(pos) && worldIn.isAirBlock(pos)) {
                if ((!worldIn.provider.isNether || pos.y < 255) && flower.canBlockStay(worldIn, pos, state)) {
                    worldIn.setBlockState(pos, state, 2)
                }
            }
        }
    }

    override fun generate(
        rand: Random,
        chunkX: Int,
        chunkZ: Int,
        worldIn: World,
        chunkGenerator: IChunkGenerator,
        chunkProvider: IChunkProvider
    ) {
        val x = chunkX * 16 + 8
        val z = chunkZ * 16 + 8
        val position = BlockPos(x, 0, z)
        if (!worldIn.isBlockLoaded(position))
            return
        val biome = worldIn.getBiomeForCoordsBody(position)
        val chunkPos = worldIn.getChunk(chunkX, chunkZ).pos
        if (rand.nextDouble() < flower.flowerChance) {
            if (flower.validBiomes.contains(biome) && worldIn.worldType != WorldType.FLAT) {
                placeAround(worldIn, rand, chunkPos, 0..12, ::generate)
            }
        }
    }
}