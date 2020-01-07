package thedarkcolour.core.item

import net.minecraft.item.Item
import thedarkcolour.futuremc.FutureMC

open class ItemModeled(regName: String) : Item(), Modeled {
    init {
        translationKey = FutureMC.ID + "." + regName
        setRegistryName(regName)
        addModel()
    }
}