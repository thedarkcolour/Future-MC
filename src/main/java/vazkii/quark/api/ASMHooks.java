package vazkii.quark.api;

import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public final class ASMHooks {
    // used in ASM
    public static BlockPistonStructureHelper transformStructureHelper(BlockPistonStructureHelper helper, World world, BlockPos sourcePos, EnumFacing facing, boolean extending) {
        return new QuarkPistonStructureHelper(helper, world, sourcePos, facing, extending);
    }
}
