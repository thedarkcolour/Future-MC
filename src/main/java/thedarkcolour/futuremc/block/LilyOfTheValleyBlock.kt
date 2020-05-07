package thedarkcolour.futuremc.block

import net.minecraft.init.Biomes
import net.minecraft.world.biome.Biome
import thedarkcolour.futuremc.config.FConfig

class LilyOfTheValleyBlock : BlockFlower("lily_of_the_valley") {
    override fun isBiomeValid(biome: Biome): Boolean {
        return biome == Biomes.FOREST || biome == Biomes.MUTATED_FOREST
    }

    override val flowerChance = FConfig.villageAndPillage.lilyOfTheValley.spawnRate
}