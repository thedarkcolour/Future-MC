package thedarkcolour.futuremc.compat.jei.stonecutter;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.compat.jei.FutureMCJEIPlugin;
import thedarkcolour.futuremc.compat.jei.SimpleRecipeWrapper;

public class StonecutterRecipeCategory implements IRecipeCategory<SimpleRecipeWrapper> {
    public static final String NAME = "container.jei.futuremc.stonecutter.name";
    private final IDrawable background;

    public StonecutterRecipeCategory(IGuiHelper helper) {
        background = helper.createDrawable(FutureMCJEIPlugin.RECIPE_BACKGROUNDS, 0, 45, 84, 44);
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
    public void setRecipe(IRecipeLayout recipeLayout, SimpleRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup group = recipeLayout.getItemStacks();
        group.init(0, true, 2, 13);
        group.set(0, recipeWrapper.getInput());

        group.init(1, false, 60, 13);
        group.set(1, recipeWrapper.getOutput());

        group.init(2, false, 27, 2);
        group.set(2, recipeWrapper.getOutput());
    }
}