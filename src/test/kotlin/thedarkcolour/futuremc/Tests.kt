package thedarkcolour.futuremc

import thedarkcolour.futuremc.world.gen.structure.IChunkPos

fun main() {
    val x = 45
    val z = 34
    val x1 = -45
    val z1 = -34
    val pos = IChunkPos.of(x, z)
    val pos0 = IChunkPos.of(x, z)
    val pos1 = IChunkPos.of(x1, z1)

    assert(pos.x == x && pos.z == z)
    assert(pos1.x == x1 && pos1.z == z1)
    assert(pos == pos0)
}