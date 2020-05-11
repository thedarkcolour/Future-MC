package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.futuremc.block.BlockFurnaceAdvanced;
import thedarkcolour.futuremc.recipe.furnace.BlastFurnaceRecipes;

@ZenRegister
@ZenClass("mods.futuremc.BlastFurnace")
public final class BlastFurnace {
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output) {
        ItemStack inputStack = CraftTweakerMC.getItemStack(input);
        inputStack.setCount(1);
        ItemStack outputStack = CraftTweakerMC.getItemStack(output);
        outputStack.setCount(1);

        if (!BlockFurnaceAdvanced.FurnaceType.BLAST_FURNACE.canCraft(inputStack)) {
            CraftTweakerAPI.apply(new IAction() {
                @Override
                public void apply() {
                    BlastFurnaceRecipes.INSTANCE.addRecipe(inputStack, outputStack);
                }

                @Override
                public String describe() {
                    return "Adding Blast Furnace recipe (input: " + inputStack.toString() + ") -> (output: " + outputStack.toString() + ")";
                }
            });
        } else {
            System.out.println("Tried to add duplicate valid BlastFurnace input for " + inputStack.toString());
        }
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        CraftTweakerAPI.apply(new RemoveRecipe(input));
    }

    private static final class RemoveRecipe implements IAction {
        private ItemStack input;

        private RemoveRecipe(IItemStack input) {
            this.input = CraftTweakerMC.getItemStack(input);
        }

        @Override
        public void apply() {
            BlastFurnaceRecipes.INSTANCE.removeRecipe(input);
        }

        @Override
        public String describe() {
            return "Removed " + input.getItem().getRegistryName() + " from smeltable item BlastFurnace";
        }
    }

    @ZenMethod
    public static void clearRecipes() {
        BlastFurnaceRecipes.INSTANCE.clear();
    }
}