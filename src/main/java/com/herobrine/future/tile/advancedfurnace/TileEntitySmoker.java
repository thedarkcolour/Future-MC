package com.herobrine.future.tile.advancedfurnace;

import com.herobrine.future.blocks.FurnaceAdvanced;

public class TileEntitySmoker extends TileEntityAdvancedFurnace {
    public TileEntitySmoker() {
        super(FurnaceAdvanced.FurnaceType.SMOKER);
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
