package com.herobrine.future.tile.campfire;

import com.herobrine.future.MainFuture;
import com.herobrine.future.blocks.BlockCampfire;
import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.init.Init;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import org.apache.logging.log4j.Level;

import java.util.ConcurrentModificationException;

public class TileCampfire extends TileEntity implements ITickable {
    private int updateTicks = 0;
    private boolean hasUpdated = false;

    public TileCampfire() {
    }

    @Override
    public void onChunkUnload() {
        this.updateTicks = 0;
        this.hasUpdated = false;
    }

    private void updateThis(int meta) {
        BlockCampfire block = (BlockCampfire) world.getBlockState(pos).getBlock();

        if(FutureConfig.general.oldCampfire && meta < 2) {
            try {
                world.setBlockState(pos, block.getStateFromMeta(meta + 2));
            } catch (ConcurrentModificationException e) {
                MainFuture.logger.log(Level.ERROR, "Failed to modify old state");
            }
        }
        if(!FutureConfig.general.oldCampfire && meta > 1) {
            try {
                world.setBlockState(pos, block.getStateFromMeta(meta - 2));
            } catch (ConcurrentModificationException e) {
                MainFuture.logger.log(Level.ERROR, "Failed to modify old state");
            }
        }
    }

    @Override
    public void update() {
        if(updateTicks < 20) {
            updateTicks++;
        }
        else if(!this.hasUpdated){
            int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
            updateThis(meta);
            this.hasUpdated = true;
        }
        if(FutureConfig.general.campfireBurnsOutInRain && world.isRaining() && world.canSeeSky(pos) && world.getBiome(pos).canRain() && world.getBlockState(pos).getValue(BlockCampfire.LIT)) {
            world.setBlockState(pos, Init.CAMPFIRE.getBlockState().getBaseState().withProperty(BlockCampfire.LIT, false).withProperty(BlockCampfire.NEW, world.getBlockState(pos).getValue(BlockCampfire.NEW)));
        }
    }
}