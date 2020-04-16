package thedarkcolour.futuremc.surfacebuilder

import com.mojang.datafixers.Dynamic
import it.unimi.dsi.fastutil.ints.IntRBTreeSet
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
import thedarkcolour.futuremc.registry.FBlocks
import java.util.*
import java.util.function.Function

class SoulSandValleySurfaceBuilder(deserializer: Function<Dynamic<*>, out SurfaceBuilderConfig>)
    : SurfaceBuilder<SurfaceBuilderConfig>(deserializer) {
    private var octave5: OctavesNoiseGenerator? = null
    private var octave1: OctavesNoiseGenerator? = null
    private var octave2: OctavesNoiseGenerator? = null
    private var octave3: OctavesNoiseGenerator? = null
    private var octave4: OctavesNoiseGenerator? = null
    @JvmField var seed = 0L

    override fun buildSurface(
            random: Random, chunk: IChunk, biome: Biome, i: Int, j: Int, k: Int, d: Double,
            blockState: BlockState, blockState2: BlockState, l: Int, m: Long, ternarySurfaceConfig: SurfaceBuilderConfig
    ) {
        val n = l + 1
        val o = i and 15
        val p = j and 15
        val q = (d / 3.0 + 3.0 + random.nextDouble() * 0.25).toInt()
        val r = (d / 3.0 + 3.0 + random.nextDouble() * 0.25).toInt()
        val f: Double = octave1!!.func_205563_a(i.toDouble(), l.toDouble(), j.toDouble())
        val s: Double = octave2!!.func_205563_a(i.toDouble(), l.toDouble(), j.toDouble())
        val h: Double = octave3!!.func_205563_a(i.toDouble(), l.toDouble(), j.toDouble())
        val bl: Boolean = octave4!!.func_205563_a(i.toDouble() * 0.03125, 109.0, j.toDouble() * 0.03125) * 75.0 + random.nextDouble() > 0.0
        val g: Double = octave5!!.func_205563_a(i.toDouble(), l.toDouble(), j.toDouble())
        val blockState3: BlockState = if (h > s) SOUL_SOIL else SOUL_SAND
        val blockState4: BlockState = if (f > g) SOUL_SOIL else SOUL_SAND
        val mutable = BlockPos.Mutable()
        var blockState5 = chunk.getBlockState(mutable.setPos(o, 128, p))

        for (t in 127 downTo 0) {
            mutable.setPos(o, t, p)
            val blockState6 = chunk.getBlockState(mutable)
            var v: Int
            if (blockState5.block === blockState.block && (blockState6.isAir || blockState6 === blockState2)) {
                v = 0
                while (v < q) {
                    mutable.move(Direction.UP)
                    if (chunk.getBlockState(mutable).block !== blockState.block) {
                        break
                    }
                    chunk.setBlockState(mutable, blockState3, false)
                    ++v
                }
                mutable.setPos(o, t, p)
            }
            if ((blockState5.isAir || blockState5 === blockState2) && blockState6.block === blockState.block) {
                v = 0
                while (v < r && chunk.getBlockState(mutable).block === blockState.block) {
                    if (bl && t >= n - 4 && t <= n + 1) {
                        chunk.setBlockState(mutable, GRAVEL, false)
                    } else {
                        chunk.setBlockState(mutable, blockState4, false)
                    }
                    mutable.move(Direction.DOWN)
                    ++v
                }
            }
            blockState5 = blockState6
        }
    }

    override fun setSeed(seed: Long) {
        if (this.seed != seed || octave1 == null || octave2 == null || octave3 == null || octave4 == null || octave5 == null) {
            octave1 = OctavesNoiseGenerator(SharedSeedRandom(seed), IntRBTreeSet().also { it.add(-4) })
            octave2 = OctavesNoiseGenerator(SharedSeedRandom(seed + 1L), IntRBTreeSet().also { it.add(-4) })
            octave3 = OctavesNoiseGenerator(SharedSeedRandom(seed + 2L), IntRBTreeSet().also { it.add(-4) })
            octave4 = OctavesNoiseGenerator(SharedSeedRandom(seed + 3L), IntRBTreeSet().also { it.add(-4) })
            octave5 = OctavesNoiseGenerator(SharedSeedRandom(seed + 4L), IntRBTreeSet().also { it.add(-4) })
        }

        this.seed = seed
    }

    companion object {
        val GRAVEL = Blocks.GRAVEL.defaultState
        val SOUL_SAND = Blocks.SOUL_SAND.defaultState
        val SOUL_SOIL = FBlocks.SOUL_SOIL.defaultState
    }
}