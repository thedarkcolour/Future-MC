package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.minecraft.CraftTweakerMC;
import org.apache.logging.log4j.Level;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.entity.bee.BeeEntity;

@ZenRegister
@ZenClass("mods.futuremc.Bee")
public final class Bee {
    @ZenMethod
    public static void addFlower(IBlockState block) {
        net.minecraft.block.state.IBlockState state = CraftTweakerMC.getBlockState(block);
        if (!BeeEntity.FLOWERS.contains(state)) {
            CraftTweakerAPI.apply(AddFlower.of(() -> BeeEntity.FLOWERS.add(state)));
        } else {
            FutureMC.LOGGER.log(Level.WARN, "Tried to add duplicate flower block to bee: " + state);
        }
    }

    private interface AddFlower extends IAction {
        @Override
        void apply();

        /**
         * Get rid of ugly casting
         * @param runnable the remove action
         * @return the remove action
         */
        static IAction of(AddFlower runnable) {
            return runnable;
        }

        @Override
        default String describe() {
            return "Added valid flower for bee pollination";
        }
    }

    @ZenMethod
    public static void removeFlower(IBlockState block) {
        net.minecraft.block.state.IBlockState state = CraftTweakerMC.getBlockState(block);

        if (BeeEntity.FLOWERS.contains(state)) {
            CraftTweakerAPI.apply(RemoveFlower.of(() -> BeeEntity.FLOWERS.remove(state)));
        } else {
            FutureMC.LOGGER.log(Level.WARN, "Tried to remove non pollinateable flower block to bee " + state);
        }
    }

    private interface RemoveFlower extends IAction {
        @Override
        void apply();

        /**
         * Get rid of ugly casting
         * @param runnable the remove action
         * @return the remove action
         */
        static IAction of(RemoveFlower runnable) {
            return runnable;
        }

        @Override
        default String describe() {
            return "Removed a valid flower for bee pollination";
        }
    }

    /**
     * Clears all flowers added up to the point this method is called.
     */
    @ZenMethod
    public static void clearValidFlowers() {
        CraftTweakerAPI.apply(new IAction() {
            @Override
            public void apply() {
                BeeEntity.FLOWERS.clear();
            }

            @Override
            public String describe() {
                return "Cleared pollinateable flowers";
            }
        });
    }
}