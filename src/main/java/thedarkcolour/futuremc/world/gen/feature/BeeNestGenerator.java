package thedarkcolour.futuremc.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomePlains;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import sun.reflect.Reflection;
import thedarkcolour.futuremc.block.BlockBeeHive;
import thedarkcolour.futuremc.entity.bee.EntityBee;
import thedarkcolour.futuremc.init.FutureConfig;
import thedarkcolour.futuremc.init.Init;
import thedarkcolour.futuremc.tile.TileBeeHive;

import java.util.Random;

public final class BeeNestGenerator {
    protected static final IBlockState BEE_NEST = Init.BEE_NEST.getDefaultState().withProperty(BlockBeeHive.FACING, EnumFacing.SOUTH);
    protected static final EnumFacing[] VALID_OFFSETS = new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST};

    /**
     * Called with ASM.
     * @see thedarkcolour.futuremc.asm.CoreLoader
     * @see thedarkcolour.futuremc.asm.CoreTransformer
     */
    @SuppressWarnings("unused")
    public static void generateBeeNestsForSmallTrees(World worldIn, Random rand, BlockPos position, int height, WorldGenAbstractTree trees) {
        if (Reflection.getCallerClass(3) != BiomeDecorator.class || (!(worldIn.getBiome(position) instanceof BiomePlains) && worldIn.getBiome(position) != Biomes.MUTATED_FOREST)) {
            return;
        }
        Biome biome = worldIn.getBiome(position);
        boolean shouldGenerate = rand.nextFloat() < (biome == Biomes.MUTATED_FOREST ? (FutureConfig.general.beeNestChance / 5) : FutureConfig.general.beeNestChance);
        if (!shouldGenerate) {
            return;
        }
        EnumFacing offset = VALID_OFFSETS[rand.nextInt(3)];
        position = position.offset(offset);

        for (int j3 = 0; j3 < height; ++j3) {
            BlockPos upN = position.up(j3);
            IBlockState state = worldIn.getBlockState(upN);

            if (state.getBlock().isAir(state, worldIn, upN) || state.getBlock().isLeaves(state, worldIn, upN)) {
                if (state.getBlock().isLeaves(state, worldIn, upN)) {
                    BlockPos hivePos = upN.down();

                    while (worldIn.getBlockState(hivePos).getBlock() instanceof BlockLeaves) {
                        hivePos = hivePos.down();
                    }

                    if (worldIn.getBlockState(hivePos.offset(offset)).getCollisionBoundingBox(worldIn, hivePos.offset(offset)) == Block.NULL_AABB) {
                        worldIn.setBlockState(hivePos, BEE_NEST);

                        TileEntity te = worldIn.getTileEntity(hivePos);

                        if (te instanceof TileBeeHive) {
                            for (int j = 0; j < 3; ++j) {
                                EntityBee bee = new EntityBee(worldIn);
                                ((TileBeeHive) te).tryEnterHive(bee, false, rand.nextInt(599));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Slightly different than {@link #generateBeeNestsForSmallTrees(World, Random, BlockPos, int, WorldGenAbstractTree)},
     * but starts at 4 blocks high to reduce iteration.
     * All big trees have a trunk of at least four blocks tall.
     */
    @SuppressWarnings("unused")
    public static void generateBeeNestsForBigTrees(World worldIn, Random rand, BlockPos position, int height, WorldGenAbstractTree tree) {
        if (Reflection.getCallerClass(3) != BiomeDecorator.class || (!(worldIn.getBiome(position) instanceof BiomePlains) && worldIn.getBiome(position) != Biomes.MUTATED_FOREST)) {
            return;
        }
        Biome biome = worldIn.getBiome(position);
        boolean shouldGenerate = rand.nextFloat() < (biome == Biomes.MUTATED_FOREST ? (FutureConfig.general.beeNestChance / 5) : FutureConfig.general.beeNestChance);
        if (!shouldGenerate) {
            return;
        }
        EnumFacing offset = VALID_OFFSETS[rand.nextInt(3)];
        position = position.offset(offset);

        for (int j3 = 0; j3 < height; ++j3) {
            BlockPos upN = position.up(j3 + 4);
            IBlockState state = worldIn.getBlockState(upN);

            if (state.getBlock().isAir(state, worldIn, upN) || state.getBlock().isLeaves(state, worldIn, upN)) {
                if (state.getBlock().isLeaves(state, worldIn, upN)) {
                    BlockPos hivePos = upN.down();

                    while (worldIn.getBlockState(hivePos).getBlock() instanceof BlockLeaves) {
                        hivePos = hivePos.down();
                    }

                    if (worldIn.getBlockState(hivePos.offset(offset)).getCollisionBoundingBox(worldIn, hivePos.offset(offset)) == Block.NULL_AABB) {
                        worldIn.setBlockState(hivePos, BEE_NEST);

                        TileEntity te = worldIn.getTileEntity(hivePos);

                        if (te instanceof TileBeeHive) {
                            for (int j = 0; j < 3; ++j) {
                                EntityBee bee = new EntityBee(worldIn);
                                ((TileBeeHive) te).tryEnterHive(bee, false, rand.nextInt(599));
                            }
                        }
                    }
                }
            }
        }
    }
}