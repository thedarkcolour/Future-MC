package thedarkcolour.futuremc.block

import net.minecraft.block.SoundType
import net.minecraft.init.Biomes
import net.minecraft.world.biome.Biome
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.init.matchesAny

class BlockCornflower : BlockFlower("cornflower") {
    override fun isBiomeValid(biome: Biome): Boolean {
        return biome.matchesAny(Biomes.PLAINS, Biomes.MUTATED_FOREST)
    }

    override val flowerChance: Double
        get() = FConfig.villageAndPillage.cornflower.spawnRate

    //Adds blue flower
    init {
        soundType = SoundType.PLANT
    }
}