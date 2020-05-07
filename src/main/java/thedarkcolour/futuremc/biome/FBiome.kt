package thedarkcolour.futuremc.biome

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import thedarkcolour.futuremc.biome.feature.Feature
import java.util.*

class FBiome(properties: BiomeProperties) : Biome(properties) {
    private val features = arrayListOf<Feature>()

    fun addFeature(feature: Feature) {

    }

    override fun decorate(worldIn: World, rand: Random, pos: BlockPos) {
        val x = rand.nextInt(16) + 8
        val z = rand.nextInt(16) + 8
        val pos1 = worldIn.getHeight(x, z)

    }
}