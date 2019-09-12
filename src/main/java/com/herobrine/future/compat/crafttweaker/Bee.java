package com.herobrine.future.compat.crafttweaker;

import com.herobrine.future.FutureMC;
import com.herobrine.future.block.BlockBerryBush;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.block.IBlockPattern;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockStem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.Level;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thedarkcolour.core.util.CheckedArrayList;

@ZenRegister
@ZenClass("mods.minecraftfuture.Bee")
public final class Bee {
    private static final CheckedArrayList<IBlock> FLOWERS = new CheckedArrayList<>(IBlockPattern::matches).addAll(
            CraftTweakerMC.getBlock(Blocks.RED_FLOWER, 0),
            CraftTweakerMC.getBlock(Blocks.RED_FLOWER, 1),
            CraftTweakerMC.getBlock(Blocks.RED_FLOWER, 2),
            CraftTweakerMC.getBlock(Blocks.RED_FLOWER, 3),
            CraftTweakerMC.getBlock(Blocks.RED_FLOWER, 4)
    );

    @ZenMethod
    public static void addFlower(IBlock block) {
        if(!FLOWERS.containsEquivalent(block)) {
            CraftTweakerAPI.apply((AddFlower)() -> block);
        } else {
            FutureMC.LOGGER.log(Level.ERROR, "Tried to add duplicate flower block to bee " + block.getDefinition().getId() + ":" + block.getMeta());
        }
    }

    public static boolean isFlowerValid(IBlockState state) {
        Block block = state.getBlock();
        int meta = block.getMetaFromState(state);

        if(block == Blocks.AIR || meta > 15)  {
            return false;
        }

        return Loader.isModLoaded("crafttweaker") ? FLOWERS.containsEquivalent(CraftTweakerMC.getBlock(block, meta)) :
                block instanceof BlockFlower || block instanceof com.herobrine.future.block.BlockFlower;
    }

    public static boolean canGrowBlock(Block block) {
        return block instanceof BlockCrops || block instanceof BlockStem || block instanceof BlockBerryBush;
    }

    private interface AddFlower extends IAction {
        @Override
        default void apply() {
            FLOWERS.add(getBlock());
        }

        @Override
        default String describe() {
            return "Added valid flower for bee pollination";
        }

        IBlock getBlock();
    }

    @ZenMethod
    public static void removeFlower(IBlock block) {
        if(FLOWERS.containsEquivalent(block)) {
            CraftTweakerAPI.apply((RemoveFlower)() -> block);
        } else {
            FutureMC.LOGGER.log(Level.ERROR, "Tried to remove non pollinate-able flower block to bee " + block.getDefinition().getId() + ":" + block.getMeta());
        }
    }

    private interface RemoveFlower extends IAction {
        @Override
        default void apply() {
            FLOWERS.removeEquivalent(getBlock());
        }

        @Override
        default String describe() {
            return "Removed a valid flower for bee pollination";
        }

        IBlock getBlock();
    }
}