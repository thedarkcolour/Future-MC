package thedarkcolour.futuremc.compat.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import thedarkcolour.futuremc.block.BlockFurnaceAdvanced;
import thedarkcolour.futuremc.client.gui.GuiFurnaceAdvanced;
import thedarkcolour.futuremc.compat.jei.blastfurnace.BlastFurnaceRecipeCategory;
import thedarkcolour.futuremc.compat.jei.blastfurnace.BlastFurnaceRecipeWrapper;
import thedarkcolour.futuremc.compat.jei.campfire.CampfireRecipeCategory;
import thedarkcolour.futuremc.compat.jei.campfire.CampfireRecipeWrapper;
import thedarkcolour.futuremc.compat.jei.smoker.SmokerRecipeCategory;
import thedarkcolour.futuremc.compat.jei.smoker.SmokerRecipeWrapper;
import thedarkcolour.futuremc.init.Init;
import thedarkcolour.futuremc.tile.TileCampfire;

import java.util.List;
import java.util.Map;

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
        registry.handleRecipes(Map.Entry.class, BlastFurnaceRecipeWrapper::new, BlastFurnaceRecipeCategory.NAME);
        registry.handleRecipes(Map.Entry.class, SmokerRecipeWrapper::new, SmokerRecipeCategory.NAME);

        List<Map.Entry<ItemStack, ItemStack>> blastFurnace = Lists.newArrayList();
        List<Map.Entry<ItemStack, ItemStack>> smoker = Lists.newArrayList();

        for (Map.Entry<ItemStack, ItemStack> entry : FurnaceRecipes.instance().getSmeltingList().entrySet()) {
            if (BlockFurnaceAdvanced.FurnaceType.BLAST_FURNACE.isOre(entry.getKey())) {
                blastFurnace.add(entry);
            }
            if (BlockFurnaceAdvanced.FurnaceType.SMOKER.isFood(entry.getKey())) {
                smoker.add(entry);
            }
        }

        registry.addRecipes(blastFurnace, BlastFurnaceRecipeCategory.NAME);
        registry.addRecipes(smoker, SmokerRecipeCategory.NAME);
        registry.addRecipes(TileCampfire.Recipes.getAllRecipes(), CampfireRecipeCategory.NAME);

        registry.addRecipeClickArea(GuiFurnaceAdvanced.BlastFurnace.class, 78, 32, 28, 23, BlastFurnaceRecipeCategory.NAME, VanillaRecipeCategoryUid.FUEL);
        registry.addRecipeClickArea(GuiFurnaceAdvanced.Smoker.class, 78, 32, 28, 23, SmokerRecipeCategory.NAME, VanillaRecipeCategoryUid.FUEL);

        registry.addRecipeCatalyst(new ItemStack(Init.CAMPFIRE), CampfireRecipeCategory.NAME);
        registry.addRecipeCatalyst(new ItemStack(Init.BLAST_FURNACE), BlastFurnaceRecipeCategory.NAME);
        registry.addRecipeCatalyst(new ItemStack(Init.SMOKER), SmokerRecipeCategory.NAME);
    }
}