package thedarkcolour.futuremc.world.gen.feature

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.world.DimensionType
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import net.minecraft.world.gen.feature.WorldGenMinable
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.registry.FBlocks
import java.util.*

object AncientDebrisWorldGen : FWorldGen {
    private var normalGen = WorldGenMinable(FBlocks.ANCIENT_DEBRIS.defaultState, 2, NetherBlocks)
    private var lapisStyleGen = WorldGenMinable(FBlocks.ANCIENT_DEBRIS.defaultState, 3, NetherBlocks)

    override fun generate(
        rand: Random,
        chunkX: Int,
        chunkZ: Int,
        worldIn: World,
        chunkGenerator: IChunkGenerator,
        chunkProvider: IChunkProvider
    ) {
        generateNormalOre(worldIn, rand, chunkX, chunkZ, FConfig.netherUpdate.ancientDebris.normalVein.count, FConfig.netherUpdate.ancientDebris.normalVein.minLevel, FConfig.netherUpdate.ancientDebris.normalVein.maxLevel) { w, r, p ->
            if (worldIn.provider.dimensionType == DimensionType.NETHER) {
                normalGen.generate(w, r, p)
            }
        }

        generateLapisStyleOre(worldIn, rand, chunkX, chunkZ, FConfig.netherUpdate.ancientDebris.lapisStyleVein.count, FConfig.netherUpdate.ancientDebris.lapisStyleVein.baseline, FConfig.netherUpdate.ancientDebris.lapisStyleVein.spread) { w, r, p ->
            if (worldIn.provider.dimensionType == DimensionType.NETHER) {
                lapisStyleGen.generate(w, r, p)
            }
        }
    }

    fun refresh() {
        normalGen = WorldGenMinable(FBlocks.ANCIENT_DEBRIS.defaultState, FConfig.netherUpdate.ancientDebris.normalVein.size, NetherBlocks)
        lapisStyleGen = WorldGenMinable(FBlocks.ANCIENT_DEBRIS.defaultState, 3, NetherBlocks)
    }

    object NetherBlocks : com.google.common.base.Predicate<IBlockState> {
        @JvmField
        @ObjectHolder("biomesoplenty:flesh")
        val flesh: Block? = null

        @JvmField
        @ObjectHolder("biomesoplenty:ash_block")
        val ashBlock: Block? = null

        override fun apply(input: IBlockState?): Boolean {
            val block = input?.block ?: return false

            return block == Blocks.NETHERRACK || block == Blocks.SOUL_SAND || block == flesh || block == ashBlock
        }
    }
}