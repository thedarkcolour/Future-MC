package thedarkcolour.futuremc.compat.dynamictrees

import com.ferreusveritas.dynamictrees.api.IPostGenFeature
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature
import com.ferreusveritas.dynamictrees.blocks.BlockBranchThick
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves
import com.ferreusveritas.dynamictrees.trees.Species
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds
import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockPos.MutableBlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import thedarkcolour.futuremc.block.buzzybees.BeeHiveBlock
import thedarkcolour.futuremc.compat.DYNAMIC_TREES
import thedarkcolour.futuremc.entity.bee.BeeEntity
import thedarkcolour.futuremc.entity.bee.EntityBee
import thedarkcolour.futuremc.tile.BeeHiveTile
import thedarkcolour.futuremc.world.gen.feature.BeeNestGenerator

object BeeHiveFeature : IPostGrowFeature, IPostGenFeature {
    val OAK = Species.REGISTRY.getValue(ResourceLocation(DYNAMIC_TREES, "oak"))
    val BIRCH = Species.REGISTRY.getValue(ResourceLocation(DYNAMIC_TREES, "birch"))

    override fun postGrow(
        worldIn: World, rootPos: BlockPos, treePos: BlockPos, species: Species, soilLife: Int, natural: Boolean
    ): Boolean {
        val rand = worldIn.rand
        if (
            (species == OAK || species == BIRCH) &&
            BeeNestGenerator.BIOMES_AND_CHANCES.getDouble(worldIn.getBiome(treePos)) != 0.0
        ) {
            for (pos in MutableBlockPos.getAllInBoxMutable(
                treePos.down().north(2).west(2),
                treePos.up().south(2).east(2)
            )) {
                val testState = worldIn.getBlockState(pos)

                if (BeeEntity.FLOWERS.contains(testState)) {
                    val offset =
                        MutableBlockPos(treePos.offset(BeeNestGenerator.VALID_OFFSETS[rand.nextInt(3)]))
                    var state = worldIn.getBlockState(offset)

                    while (state.block.isAir(run {
                            state = worldIn.getBlockState(pos.move(EnumFacing.UP))
                            state
                        }, worldIn, offset) && !worldIn.isOutsideBuildHeight(pos)
                    ) {
                        if (state.block is BeeHiveBlock) {
                            return false
                        } else {
                            val upBlock = worldIn.getBlockState(pos.up()).block

                            if (upBlock is BlockBranchThick || upBlock is BlockDynamicLeaves) {
                                worldIn.setBlockState(pos, BeeNestGenerator.BEE_NEST)
                                val te = worldIn.getTileEntity(pos)

                                if (te is BeeHiveTile) {
                                    for (j in 0..2) {
                                        val bee = EntityBee(worldIn)
                                        te.tryEnterHive(bee, false, worldIn.rand.nextInt(599))
                                    }
                                }
                            }

                        }
                    }

                    break
                }
            }
        } else {
            return false
        }

        return false
    }

    override fun postGeneration(
        worldIn: World, rootPos: BlockPos, species: Species, biome: Biome, radius: Int,
        endPoints: MutableList<BlockPos>, safeBounds: SafeChunkBounds, initialDirtState: IBlockState
    ): Boolean {
        return false
    }
}