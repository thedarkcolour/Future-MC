package thedarkcolour.futuremc.surfacebuilder

import com.mojang.datafixers.Dynamic
import it.unimi.dsi.fastutil.ints.IntRBTreeSet
import kotlinx.coroutines.withTimeoutOrNull
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.Direction
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.IChunk
import net.minecraft.world.gen.OctavesNoiseGenerator
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig
import java.util.*
import java.util.function.Function

abstract class AbstractNetherSurfaceBuilder(configFactory: Function<Dynamic<*>, out SurfaceBuilderConfig>) : SurfaceBuilder<SurfaceBuilderConfig>(configFactory) {
    private var noiseGen: OctavesNoiseGenerator? = null
    private var middleOctaves = hashMapOf<BlockState, OctavesNoiseGenerator>()
    private var bottomOctaves = hashMapOf<BlockState, OctavesNoiseGenerator>()
    private var seed = 0L

    override fun buildSurface(random: Random, chunk: IChunk, biome: Biome, i: Int, j: Int, k: Int, d: Double, blockState: BlockState, blockState2: BlockState, l: Int, m: Long, config: SurfaceBuilderConfig) {
        val noiseGen = noiseGen!!
        val n = l + 1
        val o = i and 15
        val p = j and 15
        val q = (d / 3.0 + 3.0 + random.nextDouble() * 0.25).toInt()
        val r = (d / 3.0 + 3.0 + random.nextDouble() * 0.25).toInt()
        val e = 0.03125
        val bl = noiseGen.func_205563_a(i * 0.03125, 109.0, j * 0.03125) * 75.0 + random.nextDouble() > 0.0
        val blockState3 = bottomOctaves.entries.stream().max(Comparator.comparing { entry ->
            entry.value.func_205563_a(i.toDouble(), l.toDouble(), j.toDouble())
        }).get().key
        val blockState4 = middleOctaves.entries.stream().max(Comparator.comparing { entry ->
            entry.value.func_205563_a(i.toDouble(), l.toDouble(), j.toDouble())
        }).get().key
        val mutable = BlockPos.Mutable()
        var blockState5 = chunk.getBlockState(mutable.setPos(o, 128, p))

        for (s in 127 downTo 0) {
            mutable.setPos(o, s, p)
            val blockState6 = chunk.getBlockState(mutable)
            if (blockState5.block == blockState.block && (blockState6.isAir(chunk, mutable) || blockState6 == blockState2)) {
                for (u in 0 until q) {
                    mutable.move(0, 1, 0)
                    if (chunk.getBlockState(mutable).block != blockState.block) break

                    chunk.setBlockState(mutable, blockState3, false)
                }

                mutable.setPos(o, s, p)
            }

            @Suppress("DEPRECATION")
            if ((blockState5.isAir || blockState5 == blockState2) && blockState6.block == blockState.block) {
                var u = 0
                while (u < r && chunk.getBlockState(mutable).block == blockState.block) {
                    if (bl && s >= n - 4 && s <= n + 1) {
                        chunk.setBlockState(mutable, GRAVEL, false)
                    } else {
                        chunk.setBlockState(mutable, blockState4, false)
                    }

                    mutable.move(0, -1, 0)
                    ++u
                }
            }

            blockState5 = blockState6
        }
    }

    override fun setSeed(seed: Long) {
        if (this.seed != seed || noiseGen == null || middleOctaves.isEmpty() || bottomOctaves.isEmpty()) {
            setOctaves(middleOctaves, getMiddleBlocks(), seed)
            setOctaves(bottomOctaves, getBottomBlocks(), seed + middleOctaves.size)
            noiseGen = OctavesNoiseGenerator(SharedSeedRandom(seed + middleOctaves.size + bottomOctaves.size), ZERO_OCTAVE)
        }

        this.seed = seed
    }

    private fun setOctaves(map: MutableMap<BlockState, OctavesNoiseGenerator>, blocks: List<BlockState>, seed: Long) {
        map.clear()

        for (i in blocks.indices) {
            map[blocks[i]] = OctavesNoiseGenerator(SharedSeedRandom(seed + i), MINUS_4_OCTAVE)
        }
    }

    abstract fun getMiddleBlocks(): List<BlockState>

    abstract fun getBottomBlocks(): List<BlockState>

    private companion object {
        private val ZERO_OCTAVE = IntRBTreeSet()
        private val MINUS_4_OCTAVE = IntRBTreeSet()
        private val GRAVEL = Blocks.GRAVEL.defaultState

        init {
            ZERO_OCTAVE.add(0)
            MINUS_4_OCTAVE.add(-4)
        }
    }
}