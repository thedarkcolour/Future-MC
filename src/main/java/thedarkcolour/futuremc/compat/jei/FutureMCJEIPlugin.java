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
import thedarkcolour.futuremc.config.FConfig;
import thedarkcolour.futuremc.container.ContainerFurnaceAdvanced;
import thedarkcolour.futuremc.init.FBlocks;
import thedarkcolour.futuremc.recipe.campfire.CampfireRecipe;
import thedarkcolour.futuremc.recipe.campfire.CampfireRecipes;
import thedarkcolour.futuremc.recipe.furnace.BlastFurnaceRecipes;
import thedarkcolour.futuremc.recipe.furnace.FurnaceRecipe;
import thedarkcolour.futuremc.recipe.furnace.SmokerRecipes;

@JEIPlugin
public class FutureMCJEIPlugin implements IModPlugin {
    public static final ResourceLocation RECIPE_BACKGROUNDS = new ResourceLocation(FutureMC.ID, "textures/gui/recipes.png");
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();
        if (FConfig.INSTANCE.getVillageAndPillage().smoker) {
            registry.addRecipeCategories(new SmokerRecipeCategory(helper));
        }

        if (FConfig.INSTANCE.getVillageAndPillage().blastFurnace) {
            registry.addRecipeCategories(new BlastFurnaceRecipeCategory(helper));
        }

        if (FConfig.INSTANCE.getVillageAndPillage().campfire.enabled) {
            registry.addRecipeCategories(new CampfireRecipeCategory(helper));
        }

        if (FConfig.INSTANCE.getVillageAndPillage().stonecutter.enabled) {
            registry.addRecipeCategories(new StonecutterRecipeCategory(helper));
        }
    }

    @Override
    public void register(IModRegistry registry) {
        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

        if (FConfig.INSTANCE.getVillageAndPillage().smoker) {
            registry.handleRecipes(FurnaceRecipe.class, SmokerRecipeWrapper::new, SmokerRecipeCategory.NAME);
            registry.addRecipes(SmokerRecipes.getAllRecipes(), SmokerRecipeCategory.NAME);
            registry.addRecipeClickArea(GuiFurnaceAdvanced.BlastFurnace.class, 78, 32, 28, 23, BlastFurnaceRecipeCategory.NAME, VanillaRecipeCategoryUid.FUEL);
            recipeTransferRegistry.addRecipeTransferHandler(ContainerFurnaceAdvanced.class, SmokerRecipeCategory.NAME, 0, 1, 3, 36);
            ItemStack stack = new ItemStack(FBlocks.INSTANCE.getSMOKER());
            registry.addRecipeCatalyst(stack, SmokerRecipeCategory.NAME);
            registry.addRecipeCatalyst(stack, VanillaRecipeCategoryUid.FUEL);
        }

        if (FConfig.INSTANCE.getVillageAndPillage().blastFurnace) {
            registry.handleRecipes(FurnaceRecipe.class, BlastFurnaceRecipeWrapper::new, BlastFurnaceRecipeCategory.NAME);
            registry.addRecipes(BlastFurnaceRecipes.getAllRecipes(), BlastFurnaceRecipeCategory.NAME);
            registry.addRecipeClickArea(GuiFurnaceAdvanced.Smoker.class, 78, 32, 28, 23, SmokerRecipeCategory.NAME, VanillaRecipeCategoryUid.FUEL);
            recipeTransferRegistry.addRecipeTransferHandler(ContainerFurnaceAdvanced.class, BlastFurnaceRecipeCategory.NAME, 0, 1, 3, 36);
            ItemStack stack = new ItemStack(FBlocks.INSTANCE.getBLAST_FURNACE());
            registry.addRecipeCatalyst(stack, BlastFurnaceRecipeCategory.NAME);
            registry.addRecipeCatalyst(stack, VanillaRecipeCategoryUid.FUEL);
        }

        if (FConfig.INSTANCE.getVillageAndPillage().campfire.enabled) {
            registry.handleRecipes(CampfireRecipe.class, CampfireRecipeWrapper::new, CampfireRecipeCategory.NAME);
            registry.addRecipes(CampfireRecipes.INSTANCE.getRecipes(), CampfireRecipeCategory.NAME);
            registry.addRecipeCatalyst(new ItemStack(FBlocks.INSTANCE.getCAMPFIRE()), CampfireRecipeCategory.NAME);
        }

        if (FConfig.INSTANCE.getVillageAndPillage().stonecutter.enabled) {
            registry.handleRecipes(StonecutterRecipeWrapper.class, recipe -> recipe, StonecutterRecipeCategory.NAME);
            registry.addRecipes(StonecutterRecipeCategory.getAllRecipeWrappers(), StonecutterRecipeCategory.NAME);
            if (FConfig.INSTANCE.getVillageAndPillage().stonecutter.recipeButton) {
                registry.addRecipeClickArea(GuiStonecutter.class, 143, 8, 16, 16, StonecutterRecipeCategory.NAME);
            }
            registry.addRecipeCatalyst(new ItemStack(FBlocks.INSTANCE.getSTONECUTTER()), StonecutterRecipeCategory.NAME);
        }

        if (FConfig.INSTANCE.getVillageAndPillage().smoker || FConfig.INSTANCE.getVillageAndPillage().blastFurnace) {
            recipeTransferRegistry.addRecipeTransferHandler(ContainerFurnaceAdvanced.class, VanillaRecipeCategoryUid.FUEL, 1, 1, 3, 36);
        }
    }
}