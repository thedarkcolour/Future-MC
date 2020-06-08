package thedarkcolour.futuremc.recipe.stonecutter

import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import thedarkcolour.futuremc.recipe.Recipes
import thedarkcolour.futuremc.recipe.SimpleRecipe
import thedarkcolour.futuremc.registry.FBlocks

/**
 * @author TheDarkColour
 */
object StonecutterRecipes : Recipes<SimpleRecipe>() {
    override val recipes = arrayListOf(
        SimpleRecipe(ItemStack(Blocks.STONE), ItemStack(Blocks.STONE_SLAB, 2)),
        SimpleRecipe(ItemStack(Blocks.STONE), ItemStack(Blocks.STONEBRICK)),
        SimpleRecipe(ItemStack(Blocks.STONE), ItemStack(Blocks.STONE_SLAB, 2, 5)),
        SimpleRecipe(ItemStack(Blocks.STONE), ItemStack(Blocks.STONE_BRICK_STAIRS)),
        SimpleRecipe(ItemStack(Blocks.STONE), ItemStack(FBlocks.STONE_BRICK_WALL)),
        SimpleRecipe(ItemStack(Blocks.STONE), ItemStack(Blocks.STONEBRICK, 1, 3)),
        SimpleRecipe(ItemStack(FBlocks.SMOOTH_STONE), ItemStack(Blocks.STONE_SLAB, 2, 0)),
        SimpleRecipe(ItemStack(Blocks.STONEBRICK), ItemStack(Blocks.STONE_BRICK_STAIRS)),
        SimpleRecipe(ItemStack(Blocks.STONEBRICK), ItemStack(FBlocks.STONE_BRICK_WALL)),
        SimpleRecipe(ItemStack(Blocks.STONEBRICK), ItemStack(Blocks.STONEBRICK, 1, 3)),
        SimpleRecipe(ItemStack(Blocks.STONEBRICK, 1, 1), ItemStack(FBlocks.MOSSY_STONE_BRICK_WALL)),
        SimpleRecipe(ItemStack(Blocks.STONE, 1, 1), ItemStack(FBlocks.GRANITE_WALL)),
        SimpleRecipe(ItemStack(Blocks.STONE, 1, 1), ItemStack(Blocks.STONE, 1, 2)),
        SimpleRecipe(ItemStack(Blocks.STONE, 1, 3), ItemStack(FBlocks.DIORITE_WALL)),
        SimpleRecipe(ItemStack(Blocks.STONE, 1, 5), ItemStack(FBlocks.ANDESITE_WALL)),
        SimpleRecipe(ItemStack(Blocks.COBBLESTONE), ItemStack(Blocks.STONE_SLAB, 2, 3)),
        SimpleRecipe(ItemStack(Blocks.COBBLESTONE), ItemStack(Blocks.STONE_STAIRS)),
        SimpleRecipe(ItemStack(Blocks.COBBLESTONE), ItemStack(Blocks.COBBLESTONE_WALL)),
        SimpleRecipe(ItemStack(Blocks.MOSSY_COBBLESTONE), ItemStack(Blocks.COBBLESTONE_WALL)),
        SimpleRecipe(ItemStack(Blocks.SANDSTONE), ItemStack(Blocks.STONE_SLAB, 2, 1)),
        SimpleRecipe(ItemStack(Blocks.SANDSTONE), ItemStack(Blocks.SANDSTONE_STAIRS)),
        SimpleRecipe(ItemStack(Blocks.SANDSTONE), ItemStack(FBlocks.SANDSTONE_WALL)),
        SimpleRecipe(ItemStack(Blocks.SANDSTONE), ItemStack(Blocks.SANDSTONE, 1, 2)),
        SimpleRecipe(ItemStack(Blocks.SANDSTONE), ItemStack(Blocks.SANDSTONE, 1, 1)),
        SimpleRecipe(ItemStack(Blocks.RED_SANDSTONE), ItemStack(Blocks.STONE_SLAB2, 2)),
        SimpleRecipe(ItemStack(Blocks.RED_SANDSTONE), ItemStack(Blocks.RED_SANDSTONE_STAIRS)),
        SimpleRecipe(ItemStack(Blocks.RED_SANDSTONE), ItemStack(FBlocks.RED_SANDSTONE_WALL)),
        SimpleRecipe(ItemStack(Blocks.RED_SANDSTONE), ItemStack(Blocks.RED_SANDSTONE, 1, 2)),
        SimpleRecipe(ItemStack(Blocks.RED_SANDSTONE), ItemStack(Blocks.RED_SANDSTONE, 1, 1)),
        SimpleRecipe(ItemStack(Blocks.PRISMARINE), ItemStack(FBlocks.PRISMARINE_WALL)),
        SimpleRecipe(ItemStack(Blocks.QUARTZ_BLOCK), ItemStack(Blocks.STONE_SLAB, 1, 7)),
        SimpleRecipe(ItemStack(Blocks.QUARTZ_BLOCK), ItemStack(Blocks.QUARTZ_STAIRS)),
        SimpleRecipe(ItemStack(Blocks.QUARTZ_BLOCK), ItemStack(Blocks.QUARTZ_BLOCK, 1, 1)),
        SimpleRecipe(ItemStack(Blocks.QUARTZ_BLOCK), ItemStack(Blocks.QUARTZ_BLOCK, 1, 2)),
        SimpleRecipe(ItemStack(Blocks.PURPUR_BLOCK), ItemStack(Blocks.PURPUR_SLAB, 2)),
        SimpleRecipe(ItemStack(Blocks.PURPUR_BLOCK), ItemStack(Blocks.PURPUR_STAIRS)),
        SimpleRecipe(ItemStack(Blocks.PURPUR_BLOCK), ItemStack(Blocks.PURPUR_PILLAR)),
        SimpleRecipe(ItemStack(Blocks.BRICK_BLOCK), ItemStack(Blocks.STONE_SLAB, 2, 4)),
        SimpleRecipe(ItemStack(Blocks.BRICK_BLOCK), ItemStack(Blocks.BRICK_STAIRS)),
        SimpleRecipe(ItemStack(Blocks.NETHER_BRICK), ItemStack(Blocks.STONE_SLAB, 2, 6)),
        SimpleRecipe(ItemStack(Blocks.NETHER_BRICK), ItemStack(Blocks.NETHER_BRICK_STAIRS)),
        SimpleRecipe(ItemStack(Blocks.NETHER_BRICK), ItemStack(FBlocks.NETHER_BRICK_WALL)),
        SimpleRecipe(ItemStack(Blocks.RED_NETHER_BRICK), ItemStack(FBlocks.RED_NETHER_BRICK_WALL)),
        SimpleRecipe(ItemStack(Blocks.END_BRICKS), ItemStack(FBlocks.END_STONE_BRICK_WALL)),
        SimpleRecipe(ItemStack(Blocks.END_STONE), ItemStack(Blocks.END_BRICKS)),
        SimpleRecipe(ItemStack(Blocks.END_STONE), ItemStack(FBlocks.END_STONE_BRICK_WALL))
    )

    fun addRecipe(input: ItemStack, output: ItemStack) {
        recipes.add(SimpleRecipe(input, output))
    }

    fun removeRecipe(input: ItemStack, output: ItemStack) {
        recipes.removeIf { recipe -> recipe.matches(input, output) }
    }
}