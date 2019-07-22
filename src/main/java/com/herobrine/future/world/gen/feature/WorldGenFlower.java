package com.herobrine.future.world.gen.feature;

import com.herobrine.future.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenFlower implements IWorldGenerator {
    private BlockFlower flower;
    private IBlockState state;

    public WorldGenFlower(BlockFlower flower) {
        this.flower = flower;
        this.state = flower.getDefaultState();
    }

    private void generate(World world, Random random, BlockPos pos) {
        for (int i = 0; i < 64; ++i) {
            BlockPos blockpos = pos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

            if (world.isAirBlock(blockpos) && (!world.provider.isNether() || blockpos.getY() < 255) && this.flower.canBlockStay(world, blockpos, this.state)) {
                world.setBlockState(blockpos, this.state, 2);
            }
        }
    }

    @Override
    public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int x = chunkX * 16 + 8;
        int z = chunkZ * 16 + 8;

        Biome biome = world.getBiomeForCoordsBody(new BlockPos(x, 0, z));
        ChunkPos chunkPos = world.getChunkFromChunkCoords(chunkX, chunkZ).getPos();

        if(rand.nextInt(100) <= flower.getFlowerChance()) {
            if(flower.isBiomeValid(biome) && world.getWorldType() != WorldType.FLAT) {
                for (int i = 0; i < 5; i++) {
                    int xPos = rand.nextInt(16) + 8;
                    int zPos = rand.nextInt(16) + 8;
                    int yPos = rand.nextInt(world.getHeight(chunkPos.getBlock(0, 0, 0).add(xPos, 0, zPos)).getY() + 32);
                    BlockPos pos = chunkPos.getBlock(0, 0, 0).add(xPos, yPos, zPos);

                    generate(world, rand, pos);
                }
            }
        }
    }
}