package thedarkcolour.futuremc.world

import net.minecraft.block.Blocks
import net.minecraft.world.World
import net.minecraft.world.WorldType
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.ChunkGeneratorType
import thedarkcolour.futuremc.biome.provider.MultiNoiseBiomeProvider
import thedarkcolour.futuremc.biome.provider.MultiNoiseBiomeProviderSettings

object FWorldType : WorldType("futuremc_nether") {
    override fun createChunkGenerator(worldIn: World): ChunkGenerator<*> {
        return if (worldIn.getDimension().type == DimensionType.THE_NETHER) {
            val settings = ChunkGeneratorType.CAVES.createSettings()
            settings.defaultBlock = Blocks.NETHERRACK.defaultState
            settings.defaultFluid = Blocks.LAVA.defaultState

            val biomeProviderSettings = MultiNoiseBiomeProviderSettings(worldIn.worldInfo)
            ChunkGeneratorType.CAVES.create(worldIn, MultiNoiseBiomeProvider(biomeProviderSettings), settings)
        } else {
            super.createChunkGenerator(worldIn)
        }
    }
}