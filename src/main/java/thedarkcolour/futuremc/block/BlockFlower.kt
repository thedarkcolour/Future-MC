package thedarkcolour.futuremc.block

import net.minecraft.block.BlockBush
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.world.biome.Biome
import net.minecraftforge.client.model.ModelLoader
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig

abstract class BlockFlower(regName: String) : BlockBush() {
    init {
        translationKey = FutureMC.ID + "." + regName
        setRegistryName(regName)
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.DECORATIONS else FutureMC.TAB
    }

    // Check if this is needed
    fun model() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, ModelResourceLocation(registryName!!, "inventory"))
    }

    abstract fun isBiomeValid(biome: Biome): Boolean

    abstract val flowerChance: Double
}