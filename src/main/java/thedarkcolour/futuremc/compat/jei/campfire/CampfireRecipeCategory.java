package thedarkcolour.futuremc.compat.jei.campfire;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import thedarkcolour.futuremc.FutureMC;

public class CampfireRecipeCategory implements IRecipeCategory<CampfireRecipeWrapper> {
    public static final String NAME = "container.jei.minecraftfuture.campfire.name";
    private final IDrawable background;

    public CampfireRecipeCategory(IGuiHelper helper) {
        background = helper.createDrawable(new ResourceLocation(FutureMC.ID, "textures/gui/recipes.png"), 0, 0, 73, 44);
    }

    @Override
    public String getUid() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return I18n.format(NAME);
    }

    @Override
    public String getModName() {
        return FutureMC.ID;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CampfireRecipeWrapper wrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 2, 13);
        recipeLayout.getItemStacks().set(0, wrapper.recipe.input);

        recipeLayout.getItemStacks().init(1, false, 53, 13);
        recipeLayout.getItemStacks().set(1, wrapper.recipe.output);
    }
}
