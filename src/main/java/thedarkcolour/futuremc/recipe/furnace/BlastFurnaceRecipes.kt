package thedarkcolour.futuremc.recipe.furnace

import net.minecraft.item.ItemStack
import thedarkcolour.futuremc.recipe.Recipes
import thedarkcolour.futuremc.recipe.SimpleRecipe
import java.util.*

object BlastFurnaceRecipes : Recipes<SimpleRecipe>() {
    override val recipes = object : ArrayList<SimpleRecipe>() {
        /**
         * Appends the specified element to the end of this list.
         *
         * @param e element to be appended to this list
         * @return <tt>true</tt> (as specified by [Collection.add])
         */
        override fun add(element: SimpleRecipe): Boolean {
            println(element)
            return super.add(element)
        }

        /**
         * Removes the first occurrence of the specified element from this list,
         * if it is present.  If the list does not contain the element, it is
         * unchanged.  More formally, removes the element with the lowest index
         * <tt>i</tt> such that
         * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
         * (if such an element exists).  Returns <tt>true</tt> if this list
         * contained the specified element (or equivalently, if this list
         * changed as a result of the call).
         *
         * @param o element to be removed from this list, if present
         * @return <tt>true</tt> if this list contained the specified element
         */
        override fun remove(element: SimpleRecipe): Boolean {
            println(element)
            return super.remove(element)
        }
    }

    fun addRecipe(input: ItemStack, output: ItemStack) {
        recipes.add(SimpleRecipe(input, output))
    }
}