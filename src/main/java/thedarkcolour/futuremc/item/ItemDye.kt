package thedarkcolour.futuremc.item

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import thedarkcolour.core.util.setItemModel
import thedarkcolour.core.util.setItemName
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig

class ItemDye : Item() {
    init {
        setItemName(this, "dye")
        setHasSubtypes(true)
        maxDamage = 0
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.MISC else FutureMC.TAB

        addModels()
    }

    private fun addModels() {
        setItemModel(this, 0, registryName.toString() + "white")
        setItemModel(this, 1, registryName.toString() + "blue")
        setItemModel(this, 2, registryName.toString() + "brown")
        setItemModel(this, 3, registryName.toString() + "black")
    }

    override fun getTranslationKey(stack: ItemStack): String {
        return translationKey + "." + stack.metadata
    }

    override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>) {
        if (isInCreativeTab(tab)) {
            for (i in 0..3) {
                items.add(ItemStack(this, 1, i))
            }
        }
    }

    override fun itemInteractionForEntity(
        stack: ItemStack,
        playerIn: EntityPlayer,
        target: EntityLivingBase,
        hand: EnumHand
    ): Boolean {
        if (target is EntitySheep) {
            val color = getColor(stack)
            if (target.fleeceColor != color) {
                target.fleeceColor = color
                stack.shrink(1)
                return true
            }
        }
        return false
    }

    private fun getColor(stack: ItemStack): EnumDyeColor {
        return if (stack.metadata > 3) EnumDyeColor.WHITE else when (stack.metadata) {
            1 -> EnumDyeColor.BLUE
            2 -> EnumDyeColor.BROWN
            3 -> EnumDyeColor.BLACK
            else -> EnumDyeColor.WHITE
        }
    }
}