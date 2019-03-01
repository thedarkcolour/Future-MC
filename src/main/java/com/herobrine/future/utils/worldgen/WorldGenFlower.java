package com.herobrine.future.utils.worldgen;

import com.herobrine.future.blocks.BerryBush;
import com.herobrine.future.blocks.FlowerBlue;
import com.herobrine.future.blocks.FlowerWhite;
import com.herobrine.future.utils.config.FutureConfig;
import com.herobrine.future.utils.proxy.Init;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenFlower implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        final int x = chunkX * 16 + 8;
        final int z = chunkZ * 16 + 8;

        if (FutureConfig.b.cornflowergen && FutureConfig.b.cornflower) {
            generateFlower(Init.flowerblue, world, random, x, z);
        }
        if(FutureConfig.b.lilygen && FutureConfig.b.lily) {
            generateFlower1(Init.flowerwhite, world, random, x, z);
        }
        if(FutureConfig.a.berrybush && FutureConfig.a.berrybushgen) {
            generateBush(Init.berrybush, world, random, x, z);
        }
    }

    private void generateFlower(FlowerBlue flowerBlock, World world, Random random, int x, int z) {
        if (random.nextFloat() < 0.5) {
            final int posX = x + world.rand.nextInt(16);
            final int posZ = z + world.rand.nextInt(16);
            final BlockPos newPos = WorldGenHelper.getGroundPos(world, posX, posZ);

            if (newPos != null && flowerBlock.canPlaceBlockAt(world, newPos) && world.getBiome(newPos) == Biome.getBiome(1)) {
                world.setBlockState(newPos, flowerBlock.getDefaultState(), 2);
            } else if(newPos != null && flowerBlock.canPlaceBlockAt(world, newPos) && world.getBiome(newPos) == Biome.getBiome(132)) {
                world.setBlockState(newPos, flowerBlock.getDefaultState(), 2);
            }
        }
    }

    private void generateFlower1(FlowerWhite flowerBlock, World world, Random random, int x, int z) {
        if (random.nextFloat() < 2) {
            final int posX = x + world.rand.nextInt(16);
            final int posZ = z + world.rand.nextInt(16);
            final BlockPos newPos = WorldGenHelper.getGroundPos(world, posX, posZ);
            if (newPos != null && flowerBlock.canPlaceBlockAt(world, newPos) && world.getBiome(newPos) == Biome.getBiome(4)) {
                world.setBlockState(newPos, flowerBlock.getDefaultState(), 2);
            } else if(newPos != null && flowerBlock.canPlaceBlockAt(world, newPos) && world.getBiome(newPos) == Biome.getBiome(132)) {
                world.setBlockState(newPos, flowerBlock.getDefaultState(),  2);
            } else if(newPos != null && flowerBlock.canPlaceBlockAt(world, newPos) && world.getBiome(newPos) == Biome.getBiome(5)) {
                world.setBlockState(newPos, flowerBlock.getDefaultState(),  2);
            } else if(newPos != null && flowerBlock.canPlaceBlockAt(world, newPos) && world.getBiome(newPos) == Biomes.BIRCH_FOREST) {
                world.setBlockState(newPos, flowerBlock.getDefaultState(), 2);
            }
        }
    }

    private void generateBush(BerryBush flowerBlock, World world, Random random, int x, int z) {
        if (random.nextDouble() < 0.3) {
            final int posX = x + world.rand.nextInt(16);
            final int posZ = z + world.rand.nextInt(16);
            final BlockPos newPos = WorldGenHelper.getGroundPos(world, posX, posZ);

            if(newPos != null && flowerBlock.canPlaceBlockAt(world, newPos)) {
                IBlockState bush = flowerBlock.getDefaultState().withProperty(BerryBush.AGE, 1);
                if (world.getBiome(newPos) == Biomes.TAIGA) {
                    world.setBlockState(newPos, bush, 2);
                } else if (world.getBiome(newPos) == Biomes.TAIGA_HILLS) {
                    world.setBlockState(newPos, bush, 2);
                } else if (world.getBiome(newPos) == Biomes.COLD_TAIGA) {
                    world.setBlockState(newPos, bush, 2);
                } else if (world.getBiome(newPos) == Biomes.COLD_TAIGA_HILLS) {
                    world.setBlockState(newPos, bush, 2);
                } else if (world.getBiome(newPos) == Biomes.MUTATED_REDWOOD_TAIGA) {
                    world.setBlockState(newPos, bush, 2);
                } else if (world.getBiome(newPos) == Biomes.REDWOOD_TAIGA_HILLS) {
                    world.setBlockState(newPos, bush, 2);
                } else if (world.getBiome(newPos) == Biomes.REDWOOD_TAIGA) {
                    world.setBlockState(newPos, bush, 2);
                } else if (world.getBiome(newPos) == Biomes.MUTATED_REDWOOD_TAIGA_HILLS) {
                    world.setBlockState(newPos, bush, 2);
                } else if (world.getBiome(newPos) == Biomes.MUTATED_TAIGA_COLD) {
                    world.setBlockState(newPos, bush, 2);
                }
            }
        }
    }
}