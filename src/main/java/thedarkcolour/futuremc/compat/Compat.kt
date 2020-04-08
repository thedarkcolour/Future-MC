@file:JvmName("Compat")
package thedarkcolour.futuremc.compat

import net.minecraftforge.fml.common.Loader
import thedarkcolour.futuremc.compat.dynamictrees.DynamicTreesCompat
import thedarkcolour.futuremc.compat.jei.JeiCompat

const val DYNAMIC_TREES = "dynamictrees"
const val JEI = "jei"

fun checkDynamicTrees(): DynamicTreesCompat? {
    return if (isModLoaded(DYNAMIC_TREES)) {
        DynamicTreesCompat
    } else null
}

fun checkJei(): JeiCompat? {
    return if (isModLoaded(JEI)) {
        JeiCompat
    } else null
}

fun isModLoaded(modid: String): Boolean {
    return Loader.isModLoaded(modid)
}