package thedarkcolour.futuremc.compat.jei.smoker;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class SmokerRecipeWrapper implements IRecipeWrapper {
    private final ItemStack input;
    private final ItemStack output;

    public SmokerRecipeWrapper(Map.Entry<ItemStack, ItemStack> entry) {
        this.input = entry.getKey();
        this.output = entry.getValue();
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, input);
        ingredients.setOutput(VanillaTypes.ITEM, output);
    }
}