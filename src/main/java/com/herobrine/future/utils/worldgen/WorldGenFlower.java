package com.herobrine.future.utils.worldgen;

import com.herobrine.future.blocks.FlowerBlue;
import com.herobrine.future.blocks.FlowerWhite;
import com.herobrine.future.utils.Config;
import com.herobrine.future.utils.Init;
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

        if (Config.bluefg) {
            generateFlower(Init.flowerblue, world, random, x, z);
        }
        if(Config.lilyg) {
            generateFlower1(Init.flowerwhite, world, random, x, z);
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
}