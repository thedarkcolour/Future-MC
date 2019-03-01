package com.herobrine.future.recipe.stonecutter;


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RecipeStack {
    private int meta;
    private int amount;
    private Item item;

    /**
     * Essentially an override of ItemStack that can be used in lists
     * and properly converts to a string that holds the ItemStack data.
     * @param itemIn Inherited parameter from ItemStack.
     * @param metadata Inherited parameter from ItemStack.
     * @param amountIn Inherited parameter from ItemStack.
     */
    private RecipeStack(Item itemIn, int metadata, int amountIn) {
        this.item = itemIn;
        this.meta = metadata;
        this.amount = amountIn;
    }

    /**
     * Converts RecipeStack back into an ItemStack.
     * @param stack RecipeStack to be converted.
     * @return Returns an ItemStack identical to the ItemStack @stack input.
     */
    static ItemStack getItemStackFromRecipeStack(RecipeStack stack) {
        return new ItemStack(stack.getItem(), stack.getAmount(), stack.getMeta());
    }

    /**
     * Converts an ItemStack into a new RecipeStack.
     * @param stack Input ItemStack to be converted to RecipeStack
     * @return returns a RecipeStack that is equivalent to the @stack input
     */
    static RecipeStack  getRecipeStackFromItemStack(ItemStack stack) {
        return new RecipeStack(stack.getItem(), stack.getMetadata(), stack.getCount());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeStack that = (RecipeStack) o;
        return meta == that.meta &&
                amount == that.amount &&
                item.equals(that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meta, amount, item.getRegistryName());
    }

    @Override
    public String toString() {
        return "RecipeStackToString{" +
                 meta +
                "," + amount +
                "," + item.getRegistryName();
    }

    public int getMeta() {
        return meta;
    }

    public Item getItem() {
        return item;
    }

    private int getAmount() {
        return amount;
    }

    /**
     * Parses RecipeStacks from strings to ItemStacks.
     */
    static class RecipeStackParser {
        static RecipeStack getRStackFromString(String string) {
            List<String> list = new ArrayList<>(Arrays.asList(parseString(string)));

            int metadata = Integer.parseInt(list.get(0));
            int amount = Integer.parseInt(list.get(1));
            ResourceLocation regName = new ResourceLocation(getRegFromRString(string, metadata, amount));

            return new RecipeStack(Item.REGISTRY.getObject(regName), metadata, amount);
        }

        private static String[] parseString(String string) {
            String preFix = "RecipeStackToString{";
            String clearFixEnd = string.substring(string.indexOf(preFix) + preFix.length());

            return clearFixEnd.split(",");
        }

        private static String getRegFromRString(String string, int meta, int amount) {
            String prefix = "RecipeStackToString{" + "," + meta + "," + amount + ",";
            String s = string.substring(string.indexOf(prefix) + prefix.length());
            return s;
        }

        public static RecipeStackParser instance() {
            return new RecipeStackParser();
        }
    }
}