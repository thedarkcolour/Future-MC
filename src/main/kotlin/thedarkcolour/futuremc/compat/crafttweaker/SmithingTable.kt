package thedarkcolour.futuremc.compat.crafttweaker

import com.blamejared.crafttweaker.api.CraftTweakerAPI
import com.blamejared.crafttweaker.api.annotations.ZenRegister
import com.blamejared.crafttweaker.api.item.IIngredient
import com.blamejared.crafttweaker.api.item.IItemStack
import com.blamejared.crafttweaker.api.managers.IRecipeManager
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe
import net.minecraft.item.crafting.IRecipeType
import net.minecraft.util.ResourceLocation
import org.openzen.zencode.java.ZenCodeGlobals.Global
import org.openzen.zencode.java.ZenCodeType.Method
import org.openzen.zencode.java.ZenCodeType.Name
import thedarkcolour.futuremc.recipe.SmithingRecipe
import thedarkcolour.futuremc.registry.FRecipes

@ZenRegister
@Name("futuremc.crafttweaker.SmithingTable")
object SmithingTable : IRecipeManager {
    @JvmStatic
    @Global("smithingTable")
    val smithingTable = SmithingTable

    override fun getRecipeType(): IRecipeType<*> {
        return FRecipes.SMITHING
    }

    @Method
    fun addRecipe(recipeName: String, input: IIngredient, material: IIngredient, materialCost: Int, result: IItemStack) {
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