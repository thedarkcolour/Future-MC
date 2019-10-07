package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import org.apache.logging.log4j.Level;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.tile.TileCampfire;

@ZenRegister
@ZenClass("mods.minecraftfuture.Campfire")
public final class Campfire {
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output, int duration) {
        if (TileCampfire.Recipes.getRecipe(CraftTweakerMC.getItemStack(input)) == null) {
            CraftTweakerAPI.apply(new AddRecipe(input, output, duration));
        } else {
            FutureMC.logger.log(Level.WARN, "Failed to add duplicate recipe for " + input.getDefinition().getId());
        }
    }

    private static class AddRecipe implements IAction {
        private final IItemStack input;
        private final IItemStack output;
        private final int duration;
        //private final int experience; Seems to not be given to the player

        private AddRecipe(IItemStack input, IItemStack output, int duration) {
            this.input = input;
            this.output = output;
            this.duration = duration;
            //this.experience = experience;
        }

        @Override
        public void apply() {
            TileCampfire.Recipes.recipe(CraftTweakerMC.getItemStack(input), CraftTweakerMC.getItemStack(output), duration);
        }

        @Override
        public String describe() {
            return "Added recipe for" + input.getDefinition().getId();
        }
    }

    @ZenMethod
    public static void removeRecipe(IItemStack stack) {
        if (TileCampfire.Recipes.getRecipe(CraftTweakerMC.getItemStack(stack)) != null) {
            CraftTweakerAPI.apply(new RemoveRecipe(stack));
        }
    }

    private static class RemoveRecipe implements IAction {
        private final IItemStack input;

        private RemoveRecipe(IItemStack input) {
            this.input = input;
        }

        @Override
        public void apply() {
            TileCampfire.Recipes.remove(CraftTweakerMC.getItemStack(input));
        }

        @Override
        public String describe() {
            return "Added recipe for" + input.getDefinition().getId();
        }
    }
}