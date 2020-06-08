package thedarkcolour.futuremc.recipe.smithing

import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import thedarkcolour.futuremc.recipe.Recipes
import thedarkcolour.futuremc.registry.FItems

object SmithingRecipes : Recipes<SmithingRecipe>() {
    override val recipes = arrayListOf(
        SmithingRecipe(ItemStack(Items.DIAMOND_AXE), ItemStack(FItems.NETHERITE_INGOT), ItemStack(FItems.NETHERITE_AXE)),
        SmithingRecipe(ItemStack(Items.DIAMOND_HOE), ItemStack(FItems.NETHERITE_INGOT), ItemStack(FItems.NETHERITE_HOE)),
        SmithingRecipe(ItemStack(Items.DIAMOND_PICKAXE), ItemStack(FItems.NETHERITE_INGOT), ItemStack(FItems.NETHERITE_PICKAXE)),
        SmithingRecipe(ItemStack(Items.DIAMOND_SHOVEL), ItemStack(FItems.NETHERITE_INGOT), ItemStack(FItems.NETHERITE_SHOVEL)),
        SmithingRecipe(ItemStack(Items.DIAMOND_SWORD), ItemStack(FItems.NETHERITE_INGOT), ItemStack(FItems.NETHERITE_SWORD)),
        SmithingRecipe(ItemStack(Items.DIAMOND_HELMET), ItemStack(FItems.NETHERITE_INGOT), ItemStack(FItems.NETHERITE_HELMET)),
        SmithingRecipe(ItemStack(Items.DIAMOND_CHESTPLATE), ItemStack(FItems.NETHERITE_INGOT), ItemStack(FItems.NETHERITE_CHESTPLATE)),
        SmithingRecipe(ItemStack(Items.DIAMOND_LEGGINGS), ItemStack(FItems.NETHERITE_INGOT), ItemStack(FItems.NETHERITE_LEGGINGS)),
        SmithingRecipe(ItemStack(Items.DIAMOND_BOOTS), ItemStack(FItems.NETHERITE_INGOT), ItemStack(FItems.NETHERITE_BOOTS))
    )

    /**
     * Gets a recipe for the given input.
     */
    @Deprecated("Use overload with material parameter", level = DeprecationLevel.ERROR)
    override fun getRecipe(input: ItemStack): SmithingRecipe? {
        return null
    }

    /**
     * Gets a recipe for the given input and material.
     */
    fun getRecipe(input: ItemStack, material: ItemStack): SmithingRecipe? {
        for (recipe in recipes) {
            if (recipe.matchesMaterial(input, material)) {
                return recipe
            }
        }

        return null
    }

    /**
     * Removes a recipe with the given input
     * Ignores if the recipe doesn't exist.
     */
    @Deprecated("Use overload with material parameter", level = DeprecationLevel.ERROR)
    override fun removeRecipe(input: ItemStack) {}

    fun removeRecipe(input: ItemStack, material: ItemStack) {
        val iterator = recipes.iterator()

        while (iterator.hasNext()) {
            val a = iterator.next()

            if (a.doesInputMatchIgnoreDurability(a.input, input) && a.doesInputMatch(a.material, material)) {
                iterator.remove()
            }
        }
    }
}