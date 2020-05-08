package thedarkcolour.futuremc.compat.jei

import mezz.jei.api.IModPlugin
import mezz.jei.api.IModRegistry
import mezz.jei.api.JEIPlugin
import mezz.jei.api.recipe.IRecipeCategoryRegistration
import mezz.jei.api.recipe.VanillaRecipeCategoryUid
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.client.gui.GuiFurnaceAdvanced
import thedarkcolour.futuremc.compat.jei.blastfurnace.BlastFurnaceRecipeCategory
import thedarkcolour.futuremc.compat.jei.campfire.CampfireRecipeCategory
import thedarkcolour.futuremc.compat.jei.campfire.CampfireRecipeWrapper
import thedarkcolour.futuremc.compat.jei.smoker.SmokerRecipeCategory
import thedarkcolour.futuremc.compat.jei.stonecutter.StonecutterRecipeCategory
import thedarkcolour.futuremc.config.FConfig.villageAndPillage
import thedarkcolour.futuremc.container.ContainerFurnaceAdvanced
import thedarkcolour.futuremc.container.StonecutterGui
import thedarkcolour.futuremc.recipe.SimpleRecipe
import thedarkcolour.futuremc.recipe.campfire.CampfireRecipe
import thedarkcolour.futuremc.recipe.campfire.CampfireRecipes
import thedarkcolour.futuremc.recipe.furnace.BlastFurnaceRecipes
import thedarkcolour.futuremc.recipe.furnace.SmokerRecipes
import thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipes
import thedarkcolour.futuremc.registry.FBlocks.BLAST_FURNACE
import thedarkcolour.futuremc.registry.FBlocks.CAMPFIRE
import thedarkcolour.futuremc.registry.FBlocks.SMOKER
import thedarkcolour.futuremc.registry.FBlocks.STONECUTTER

@JEIPlugin
class FutureMCJEIPlugin : IModPlugin {
    override fun registerCategories(registry: IRecipeCategoryRegistration) {
        val helper = registry.jeiHelpers.guiHelper

        if (villageAndPillage.smoker) {
            registry.addRecipeCategories(SmokerRecipeCategory(helper))
        }
        if (villageAndPillage.blastFurnace) {
            registry.addRecipeCategories(BlastFurnaceRecipeCategory(helper))
        }
        if (villageAndPillage.campfire.enabled) {
            registry.addRecipeCategories(CampfireRecipeCategory(helper))
        }
        if (villageAndPillage.stonecutter.enabled) {
            registry.addRecipeCategories(StonecutterRecipeCategory(helper))
        }
    }

    override fun register(registry: IModRegistry) {
        val recipeTransferRegistry = registry.recipeTransferRegistry
        if (villageAndPillage.smoker) {
            registry.handleRecipes(SimpleRecipe::class.java, ::SimpleRecipeWrapper, SmokerRecipeCategory.NAME)
            registry.addRecipes(SmokerRecipes.recipes, SmokerRecipeCategory.NAME)
            registry.addRecipeClickArea(GuiFurnaceAdvanced.BlastFurnace::class.java, 78, 32, 28, 23, BlastFurnaceRecipeCategory.NAME, VanillaRecipeCategoryUid.FUEL)
            recipeTransferRegistry.addRecipeTransferHandler(ContainerFurnaceAdvanced::class.java, SmokerRecipeCategory.NAME, 0, 1, 3, 36)
            val stack = ItemStack(SMOKER)
            registry.addRecipeCatalyst(stack, SmokerRecipeCategory.NAME)
            registry.addRecipeCatalyst(stack, VanillaRecipeCategoryUid.FUEL)
        }
        if (villageAndPillage.blastFurnace) {
            registry.handleRecipes(SimpleRecipe::class.java, ::SimpleRecipeWrapper, BlastFurnaceRecipeCategory.NAME)
            registry.addRecipes(BlastFurnaceRecipes.recipes, BlastFurnaceRecipeCategory.NAME)
            registry.addRecipeClickArea(GuiFurnaceAdvanced.Smoker::class.java, 78, 32, 28, 23, SmokerRecipeCategory.NAME, VanillaRecipeCategoryUid.FUEL)
            recipeTransferRegistry.addRecipeTransferHandler(ContainerFurnaceAdvanced::class.java, BlastFurnaceRecipeCategory.NAME, 0, 1, 3, 36)
            val stack = ItemStack(BLAST_FURNACE)
            registry.addRecipeCatalyst(stack, BlastFurnaceRecipeCategory.NAME)
            registry.addRecipeCatalyst(stack, VanillaRecipeCategoryUid.FUEL)
        }
        if (villageAndPillage.campfire.enabled) {
            registry.handleRecipes(CampfireRecipe::class.java, ::CampfireRecipeWrapper, CampfireRecipeCategory.NAME)
            registry.addRecipes(CampfireRecipes.recipes, CampfireRecipeCategory.NAME)
            registry.addRecipeCatalyst(ItemStack(CAMPFIRE), CampfireRecipeCategory.NAME)
        }
        if (villageAndPillage.stonecutter.enabled) {
            registry.handleRecipes(SimpleRecipe::class.java, ::SimpleRecipeWrapper, StonecutterRecipeCategory.NAME)
            registry.addRecipes(StonecutterRecipes.recipes, StonecutterRecipeCategory.NAME)
            if (villageAndPillage.stonecutter.recipeButton) {
                registry.addRecipeClickArea(StonecutterGui::class.java, 143, 8, 16, 16, StonecutterRecipeCategory.NAME)
            }
            registry.addRecipeCatalyst(ItemStack(STONECUTTER), StonecutterRecipeCategory.NAME)
        }
        if (villageAndPillage.smoker || villageAndPillage.blastFurnace) {
            recipeTransferRegistry.addRecipeTransferHandler(ContainerFurnaceAdvanced::class.java, VanillaRecipeCategoryUid.FUEL, 1, 1, 3, 36)
        }
    }

    companion object {
        @JvmField
        val RECIPE_BACKGROUNDS = ResourceLocation(FutureMC.ID, "textures/gui/recipes.png")
    }
}