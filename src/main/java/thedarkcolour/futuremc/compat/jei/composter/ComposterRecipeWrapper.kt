package thedarkcolour.futuremc.compat.jei.composter

import it.unimi.dsi.fastutil.objects.Object2ByteMap
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n
import net.minecraft.item.ItemStack

class ComposterRecipeWrapper(entry: Object2ByteMap.Entry<ItemStack>) : IRecipeWrapper {
    val input = entry.key
    val chance = entry.byteValue

    override fun getIngredients(ingredients: IIngredients) {
        ingredients.setInput(VanillaTypes.ITEM, input)
    }

    override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {
        minecraft.fontRenderer.drawString(I18n.format("container.jei.futuremc.composter.chance") + ": $chance%", 0, 1, 0x808080)
    }
}