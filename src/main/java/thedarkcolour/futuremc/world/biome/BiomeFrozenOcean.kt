@file:Suppress("PackageDirectoryMismatch")

// not exactly sure why i needed protected access...
// check this later
package net.minecraft.world.biome

import net.minecraft.entity.monster.EntityPolarBear
import net.minecraft.util.math.BlockPos.MutableBlockPos
import net.minecraft.world.World
import net.minecraft.world.chunk.ChunkPrimer
import net.minecraft.world.gen.NoiseGeneratorPerlin
import java.util.*

// TODO
class BiomeFrozenOcean : Biome(BiomeProperties("FrozenOcean").setBaseHeight(-1F).setHeightVariation(0.1F).setTemperature(0F).setRainfall(0.5F)) {
    private var worldSeed: Long = 0
    private var noise: NoiseGeneratorPerlin? = null
    private var roofNoise: NoiseGeneratorPerlin? = null

    init {
        spawnableCreatureList.clear()
        spawnableCreatureList.add(SpawnListEntry(EntityPolarBear::class.java, 1, 1, 2))
    }

    override fun genTerrainBlocks(
        worldIn: World,
        rand: Random,
        chunkPrimerIn: ChunkPrimer,
        x: Int,
        z: Int,
        noiseVal: Double
    ) {
        if (worldSeed != worldIn.seed || noise == null || roofNoise == null) {
            val random = Random(worldSeed)
            noise = NoiseGeneratorPerlin(random, 4)
            roofNoise = NoiseGeneratorPerlin(random, 1)
        }

        worldSeed = worldIn.seed

        var d0 = 0.0
        var d1 = 0.0
        val mutableBlockPos = MutableBlockPos(x, 63, z)
        val f = getTemperature(mutableBlockPos)
    }
}