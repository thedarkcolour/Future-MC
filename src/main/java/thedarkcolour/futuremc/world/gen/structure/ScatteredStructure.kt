package thedarkcolour.futuremc.world.gen.structure

import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.BiomeManager
import thedarkcolour.futuremc.world.gen.config.IFeatureConfig
import java.util.*

class ScatteredStructure<FC : IFeatureConfig> : AbstractStructure<FC>() {
    override fun shouldStartAt(
        manager: BiomeManager,
        worldIn: World,
        rand: Random,
        chunkX: Int,
        chunkZ: Int,
        biome: Biome
    ): Boolean {
        TODO("not implemented")
    }

    override fun getStartFactory(): IStartFactory {
        TODO("not implemented")
    }

    override val structureName: String
        get() = TODO("Not yet implemented")
}