package thedarkcolour.futuremc.compat.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.compat.jei.FutureMCJEIPlugin;
import thedarkcolour.futuremc.compat.jei.recipe.SimpleRecipeWrapper;

public class StonecutterRecipeCategory implements IRecipeCategory<SimpleRecipeWrapper> {
    public static final String NAME = "container.jei.futuremc.stonecutter.name";
    private final IDrawable background;

    public StonecutterRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(FutureMCJEIPlugin.RECIPE_BACKGROUNDS, 0, 45, 84, 44);
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
        group.init(1, false, 60, 13);
        group.init(2, false, 27, 2);

        group.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

        ItemStack output = ingredients.getOutputs(VanillaTypes.ITEM).get(0).get(0);
        group.set(1, output);
        group.set(2, output);
    }
}