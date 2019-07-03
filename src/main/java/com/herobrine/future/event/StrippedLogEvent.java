package com.herobrine.future.event;

import com.herobrine.future.FutureMC;
import com.herobrine.future.blocks.BlockStrippedLog;
import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.init.Init;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

@Mod.EventBusSubscriber
public class StrippedLogEvent {
    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void stripLogEvent(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = event.getItemStack();

        if (FutureConfig.general.strippedLogs) {
            if (stack.getItem() instanceof ItemTool) {
                ItemTool tool = (ItemTool) stack.getItem();
                if (tool.getToolClasses(stack).contains("axe")) {
                    IBlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();

                    if (block == Blocks.LOG || block == Blocks.LOG2) {
                        IProperty axis = null;
                        IProperty variant = null;
                        for (IProperty<?> prop : state.getPropertyKeys()) {
                            if (prop.getName().equals("axis")) {
                                axis = prop;
                            }
                            if (prop.getName().equals("variant")) {
                                variant = prop;
                            }
                        }
                        if (axis != null && variant != null) {
                            if (BlockStrippedLog.variants.contains(state.getValue(variant).toString())) {
                                player.swingArm(event.getHand());
                                world.playSound(player, pos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);

                                world.setBlockState(pos, getState(state, block).withProperty(axis, state.getValue(axis)));

                                stack.damageItem(1, player);
                            }
                        }
                    }
                }
            }
        }
    }

    public static IBlockState getState(IBlockState state, Block block) {
        String variant = null;
        if (block == Blocks.LOG) {
            variant = state.getValue(BlockOldLog.VARIANT).getName();
        }
        if (block == Blocks.LOG2) {
            variant = state.getValue(BlockNewLog.VARIANT).getName();
        }

        if (variant != null) {
            switch (variant) {
                case "acacia":
                    return Init.STRIPPED_ACACIA_LOG.getDefaultState();//.withProperty(BlockRotatedPillar.AXIS, state.getValue(BlockRotatedPillar.AXIS));
                case "jungle":
                    return Init.STRIPPED_JUNGLE_LOG.getDefaultState();//.withProperty(BlockRotatedPillar.AXIS, state.getValue(BlockRotatedPillar.AXIS));
                case "birch":
                    return Init.STRIPPED_BIRCH_LOG.getDefaultState();//.withProperty(BlockRotatedPillar.AXIS, state.getValue(BlockRotatedPillar.AXIS));
                case "oak":
                    return Init.STRIPPED_OAK_LOG.getDefaultState();//.withProperty(BlockRotatedPillar.AXIS, parseAxis(state.getProperties().get(BlockRotatedPillar.AXIS).toString()));
                case "spruce":
                    return Init.STRIPPED_SPRUCE_LOG.getDefaultState();//.withProperty(BlockRotatedPillar.AXIS, state.getValue(BlockRotatedPillar.AXIS));
                case "dark_oak":
                    return Init.STRIPPED_DARK_OAK_LOG.getDefaultState();//.withProperty(BlockRotatedPillar.AXIS, state.getValue(BlockRotatedPillar.AXIS));
            }
        }
        FutureMC.LOGGER.log(Level.ERROR, "Failed to find stripped log for event");
        return state;
    }
}