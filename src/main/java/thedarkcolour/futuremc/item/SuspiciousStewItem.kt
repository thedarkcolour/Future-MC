package thedarkcolour.futuremc.item

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.Ingredient
import net.minecraft.item.crafting.ShapelessRecipes
import net.minecraft.potion.PotionEffect
import net.minecraft.potion.PotionUtils
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.registries.IForgeRegistry
import thedarkcolour.core.util.setItemModel
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.registry.FItems

class SuspiciousStewItem : ItemFood(6, 0.6f, false) {
    init {
        setMaxStackSize(1)
        setRegistryName("suspicious_stew")
        translationKey = "${FutureMC.ID}.suspicious_stew"
        if (!FConfig.useVanillaCreativeTabs) creativeTab = FutureMC.GROUP
        setItemModel(this, 0)
    }

    override fun onItemUseFinish(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase): ItemStack {
        super.onItemUseFinish(stack, worldIn, entityLiving)
        return ItemStack(Items.BOWL)
    }

    override fun onFoodEaten(stack: ItemStack, worldIn: World, player: EntityPlayer) {
        if (!worldIn.isRemote) {
            for (effect in PotionUtils.getFullEffectsFromItem(stack)) {
                player.addPotionEffect(effect)
            }
        }
    }

    companion object {
        fun addRecipe(registry: IForgeRegistry<IRecipe>, flower: ItemStack, effect: PotionEffect) {
            val r = ShapelessRecipes(
                "",
                PotionUtils.appendEffects(ItemStack(FItems.SUSPICIOUS_STEW), listOf(effect)),
                NonNullList.create<Ingredient>().also { ingredients ->
                    ingredients.addAll(
                        arrayOf(
                            Ingredient.fromStacks(ItemStack(Blocks.BROWN_MUSHROOM)),
                            Ingredient.fromStacks(ItemStack(Blocks.RED_MUSHROOM)),
                            Ingredient.fromStacks(ItemStack(Items.BOWL)),
                            Ingredient.fromStacks(flower)
                        )
                    )
                })
            registry.register(r.setRegistryName("futuremc:suspicious_stew_${flower.item.registryName!!.path}"))
        }
    }
}