package thedarkcolour.futuremc.item

import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraftforge.client.model.ModelLoader
import thedarkcolour.core.item.ItemModeled
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig

class ItemDye : ItemModeled("dye") {
    init {
        setHasSubtypes(true)
        maxDamage = 0
        setCreativeTab(if (FConfig.useVanillaCreativeTabs) CreativeTabs.MISC else FutureMC.TAB)
    }

    override fun model() {
        ModelLoader.setCustomModelResourceLocation(this, 0, ModelResourceLocation(registryName.toString() + "white", "inventory"))
        ModelLoader.setCustomModelResourceLocation(this, 1, ModelResourceLocation(registryName.toString() + "blue", "inventory"))
        ModelLoader.setCustomModelResourceLocation(this, 2, ModelResourceLocation(registryName.toString() + "brown", "inventory"))
        ModelLoader.setCustomModelResourceLocation(this, 3, ModelResourceLocation(registryName.toString() + "black", "inventory"))
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

    override fun itemInteractionForEntity(stack: ItemStack, playerIn: EntityPlayer, target: EntityLivingBase, hand: EnumHand): Boolean {
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