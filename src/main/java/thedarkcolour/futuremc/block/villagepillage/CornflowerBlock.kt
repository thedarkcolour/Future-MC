package thedarkcolour.futuremc.block.villagepillage

import net.minecraft.init.Biomes
import thedarkcolour.futuremc.config.FConfig

class CornflowerBlock : BlockFlower("cornflower") {
    override val validBiomes = setOf(Biomes.PLAINS, Biomes.MUTATED_FOREST)
    override val flowerChance = FConfig.villageAndPillage.cornflower.spawnRate
}