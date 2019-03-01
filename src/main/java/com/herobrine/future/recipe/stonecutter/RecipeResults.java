package com.herobrine.future.recipe.stonecutter;

import com.google.common.collect.Maps;
import com.herobrine.future.FutureJava;
import com.herobrine.future.utils.config.FutureConfig;
import com.herobrine.future.utils.proxy.Init;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeResults {
    private static final RecipeResults INSTANCE = new RecipeResults();
    private static Map<String, List<String>> recipeMap = Maps.newHashMap();

    public static RecipeResults instance() {
        return INSTANCE;
    }

    /**
     * Registers the results for each valid input.
     * Adds a result one at a time.
     */
    public static void initRecipeResults() {
        ItemStack stack0 = new ItemStack(Blocks.COBBLESTONE);


        addStackResult(Blocks.STONE, new ItemStack(Blocks.STONE_SLAB, 2, 0), 0);
        addStackResult(Blocks.STONE, Blocks.STONE_BRICK_STAIRS, 1);
        addStackResult(Blocks.STONE, Blocks.STONEBRICK, 2);
        addStackResult(Blocks.STONE, new ItemStack(Blocks.STONEBRICK, 1, 3), 3);
        addStackResult(Blocks.STONE, new ItemStack(Blocks.STONE_SLAB, 2, 5), 4);
        if(FutureConfig.a.newwallvariants) addStackResult(Blocks.STONE, Init.newwall.get(6), 5);
        addStackResult(Blocks.STONE, Blocks.STONE_BUTTON, 6);
        addStackResult(Blocks.STONE, Blocks.STONE_PRESSURE_PLATE, 7);
        addStackResult(Blocks.COBBLESTONE, new ItemStack(Blocks.STONE_SLAB, 1, 3), 0);
        addStackResult(Blocks.COBBLESTONE, Blocks.STONE_STAIRS, 1);
        addStackResult(Blocks.COBBLESTONE, Blocks.COBBLESTONE_WALL, 2);
    }

    /**
     * Allows the item to have slots in the Stonecutter's gui, to allow crafting.
     * @param input Item on the left side of GUI that displays slots.
     * @param output Adds a valid output to the input entry in the recipeMap.
     * @param index Which place on the index to add the @output to @input.
     */
    public static void addStackResult(ItemStack input, ItemStack output, int index) {
        /*
          Adds a entry for @input item by converting it to a localized
          string of a RecipeStack of @input.
         */
        recipeMap.putIfAbsent(makeStackString(input), new ArrayList<>());
        /*
         *  Double checks that the entry exists before running body.
         */
        if (recipeMap.containsKey(makeStackString(input))) {
                /*
                 * Navigates to the checked @input entry string, then adds the valid
                 * @output stack to @input entry's list. Does not add if the @index value is invalid.
                 */
            recipeMap.get(makeStackString(input)).add(index, makeStackString(output));
            if (FutureConfig.c.debug) FutureJava.logger.log(Level.INFO, "Added stack result '" + output + "@" + index + "' for input '" + input + "'");
        } else {
            if (FutureConfig.c.debug) FutureJava.logger.log(Level.ERROR,"FAILED to add stack result '" + output + "@" + index + "' for input '" + input + "'");
        }
    }

    /**
     * Allows a simpler way to add recipes that do not have metadata or count.
     * @param input Input block is converted to itemstack without data
     * @param output Output blocks is also converted to itemstack without data.
     * @param index Index is reused in other overload of addStackResult
     */
    private static void addStackResult(Block input, Block output, int index) {
        addStackResult(new ItemStack(input), new ItemStack(output), index);
    }

    /**
     * Another overload allowing for block input / itemstack output.
     * @param input Input block is converted to itemstack without data
     * @param output Output stack
     * @param index Index is reused in other overload of addStackResult
     */
    private static void addStackResult(Block input, ItemStack output, int index) {
        addStackResult(new ItemStack(input), output, index);
    }

    /**
     * Another overload allowing for itemstack input / block output.
     * @param input Input stack
     * @param output Output block that is converted to itemstack without data
     * @param index Index is reused in other overload of addStackResult
     */
    private static void addStackResult(ItemStack input, Block output, int index) {
        addStackResult(input, new ItemStack(output), index);
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
    public ItemStack getStackResult(ItemStack input, int result) {
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
}