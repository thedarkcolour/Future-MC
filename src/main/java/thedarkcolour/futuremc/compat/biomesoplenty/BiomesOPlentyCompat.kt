package thedarkcolour.futuremc.compat.biomesoplenty

import net.minecraft.block.BlockLeaves
import net.minecraft.block.BlockLog
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.util.isAir
import thedarkcolour.futuremc.world.gen.feature.BeeNestGenerator

object BiomesOPlentyCompat {
    @JvmStatic
    fun placeBeehive(world: World, pos: BlockPos): Boolean {
        val random = world.rand

        if (BeeNestGenerator.fastCannotGenerate(world, random, pos))
            return false

        val rotationDir = random.nextInt(3)
        val dir = BeeNestGenerator.VALID_OFFSETS[rotationDir]
        val sidePos = pos.offset(dir)
        // on the side of the log
        val worldState = world.getBlockState(sidePos)

        val aboveState = world.getBlockState(sidePos.up()).block
        if (worldState.block.isReplaceable(world, sidePos) && world.isAir(sidePos.down()) && (aboveState is BlockLeaves || aboveState is BlockLog)) {
            BeeNestGenerator.placeBeeHive(world, random, sidePos)
            return true
        }

        return false
    }
}