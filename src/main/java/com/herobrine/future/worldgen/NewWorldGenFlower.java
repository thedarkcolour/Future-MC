package com.herobrine.future.worldgen;

import com.herobrine.future.blocks.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class NewWorldGenFlower implements IWorldGenerator {
    private BlockFlower flower;
    private IBlockState state;

    public NewWorldGenFlower(BlockFlower flower) {
        this.flower = flower;
        this.state = flower.getDefaultState();
    }

    @Override
    public void generate(Random rand, int chunkX, int chunkZ, World worldIn, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int x = chunkX * 16;
        int z = chunkZ * 16;

        if(flower.getChunkChance(rand)) {
            for(int i = 0; i < 16; i++) {
                for(int j = 0; j < 16; j++) {
                    if(flower.getSpawnChance(rand))
                    placeFlowerBlock(x + i, z + j, worldIn);
                }
            }
        }
    }

    public void placeFlowerBlock(int x, int z, World worldIn) {
        if(!worldIn.provider.isNether()) {
            for (int i = 32; i < 100; i++) {
                BlockPos pos = new BlockPos(x + 8, i, z + 8);

                if(flower.isBiomeValid(worldIn.getBiome(pos)) && worldIn.isBlockLoaded(pos)) {
                    if (worldIn.isAirBlock(pos) && this.flower.canBlockStay(worldIn, pos, this.state)) {
                        worldIn.setBlockState(pos, this.state, 2);
                    }
                }
            }
        }
    }
}