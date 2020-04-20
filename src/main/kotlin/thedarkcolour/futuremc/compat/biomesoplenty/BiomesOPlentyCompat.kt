package thedarkcolour.futuremc.compat.biomesoplenty

import biomesoplenty.api.enums.BOPClimates
import net.minecraft.world.biome.Biome
import thedarkcolour.futuremc.compat.checkBiomesOPlenty

/**
 * Do not access directly,
 * use [checkBiomesOPlenty]
 */
object BiomesOPlentyCompat {
    fun addNetherBiome(biome: Biome) {
        BOPClimates.NETHER.addBiome(10, biome)
    }
}