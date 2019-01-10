package com.herobrine.future.utils.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenHelper {

    public static BlockPos getGroundPos(World world, int x, int z) {
        final BlockPos topPos = world.getHeight(new BlockPos(x, 0, z));
        if (topPos.getY() > 120) {
            return null;
        }

        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(topPos);

        IBlockState blockState = world.getBlockState(pos);
        while (isAir(blockState, world, pos)) {
            pos.move(EnumFacing.DOWN);
            if (pos.getY() < 31) {
                return null;
            }
            blockState = world.getBlockState(pos);
        }
        return pos.up();
    }

    public static boolean isAir(IBlockState blockState, World world, BlockPos pos) {
        Block block = blockState.getBlock();
        return block.isAir(blockState, world, pos);
    }
}