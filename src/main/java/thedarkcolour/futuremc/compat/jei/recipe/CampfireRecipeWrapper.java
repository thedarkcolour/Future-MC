package thedarkcolour.futuremc.compat.jei.recipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import thedarkcolour.futuremc.recipe.campfire.CampfireRecipe;

public class CampfireRecipeWrapper implements IRecipeWrapper {
    public final CampfireRecipe recipe;

    public CampfireRecipeWrapper(CampfireRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, recipe.getInput());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawString(I18n.format("container.jei.futuremc.campfire.duration") + ": " + recipe.getDuration(), 2, 2, 0x808080);
    }
}