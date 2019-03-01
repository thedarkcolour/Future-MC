package com.herobrine.future.recipe.stonecutter;

import com.google.common.collect.Maps;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StonecutterRecipes {
    private static final StonecutterRecipes STONECUTTER_RECIPES = new StonecutterRecipes(); // Access to class
    private List<String> validItems = new ArrayList<>(); // Valid items list with RecipeStack Strings
    private Map<String, Integer> resultInts = Maps.newHashMap(); // Contains the list of RecipeStack Strings that have a result count greater than one.

    public static void initStonecutterRecipes() {
        StonecutterRecipes.instance().addValidItems();
    }

    /**
     * Used to access methods.
     * @return Returns the class.
     */
    public static StonecutterRecipes instance() {
        return STONECUTTER_RECIPES;
    }

    /**
     * Registers valid items during the init() phase.
     */
    private void addValidItems() {
        makeStackValid(new ItemStack(Blocks.STONE,1 ,0), 8);
        makeStackValid(new ItemStack(Blocks.COBBLESTONE,1,0), 3);
        makeStackValid(new ItemStack(Blocks.STONE,1 ,2), 3);
        //makeStackValid(new ItemStack(Blocks.STONE));
    }

    /**
     * Makes an ItemStack valid by using a RecipeStack String of it to store it in
     * the validItems and resultInts lists. Also adds its result count to resultInts.
     * @param stack Input ItemStack that is converted to a RecipeStack String.
     * @param resultCount Reused result # when calling addResultCount.
     */
    private void makeStackValid(ItemStack stack, int resultCount) {
        validItems.add(makeStackString(stack));
        addResultCount(stack, resultCount);
    }

    /**
     * Adds an entry to the resultInts list that contains a RecipeStack String
     * of the @stack input and its returned resultCount. Used in makeStackValid.
     * @param stack Input stack converted to a RecipeStack String.
     * @param resultCount # of results that the @stack input should have.
     *                    If there are more results added in RecipeResults than this number,
     *                    the invalid results will be ignored starting from the top.
     */
    private void addResultCount(ItemStack stack, int resultCount) {
        resultInts.put(makeStackString(stack), resultCount);
    }

    /**
     * Returns the number of results for a given itemstack.
     * @param stack If this is a valid ItemStack, return the fixed number of results. Forces its amount to be one,
     *              because resultInts and validItems both only entries with an amount of one.
     * @return Returns 0 if the itemstack is invalid.
     * Remember, a valid stack's result count is always greater than 0.
     */
    public int getResultCount(ItemStack stack) {
        return isStackValid(stack) ? resultInts.get(makeStackString(new ItemStack(stack.getItem(), 1, stack.getMetadata()))) : 0;
    }

    /**
     * Checks if given Itemstack is valid.
     * @param stack Checked stack is converted to a RecipeStack as a string.
     *              Uses an amount of one to make sure stacks of valid items count
     *              as a valid item.
     * @return Returns true if input @stack has an entry in the resultInts list
     *  as a RecipeStack string.
     */
    public boolean isStackValid(ItemStack stack) {
        return validItems.contains(makeStackString(new ItemStack(stack.getItem(), 1, stack.getMetadata())));
    }

    /**
     * Shortened RecipeStack string method for given ItemStack, makes code easier to look at
     * @param stack Input stack
     * @return Returns RecipeStack string for @stack input
     */
    static String makeStackString(ItemStack stack) {
        return RecipeStack.getRecipeStackFromItemStack(stack).toString();
    }
}