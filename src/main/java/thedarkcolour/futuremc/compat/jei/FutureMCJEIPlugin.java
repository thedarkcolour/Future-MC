package thedarkcolour.futuremc.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;
import thedarkcolour.futuremc.block.BlockFurnaceAdvanced;
import thedarkcolour.futuremc.client.gui.GuiFurnaceAdvanced;
import thedarkcolour.futuremc.compat.jei.blastfurnace.BlastFurnaceRecipeCategory;
import thedarkcolour.futuremc.compat.jei.blastfurnace.BlastFurnaceRecipeWrapper;
import thedarkcolour.futuremc.compat.jei.campfire.CampfireRecipeCategory;
import thedarkcolour.futuremc.compat.jei.campfire.CampfireRecipeWrapper;
import thedarkcolour.futuremc.compat.jei.smoker.SmokerRecipeCategory;
import thedarkcolour.futuremc.compat.jei.smoker.SmokerRecipeWrapper;
import thedarkcolour.futuremc.container.ContainerFurnaceAdvanced;
import thedarkcolour.futuremc.init.Init;
import thedarkcolour.futuremc.tile.TileCampfire;

@JEIPlugin
public class FutureMCJEIPlugin implements IModPlugin {
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new CampfireRecipeCategory(helper), new BlastFurnaceRecipeCategory(helper), new SmokerRecipeCategory(helper));
    }

    @Override
    public void register(IModRegistry registry) {
        registry.handleRecipes(TileCampfire.Recipe.class, CampfireRecipeWrapper::new, CampfireRecipeCategory.NAME);
        registry.handleRecipes(BlockFurnaceAdvanced.Recipe.class, BlastFurnaceRecipeWrapper::new, BlastFurnaceRecipeCategory.NAME);
        registry.handleRecipes(BlockFurnaceAdvanced.Recipe.class, SmokerRecipeWrapper::new, SmokerRecipeCategory.NAME);

        registry.addRecipes(BlockFurnaceAdvanced.Recipes.getBlastFurnaceRecipes(), BlastFurnaceRecipeCategory.NAME);
        registry.addRecipes(BlockFurnaceAdvanced.Recipes.getSmokerRecipes(), SmokerRecipeCategory.NAME);
        registry.addRecipes(TileCampfire.Recipes.getAllRecipes(), CampfireRecipeCategory.NAME);

        registry.addRecipeClickArea(GuiFurnaceAdvanced.BlastFurnace.class, 78, 32, 28, 23, BlastFurnaceRecipeCategory.NAME, VanillaRecipeCategoryUid.FUEL);
        registry.addRecipeClickArea(GuiFurnaceAdvanced.Smoker.class, 78, 32, 28, 23, SmokerRecipeCategory.NAME, VanillaRecipeCategoryUid.FUEL);

        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

        recipeTransferRegistry.addRecipeTransferHandler(ContainerFurnaceAdvanced.class, VanillaRecipeCategoryUid.FUEL, 1, 1, 3, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerFurnaceAdvanced.class, SmokerRecipeCategory.NAME, 0, 1, 3, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerFurnaceAdvanced.class, BlastFurnaceRecipeCategory.NAME, 0, 1, 3, 36);

        registry.addRecipeCatalyst(new ItemStack(Init.CAMPFIRE), CampfireRecipeCategory.NAME);
        registry.addRecipeCatalyst(new ItemStack(Init.BLAST_FURNACE), BlastFurnaceRecipeCategory.NAME);
        registry.addRecipeCatalyst(new ItemStack(Init.SMOKER), SmokerRecipeCategory.NAME);
    }
}