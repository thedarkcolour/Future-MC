package com.herobrine.future.tile.advancedfurnace;

import com.herobrine.future.blocks.BlockFurnaceAdvanced;

public class TileBlastFurnace extends TileAdvancedFurnace {
    public TileBlastFurnace() {
        super(BlockFurnaceAdvanced.FurnaceType.BLAST_FURNACE);
    }

    @Override
    public BlockFurnaceAdvanced.FurnaceType getType() {
        return type;
    }
}