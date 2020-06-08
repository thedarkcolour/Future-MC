package thedarkcolour.futuremc.world.gen

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.futuremc.world.IWorld
import thedarkcolour.futuremc.world.gen.chunk.ChunkGenerator
import thedarkcolour.futuremc.world.gen.config.IFeatureConfig
import thedarkcolour.futuremc.world.gen.structure.StructureRefs
import thedarkcolour.futuremc.world.gen.structure.StructureStarts
import java.util.*

class ConfiguredFeature<F : AbstractFeature<FC>, FC : IFeatureConfig>(val feature: F, val config: FC) {
    fun place(worldIn: World, rand: Random, pos: BlockPos, structureStarts: StructureStarts, structureRefs: StructureRefs): Boolean {
        return feature.place(worldIn, rand, pos, structureStarts, structureRefs, config)
    }

    fun place(worldIn: World, rand: Random, pos: BlockPos): Boolean {
        return feature.place(worldIn, rand, pos, config)
    }

    /**
     * Places this [feature] in an FMC world with the given [config].
     */
    fun place(worldIn: IWorld, generator: ChunkGenerator<*>, rand: Random, pos: BlockPos): Boolean {
        return feature.place(worldIn, generator, rand, pos)
    }
}