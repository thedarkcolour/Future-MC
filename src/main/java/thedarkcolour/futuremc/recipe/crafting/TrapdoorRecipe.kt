package thedarkcolour.futuremc.recipe.crafting

import net.minecraft.init.Blocks
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.Ingredient
import net.minecraft.item.crafting.ShapedRecipes
import net.minecraft.util.NonNullList
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

    companion object {
        // In case a trapdoor is disabled
        private val OAK = ItemStack(Blocks.TRAPDOOR, 2)
        private val SPRUCE = if (FConfig.villageAndPillage.newTrapdoors.spruce) ItemStack(FItems.SPRUCE_TRAPDOOR, 2) else OAK
        private val BIRCH = if (FConfig.villageAndPillage.newTrapdoors.birch) ItemStack(FItems.BIRCH_TRAPDOOR, 2) else OAK
        private val JUNGLE = if (FConfig.villageAndPillage.newTrapdoors.jungle) ItemStack(FItems.JUNGLE_TRAPDOOR, 2) else OAK
        private val ACACIA = if (FConfig.villageAndPillage.newTrapdoors.acacia) ItemStack(FItems.ACACIA_TRAPDOOR, 2) else OAK
        private val DARK_OAK = if (FConfig.villageAndPillage.newTrapdoors.darkOak) ItemStack(FItems.DARK_OAK_TRAPDOOR, 2) else OAK

        /**
         * Create method for annoying ingredient list and stuff.
         */
        fun create(): List<IRecipe> {
            val ore = OreIngredient("plankWood")
            val spruce = Ingredient.fromStacks(ItemStack(Blocks.PLANKS, 1, 1))
            val birch = Ingredient.fromStacks(ItemStack(Blocks.PLANKS, 1, 2))
            val jungle = Ingredient.fromStacks(ItemStack(Blocks.PLANKS, 1, 3))
            val acacia = Ingredient.fromStacks(ItemStack(Blocks.PLANKS, 1, 4))
            val darkOak = Ingredient.fromStacks(ItemStack(Blocks.PLANKS, 1, 5))
            val list = NonNullList.create<Ingredient>()

            for (i in 0 until 6) {
                list.add(ore)
            }

            val spruceBool = FConfig.villageAndPillage.newTrapdoors.spruce
            val birchBool = FConfig.villageAndPillage.newTrapdoors.birch
            val jungleBool = FConfig.villageAndPillage.newTrapdoors.jungle
            val acaciaBool = FConfig.villageAndPillage.newTrapdoors.acacia
            val darkOakBool = FConfig.villageAndPillage.newTrapdoors.darkOak

            val recipeList = arrayListOf<IRecipe>()

            if (spruceBool || birchBool || jungleBool || acaciaBool || darkOakBool) {
                recipeList.add(TrapdoorRecipe(list, OAK).setRegistryName("futuremc:oak_wooden_trapdoor"))
            } else {
                // skip ahead if all of em are disable
                return emptyList()
            }
            if (spruceBool) {
                recipeList.add(TrapdoorRecipe(NonNullList.create<Ingredient>().also { it.addAll(listOf(spruce, spruce, spruce, spruce, spruce, spruce)) }, SPRUCE).setRegistryName("futuremc:spruce_wooden_trapdoor"))
            }
            if (birchBool) {
                recipeList.add(TrapdoorRecipe(NonNullList.create<Ingredient>().also { it.addAll(listOf(birch, birch, birch, birch, birch, birch)) }, BIRCH).setRegistryName("futuremc:birch_wooden_trapdoor"))
            }
            if (jungleBool) {
                recipeList.add(TrapdoorRecipe(NonNullList.create<Ingredient>().also { it.addAll(listOf(jungle, jungle, jungle, jungle, jungle, jungle)) }, JUNGLE).setRegistryName("futuremc:jungle_wooden_trapdoor"))
            }
            if (acaciaBool) {
                recipeList.add(TrapdoorRecipe(NonNullList.create<Ingredient>().also { it.addAll(listOf(acacia, acacia, acacia, acacia, acacia, acacia)) }, ACACIA).setRegistryName("futuremc:acacia_wooden_trapdoor"))
            }
            if (darkOakBool) {
                recipeList.add(TrapdoorRecipe(NonNullList.create<Ingredient>().also { it.addAll(listOf(darkOak, darkOak, darkOak, darkOak, darkOak, darkOak)) }, DARK_OAK).setRegistryName("futuremc:dark_oak_wooden_trapdoor"))
            }

            return recipeList
        }
    }
}