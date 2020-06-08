package thedarkcolour.futuremc.world.gen.feature

import it.unimi.dsi.fastutil.objects.Object2DoubleMap
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockPos.MutableBlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeDecorator
import net.minecraft.world.gen.feature.WorldGenAbstractTree
import net.minecraftforge.fml.common.registry.ForgeRegistries
import sun.reflect.Reflection
import thedarkcolour.core.util.getDoubleOrDefault
import thedarkcolour.core.util.isAir
import thedarkcolour.core.util.offset
import thedarkcolour.futuremc.block.BeeHiveBlock
import thedarkcolour.futuremc.config.FConfig.buzzyBees
import thedarkcolour.futuremc.entity.bee.EntityBee
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.tile.BeeHiveTile
import java.util.*

object BeeNestGenerator {
    val BEE_NEST =
        FBlocks.BEE_NEST.defaultState.withProperty(BeeHiveBlock.FACING, EnumFacing.SOUTH)
    val VALID_OFFSETS =
        arrayOf(EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST)
    val BIOMES_AND_CHANCES: Object2DoubleMap<Biome?> = Object2DoubleOpenHashMap()

    /**
     * Called with ASM.
     *
     * @see thedarkcolour.futuremc.asm.CoreTransformer
     */
    @JvmStatic
    fun generateBeeNestsForSmallTrees(
        worldIn: World,
        rand: Random,
        position: BlockPos,
        height: Int
    ) {
        if (cannotGenerate(worldIn, rand, position)) return

        val offset = VALID_OFFSETS[rand.nextInt(3)]
        val pos = MutableBlockPos(position.x, position.y + height - 4, position.z).move(offset)
        if (worldIn.isAir(pos) && worldIn.isAir(pos.move(EnumFacing.SOUTH))) {
            placeBeeHive(worldIn, rand, pos.move(EnumFacing.NORTH))
        }
    }

    /**
     * Called with ASM.
     *
     * @see thedarkcolour.futuremc.asm.CoreTransformer
     */
    @JvmStatic
    fun generateBeeNestsForBigTrees(
        worldIn: World,
        rand: Random,
        position: BlockPos,
        height: Int,
        tree: WorldGenAbstractTree
    ) {
        if (cannotGenerate(worldIn, rand, position)) return

        val offset = VALID_OFFSETS[rand.nextInt(3)]
        // base trunk position
        val pos = MutableBlockPos(position.offset(offset))
        for (i in 0 until height) {
            // move pos in this air check
            if (worldIn.isAir(pos.offset(0, 1, 0))) {
                continue
            }

            // move back down when checking
            // see core.util.Extensions.kt
            // first down, then south
            if (tree.isReplaceable(worldIn, pos.offset(0, -1, 0)) && tree.isReplaceable(worldIn, pos.offset(0, 0, 1))) {
                // set to air at the front position
                worldIn.setBlockToAir(pos)

                // move back north to bee hive position
                placeBeeHive(worldIn, rand, pos.offset(0, 0, -1))
            }
        }
    }

    private fun cannotGenerate(worldIn: World, rand: Random, pos: BlockPos): Boolean {
        if (!buzzyBees.bee.enabled) {
            return true
        }
        if (Reflection.getCallerClass(4) != BiomeDecorator::class.java && hasNoFlowersNearby(worldIn, pos)) {
            return true
        }
        val biome = worldIn.getBiome(pos)
        if (rand.nextDouble() > getDoubleOrDefault(BIOMES_AND_CHANCES, biome, 0.0)) {
            return true
        }
        return false
    }

    private fun hasNoFlowersNearby(worldIn: World, pos: BlockPos): Boolean {
        val startX = pos.x - 2
        val startY = pos.x - 1
        val startZ = pos.z - 2
        val endX   = pos.x + 2
        val endY   = pos.x + 1
        val endZ   = pos.z + 2

        if (worldIn.isAreaLoaded(startX, startY, startZ, endX, endY, endZ, true)) {
            for (pos1 in BlockPos.getAllInBoxMutable(startX, startY, startZ, endX, endY, endZ)) {
                if (EntityBee.isFlowerValid(worldIn.getBlockState(pos1))) {
                    return false
                }
            }
        }

        return true
    }

    private fun placeBeeHive(worldIn: World, rand: Random, pos: MutableBlockPos) {
        worldIn.setBlockState(pos, BEE_NEST)
        val te = worldIn.getTileEntity(pos)

        if (te is BeeHiveTile) {
            for (j in 0..2) {
                val bee = EntityBee(worldIn)
                te.tryEnterHive(bee, false, rand.nextInt(599))
            }
        }
    }

    fun refresh() {
        BIOMES_AND_CHANCES.clear()
        for (entry in buzzyBees.validBiomesForBeeNest) {
            val parts = entry.split(":".toRegex()).toTypedArray()
            val loc = ResourceLocation(parts[0], parts[1])
            val biome = ForgeRegistries.BIOMES.getValue(loc)
            val chance = parts[2].toDouble()
            BIOMES_AND_CHANCES[biome] = chance
        }
    }
}