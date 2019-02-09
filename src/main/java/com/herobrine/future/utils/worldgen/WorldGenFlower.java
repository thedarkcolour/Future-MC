package com.herobrine.future.utils.worldgen;

import com.herobrine.future.blocks.BerryBush;
import com.herobrine.future.blocks.FlowerBlue;
import com.herobrine.future.blocks.FlowerWhite;
import com.herobrine.future.utils.Config;
import com.herobrine.future.utils.Init;
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

        if (Config.bluefg) {
            generateFlower(Init.flowerblue, world, random, x, z);
        }
        if(Config.lilyg) {
            generateFlower1(Init.flowerwhite, world, random, x, z);
        }
        if(Config.berrybush) {
            generateBush(Init.berrybush, world, random, x, z);
        }
    }

    private void generateFlower(FlowerBlue flowerBlock, World world, Random random, int x, int z) {
        if (random.nextFloat() < 0.5) {
            final int posX = x + world.field_73012_v.nextInt(16);
            final int posZ = z + world.field_73012_v.nextInt(16);
            final BlockPos newPos = WorldGenHelper.getGroundPos(world, posX, posZ);

            if (newPos != null && flowerBlock.func_176196_c(world, newPos) && world.func_180494_b(newPos) == Biome.func_150568_d(1)) {
                world.func_180501_a(newPos, flowerBlock.func_176223_P(), 2);
            } else if(newPos != null && flowerBlock.func_176196_c(world, newPos) && world.func_180494_b(newPos) == Biome.func_150568_d(132)) {
                world.func_180501_a(newPos, flowerBlock.func_176223_P(), 2);
            }
        }
    }

    private void generateFlower1(FlowerWhite flowerBlock, World world, Random random, int x, int z) {
        if (random.nextFloat() < 2) {
            final int posX = x + world.field_73012_v.nextInt(16);
            final int posZ = z + world.field_73012_v.nextInt(16);
            final BlockPos newPos = WorldGenHelper.getGroundPos(world, posX, posZ);
            if (newPos != null && flowerBlock.func_176196_c(world, newPos) && world.func_180494_b(newPos) == Biome.func_150568_d(4)) {
                world.func_180501_a(newPos, flowerBlock.func_176223_P(), 2);
            } else if(newPos != null && flowerBlock.func_176196_c(world, newPos) && world.func_180494_b(newPos) == Biome.func_150568_d(132)) {
                world.func_180501_a(newPos, flowerBlock.func_176223_P(),  2);
            } else if(newPos != null && flowerBlock.func_176196_c(world, newPos) && world.func_180494_b(newPos) == Biome.func_150568_d(5)) {
                world.func_180501_a(newPos, flowerBlock.func_176223_P(),  2);
            } else if(newPos != null && flowerBlock.func_176196_c(world, newPos) && world.func_180494_b(newPos) == Biomes.field_150583_P) {
                world.func_180501_a(newPos, flowerBlock.func_176223_P(), 2);
            }
        }
    }

    private void generateBush(BerryBush flowerBlock, World world, Random random, int x, int z) {
        if (random.nextDouble() < 0.3) {
            final int posX = x + world.field_73012_v.nextInt(16);
            final int posZ = z + world.field_73012_v.nextInt(16);
            final BlockPos newPos = WorldGenHelper.getGroundPos(world, posX, posZ);

            if(newPos != null && flowerBlock.func_176196_c(world, newPos)) {
                IBlockState bush = flowerBlock.func_176223_P().func_177226_a(BerryBush.AGE, 1);
                if (world.func_180494_b(newPos) == Biomes.field_76768_g) {
                    world.func_180501_a(newPos, bush, 2);
                } else if (world.func_180494_b(newPos) == Biomes.field_76784_u) {
                    world.func_180501_a(newPos, bush, 2);
                } else if (world.func_180494_b(newPos) == Biomes.field_150584_S) {
                    world.func_180501_a(newPos, bush, 2);
                } else if (world.func_180494_b(newPos) == Biomes.field_150579_T) {
                    world.func_180501_a(newPos, bush, 2);
                } else if (world.func_180494_b(newPos) == Biomes.field_185432_ad) {
                    world.func_180501_a(newPos, bush, 2);
                } else if (world.func_180494_b(newPos) == Biomes.field_150581_V) {
                    world.func_180501_a(newPos, bush, 2);
                } else if (world.func_180494_b(newPos) == Biomes.field_150578_U) {
                    world.func_180501_a(newPos, bush, 2);
                } else if (world.func_180494_b(newPos) == Biomes.field_185433_ae) {
                    world.func_180501_a(newPos, bush, 2);
                } else if (world.func_180494_b(newPos) == Biomes.field_185431_ac) {
                    world.func_180501_a(newPos, bush, 2);
                }
            }
        }
    }
}
