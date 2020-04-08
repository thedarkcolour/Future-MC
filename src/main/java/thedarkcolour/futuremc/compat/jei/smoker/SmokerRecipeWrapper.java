package thedarkcolour.futuremc.compat.jei.smoker;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import thedarkcolour.futuremc.recipe.furnace.FurnaceRecipe;

public class SmokerRecipeWrapper implements IRecipeWrapper {
    private final ItemStack input;
    private final ItemStack output;

    public SmokerRecipeWrapper(FurnaceRecipe recipe) {
        this.input = recipe.getInput();
        this.output = recipe.getOutput();
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, input);
        ingredients.setOutput(VanillaTypes.ITEM, output);
    }
}