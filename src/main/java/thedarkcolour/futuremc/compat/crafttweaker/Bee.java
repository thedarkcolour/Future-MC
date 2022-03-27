package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.minecraft.CraftTweakerMC;
import org.apache.logging.log4j.Level;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.entity.bee.EntityBee;

@ZenRegister
@ZenClass("mods.futuremc.Bee")
public final class Bee {
    @ZenMethod
    public static void addFlower(IBlockState block) {
        net.minecraft.block.state.IBlockState state = CraftTweakerMC.getBlockState(block);

        if (!EntityBee.FLOWERS.contains(state)) {
            CraftTweakerAPI.apply(new RecipeUtil.NamedAction("Added flower as bee pollination target", () -> EntityBee.FLOWERS.add(state)));
        } else {
            FutureMC.LOGGER.log(Level.WARN, "Tried to add duplicate flower block to bee: " + state);
        }
    }

    @ZenMethod
    public static void removeFlower(IBlockState block) {
        net.minecraft.block.state.IBlockState state = CraftTweakerMC.getBlockState(block);

        if (EntityBee.FLOWERS.contains(state)) {
            CraftTweakerAPI.apply(new RecipeUtil.NamedAction("Removed flower from bee pollination targets", () -> EntityBee.FLOWERS.remove(state)));
        } else {
            FutureMC.LOGGER.log(Level.WARN, "Tried to remove non pollinateable flower block to bee " + state);
        }
    }

    /**
     * Clears all flowers added up to the point this method is called.
     */
    @ZenMethod
    public static void clearValidFlowers() {
        CraftTweakerAPI.apply(new RecipeUtil.NamedAction("Cleared bee pollination targets", EntityBee.FLOWERS::clear));
    }
}