package thedarkcolour.futuremc.compat.otg

import com.pg85.otg.customobjects.bo3.BO3
import java.io.File

/**
 * Adds Beehives to OTG trees by hooking into the BO3 object placement
 */
object OTGCompat {
    /**
     * Used in ASM core mod
     */
    @JvmStatic
    fun patchBO3(name: String, file: File): BO3 {
        val ret = BO3(name, file)

        // Fix Ancient swampland trees that don't count as trees for some reason
        if (name.toLowerCase().contains("tree")) {
            return TreeBO3(ret)
        }

        return if (ret.onEnable() && ret.canSpawnAsTree()) {
            TreeBO3(ret)
        } else {
            ret
        }
    }
}