package thedarkcolour.futuremc.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.block.BlockFurnaceAdvanced;
import thedarkcolour.futuremc.client.gui.GuiFurnaceAdvanced;
import thedarkcolour.futuremc.client.gui.GuiStonecutter;
import thedarkcolour.futuremc.compat.jei.blastfurnace.BlastFurnaceRecipeCategory;
import thedarkcolour.futuremc.compat.jei.blastfurnace.BlastFurnaceRecipeWrapper;
import thedarkcolour.futuremc.compat.jei.campfire.CampfireRecipeCategory;
import thedarkcolour.futuremc.compat.jei.campfire.CampfireRecipeWrapper;
import thedarkcolour.futuremc.compat.jei.smoker.SmokerRecipeCategory;
import thedarkcolour.futuremc.compat.jei.smoker.SmokerRecipeWrapper;
import thedarkcolour.futuremc.compat.jei.stonecutter.StonecutterRecipeCategory;
import thedarkcolour.futuremc.compat.jei.stonecutter.StonecutterRecipeWrapper;
import thedarkcolour.futuremc.container.ContainerFurnaceAdvanced;
import thedarkcolour.futuremc.init.FutureConfig;
import thedarkcolour.futuremc.init.Init;
import thedarkcolour.futuremc.tile.TileCampfire;

@JEIPlugin
public class FutureMCJEIPlugin implements IModPlugin {
    public static final ResourceLocation RECIPE_BACKGROUNDS = new ResourceLocation(FutureMC.ID, "textures/gui/recipes.png");
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();
        if (FutureConfig.general.smoker) {
            registry.addRecipeCategories(new SmokerRecipeCategory(helper));
        }

        if (FutureConfig.general.blastFurnace) {
            registry.addRecipeCategories(new BlastFurnaceRecipeCategory(helper));
        }

        if (FutureConfig.general.campfire) {
            registry.addRecipeCategories(new CampfireRecipeCategory(helper));
        }

        if (FutureConfig.general.stonecutter) {
            registry.addRecipeCategories(new StonecutterRecipeCategory(helper));
        }
    }

    @Override
    public void register(IModRegistry registry) {
        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

        if (FutureConfig.general.smoker) {
            registry.handleRecipes(BlockFurnaceAdvanced.Recipe.class, SmokerRecipeWrapper::new, SmokerRecipeCategory.NAME);
            registry.addRecipes(BlockFurnaceAdvanced.Recipes.getSmokerRecipes(), SmokerRecipeCategory.NAME);
            registry.addRecipeClickArea(GuiFurnaceAdvanced.BlastFurnace.class, 78, 32, 28, 23, BlastFurnaceRecipeCategory.NAME, VanillaRecipeCategoryUid.FUEL);
            recipeTransferRegistry.addRecipeTransferHandler(ContainerFurnaceAdvanced.class, SmokerRecipeCategory.NAME, 0, 1, 3, 36);
            ItemStack stack = new ItemStack(Init.SMOKER);
            registry.addRecipeCatalyst(stack, SmokerRecipeCategory.NAME);
            registry.addRecipeCatalyst(stack, VanillaRecipeCategoryUid.FUEL);
        }

        if (FutureConfig.general.blastFurnace) {
            registry.handleRecipes(BlockFurnaceAdvanced.Recipe.class, BlastFurnaceRecipeWrapper::new, BlastFurnaceRecipeCategory.NAME);
            registry.addRecipes(BlockFurnaceAdvanced.Recipes.getBlastFurnaceRecipes(), BlastFurnaceRecipeCategory.NAME);
            registry.addRecipeClickArea(GuiFurnaceAdvanced.Smoker.class, 78, 32, 28, 23, SmokerRecipeCategory.NAME, VanillaRecipeCategoryUid.FUEL);
            recipeTransferRegistry.addRecipeTransferHandler(ContainerFurnaceAdvanced.class, BlastFurnaceRecipeCategory.NAME, 0, 1, 3, 36);
            ItemStack stack = new ItemStack(Init.BLAST_FURNACE);
            registry.addRecipeCatalyst(stack, BlastFurnaceRecipeCategory.NAME);
            registry.addRecipeCatalyst(stack, VanillaRecipeCategoryUid.FUEL);
        }

        if (FutureConfig.general.campfire) {
            registry.handleRecipes(TileCampfire.Recipe.class, CampfireRecipeWrapper::new, CampfireRecipeCategory.NAME);
            registry.addRecipes(TileCampfire.Recipes.getAllRecipes(), CampfireRecipeCategory.NAME);
            registry.addRecipeCatalyst(new ItemStack(Init.CAMPFIRE), CampfireRecipeCategory.NAME);
        }

        if (FutureConfig.general.stonecutter) {
            registry.handleRecipes(StonecutterRecipeWrapper.class, recipe -> recipe, StonecutterRecipeCategory.NAME);
            registry.addRecipes(StonecutterRecipeCategory.getAllRecipeWrappers(), StonecutterRecipeCategory.NAME);
            if (FutureConfig.general.stonecutterRecipeButton) {
                registry.addRecipeClickArea(GuiStonecutter.class, 143, 8, 16, 16, StonecutterRecipeCategory.NAME);
            }
            registry.addRecipeCatalyst(new ItemStack(Init.STONECUTTER), StonecutterRecipeCategory.NAME);
        }

        if (FutureConfig.general.smoker || FutureConfig.general.blastFurnace) {
            recipeTransferRegistry.addRecipeTransferHandler(ContainerFurnaceAdvanced.class, VanillaRecipeCategoryUid.FUEL, 1, 1, 3, 36);
        }
    }
}