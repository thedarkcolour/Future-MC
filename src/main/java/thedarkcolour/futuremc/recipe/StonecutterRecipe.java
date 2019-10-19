package thedarkcolour.futuremc.recipe;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import thedarkcolour.core.util.Util;

import java.util.List;

public class StonecutterRecipe {
    private final ItemStack input;
    private final List<ItemStack> outputs;

    public StonecutterRecipe(ItemStack input, ItemStack... outputs) {
        this.input = input;
        this.outputs = Lists.newArrayList(outputs);
    }

    public ItemStack getInput() {
        return input.copy();
    }

    public boolean matches(ItemStack input) {
        return this.input.isItemEqual(input);
    }

    public ItemStack getOutput(int index) {
        return outputs.get(index).copy();
    }

    public List<ItemStack> getOutputs() {
        return Util.make(Lists.newArrayList(), list -> outputs.forEach(stack -> list.add(stack.copy())));
    }

    public int getTotalOutputs() {
        return outputs.size();
    }

    public StonecutterRecipe addOutput(ItemStack output) {
        outputs.add(output);
        return this;
    }

    public StonecutterRecipe addOutput(Block block) {
        return addOutput(new ItemStack(block));
    }

    public void removeOutput(ItemStack output) {
        outputs.remove(output);
    }
}