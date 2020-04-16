package thedarkcolour.futuremc.feature

import com.mojang.datafixers.Dynamic
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.feature.structure.ScatteredStructure
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import net.minecraft.world.gen.feature.structure.StructureStart
import net.minecraft.world.gen.feature.template.TemplateManager
import thedarkcolour.futuremc.feature.structure.NetherFossilPieces
import thedarkcolour.futuremc.util.instantiateEnum
import java.util.function.Predicate

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
            val chunkPos = ChunkPos(chunkX, chunkZ)
            val x = chunkPos.xStart + rand.nextInt(16)
            val j = chunkPos.zStart + rand.nextInt(16)
            val z = generator.seaLevel
            val l = z + rand.nextInt(generator.maxHeight - 2 - z)
            val y = generator.func_222531_c(x, j, Heightmap.Type.WORLD_SURFACE_WG)

            if (y > 0 && y != 127) { //  && l - y > z
                NetherFossilPieces.start(manager, components, rand, BlockPos(x, y, z))
                println("HI WE HERE $x $y $z")
                recalculateStructureSize()
            }
        }

        companion object {
            // Hacky way to check for soul sand
            val SOUL_SAND_PREDICATE = instantiateEnum(Heightmap.Type::class.java, "SOUL_SAND_PREDICATE", "SOUL_SAND_PREDICATE", Heightmap.Usage.WORLDGEN, Predicate { state: BlockState ->
                !state.isAir
            })
        }
    }
}