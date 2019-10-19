package thedarkcolour.futuremc.compat.jei.stonecutter;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class StonecutterRecipeWrapper implements IRecipeWrapper {
    protected final ItemStack output;
    protected final ItemStack input;
    public final int index;

    public StonecutterRecipeWrapper(ItemStack input, ItemStack output, int index) {
        this.input = input;
        this.output = output;
        this.index = index;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, input);
        ingredients.setOutput(VanillaTypes.ITEM, output);
    }
}