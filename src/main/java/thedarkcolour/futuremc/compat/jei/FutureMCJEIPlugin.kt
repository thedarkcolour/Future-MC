package thedarkcolour.futuremc.compat.jei

import it.unimi.dsi.fastutil.objects.Object2ByteMap
import mezz.jei.api.IModPlugin
import mezz.jei.api.IModRegistry
import mezz.jei.api.JEIPlugin
import mezz.jei.api.recipe.IRecipeCategoryRegistration
import mezz.jei.api.recipe.VanillaRecipeCategoryUid
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.block.villagepillage.ComposterBlock
import thedarkcolour.futuremc.client.gui.GuiFurnaceAdvanced
import thedarkcolour.futuremc.client.gui.SmithingGui
import thedarkcolour.futuremc.client.gui.StonecutterScreen
import thedarkcolour.futuremc.compat.jei.category.*
import thedarkcolour.futuremc.compat.jei.recipe.CampfireRecipeWrapper
import thedarkcolour.futuremc.compat.jei.recipe.ComposterRecipeWrapper
import thedarkcolour.futuremc.compat.jei.recipe.SimpleRecipeWrapper
import thedarkcolour.futuremc.compat.jei.recipe.SmithingRecipeWrapper
import thedarkcolour.futuremc.config.FConfig.villageAndPillage
import thedarkcolour.futuremc.container.ContainerFurnaceAdvanced
import thedarkcolour.futuremc.recipe.SimpleRecipe
import thedarkcolour.futuremc.recipe.campfire.CampfireRecipe
import thedarkcolour.futuremc.recipe.campfire.CampfireRecipes
import thedarkcolour.futuremc.recipe.furnace.SmokerRecipes
import thedarkcolour.futuremc.recipe.smithing.SmithingRecipe
import thedarkcolour.futuremc.recipe.smithing.SmithingRecipes
import thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipes
import thedarkcolour.futuremc.registry.FBlocks.BLAST_FURNACE
import thedarkcolour.futuremc.registry.FBlocks.CAMPFIRE
import thedarkcolour.futuremc.registry.FBlocks.COMPOSTER
import thedarkcolour.futuremc.registry.FBlocks.SMITHING_TABLE
import thedarkcolour.futuremc.registry.FBlocks.SMOKER
import thedarkcolour.futuremc.registry.FBlocks.STONECUTTER

@JEIPlugin
class FutureMCJEIPlugin : IModPlugin {
    override fun registerCategories(registry: IRecipeCategoryRegistration) {
        val helper = registry.jeiHelpers.guiHelper

        if (villageAndPillage.smoker) {
            registry.addRecipeCategories(
                AdvancedFurnaceRecipeCategory(
                    helper,
                    AdvancedFurnaceRecipeCategory.SMOKING,
                    SMOKER
                )
            )
        }
        if (villageAndPillage.blastFurnace) {
            registry.addRecipeCategories(
                AdvancedFurnaceRecipeCategory(
                    helper,
                    AdvancedFurnaceRecipeCategory.BLASTING,
                    BLAST_FURNACE
                )
            )
        }
        if (villageAndPillage.campfire.enabled) {
            registry.addRecipeCategories(
                CampfireRecipeCategory(
                    helper
                )
            )
        }
        if (villageAndPillage.smithingTable.enabled) {
            registry.addRecipeCategories(SmithingRecipeCategory(helper))
        }
        if (villageAndPillage.composter) {
            registry.addRecipeCategories(ComposterRecipeCategory(helper))
        }
        if (villageAndPillage.stonecutter.enabled) {
            registry.addRecipeCategories(StonecutterRecipeCategory(helper))
        }
    }

    override fun register(registry: IModRegistry) {
        val recipeTransferRegistry = registry.recipeTransferRegistry

        if (villageAndPillage.smoker) {
            registerAdvancedFurnace(GuiFurnaceAdvanced.Smoker::class.java, AdvancedFurnaceRecipeCategory.SMOKING, SMOKER, registry, recipeTransferRegistry)
        }
        if (villageAndPillage.blastFurnace) {
            registerAdvancedFurnace(GuiFurnaceAdvanced.BlastFurnace::class.java, AdvancedFurnaceRecipeCategory.BLASTING, BLAST_FURNACE, registry, recipeTransferRegistry)
        }
        if (villageAndPillage.campfire.enabled && villageAndPillage.campfire.functionality) {
            registry.handleRecipes(CampfireRecipe::class.java, ::CampfireRecipeWrapper, CampfireRecipeCategory.NAME)
            registry.addRecipes(CampfireRecipes.recipes, CampfireRecipeCategory.NAME)
            registry.addRecipeCatalyst(ItemStack(CAMPFIRE), CampfireRecipeCategory.NAME)
        }
        if (villageAndPillage.stonecutter.enabled && villageAndPillage.stonecutter.functionality) {
            registry.handleRecipes(SimpleRecipe::class.java, ::SimpleRecipeWrapper, StonecutterRecipeCategory.NAME)
            registry.addRecipes(StonecutterRecipes.recipes, StonecutterRecipeCategory.NAME)
            if (villageAndPillage.stonecutter.recipeButton) {
                registry.addRecipeClickArea(StonecutterScreen::class.java, 143, 8, 16, 16, StonecutterRecipeCategory.NAME)
            }
            registry.addRecipeCatalyst(ItemStack(STONECUTTER), StonecutterRecipeCategory.NAME)
        }
        if (villageAndPillage.smithingTable.enabled && villageAndPillage.smithingTable.functionality) {
            registry.handleRecipes(SmithingRecipe::class.java, ::SmithingRecipeWrapper, SmithingRecipeCategory.NAME)
            registry.addRecipes(SmithingRecipes.recipes, SmithingRecipeCategory.NAME)
            registry.addRecipeCatalyst(ItemStack(SMITHING_TABLE), SmithingRecipeCategory.NAME)
            registry.addRecipeClickArea(SmithingGui::class.java, 102, 48, 22, 15, SmithingRecipeCategory.NAME)
        }
        @Suppress("UNCHECKED_CAST")
        if (villageAndPillage.composter) {
            registry.handleRecipes(Object2ByteMap.Entry::class.java as Class<Object2ByteMap.Entry<ItemStack>>, ::ComposterRecipeWrapper, ComposterRecipeCategory.NAME)
            registry.addRecipes(ComposterBlock.ItemsForComposter.entries, ComposterRecipeCategory.NAME)
            registry.addRecipeCatalyst(ItemStack(COMPOSTER), ComposterRecipeCategory.NAME)
        }
        if (villageAndPillage.smoker || villageAndPillage.blastFurnace) {
            recipeTransferRegistry.addRecipeTransferHandler(ContainerFurnaceAdvanced::class.java, VanillaRecipeCategoryUid.FUEL, 1, 1, 3, 36)
        }
    }

    private fun registerAdvancedFurnace(
        guiClass: Class<out GuiFurnaceAdvanced>,
        title: String,
        block: Block,
        registry: IModRegistry,
        recipeTransferRegistry: IRecipeTransferRegistry
    ) {
        registry.handleRecipes(SimpleRecipe::class.java, ::SimpleRecipeWrapper, title)
        registry.addRecipes(SmokerRecipes.recipes, title)
        registry.addRecipeClickArea(guiClass, 78, 32, 28, 23, title, VanillaRecipeCategoryUid.FUEL)
        recipeTransferRegistry.addRecipeTransferHandler(ContainerFurnaceAdvanced::class.java, title, 0, 1, 3, 36)
        val stack = ItemStack(block)
        registry.addRecipeCatalyst(stack, title)
        registry.addRecipeCatalyst(stack, VanillaRecipeCategoryUid.FUEL)
    }

    companion object {
        @JvmField
        val RECIPE_BACKGROUNDS = ResourceLocation(FutureMC.ID, "textures/gui/recipes.png")
    }
}