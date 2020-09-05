package thedarkcolour.futuremc.recipe.crafting

import net.minecraft.init.Blocks
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.item.crafting.ShapedRecipes
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.oredict.OreIngredient
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.registry.FItems

/**
 * Custom recipe for crafting trapdoors.
 *
 * @author TheDarkColour
 */
class TrapdoorRecipe private constructor(ingredients: NonNullList<Ingredient>, result: ItemStack) : ShapedRecipes("", 3, 2, ingredients, result) {
    /**
     * Returns an Item that is the result of this recipe
     */
    override fun getCraftingResult(inv: InventoryCrafting): ItemStack {
        var lastMeta = -1

        for (item in inv.stackList) {
            if (!item.isEmpty) {
                if (lastMeta == -1) {
                    lastMeta = item.metadata
                }

                if (lastMeta != item.metadata) {
                    return OAK.copy()
                }
            }
        }

        return when (lastMeta) {
            1 -> SPRUCE.copy()
            2 -> BIRCH.copy()
            3 -> JUNGLE.copy()
            4 -> ACACIA.copy()
            5 -> DARK_OAK.copy()
            else -> OAK.copy()
        }
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    override fun matches(inv: InventoryCrafting, worldIn: World?): Boolean {
        val ore = OreIngredient("plankWood")
        var has1Row = false

        rowLoop@ for (row in 0..2) {
            for (col in 0..2) {
                if (!ore.test(inv.getStackInRowAndColumn(col, row))) {
                    has1Row = false
                    continue@rowLoop
                }
            }

            if (has1Row) {
                return true
            } else {
                has1Row = true
            }
        }

        return false
    }

    companion object {
        // In case a trapdoor is disabled
        private val OAK = ItemStack(Blocks.TRAPDOOR)
        private val SPRUCE = if (FConfig.villageAndPillage.newTrapdoors.spruce) ItemStack(FItems.SPRUCE_TRAPDOOR) else OAK
        private val BIRCH = if (FConfig.villageAndPillage.newTrapdoors.birch) ItemStack(FItems.BIRCH_TRAPDOOR) else OAK
        private val JUNGLE = if (FConfig.villageAndPillage.newTrapdoors.jungle) ItemStack(FItems.JUNGLE_TRAPDOOR) else OAK
        private val ACACIA = if (FConfig.villageAndPillage.newTrapdoors.acacia) ItemStack(FItems.ACACIA_TRAPDOOR) else OAK
        private val DARK_OAK = if (FConfig.villageAndPillage.newTrapdoors.darkOak) ItemStack(FItems.DARK_OAK_TRAPDOOR) else OAK

        /**
         * Create method for annoying ingredient list and stuff.
         */
        fun create(): TrapdoorRecipe {
            val ore = OreIngredient("plankWood")
            val list = NonNullList.create<Ingredient>()
            list.addAll(listOf(ore, ore, ore, ore, ore, ore))

            return TrapdoorRecipe(list, ItemStack(Blocks.TRAPDOOR))
        }
    }
}