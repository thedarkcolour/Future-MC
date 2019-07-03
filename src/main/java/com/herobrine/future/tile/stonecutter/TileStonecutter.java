package com.herobrine.future.tile.stonecutter;

import com.herobrine.future.FutureMC;
import com.herobrine.future.blocks.BlockStonecutter;
import com.herobrine.future.config.FutureConfig;
import net.minecraft.tileentity.TileEntity;
import org.apache.logging.log4j.Level;

import java.util.ConcurrentModificationException;

public class TileStonecutter extends TileEntity {// implements ITickable {
    private int updateTicks = 0;
    private boolean hasUpdated = false;

    public TileStonecutter() {
    }

    @Override
    public void onChunkUnload() {
        //this.updateTicks = 0;
        this.hasUpdated = false;
    }

    @Override
    public void onLoad() {
        if(!world.isBlockLoaded(pos) || world.isRemote) return;
        int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
        updateThis(meta);
    }



    private void updateThis(int meta) {
        BlockStonecutter block = (BlockStonecutter) world.getBlockState(pos).getBlock();

        if(FutureConfig.general.stonecutterOld && meta < 4) {
            try {
                world.setBlockState(pos, block.getStateFromMeta(meta + 4));
            } catch (ConcurrentModificationException e) {
                FutureMC.LOGGER.log(Level.ERROR, "Failed to modify old state");
            }
        }
        if(!FutureConfig.general.stonecutterOld && 3 < meta) {
            try {
                world.setBlockState(pos, block.getStateFromMeta(meta - 4));
            } catch (ConcurrentModificationException e) {
                FutureMC.LOGGER.log(Level.ERROR, "Failed to modify old state");
            }
        }
    }

    //@Override
    public void update() {
        if(updateTicks < 20) {
            updateTicks++;
        }
        else if(!this.hasUpdated){
            int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
            updateThis(meta);
            this.hasUpdated = true;
        }
    }
}