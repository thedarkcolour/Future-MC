package thedarkcolour.futuremc.block.villagepillage

import net.minecraft.init.Biomes
import thedarkcolour.futuremc.config.FConfig

class LilyOfTheValleyBlock : BlockFlower("lily_of_the_valley") {
    override val validBiomes = setOf(Biomes.FOREST, Biomes.MUTATED_FOREST)
    override val flowerChance = FConfig.villageAndPillage.lilyOfTheValley.spawnRate
}