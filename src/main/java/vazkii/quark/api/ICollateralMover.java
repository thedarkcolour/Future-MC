package vazkii.quark.api;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ICollateralMover {

    default boolean isCollateralMover(World world, BlockPos source, EnumFacing moveDirection, BlockPos pos) {
        return true;
    }

    MoveResult getCollateralMovement(World world, BlockPos source, EnumFacing moveDirection, EnumFacing side, BlockPos pos);

    public static enum MoveResult {

        MOVE,
        BREAK,
        SKIP,
        PREVENT

    }
}
