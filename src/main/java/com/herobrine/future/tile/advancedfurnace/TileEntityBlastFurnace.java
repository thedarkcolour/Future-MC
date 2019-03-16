package com.herobrine.future.tile.advancedfurnace;

import com.herobrine.future.blocks.FurnaceAdvanced;

public class TileEntityBlastFurnace extends TileEntityAdvancedFurnace {
    public TileEntityBlastFurnace() {
        super(FurnaceAdvanced.FurnaceType.BLAST_FURNACE);
    }

    @Override
    public FurnaceAdvanced.FurnaceType getType() {
        return type;
    }

    @Override
    public void update() {
        fuelFunction(getType());
        updateState();
    }
}