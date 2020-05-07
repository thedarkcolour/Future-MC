package thedarkcolour.futuremc.biome

import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeDecorator

class WarpedForestBiome(properties: BiomeProperties) : Biome(properties) {
    override fun getModdedBiomeDecorator(ignored: BiomeDecorator): BiomeDecorator {
        return super.getModdedBiomeDecorator(ignored)
    }
}