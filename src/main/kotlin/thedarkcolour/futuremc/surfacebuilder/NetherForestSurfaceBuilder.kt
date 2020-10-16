package thedarkcolour.futuremc.surfacebuilder

import com.mojang.datafixers.Dynamic
import it.unimi.dsi.fastutil.ints.IntRBTreeSet
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.IChunk
import net.minecraft.world.gen.OctavesNoiseGenerator
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig
import java.util.*
import java.util.function.Function

class NetherForestSurfaceBuilder(configFactory: Function<Dynamic<*>, out SurfaceBuilderConfig>) : SurfaceBuilder<SurfaceBuilderConfig>(configFactory) {
    private var noiseGen: OctavesNoiseGenerator? = null
    private var seed = 0L

    override fun buildSurface(random: Random, chunk: IChunk, biomeIn: Biome, x: Int, z: Int, startHeight: Int, noise: Double, defaultBlock: BlockState, defaultFluid: BlockState, seaLevel: Int, seed: Long, config: SurfaceBuilderConfig) {
        val noiseGen = noiseGen!!
        val i = x and 15
        val j = z and 15
        val k = noiseGen.func_205563_a(x * 0.1, seaLevel.toDouble(), z * 0.1)
        val flag = k > 0.15 + random.nextDouble() * 0.35
        val l = noiseGen.func_205563_a(x * 0.1, seaLevel.toDouble(), z * 0.1)
        val flag1 = l > 0.25 + random.nextDouble() * 0.9
        val q = (noise / 3.0 + 3.0 + random.nextDouble() * 0.25).toInt()
        val mutable = BlockPos.Mutable()
        var r = -1
        var blockState3 = config.under

        for (s in 127 downTo 0) {
            mutable.setPos(i, s, j)
            var blockState4 = config.top
            val blockState5 = chunk.getBlockState(mutable)

            if (blockState5.isAir(chunk, mutable)) {
                r = -1
            } else if (blockState5.block == defaultBlock.block) {
                if (r == -1) {
                    if (q <= 0) {
                        blockState4 = CAVE_AIR
                        blockState3 = config.under
                    }
                    if (flag) {
                        blockState4 = config.under
                    } else if (flag1) {
                        blockState4 = config.underWaterMaterial
                    }
                    if (s < seaLevel + 1 && blockState4.isAir) {
                        blockState4 = defaultFluid
                    }
                    r = q
                    if (s >= seaLevel) {
                        chunk.setBlockState(mutable, blockState4, false)
                    } else {
                        chunk.setBlockState(mutable, blockState3, false)
                    }
                } else if (r > 0) {
                    --r
                    chunk.setBlockState(mutable, blockState3, false)
                }
            }
        }
    }

    override fun setSeed(seed: Long) {
        if (this.seed != seed || noiseGen == null) {
            noiseGen = OctavesNoiseGenerator(SharedSeedRandom(seed), IntRBTreeSet().also { it.add(0) })
        }

        this.seed = seed
    }

    companion object {
        private val CAVE_AIR = Blocks.CAVE_AIR.defaultState
    }
}