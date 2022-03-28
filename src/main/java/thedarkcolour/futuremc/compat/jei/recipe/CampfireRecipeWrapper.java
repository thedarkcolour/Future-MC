package thedarkcolour.futuremc.compat.jei.recipe;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import thedarkcolour.futuremc.recipe.campfire.CampfireRecipe;

import java.util.Collections;

public class CampfireRecipeWrapper implements IRecipeWrapper {
    private final IJeiHelpers helpers;
    public final CampfireRecipe recipe;

    public CampfireRecipeWrapper(IJeiHelpers helpers, CampfireRecipe recipe) {
        this.helpers = helpers;
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(helpers.getStackHelper().toItemStackList(recipe.getInput())));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawString(I18n.format("container.jei.futuremc.campfire.duration") + ": " + recipe.getDuration(), 2, 2, 0x808080);
    }
}