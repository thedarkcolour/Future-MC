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
import thedarkcolour.futuremc.recipe.furnace.SmokerRecipes;

@ZenRegister
@ZenClass("mods.futuremc.Smoker")
public final class Smoker {
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output) {
        if (!BlockFurnaceAdvanced.FurnaceType.SMOKER.canCraft(CraftTweakerMC.getItemStack(input))) {
            CraftTweakerAPI.apply(new AddRecipe(input, output));
        } else {
            FutureMC.LOGGER.log(Level.WARN, "Tried to add duplicate valid Smoker input for " + input.getDefinition().getId());
        }
    }

    private static final class AddRecipe implements IAction {
        private final ItemStack input;
        private final ItemStack output;

        private AddRecipe(IItemStack input, IItemStack output) {
            this.input = CraftTweakerMC.getItemStack(input);
            this.input.setCount(1);
            this.output = CraftTweakerMC.getItemStack(output);
            this.output.setCount(1);
        }

        @Override
        public void apply() {
            SmokerRecipes.INSTANCE.addRecipe(input, output);
        }

        @Override
        public String describe() {
            return "Added " + input.getItem().getRegistryName() + " to smeltable item Smoker";
        }
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        CraftTweakerAPI.apply(new RemoveRecipe(input));
    }

    private static final class RemoveRecipe implements IAction {
        private final ItemStack input;

        private RemoveRecipe(IItemStack input) {
            this.input = CraftTweakerMC.getItemStack(input);
        }

        @Override
        public void apply() {
            SmokerRecipes.INSTANCE.removeRecipe(input);
        }

        @Override
        public String describe() {
            return "Removed " + input.getItem().getRegistryName() + " from smeltable item Smoker";
        }
    }

    @ZenMethod
    public static void clearRecipes() {
        SmokerRecipes.INSTANCE.clear();
    }
}