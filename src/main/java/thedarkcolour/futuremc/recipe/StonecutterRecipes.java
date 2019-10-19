package thedarkcolour.futuremc.recipe;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import thedarkcolour.futuremc.init.Init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public final class StonecutterRecipes {
    private static final ArrayList<StonecutterRecipe> RECIPES = Lists.newArrayList();

    public static void addDefaults() {
        addRecipe(Blocks.STONE, Blocks.STONE_SLAB, Blocks.STONEBRICK).addOutput(new ItemStack(Blocks.STONE_SLAB, 1, 5)).addOutput(new ItemStack(Blocks.STONE_BRICK_STAIRS)).addOutput(Init.STONE_BRICK_WALL).addOutput(new ItemStack(Blocks.STONEBRICK, 1, 3));
        addRecipe(Init.SMOOTH_STONE, new ItemStack(Blocks.STONE_SLAB, 1, 0));
        addRecipe(Blocks.STONEBRICK, Blocks.STONE_BRICK_STAIRS, Init.STONE_BRICK_WALL).addOutput(new ItemStack(Blocks.STONEBRICK, 1, 3));
        addRecipe(new ItemStack(Blocks.STONEBRICK, 1, 1), new ItemStack(Init.MOSSY_STONE_WALL));
        addRecipe(new ItemStack(Blocks.STONE, 1, 1)).addOutput(Init.GRANITE_WALL).addOutput(new ItemStack(Blocks.STONE, 1, 2));
        addRecipe(new ItemStack(Blocks.STONE, 1, 3)).addOutput(Init.DIORITE_WALL);
        addRecipe(new ItemStack(Blocks.STONE, 1, 5)).addOutput(Init.ANDESITE_WALL);
        addRecipe(Blocks.COBBLESTONE, new ItemStack(Blocks.STONE_SLAB, 1, 3), new ItemStack(Blocks.STONE_STAIRS), new ItemStack(Blocks.COBBLESTONE_WALL));
        addRecipe(new ItemStack(Blocks.MOSSY_COBBLESTONE)).addOutput(Blocks.COBBLESTONE_WALL);
        addRecipe(new ItemStack(Blocks.SANDSTONE), new ItemStack(Blocks.STONE_SLAB, 1, 1), new ItemStack(Blocks.SANDSTONE_STAIRS), new ItemStack(Init.SANDSTONE_WALL), new ItemStack(Blocks.SANDSTONE, 1, 2), new ItemStack(Blocks.SANDSTONE, 1, 1));
        addRecipe(new ItemStack(Blocks.RED_SANDSTONE), new ItemStack(Blocks.STONE_SLAB2), new ItemStack(Blocks.RED_SANDSTONE_STAIRS), new ItemStack(Init.RED_SANDSTONE_WALL), new ItemStack(Blocks.RED_SANDSTONE, 1, 2), new ItemStack(Blocks.RED_SANDSTONE, 1, 1));
        addRecipe(new ItemStack(Blocks.PRISMARINE)).addOutput(Init.PRISMARINE_WALL);
        addRecipe(new ItemStack(Blocks.QUARTZ_BLOCK), new ItemStack(Blocks.STONE_SLAB, 1, 7), new ItemStack(Blocks.QUARTZ_STAIRS), new ItemStack(Blocks.QUARTZ_BLOCK, 1, 1), new ItemStack(Blocks.QUARTZ_BLOCK, 1, 2));
        addRecipe(Blocks.PURPUR_BLOCK, Blocks.PURPUR_SLAB, Blocks.PURPUR_STAIRS, Blocks.PURPUR_PILLAR);
        addRecipe(Blocks.BRICK_BLOCK, new ItemStack(Blocks.STONE_SLAB, 1, 4)).addOutput(Blocks.BRICK_STAIRS);
        addRecipe(Blocks.NETHER_BRICK, new ItemStack(Blocks.STONE_SLAB, 1, 6), new ItemStack(Blocks.NETHER_BRICK_STAIRS), new ItemStack(Init.NETHER_BRICK_WALL));
        addRecipe(Blocks.RED_NETHER_BRICK, Init.RED_NETHER_BRICK_WALL);
        addRecipe(Blocks.END_STONE, Blocks.END_BRICKS, Init.END_STONE_WALL);
        addRecipe(Blocks.END_BRICKS, Init.END_STONE_WALL);
    }

    public static StonecutterRecipe addRecipe(ItemStack input, ItemStack... outputs) {
        StonecutterRecipe recipe = new StonecutterRecipe(input, outputs);
        RECIPES.add(recipe);
        return recipe;
    }

    public static StonecutterRecipe addRecipe(Block input, ItemStack... outputs) {
        return addRecipe(new ItemStack(input), outputs);
    }

    public static StonecutterRecipe addRecipe(Block input, Block... outputs) {
        return addRecipe(new ItemStack(input), Arrays.stream(outputs).map(ItemStack::new).toArray(ItemStack[]::new));
    }

    public static void addOrCreateRecipe(ItemStack input, ItemStack... outputs) {
        Optional<StonecutterRecipe> recipe = getRecipe(input);
        if (recipe.isPresent()) {
            for (ItemStack output : outputs) {
                recipe.get().addOutput(output);
            }
        } else {
            addRecipe(input, outputs);
        }
    }

    public static void removeOutputs(ItemStack input, ItemStack... outputs) {
        getRecipe(input).ifPresent(recipe -> {
            for (ItemStack output : outputs) {
                recipe.removeOutput(output);
            }
        });
    }

    public static Optional<StonecutterRecipe> getRecipe(ItemStack input) {
        return RECIPES.stream().filter(recipe -> recipe.matches(input)).findFirst();
    }

    public static void removeRecipe(ItemStack input) {
        RECIPES.removeIf(recipe -> recipe.matches(input));
    }

    public static void clear() {
        RECIPES.clear();
    }

    public static ArrayList<StonecutterRecipe> allRecipes() {
        return RECIPES;
    }
}