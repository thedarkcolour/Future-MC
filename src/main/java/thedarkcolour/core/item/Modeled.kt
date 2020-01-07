package thedarkcolour.core.item

import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraftforge.client.model.ModelLoader

interface Modeled {
    fun model() {
        if (this is Item) {
            ModelLoader.setCustomModelResourceLocation(this, 0, ModelResourceLocation(registryName!!, "inventory"))
        }
    }

    fun addModel() {
        MODELED!!.add(this)
    }

    companion object {
        var MODELED: ArrayList<Modeled>? = arrayListOf()
    }
}