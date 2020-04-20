package thedarkcolour.futuremc.compat

import net.minecraftforge.fml.ModList
import thedarkcolour.futuremc.compat.biomesoplenty.BiomesOPlentyCompat

const val BIOMES_O_PLENTY = "biomesoplenty"

fun checkBiomesOPlenty(): BiomesOPlentyCompat? {
    return if (isModLoaded(BIOMES_O_PLENTY)) {
        BiomesOPlentyCompat
    } else null
}

fun isModLoaded(id: String): Boolean {
    return ModList.get().isLoaded(id)
}