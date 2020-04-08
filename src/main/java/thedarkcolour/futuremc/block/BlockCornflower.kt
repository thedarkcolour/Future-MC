package thedarkcolour.futuremc.block

import net.minecraft.init.Biomes
import net.minecraft.world.biome.Biome
import thedarkcolour.core.util.matchesAny
import thedarkcolour.futuremc.config.FConfig

class BlockCornflower : BlockFlower("cornflower") {
    override fun isBiomeValid(biome: Biome): Boolean {
        return biome.matchesAny(Biomes.PLAINS, Biomes.MUTATED_FOREST)
    }

    override val flowerChance: Double
        get() = FConfig.villageAndPillage.cornflower.spawnRate
}