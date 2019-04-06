package com.herobrine.future.tile.advancedfurnace;

import com.herobrine.future.blocks.BlockFurnaceAdvanced;

public class TileSmoker extends TileAdvancedFurnace {
    public TileSmoker() {
        super(BlockFurnaceAdvanced.FurnaceType.SMOKER);
    }

    @Override
    public BlockFurnaceAdvanced.FurnaceType getType() {
        return type;
    }
}