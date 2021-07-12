package thedarkcolour.futuremc.compat.jei

import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.registration.IGuiHandlerRegistration
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.ResourceLocation
import thedarkcolour.futuremc.client.screen.SmithingScreen
import thedarkcolour.futuremc.config.FConfig

@JeiPlugin
class FutureMCJEIPlugin : IModPlugin {
    lateinit var smithingRecipeCategory: SmithingRecipeCategory

    override fun getPluginUid(): ResourceLocation {
        return ResourceLocation("futuremc:jei_plugin")
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val helper = registration.jeiHelpers.guiHelper

        if (FConfig.smithingTable.value) {
            smithingRecipeCategory = SmithingRecipeCategory(helper)

            registration.addRecipeCategories(smithingRecipeCategory)
        }
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        if (FConfig.smithingTable.value) {
            val results = RecipeValidator.getResults(smithingRecipeCategory)

            registration.addRecipes(results.smithingRecipes, SmithingRecipeCategory.NAME)
        }
    }

    override fun registerGuiHandlers(registration: IGuiHandlerRegistration) {
        registration.addRecipeClickArea(SmithingScreen::class.java, 102, 48, 22, 15, SmithingRecipeCategory.NAME)
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalyst(ItemStack(Items.SMITHING_TABLE), SmithingRecipeCategory.NAME)
    }
}