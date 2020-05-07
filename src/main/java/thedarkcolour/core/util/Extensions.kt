package thedarkcolour.core.util

import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockPos.MutableBlockPos
import net.minecraft.world.IBlockAccess

// A bunch of random extension functions that make life easier.


fun IBlockState.isAir(worldIn: IBlockAccess, pos: BlockPos): Boolean {
    return block.isAir(this, worldIn, pos)
}

fun IBlockAccess.isAir(pos: BlockPos): Boolean {
    return getBlockState(pos).isAir(this, pos)
}

fun MutableBlockPos.offset(x: Int, y: Int, z: Int): MutableBlockPos {
    return setPos(this.x + x, this.y + y, this.z + z)
}