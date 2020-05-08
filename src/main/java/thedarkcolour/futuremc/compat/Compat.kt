@file:JvmName("Compat")
package thedarkcolour.futuremc.compat

import net.minecraftforge.fml.common.Loader
import thedarkcolour.futuremc.compat.crafttweaker.CraftTweakerCompat
import thedarkcolour.futuremc.compat.dynamictrees.DynamicTreesCompat
import thedarkcolour.futuremc.compat.quark.QuarkCompat

const val CRAFTTWEAKER = "crafttweaker"
const val DYNAMIC_TREES = "dynamictrees"
const val JEI = "jei"
const val QUARK = "quark"

fun checkCraftTweaker(): CraftTweakerCompat? {
    return if (isModLoaded(CRAFTTWEAKER)) {
        CraftTweakerCompat
    } else null
}

fun checkDynamicTrees(): DynamicTreesCompat? {
    return if (isModLoaded(DYNAMIC_TREES)) {
        DynamicTreesCompat
    } else null
}

fun checkQuark(): QuarkCompat? {
    return if (isModLoaded(QUARK)) {
        QuarkCompat
    } else null
}

fun isModLoaded(modid: String): Boolean {
    return Loader.isModLoaded(modid)
}