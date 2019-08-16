package com.herobrine.future.block;

import com.herobrine.future.FutureMC;
import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;

public class BlockBase extends Block {
    public BlockBase(BlockProperties properties) {
        super(properties.material());
        setUnlocalizedName(FutureMC.ID + "." + properties.registryName());
        setRegistryName(properties.registryName());
        setSoundType(properties.soundType());
        setHardness(3.0F);
    }

    /**
     * Method that returns a proper AABB from block pixel coordinates. Makes AABBs much easier
     */
    public static AxisAlignedBB makeAABB(double startX, double startY, double startZ, double endX, double endY, double endZ) {
        return new AxisAlignedBB(startX / 16D, startY / 16D, startZ / 16D, endX / 16D, endY / 16D, endZ / 16D);
    }

    // Reminder - Block destroy breaking happens at Block.harvestBlock and uses Block.spawnAsEntity(world, pos, stack) to spawn drops
}