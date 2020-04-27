package thedarkcolour.futuremc.compat.crafttweaker

import com.blamejared.crafttweaker.api.CraftTweakerAPI
import com.blamejared.crafttweaker.api.annotations.ZenRegister
import com.blamejared.crafttweaker.api.item.IIngredient
import com.blamejared.crafttweaker.api.item.IItemStack
import com.blamejared.crafttweaker.api.managers.IRecipeManager
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe
import net.minecraft.item.crafting.IRecipeType
import net.minecraft.util.ResourceLocation
import org.openzen.zencode.java.ZenCodeType.Method
import org.openzen.zencode.java.ZenCodeType.Name
import thedarkcolour.futuremc.recipe.SmithingRecipe
import thedarkcolour.futuremc.registry.FRecipes

@ZenRegister
@Name("futuremc.crafttweaker.SmithingTable")
object SmithingTable : IRecipeManager {
    override fun getRecipeType(): IRecipeType<*> {
        return FRecipes.SMITHING
    }

    /**
     * Adds a recipe to the smithing table.
     *
     * This is the CraftTweaker method you need to call.
     * EXAMPLE:
     *
     *   import crafttweaker.api.item.IIngredient;
     *   import crafttweaker.api.item.IItemStack;
     *   import futuremc.crafttweaker.SmithingTable
     *
     *   SmithingTable.addRecipe("netherite_pickaxe", <item:futuremc:netherite_pickaxe>, <item:minecraft:diamond_pickaxe>, <item:futuremc:netherite_ingot>, 1);
     *
     * @param recipeName name of the recipe
     * @param result resulting upgraded item
     * @param input the item to be upgraded
     * @param material used to upgrade the item (ex. netherite ingot)
     * @param materialCost amount of material needed to complete the upgrade
     */
    @Method
    @JvmStatic
    fun addRecipe(recipeName: String, result: IItemStack, input: IIngredient, material: IIngredient, materialCost: Int) {
        validateRecipeName(recipeName)
        CraftTweakerAPI.apply(ActionAddRecipe(this,
                SmithingRecipe(
                        ResourceLocation("crafttweaker", recipeName),
                        input.asVanillaIngredient(),
                        material.asVanillaIngredient(),
                        materialCost,
                        result.internal
                ), "")
        )
    }
}