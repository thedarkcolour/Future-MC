package thedarkcolour.futuremc.compat.jei.campfire;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import thedarkcolour.futuremc.recipe.campfire.CampfireRecipe;

import java.awt.Color;

public class CampfireRecipeWrapper implements IRecipeWrapper {
    public final CampfireRecipe recipe;

    public CampfireRecipeWrapper(CampfireRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, recipe.input);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.output);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawString(I18n.format("container.jei.futuremc.campfire.duration") + ": " + recipe.duration, 2, 2, Color.gray.getRGB());
    }
}