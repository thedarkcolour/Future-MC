package thedarkcolour.futuremc.item

import net.minecraft.client.resources.I18n
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.BannerPattern
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import thedarkcolour.core.item.ItemModeled
import thedarkcolour.core.util.setItemModel
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig

class ItemBannerPattern : ItemModeled("banner_pattern") {
    init {
        setHasSubtypes(true)
        maxDamage = 0
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.MISC else FutureMC.TAB

        for (i in 1..4) {
            setItemModel(this, i)
        }
    }

    override fun getRarity(stack: ItemStack): EnumRarity {
        return when (stack.itemDamage) {
            1, 2 -> EnumRarity.UNCOMMON
            3, 4 -> EnumRarity.EPIC
            else -> super.getRarity(stack)
        }
    }

    override fun addInformation(
        stack: ItemStack,
        worldIn: World?,
        tooltip: MutableList<String>?,
        flagIn: ITooltipFlag?
    ) {
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