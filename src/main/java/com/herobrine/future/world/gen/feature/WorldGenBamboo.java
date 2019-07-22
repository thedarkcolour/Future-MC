package com.herobrine.future.world.gen.feature;

import com.herobrine.future.init.Init;
import com.herobrine.future.world.gen.feature.FeatureBambooStalk;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenBamboo implements IWorldGenerator {
    public void generate(World worldIn, Random random, BlockPos pos) {
        if (worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && Init.BAMBOO_STALK.canPlaceBlockAt(worldIn, pos)) {
            worldIn.setBlockState(pos, Init.BAMBOO_STALK.getDefaultState());
        }
        for (int j = 0; j < 10; j++) {
            if (worldIn.getBlockState(pos).getBlock() == Init.BAMBOO_STALK && Init.BAMBOO_STALK.canGrow(worldIn, pos, worldIn.getBlockState(pos), false)) {
                Init.BAMBOO_STALK.grow(worldIn, random, pos, null);
            }
        }
    }

    public static final FeatureBambooStalk BAMBOO_FEATURE = new FeatureBambooStalk();

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World worldIn, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int x = chunkX * 16 + 8;
        int z = chunkZ * 16 + 8;

        Biome biome = worldIn.getBiomeForCoordsBody(new BlockPos(x, 0, z));
        ChunkPos chunkPos = worldIn.getChunkFromChunkCoords(chunkX, chunkZ).getPos();

        if(isBiomeValid(biome) && worldIn.getWorldType() != WorldType.FLAT) {
            for (int i = 0; i < 13; i++) {
                int xPos = random.nextInt(16) + 8;
                int zPos = random.nextInt(16) + 8;
                int yPos = random.nextInt(worldIn.getHeight(chunkPos.getBlock(0, 0, 0).add(xPos, 0, zPos)).getY() + 32);

                BlockPos pos = chunkPos.getBlock(0, 0, 0).add(xPos, yPos, zPos);
                generate(worldIn, random, pos);
            }
        }
    }

    private boolean isBiomeValid(Biome biome) {
        return biome == Biomes.JUNGLE || biome == Biomes.JUNGLE_EDGE || biome == Biomes.JUNGLE_HILLS || biome == Biomes.MUTATED_JUNGLE
                || biome == Biomes.MUTATED_JUNGLE_EDGE;
    }
}