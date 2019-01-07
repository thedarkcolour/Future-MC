package com.herobrine.future.utils.worldgen;

import com.herobrine.future.blocks.flowerblack;
import com.herobrine.future.blocks.flowerblue;
import com.herobrine.future.blocks.flowerwhite;
import com.herobrine.future.utils.Config;
import com.herobrine.future.utils.init;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class worldgenflower implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        final int x = chunkX * 16 + 8;
        final int z = chunkZ * 16 + 8;

        switch(world.provider.getDimension()) {
            case -1:
                if(Config.wroseg) {
                    generateFlower2(init.flowerblack, world, random, x, z);
                }
                break;
            case 0:
                if (Config.bluefg) {
                    generateFlower(init.flowerblue, world, random, x, z);
                }
                if(Config.lilyg) {
                    generateFlower1(init.flowerwhite, world, random, x, z);
                }
                break;
        }
    }

    private void generateFlower(flowerblue flowerBlock, World world, Random random, int x, int z) {
        if (random.nextFloat() < 2) {
            final int posX = x + world.rand.nextInt(16);
            final int posZ = z + world.rand.nextInt(16);
            final BlockPos newPos = worldgenhelper.getGroundPos(world, posX, posZ);

            if (newPos != null && flowerBlock.canPlaceBlockAt(world, newPos) && world.getBiome(newPos) == Biome.getBiome(1)) {
                world.setBlockState(newPos, flowerBlock.getDefaultState(), 2);
            } else if(newPos != null && flowerBlock.canPlaceBlockAt(world, newPos) && world.getBiome(newPos) == Biome.getBiome(132)) {
                world.setBlockState(newPos, flowerBlock.getDefaultState(), 2);
            }
        }
    }

    private void generateFlower1(flowerwhite flowerBlock, World world, Random random, int x, int z) {
        if (random.nextFloat() < 2) {
            final int posX = x + world.rand.nextInt(16);
            final int posZ = z + world.rand.nextInt(16);
            final BlockPos newPos = worldgenhelper.getGroundPos(world, posX, posZ);

            if (newPos != null && flowerBlock.canPlaceBlockAt(world, newPos) && world.getBiome(newPos) == Biome.getBiome(4)) {
                world.setBlockState(newPos, flowerBlock.getDefaultState(), 2);
            } else if(newPos != null && flowerBlock.canPlaceBlockAt(world, newPos) && world.getBiome(newPos) == Biome.getBiome(132)) {
                world.setBlockState(newPos, flowerBlock.getDefaultState(),  2);
            } else if(newPos != null && flowerBlock.canPlaceBlockAt(world, newPos) && world.getBiome(newPos) == Biome.getBiome(5)) {
                world.setBlockState(newPos, flowerBlock.getDefaultState(),  2);
            }
        }
    }

    private void generateFlower2(flowerblack flowerBlock, World world, Random random, int x, int z) {
        if (random.nextFloat() < 2) {
            final int posX = x + world.rand.nextInt(16);
            final int posZ = z + world.rand.nextInt(16);
            final BlockPos newPos = worldgenhelper.getGroundPos(world, posX, posZ);

            if (newPos != null && (world.getBlockState(newPos).getBlock() == Blocks.SOUL_SAND) /*&& world.getBlockState(newPos).getBlock() == Blocks.AIR*/) {
                world.setBlockState(newPos, flowerBlock.getDefaultState());
            }
        }
    }
}