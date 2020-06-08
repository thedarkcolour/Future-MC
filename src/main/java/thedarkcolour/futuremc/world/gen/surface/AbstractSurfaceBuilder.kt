package thedarkcolour.futuremc.world.gen.surface

import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import java.util.*

abstract class AbstractSurfaceBuilder {

    abstract fun buildSurface(worldIn: World, rand: Random, biome: Biome, x: Int, z: Int, height: Int, noise: Double)
}