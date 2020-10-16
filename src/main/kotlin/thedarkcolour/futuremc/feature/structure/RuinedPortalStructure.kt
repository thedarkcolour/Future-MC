package thedarkcolour.futuremc.feature.structure

import com.mojang.datafixers.Dynamic
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeManager
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.feature.structure.StructureStart
import net.minecraft.world.gen.feature.template.TemplateManager
import java.util.*
import java.util.function.Function

class RuinedPortalStructure(configFactoryIn: Function<Dynamic<*>, RuinedPortalStructureConfig>) : Structure<RuinedPortalStructureConfig>(configFactoryIn) {
    override fun shouldStartAt(
        manager: BiomeManager,
        generator: ChunkGenerator<*>,
        rand: Random,
        x: Int,
        z: Int,
        biome: Biome
    ): Boolean {
        TODO("not implemented")
    }

    override fun getSize(): Int {
        TODO("not implemented")
    }

    override fun getStartFactory(): IStartFactory {
        TODO("not implemented")
    }

    override fun getStructureName(): String {
        TODO("not implemented")
    }

    enum class Type {
        STANDARD,
        DESERT,
        JUNGLE,
        SWAMP,
        MOUNTAIN,
        OCEAN,
        NETHER,
    }

    class Start(structure: Structure<*>, x: Int, z: Int, bounds: MutableBoundingBox, refs: Int, seed: Long) : StructureStart(structure, x, z, bounds, refs, seed) {
        override fun init(
            generator: ChunkGenerator<*>,
            manager: TemplateManager,
            chunkX: Int,
            chunkZ: Int,
            biomeIn: Biome
        ) {
            val properties = TODO()
        }

    }
}