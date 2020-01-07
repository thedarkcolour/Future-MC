package thedarkcolour.futuremc.recipe.campfire

import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import thedarkcolour.futuremc.recipe.Recipes
import java.util.*

object CampfireRecipes : Recipes<CampfireRecipe>() {
    override val recipes = ArrayList<CampfireRecipe>()

    override fun addDefaults() {
        addRecipe(ItemStack(Items.FISH), ItemStack(Items.COOKED_FISH), 600)
        addRecipe(ItemStack(Items.CHICKEN), ItemStack(Items.COOKED_CHICKEN), 600)
        addRecipe(ItemStack(Items.PORKCHOP), ItemStack(Items.COOKED_PORKCHOP), 600)
        addRecipe(ItemStack(Items.FISH, 1, 1), ItemStack(Items.COOKED_FISH, 1, 1), 600)
        addRecipe(ItemStack(Items.BEEF), ItemStack(Items.COOKED_BEEF), 600)
        addRecipe(ItemStack(Items.MUTTON), ItemStack(Items.COOKED_MUTTON), 600)
        addRecipe(ItemStack(Items.RABBIT), ItemStack(Items.COOKED_RABBIT), 600)
        addRecipe(ItemStack(Items.POTATO), ItemStack(Items.BAKED_POTATO), 600)
    }

    fun addRecipe(input: ItemStack, output: ItemStack, duration: Int) {
        recipes.add(CampfireRecipe(input, output, duration))
    }
}