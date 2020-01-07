package thedarkcolour.futuremc.recipe.stonecutter

import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import thedarkcolour.core.util.PredicateArrayList
import thedarkcolour.core.util.predicateArrayListOf
import thedarkcolour.futuremc.recipe.Recipe

class StonecutterRecipe(override val input: ItemStack, vararg outputs: ItemStack) : Recipe() {
    private val outputs: PredicateArrayList<ItemStack>
    val totalOutputs: Int
        get() = outputs.size

    init {
        this.outputs = predicateArrayListOf(outputs) { obj: ItemStack, other: ItemStack ->
            obj.isItemEqual(other)
        }
    }

    fun getOutput(index: Int): ItemStack {
        return outputs[index].copy()
    }

    fun addOutput(output: ItemStack): StonecutterRecipe {
        outputs.add(output)
        return this
    }

    fun addOutput(block: Block): StonecutterRecipe {
        return addOutput(ItemStack(block))
    }

    fun removeOutput(output: ItemStack) {
        outputs.removeEquivalent(output)
    }
}