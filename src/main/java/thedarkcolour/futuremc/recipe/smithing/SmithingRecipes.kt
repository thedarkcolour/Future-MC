package thedarkcolour.futuremc.recipe.smithing

import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import thedarkcolour.futuremc.recipe.Recipes
import thedarkcolour.futuremc.registry.FItems

object SmithingRecipes : Recipes<SmithingRecipe>() {
    override val recipes: ArrayList<SmithingRecipe>

    init {
        val netherite = Ingredient.fromItem(FItems.NETHERITE_INGOT)

        recipes = arrayListOf(
            SmithingRecipe(Ingredient.fromStacks(ItemStack(Items.DIAMOND_AXE)), netherite, ItemStack(FItems.NETHERITE_AXE)),
            SmithingRecipe(Ingredient.fromStacks(ItemStack(Items.DIAMOND_HOE)), netherite, ItemStack(FItems.NETHERITE_HOE)),
            SmithingRecipe(Ingredient.fromStacks(ItemStack(Items.DIAMOND_PICKAXE)), netherite, ItemStack(FItems.NETHERITE_PICKAXE)),
            SmithingRecipe(Ingredient.fromStacks(ItemStack(Items.DIAMOND_SHOVEL)), netherite, ItemStack(FItems.NETHERITE_SHOVEL)),
            SmithingRecipe(Ingredient.fromStacks(ItemStack(Items.DIAMOND_SWORD)), netherite, ItemStack(FItems.NETHERITE_SWORD)),
            SmithingRecipe(Ingredient.fromStacks(ItemStack(Items.DIAMOND_HELMET)), netherite, ItemStack(FItems.NETHERITE_HELMET)),
            SmithingRecipe(Ingredient.fromStacks(ItemStack(Items.DIAMOND_CHESTPLATE)), netherite, ItemStack(FItems.NETHERITE_CHESTPLATE)),
            SmithingRecipe(Ingredient.fromStacks(ItemStack(Items.DIAMOND_LEGGINGS)), netherite, ItemStack(FItems.NETHERITE_LEGGINGS)),
            SmithingRecipe(Ingredient.fromStacks(ItemStack(Items.DIAMOND_BOOTS)), netherite, ItemStack(FItems.NETHERITE_BOOTS))
        )
    }

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
        return recipes.firstOrNull { recipe ->
            recipe.matchesMaterial(input, material)
        }
    }

    /**
     * Removes a recipe with the given input.
     * Ignores if the recipe doesn't exist.
     */
    @Deprecated("Use overload with material parameter", level = DeprecationLevel.ERROR)
    override fun removeRecipeForInput(input: ItemStack) {}

    fun removeRecipe(input: ItemStack, material: ItemStack) {
        val iterator = recipes.iterator()

        while (iterator.hasNext()) {
            val a = iterator.next()

            if (a.input.test(input) && a.material.test(material)) {
                iterator.remove()
            }
        }
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "do not use")
    override fun addRecipe(input: Ingredient, output: ItemStack) {
        TODO("not implemented")
    }
}