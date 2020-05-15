package thedarkcolour.core.util

import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockPos.MutableBlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
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

fun Vec3i.isInRange(other: Vec3d, distance: Double): Boolean {
    return distanceSq(other.x - 0.5, other.y - 0.5, other.z - 0.5) < distance * distance
}

fun Vec3i.manhattanDistance(other: BlockPos): Int {
    val f = other.x - x
    val f1 = other.y - y
    val f2 = other.z - z

    return f + f1 + f2
}