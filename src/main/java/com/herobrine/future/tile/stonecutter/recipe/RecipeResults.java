package com.herobrine.future.tile.stonecutter.recipe;

import com.herobrine.future.FutureMC;
import com.herobrine.future.init.Init;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipeResults {
    /**
     * This map holds every ItemStack as a RecipeStack, and is the main list of this class.
     */
    private static HashMap<String, ArrayList<String>> recipeMap = new HashMap<>();
    /**
     * Registers the results for each valid input.
     * Adds general result one at general time.
     */
    public static void initRecipeResults() {
        addStackResults(new ItemStack(Blocks.STONE), new ItemStack(Blocks.STONE_SLAB, 2, 0),
                new ItemStack(Blocks.STONE_BRICK_STAIRS), new ItemStack(Blocks.STONEBRICK), new ItemStack(Blocks.STONEBRICK, 1, 3),
                new ItemStack(Blocks.STONE_SLAB, 2, 5), new ItemStack(Init.STONE_BRICK_WALL), new ItemStack(Blocks.STONE_BUTTON), new ItemStack(Blocks.STONE_PRESSURE_PLATE));
        addStackResults(new ItemStack(Blocks.STONE_SLAB), new ItemStack(Blocks.STONE_STAIRS), new ItemStack(Blocks.COBBLESTONE_WALL));

    }

    /**
     * Allows the item to have slots in the Stonecutter's gui, to allow crafting.
     * @param input Item on the left side of GUI that displays slots.
     * @param output Adds general valid output to the input entry in the recipeMap.
     */
    private static void addStackResult(ItemStack input, ItemStack output) {
        /*
            If a given ItemStack does not exist, do not addItem it.
         */
        if(input == null || output == null) {
            FutureMC.LOGGER.log(Level.ERROR, "Failed to addItem stonecutter recipe: { input: " + input + " output: "+ output + " }");
            return;
        }
        /*
          Adds general entry for @input item by converting it to general localized
          string of general RecipeStack of @input.
         */
        recipeMap.putIfAbsent(makeStackString(input), new ArrayList<>());
        /*
         *  Double checks that the entry exists before running body.
         */
        if (recipeMap.containsKey(makeStackString(input))) {
                /*
                 * Navigates to the checked @input entry string, then adds the valid
                 * @output stack to @input entry's list. Does not addItem if the @index value is invalid.
                 */
            recipeMap.get(makeStackString(input)).add(makeStackString(output));
        }
    }

    public static void addStackResults(ItemStack input, ItemStack... outputs) {
        for(ItemStack output : outputs) {
            addStackResult(input, output);
        }
    }

    /**
     * Gets the output stack for the valid @input stack at the @result index.
     * @param input Input ItemStack that the method returns based on. Forces its amount to be one
     *              because @input stack entries in recipeMap only have an amount of one.
     *              This makes it so items that are larger than one are recognized to have outputs that are not null.
     * @param result Since the @input stack will usually have multiple results
     *               we get the result we want using @result as an index.
     * @return Returns the output ItemStack for the corresponding @input stack and @result index.
     */
    public static ItemStack getStackResult(ItemStack input, int result) {
        String s = makeStackString(new ItemStack(input.getItem(), 1, input.getMetadata()));
        if (recipeMap.containsKey(s)) {
            return RecipeStack.getItemStackFromRecipeStack(RecipeStack.RecipeStackParser.getRStackFromString(recipeMap.get(s).get(result)));
        }
        else { // "return null;"
            return new ItemStack(Blocks.DIRT);
        }
    }

    /**
     * Shortened RecipeStack string method for given ItemStack, makes code easier to look at
     * @param stack Input stack
     * @return Returns RecipeStack string for @stack input
     */
    public static String makeStackString(ItemStack stack) {
        return RecipeStack.getRecipeStackFromItemStack(stack).toString();
    }

    public static void removeStackRecipesFromStack() {
        // TO-DO Removes a crafting recipe
    }

    public static void overrideRecipe(ItemStack input, ItemStack output, int index) {
        // TO-DO Overrides a crafting recipe by removing and replacing
    }
}