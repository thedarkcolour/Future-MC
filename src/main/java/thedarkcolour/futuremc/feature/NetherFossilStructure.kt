@file:Suppress("PackageDirectoryMismatch")

// used to access protected functions in NoiseChunkGenerator
package net.minecraft.world.gen

import com.mojang.datafixers.Dynamic
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.IBlockReader
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.feature.structure.ScatteredStructure
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import net.minecraft.world.gen.feature.structure.StructureStart
import net.minecraft.world.gen.feature.template.TemplateManager
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder
import thedarkcolour.futuremc.feature.BlockStateSample
import thedarkcolour.futuremc.feature.structure.NetherFossilPieces

class NetherFossilStructure(configFactory: (Dynamic<*>) -> NoFeatureConfig) : ScatteredStructure<NoFeatureConfig>(configFactory) {
    override fun getSeedModifier(): Int {
        return 14357921
    }

    override fun getStartFactory(): IStartFactory {
        return IStartFactory(::Start)
    }

    override fun getStructureName(): String {
        return "Nether_Fossil"
    }

    override fun getBiomeFeatureDistance(chunkGenerator: ChunkGenerator<*>): Int {
        return 2
    }

    override fun getBiomeFeatureSeparation(chunkGenerator: ChunkGenerator<*>): Int {
        return 1
    }

    override fun getSize(): Int {
        return 3
    }

    class Start(structure: Structure<*>, chunkX: Int, chunkZ: Int, bounds: MutableBoundingBox, references: Int, seed: Long) : StructureStart(structure, chunkX, chunkZ, bounds, references, seed) {
        override fun init(generator: ChunkGenerator<*>, manager: TemplateManager, chunkX: Int, chunkZ: Int, biomeIn: Biome) {
            if (generator !is NoiseChunkGenerator<*>) return
            val chunkPos = ChunkPos(chunkX, chunkZ)
            val x = chunkPos.xStart + rand.nextInt(16)
            val z = chunkPos.zStart + rand.nextInt(16)
            val seaLevel = generator.seaLevel
            val i = seaLevel + rand.nextInt(generator.maxHeight - 2 - seaLevel)
            val sample = generator.sample(x, z)
            val pos = BlockPos.Mutable(x, i, z)

            for (j in i downTo seaLevel) {
                val state = sample.getBlockState(pos)
                val hasAir = state.isAir(sample, pos)
                pos.move(0, -1, 0)
                val state1 = sample.getBlockState(pos)
                if (hasAir && (state1.block == Blocks.SOUL_SAND || state1.isSideSolidFullSquare(sample, pos, Direction.UP))) {
                    break
                }
            }
            if (i > seaLevel) {
                NetherFossilPieces.start(manager, components, rand, pos)
                println("HI WE HERE $pos")
                recalculateStructureSize()
            }
        }

        private fun NoiseChunkGenerator<*>.sample(x: Int, z: Int): IBlockReader {
            val states = Array<BlockState>(noiseSizeY * verticalNoiseGranularity) { Blocks.AIR.defaultState }
            fillSample(x, z, states)
            return BlockStateSample(states)
        }

        private fun NoiseChunkGenerator<*>.fillSample(x: Int, z: Int, states: Array<BlockState>) {
            /*val i = Math.floorDiv(x, horizontalNoiseGranularity)
            val j = Math.floorDiv(z, horizontalNoiseGranularity)
            val k = Math.floorMod(x, horizontalNoiseGranularity)
            val l = Math.floorMod(z, horizontalNoiseGranularity)
            val d0 = k / horizontalNoiseGranularity.toDouble()
            val d1 = l / horizontalNoiseGranularity.toDouble()
            val aDouble = arrayOf(func_222547_b(i, j), func_222547_b(i, j + 1), func_222547_b(i + 1, j), func_222547_b(i + 1, j + 1))

            for (j1 in noiseSizeY - 1 downTo 0) {
                val d2 = aDouble[0][j1]
                val d3 = aDouble[1][j1]
                val d4 = aDouble[2][j1]
                val d5 = aDouble[3][j1]
                val d6 = aDouble[0][j1 + 1]
                val d7 = aDouble[1][j1 + 1]
                val d8 = aDouble[2][j1 + 1]
                val d9 = aDouble[3][j1 + 1]

                for (k1 in verticalNoiseGranularity - 1 downTo 0) {
                    val d10 = k1.toDouble() / verticalNoiseGranularity.toDouble()
                    val d11 = MathHelper.lerp3(d10, d0, d1, d2, d6, d4, d8, d3, d7, d5, d9)
                    val l1 = j1 * verticalNoiseGranularity + k1
                    val blockState = when {
                        d11 > 0.0 -> defaultBlock
                        l1 < seaLevel -> defaultFluid
                        else -> SurfaceBuilder.AIR
                    }
                    states[x] = blockState
                }
            }*/
            val i = Math.floorDiv(x, horizontalNoiseGranularity)
            val j = Math.floorDiv(z, horizontalNoiseGranularity)
            val k = Math.floorMod(x, horizontalNoiseGranularity)
            val l = Math.floorMod(z, horizontalNoiseGranularity)
            val d0 = k.toDouble() / horizontalNoiseGranularity.toDouble()
            val d1 = l.toDouble() / horizontalNoiseGranularity.toDouble()
            val adouble = arrayOf(func_222547_b(i, j), func_222547_b(i, j + 1), func_222547_b(i + 1, j), func_222547_b(i + 1, j + 1))
            val i1 = this.seaLevel

            for (j1 in noiseSizeY - 1 downTo 0) {
                val d2 = adouble[0][j1]
                val d3 = adouble[1][j1]
                val d4 = adouble[2][j1]
                val d5 = adouble[3][j1]
                val d6 = adouble[0][j1 + 1]
                val d7 = adouble[1][j1 + 1]
                val d8 = adouble[2][j1 + 1]
                val d9 = adouble[3][j1 + 1]
                for (k1 in verticalNoiseGranularity - 1 downTo 0) {
                    val d10 = k1.toDouble() / verticalNoiseGranularity.toDouble()
                    val d11 = MathHelper.lerp3(d10, d0, d1, d2, d6, d4, d8, d3, d7, d5, d9)
                    val l1 = j1 * verticalNoiseGranularity + k1
                    if (d11 > 0.0 || l1 < i1) {
                        val blockState = when {
                            d11 > 0.0 -> defaultBlock
                            l1 < seaLevel -> defaultFluid
                            else -> SurfaceBuilder.AIR
                        }
                        states[l1] = blockState
                    }
                }
            }
        }
    /*
        companion object {
            // Hacky way to check for soul sand
            val SOUL_SAND_PREDICATE = instantiateEnum(Heightmap.Type::class.java, "SOUL_SAND_PREDICATE", "SOUL_SAND_PREDICATE", Heightmap.Usage.WORLDGEN, Predicate { state: BlockState ->
                state.block == Blocks.SOUL_SAND
            })
        }
    */
    }
}