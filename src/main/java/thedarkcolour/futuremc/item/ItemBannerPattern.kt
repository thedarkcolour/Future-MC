package thedarkcolour.futuremc.item

import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.resources.I18n
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.EnumRarity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.BannerPattern
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.client.model.ModelLoader
import thedarkcolour.core.item.Modeled
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig

class ItemBannerPattern : Item(), Modeled {
    init {
        setHasSubtypes(true)
        maxDamage = 0
        translationKey = FutureMC.ID + "." + "banner_pattern"
        setRegistryName("banner_pattern")
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.MISC else FutureMC.TAB
        addModel()
    }

    override fun getRarity(stack: ItemStack): EnumRarity {
        return when (stack.itemDamage) {
            1, 2 -> EnumRarity.UNCOMMON
            3, 4 -> EnumRarity.EPIC
            else -> super.getRarity(stack)
        }
    }

    override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>?, flagIn: ITooltipFlag?) {
        when (stack.metadata) {
            1 -> tooltip!!.add(I18n.format("item.futuremc.banner_pattern.creeper"))
            2 -> tooltip!!.add(I18n.format("item.futuremc.banner_pattern.skull"))
            3 -> tooltip!!.add(I18n.format("item.futuremc.banner_pattern.thing"))
            4 -> tooltip!!.add(I18n.format("item.futuremc.banner_pattern.globe"))
            else -> tooltip!!.add(I18n.format("item.futuremc.banner_pattern.flower"))
        }
    }

    override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>) {
        if (this.isInCreativeTab(tab)) {
            for (i in 0..4) {
                items.add(ItemStack(this, 1, i))
            }
        }
    }

    override fun model() {
        for (i in 0..4) {
            ModelLoader.setCustomModelResourceLocation(this, i, ModelResourceLocation(registryName!!, "inventory"))
        }
    }

    companion object {
        fun getBannerPattern(stack: ItemStack): BannerPattern {
            return when (stack.itemDamage) {
                1 -> BannerPattern.CREEPER
                2 -> BannerPattern.SKULL
                3 -> BannerPattern.MOJANG
                4 -> GLOBE
                else -> BannerPattern.FLOWER
            }
        }
        lateinit var GLOBE: BannerPattern
    }
}