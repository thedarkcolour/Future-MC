package thedarkcolour.futuremc.compat.crafttweaker

import com.blamejared.crafttweaker.api.CraftTweakerAPI
import com.blamejared.crafttweaker.api.annotations.ZenRegister
import com.blamejared.crafttweaker.api.item.IIngredient
import com.blamejared.crafttweaker.api.item.IItemStack
import com.blamejared.crafttweaker.api.managers.IRecipeManager
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe
import net.minecraft.util.ResourceLocation
import org.openzen.zencode.java.ZenCodeType.Method
import org.openzen.zencode.java.ZenCodeType.Name
import thedarkcolour.futuremc.recipe.SmithingRecipe
import thedarkcolour.futuremc.registry.FRecipes

@ZenRegister
@Name("futuremc.crafttweaker.SmithingTable")
object SmithingTable : IRecipeManager {
    override fun getRecipeType() = FRecipes.SMITHING

    @Method
    @JvmStatic
    fun addRecipe(recipeName: String, result: IItemStack, input: IIngredient, material: IIngredient, materialCost: Int) {
        checkRecipeName(recipeName)
        CraftTweakerAPI.apply(ActionAddRecipe(this, SmithingRecipe(ResourceLocation("crafttweaker", recipeName), input.asVanillaIngredient(), material.asVanillaIngredient(), materialCost, result.internal), ""))
    }

    private fun checkRecipeName(name: String): String {
        val fixedName = fixName(name)

        require(fixedName.chars().allMatch { ch ->
            ch == 95 || ch == 45 || ch in 97..122 || ch in 48..57 || ch == 47 || ch == 46
        }) {
            "Given name does not fit the \"[a-z0-9/._-]\" regex! Name: \"$fixedName\""
        }

        return fixedName
    }

    private fun fixName(name: String): String {
        var fixed = name

        if (fixed.indexOf(':') >= 0) {
            val temp = fixed.replace(":".toRegex(), ".")
            CraftTweakerAPI.logWarning("Invalid recipe name \"$fixed\", recipe names cannot have a \":\"! New recipe name: \"$temp\"")
            fixed = temp
        }
        if (fixed.indexOf(' ') >= 0) {
            val temp = fixed.replace(" ".toRegex(), ".")
            CraftTweakerAPI.logWarning("Invalid recipe name \"$fixed\", recipe names cannot have a \" \"! New recipe name: \"$temp\"")
            fixed = temp
        }
        if (fixed.toLowerCase() != fixed) {
            val temp = fixed.toLowerCase()
            CraftTweakerAPI.logWarning("Invalid recipe name \"$fixed\", recipe names have to be lowercase! New recipe name: \"$temp\"")
            fixed = temp
        }

        return fixed
    }
}