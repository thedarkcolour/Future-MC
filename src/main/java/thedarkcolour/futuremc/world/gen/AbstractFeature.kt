package thedarkcolour.futuremc.world.gen

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.BiomeManager
import thedarkcolour.futuremc.world.IWorld
import thedarkcolour.futuremc.world.gen.chunk.ChunkGenerator
import thedarkcolour.futuremc.world.gen.config.IFeatureConfig
import thedarkcolour.futuremc.world.gen.structure.*
import java.util.*
import kotlin.collections.HashMap

abstract class AbstractFeature<FC : IFeatureConfig> {
    /**
     * Places the feature at the position.
     *
     * [StructureStarts] and [StructureRefs]
     * are only used by structures.
     * Non-null assertions are safe if the feature
     * is a structure feature.
     *
     * @param worldIn the world
     * @param rand the world random
     * @param pos the position to place at
     * @param structureStarts null if this feature is not a structure
     * @param structureRefs null if this feature is not a structure
     * @param config the configuration for this feature
     *
     * @return if this feature was placed
     */
    abstract fun place(worldIn: World, rand: Random, pos: BlockPos, structureStarts: StructureStarts?, structureRefs: StructureRefs?, config: FC): Boolean

    /**
     * Places this feature in a FMC world.
     */
    abstract fun place(worldIn: IWorld, generator: ChunkGenerator<*>, rand: Random, pos: BlockPos): Boolean

    /**
     * Overload which is called for normal features.
     */
    fun place(worldIn: World, rand: Random, pos: BlockPos, config: FC): Boolean {
        return place(worldIn, rand, pos, null, null, config)
    }

    /**
     * Configure this feature for use in FBiome.
     */
    open fun configure(config: FC): ConfiguredFeature<out AbstractFeature<FC>, FC> {
        return ConfiguredFeature(this, config)
    }

    /**
     * Returns a list of creatures that spawn in this feature.
     * Only used in structures.
     */
    open fun getSpawnList() = emptyList<Biome.SpawnListEntry>()

    companion object {
        val REGISTRY = HashMap<AbstractFeature<*>, String>()
        // todo add features
        val MINESHAFT = object : AbstractStructure<IFeatureConfig.Default>() {
            override fun shouldStartAt(
                manager: BiomeManager,
                worldIn: World,
                rand: Random,
                chunkX: Int,
                chunkZ: Int,
                biome: Biome
            ): Boolean {
                return false
            }

            override fun getStartFactory(): IStartFactory {
                TODO("not implemented")
            }

            override val structureName = "Mineshaft"
        }
        val STRONGHOLD = StrongholdStructure()
    }
}