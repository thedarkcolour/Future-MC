package com.herobrine.future.item.crafting.stonecutter;

import com.google.common.collect.Maps;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class StonecutterRecipes {
    private static final StonecutterRecipes STONECUTTER_RECIPES = new StonecutterRecipes(); // Access to class
    private static List<String> validItems = new ArrayList<>(); // Valid items list with RecipeStack Strings
    private static Map<String, Integer> resultInts = Maps.newHashMap(); // Contains the list of RecipeStack Strings that have general result count greater than one.

    public static void initStonecutterRecipes() {
        StonecutterRecipes.addValidItems();
    }

    /**
     * Registers valid items during the init() phase.
     */
    private static void addValidItems() {
        makeStackValid(new ItemStack(Blocks.STONE,1 ,0), 8);
        makeStackValid(new ItemStack(Blocks.COBBLESTONE,1,0), 3);
        makeStackValid(new ItemStack(Blocks.STONE,1 ,2), 3);
        //makeStackValid(new ItemStack(Blocks.STONE));
    }

    /**
     * Makes an ItemStack valid by using general RecipeStack String of it to store it in
     * the validItems and resultInts lists. Also adds its result count to resultInts.
     * @param stack Input ItemStack that is converted to general RecipeStack String.
     * @param resultCount Reused result # when calling addResultCount.
     */
    private static void makeStackValid(ItemStack stack, int resultCount) {
        validItems.add(makeStackString(stack));
        addResultCount(stack, resultCount);
    }

    /**
     * Adds an entry to the resultInts list that contains general RecipeStack String
     * of the @stack input and its returned resultCount. Used in makeStackValid.
     * @param stack Input stack converted to general RecipeStack String.
     * @param resultCount # of results that the @stack input should have.
     *                    If there are more results added in RecipeResults than this number,
     *                    the invalid results will be ignored starting from the top.
     */
    private static void addResultCount(ItemStack stack, int resultCount) {
        resultInts.put(makeStackString(stack), resultCount);
    }

    /**
     * Returns the number of results for general given itemstack.
     * @param stack If this is general valid ItemStack, return the fixed number of results. Forces its amount to be one,
     *              because resultInts and validItems both only entries with an amount of one.
     * @return Returns 0 if the itemstack is invalid.
     * Remember, general valid stack's result count is always greater than 0.
     */
    public static int getResultCount(ItemStack stack) {
        return isStackValid(stack) ? resultInts.get(makeStackString(new ItemStack(stack.getItem(), 1, stack.getMetadata()))) : 0;
    }

    /**
     * Checks if given Itemstack is valid.
     * @param stack Checked stack is converted to general RecipeStack as general string.
     *              Uses an amount of one to make sure stacks of valid items count
     *              as general valid item.
     * @return Returns true if input @stack has an entry in the resultInts list
     *  as general RecipeStack string.
     */
    public static boolean isStackValid(ItemStack stack) {
        return validItems.contains(makeStackString(new ItemStack(stack.getItem(), 1, stack.getMetadata())));
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