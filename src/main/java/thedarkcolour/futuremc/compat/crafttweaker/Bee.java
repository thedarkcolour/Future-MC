package thedarkcolour.futuremc.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.block.IBlockPattern;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.init.Blocks;
import org.apache.logging.log4j.Level;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.core.util.PredicateArrayList;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.init.Init;

@ZenRegister
@ZenClass("mods.minecraftfuture.Bee")
public final class Bee {
    public static final PredicateArrayList<IBlock> FLOWERS = new PredicateArrayList<>(IBlockPattern::matches).addAll(
            CraftTweakerMC.getBlock(Blocks.YELLOW_FLOWER, 0),
            CraftTweakerMC.getBlock(Blocks.RED_FLOWER, 0),
            CraftTweakerMC.getBlock(Blocks.RED_FLOWER, 1),
            CraftTweakerMC.getBlock(Blocks.RED_FLOWER, 2),
            CraftTweakerMC.getBlock(Blocks.RED_FLOWER, 3),
            CraftTweakerMC.getBlock(Blocks.RED_FLOWER, 4),
            CraftTweakerMC.getBlock(Blocks.RED_FLOWER, 5),
            CraftTweakerMC.getBlock(Blocks.RED_FLOWER, 6),
            CraftTweakerMC.getBlock(Blocks.RED_FLOWER, 7),
            CraftTweakerMC.getBlock(Blocks.RED_FLOWER, 8),
            CraftTweakerMC.getBlock(Init.CORNFLOWER, 0),
            CraftTweakerMC.getBlock(Init.LILY_OF_VALLEY, 0)
    );

    @ZenMethod
    public static void addFlower(IBlock block) {
        if(!FLOWERS.containsEquivalent(block)) {
            CraftTweakerAPI.apply((AddFlower)() -> FLOWERS.add(block));
        } else {
            FutureMC.logger.log(Level.ERROR, "Tried to add duplicate flower block to bee " + block.getDefinition().getId() + ":" + block.getMeta());
        }
    }

    private interface AddFlower extends IAction {
        @Override
        void apply();

        @Override
        default String describe() {
            return "Added valid flower for bee pollination";
        }
    }

    @ZenMethod
    public static void removeFlower(IBlock block) {
        if(FLOWERS.containsEquivalent(block)) {
            CraftTweakerAPI.apply((RemoveFlower)() -> FLOWERS.add(block));
        } else {
            FutureMC.logger.log(Level.ERROR, "Tried to remove non pollinate-able flower block to bee " + block.getDefinition().getId() + ":" + block.getMeta());
        }
    }

    private interface RemoveFlower extends IAction {
        @Override
        void apply();

        @Override
        default String describe() {
            return "Removed a valid flower for bee pollination";
        }
    }
}