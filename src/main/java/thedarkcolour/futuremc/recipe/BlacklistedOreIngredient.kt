package thedarkcolour.futuremc.recipe

import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntComparators
import it.unimi.dsi.fastutil.ints.IntList
import net.minecraft.client.util.RecipeItemHelper
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.NonNullList
import net.minecraftforge.oredict.OreDictionary
import javax.annotation.Nonnull

/**
 * Blacklisted ore ingredient. Has all items from an ore UNLESS
 * an item matches the [isBlacklistedOre] condition.
 */
class BlacklistedOreIngredient(ore: String, private val isBlacklistedOre: (ItemStack) -> Boolean) : Ingredient(0) {
    private val ores: NonNullList<ItemStack> = OreDictionary.getOres(ore)
    private var itemIds: IntList? = null
    private var array: Array<ItemStack>? = null
    private var lastSizeA = -1
    private var lastSizeL = -1

    @Nonnull
    override fun getMatchingStacks(): Array<ItemStack> {
        if (array == null || lastSizeA != ores.size) {
            val lst = NonNullList.create<ItemStack>()

            for (ore in ores) {
                if (ore.metadata == OreDictionary.WILDCARD_VALUE) {
                    val newList = NonNullList.create<ItemStack>()

                    ore.item.getSubItems(CreativeTabs.SEARCH, newList)

                    for (stack in newList) {
                        if (!isBlacklistedOre(stack)) {
                            lst.add(stack)
                        }
                    }
                } else if (!isBlacklistedOre(ore)) {
                    lst.add(ore)
                }
            }
            array = lst.toTypedArray()
            lastSizeA = ores.size
        }
        return array!!
    }

    @Nonnull
    override fun getValidItemStacksPacked(): IntList {
        if (itemIds == null || lastSizeL != ores.size) {
            itemIds = IntArrayList(ores.size)

            for (ore in ores) {
                if (ore.metadata == OreDictionary.WILDCARD_VALUE) {
                    val newList = NonNullList.create<ItemStack>()

                    ore.item.getSubItems(CreativeTabs.SEARCH, newList)

                    for (stack in newList) {
                        if (!isBlacklistedOre(stack)) {
                            itemIds!!.add(RecipeItemHelper.pack(stack))
                        }
                    }
                } else if (!isBlacklistedOre(ore)) {
                    itemIds!!.add(RecipeItemHelper.pack(ore))
                }
            }
            itemIds!!.sortWith(IntComparators.NATURAL_COMPARATOR)
            lastSizeA = ores.size
        }
        return itemIds!!
    }

    override fun apply(input: ItemStack?): Boolean {
        if (input == null || isBlacklistedOre(input)) {
            return false
        }

        for (target in ores) {
            if (OreDictionary.itemMatches(target!!, input, false)) {
                return true
            }
        }
        return false
    }

    override fun invalidate() {
        itemIds = null
        array = null
    }

    override fun isSimple() = true
}