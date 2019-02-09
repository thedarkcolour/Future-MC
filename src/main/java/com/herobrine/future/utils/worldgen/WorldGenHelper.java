package com.herobrine.future.utils.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenHelper {

    public static BlockPos getGroundPos(World world, int x, int z) {
        final BlockPos topPos = world.func_175645_m(new BlockPos(x, 0, z));
        if (topPos.func_177956_o() > 120) {
            return null;
        }

        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(topPos);

        IBlockState blockState = world.func_180495_p(pos);
        while (isAir(blockState, world, pos)) {
            pos.func_189536_c(EnumFacing.DOWN);
            if (pos.func_177956_o() < 31) {
                return null;
            }
            blockState = world.func_180495_p(pos);
        }
        return pos.func_177984_a();
    }

    public static boolean isAir(IBlockState blockState, World world, BlockPos pos) {
        Block block = blockState.func_177230_c();
        return block.isAir(blockState, world, pos);
    }
}
