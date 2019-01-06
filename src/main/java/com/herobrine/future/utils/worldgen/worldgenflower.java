package com.herobrine.future.utils.worldgen;

import com.herobrine.future.blocks.flowerblack;
import com.herobrine.future.blocks.flowerblue;
import com.herobrine.future.blocks.flowerwhite;
import com.herobrine.future.utils.Config;
import com.herobrine.future.utils.init;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class worldgenflower implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        final int x = chunkX * 16 + 8;
        final int z = chunkZ * 16 + 8;

        final Biome biome = world.getBiomeForCoordsBody(new BlockPos(x, 0, z));

        for(int i = 0; i < 2; ++i) {
            if (i == 0 && Config.bluefg) {
                generateFlower(init.flowerblue, world, random, x, z);
            }
            if(i == 1 && Config.lilyg) {
                generateFlower1(init.flowerwhite, world, random, x, z);
            }
            if(i == 2 && Config.wroseg) {
                generateFlower2(init.flowerblack, world, random, x, z);
            }
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
            final Block down = world.getBlockState(newPos).getBlock();

            if (newPos != null && down == Blocks.SOUL_SAND) {
                world.setBlockState(newPos, flowerBlock.getDefaultState(), 2);
                /*if(world.getBiome(newPos) == BiomeDictionary.getBiomes(BiomeDictionary.Type.NETHER)) {
                    world.setBlockState(newPos, flowerBlock.getDefaultState(), 2);
                }
                if(world.getBiome(newPos) == Biome.getBiome(8)) {
                    world.setBlockState(newPos, flowerBlock.getDefaultState(), 2);
                }
                if(world.getBiome(newPos) == BiomeDictionary.getBiomes(BiomeDictionary.Type.SPOOKY)) {
                    world.setBlockState(newPos, flowerBlock.getDefaultState(), 2);
                }*/
            }  /**{
                world.setBlockState(newPos, flowerBlock.getDefaultState(), 2);
            } else if(newPos != null && flowerBlock.canPlaceBlockAt(world, newPos) && world.getBiome(newPos) == BiomeDictionary.getBiomes(BiomeDictionary.Type.SPOOKY)) {
                world.setBlockState(newPos, flowerBlock.getDefaultState(), 2);
            }*/
        }
    }
}