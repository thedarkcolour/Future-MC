package thedarkcolour.futuremc.world.test

import net.minecraft.world.World
import net.minecraft.world.WorldType
import net.minecraft.world.gen.IChunkGenerator

/**
 * Testing my world generation system.
 */
object FutureWorldType : WorldType("FMCWorld") {
    override fun getChunkGenerator(world: World, generatorOptions: String): IChunkGenerator {
        return super.getChunkGenerator(world, generatorOptions)
    }
}