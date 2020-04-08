package vazkii.quark.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface INonSticky {

    boolean canStickToBlock(World world, BlockPos pistonPos, BlockPos pos, BlockPos slimePos, IBlockState state, IBlockState slimeState, EnumFacing direction);

}
