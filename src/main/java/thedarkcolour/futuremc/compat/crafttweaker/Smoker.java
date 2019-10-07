package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.block.BlockFurnaceAdvanced;

@ZenRegister
@ZenClass("mods.minecraftfuture.Smoker")
public final class Smoker {
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output) {
        if (!BlockFurnaceAdvanced.FurnaceType.SMOKER.canCraft(CraftTweakerMC.getItemStack(input))) {
            CraftTweakerAPI.apply(new AddRecipe(input, output));
        } else {
            FutureMC.logger.log(Level.WARN, "Tried to add duplicate valid Smoker input for " + input.getDefinition().getId());
        }
    }

    private static class AddRecipe implements IAction {
        private final ItemStack input;
        private final ItemStack output;

        private AddRecipe(IItemStack input, IItemStack output) {
            this.input = CraftTweakerMC.getItemStack(input);
            this.output = CraftTweakerMC.getItemStack(output);
        }

        @Override
        public void apply() {
            BlockFurnaceAdvanced.Recipes.smokerRecipe(input, output);
        }

        @Override
        public String describe() {
            return "Added " + input.getItem().getRegistryName() + " to smeltable items Smoker";
        }
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        CraftTweakerAPI.apply(new RemoveRecipe(input));
    }

    private static class RemoveRecipe implements IAction {
        private ItemStack input;

        private RemoveRecipe(IItemStack input) {
            this.input = CraftTweakerMC.getItemStack(input);
        }

        @Override
        public void apply() {
            BlockFurnaceAdvanced.Recipes.removeSmokerRecipe(input);
        }

        @Override
        public String describe() {
            return "Removed " + input.getItem().getRegistryName() + " from smeltable items Smoker";
        }
    }
}