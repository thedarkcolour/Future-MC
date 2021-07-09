package thedarkcolour.futuremc.recipe.crafting

import net.minecraft.init.Blocks
import net.minecraft.init.Bootstrap
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import java.util.*
import java.util.function.IntFunction

class TrapdoorRecipeTest {
    fun testGetCraftingResult() {
        // Register vanilla blocks and items
        Bootstrap.register()

        val inventory = InventoryCrafting(null, 3, 3)
        val recipe = TrapdoorRecipe.create()[0]

        // Default behaviour
        fillWithPlank(inventory, 0)

        //Assert.assertTrue(recipe.matches(inventory, null))
        //Assert.assertEquals(Item.getItemFromBlock(Blocks.TRAPDOOR), recipe.getCraftingResult(inventory).item)

        // Spruce behaviour
        fillWithPlank(inventory, 1)

        //Assert.assertTrue(recipe.matches(inventory, null))
        //Assert.assertEquals(FItems.SPRUCE_TRAPDOOR, recipe.getCraftingResult(inventory).item)
        //Assert.assertNotEquals(FItems.BIRCH_TRAPDOOR, recipe.getCraftingResult(inventory).item)

        // Oredict behaviour
        fillWithRandomPlanks(inventory)

        //Assert.assertTrue(recipe.matches(inventory, null))
        //Assert.assertEquals(Item.getItemFromBlock(Blocks.TRAPDOOR), recipe.getCraftingResult(inventory).item)

        // Mismatched behaviour
        fillItem(inventory, ItemStack.EMPTY)

        //Assert.assertFalse(recipe.matches(inventory, null))
    }

    private fun fillWithPlank(inv: InventoryCrafting, meta: Int) {
        val plank = ItemStack(Blocks.PLANKS, 1, meta)

        for (i in 0 until 6) {
            inv.stackList[i] = plank
        }
    }

    private fun fillWithRandomPlanks(inv: InventoryCrafting) {
        val rand = Random(inv.stackList[1].hashCode().toLong() * System.currentTimeMillis())

        fillItem(inv, IntFunction { index ->
            ItemStack(Blocks.PLANKS, 1, Random(Objects.hash(index, rand.nextInt()).toLong()).nextInt(5))
        })
    }

    private fun fillItem(inv: InventoryCrafting, item: ItemStack) {
        fillItem(inv, IntFunction {
            item
        })
    }

    private fun fillItem(inv: InventoryCrafting, item: IntFunction<ItemStack>) {
        for (i in 0 until 6) {
            inv.stackList[i] = item.apply(i)
        }
    }
}