package thedarkcolour.futuremc.recipe.stonecutter

import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.ForgeRegistries
import thedarkcolour.futuremc.recipe.Recipes
import thedarkcolour.futuremc.registry.FBlocks
import java.util.*

object StonecutterRecipes : Recipes<StonecutterRecipe>() {
    override val recipes = ArrayList<StonecutterRecipe>()

    override fun addDefaults() {
        addRecipe(
            Blocks.STONE,
            ItemStack(Blocks.STONE_SLAB, 2),
            ItemStack(Blocks.STONEBRICK),
            ItemStack(Blocks.STONE_SLAB, 2, 5),
            ItemStack(Blocks.STONE_BRICK_STAIRS),
            ItemStack(FBlocks.STONE_BRICK_WALL),
            ItemStack(Blocks.STONEBRICK, 1, 3)
        )
        addRecipe(FBlocks.SMOOTH_STONE, ItemStack(Blocks.STONE_SLAB, 2, 0))
        addRecipe(
            Blocks.STONEBRICK,
            Blocks.STONE_BRICK_STAIRS,
            FBlocks.STONE_BRICK_WALL
        ).addOutput(ItemStack(Blocks.STONEBRICK, 1, 3))
        addRecipe(ItemStack(Blocks.STONEBRICK, 1, 1), ItemStack(FBlocks.MOSSY_STONE_WALL))
        addRecipe(ItemStack(Blocks.STONE, 1, 1)).addOutput(FBlocks.GRANITE_WALL)
            .addOutput(ItemStack(Blocks.STONE, 1, 2))
        addRecipe(ItemStack(Blocks.STONE, 1, 3)).addOutput(FBlocks.DIORITE_WALL)
        addRecipe(ItemStack(Blocks.STONE, 1, 5)).addOutput(FBlocks.ANDESITE_WALL)
        addRecipe(
            Blocks.COBBLESTONE,
            ItemStack(Blocks.STONE_SLAB, 2, 3),
            ItemStack(Blocks.STONE_STAIRS),
            ItemStack(Blocks.COBBLESTONE_WALL)
        )
        addRecipe(ItemStack(Blocks.MOSSY_COBBLESTONE)).addOutput(Blocks.COBBLESTONE_WALL)
        addRecipe(
            ItemStack(Blocks.SANDSTONE),
            ItemStack(Blocks.STONE_SLAB, 2, 1),
            ItemStack(Blocks.SANDSTONE_STAIRS),
            ItemStack(FBlocks.SANDSTONE_WALL),
            ItemStack(Blocks.SANDSTONE, 1, 2),
            ItemStack(Blocks.SANDSTONE, 1, 1)
        )
        addRecipe(
            ItemStack(Blocks.RED_SANDSTONE),
            ItemStack(Blocks.STONE_SLAB2, 2),
            ItemStack(Blocks.RED_SANDSTONE_STAIRS),
            ItemStack(FBlocks.RED_SANDSTONE_WALL),
            ItemStack(Blocks.RED_SANDSTONE, 1, 2),
            ItemStack(Blocks.RED_SANDSTONE, 1, 1)
        )
        addRecipe(ItemStack(Blocks.PRISMARINE)).addOutput(FBlocks.PRISMARINE_WALL)
        addRecipe(
            ItemStack(Blocks.QUARTZ_BLOCK),
            ItemStack(Blocks.STONE_SLAB, 1, 7),
            ItemStack(Blocks.QUARTZ_STAIRS),
            ItemStack(Blocks.QUARTZ_BLOCK, 1, 1),
            ItemStack(Blocks.QUARTZ_BLOCK, 1, 2)
        )
        addRecipe(
            Blocks.PURPUR_BLOCK,
            ItemStack(Blocks.PURPUR_SLAB, 2),
            ItemStack(Blocks.PURPUR_STAIRS),
            ItemStack(Blocks.PURPUR_PILLAR)
        )
        addRecipe(Blocks.BRICK_BLOCK, ItemStack(Blocks.STONE_SLAB, 2, 4)).addOutput(Blocks.BRICK_STAIRS)
        addRecipe(
            Blocks.NETHER_BRICK,
            ItemStack(Blocks.STONE_SLAB, 2, 6),
            ItemStack(Blocks.NETHER_BRICK_STAIRS),
            ItemStack(FBlocks.NETHER_BRICK_WALL)
        )
        addRecipe(Blocks.RED_NETHER_BRICK, FBlocks.RED_NETHER_BRICK_WALL)
        addRecipe(Blocks.END_STONE, Blocks.END_BRICKS, FBlocks.END_STONE_WALL)
        addRecipe(Blocks.END_BRICKS, FBlocks.END_STONE_WALL)
    }

    private fun addRecipe(input: ItemStack, vararg outputs: ItemStack): StonecutterRecipe {
        val recipe = StonecutterRecipe(input, *outputs)
        recipes.add(recipe)
        return recipe
    }

    private fun addRecipe(input: Block, vararg outputs: ItemStack): StonecutterRecipe {
        return addRecipe(ItemStack(input), *outputs)
    }

    private fun addRecipe(input: Block, vararg outputs: Block): StonecutterRecipe {
        val blocks: Array<ItemStack> =
            Arrays.stream(outputs).map(::ItemStack).toArray { value -> arrayOfNulls<ItemStack>(value) }
        return addRecipe(ItemStack(input), *blocks)
    }

    fun addOrCreateRecipe(input: ItemStack, vararg outputs: ItemStack) {
        val recipe = getRecipe(input)
        if (recipe != null) {
            for (output in outputs) {
                recipe.addOutput(output)
            }
        } else {
            addRecipe(input, *outputs)
        }
    }

    fun removeOutputs(input: ItemStack, vararg outputs: ItemStack) {
        getRecipe(input)?.let { recipe: StonecutterRecipe ->
            for (output in outputs) {
                recipe.removeOutput(output)
            }

            if (recipe.totalOutputs == 0) {
                recipes.remove(recipe)
            }
        }
    }

    override fun validate() {
        for (recipe in recipes) {
            val registry = ForgeRegistries.ITEMS
            for (output in recipe.outputs) {
                if (!registry.containsValue(output.item)) {
                    recipe.outputs.remove(output)
                }
            }

            if (recipe.outputs.isEmpty()) {
                recipes.remove(recipe)
            }
        }
    }
}