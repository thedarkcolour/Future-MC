package com.herobrine.future.tile;

import net.minecraft.tileentity.TileEntity;

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
}