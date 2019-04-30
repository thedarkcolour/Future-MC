package com.herobrine.future.worldgen;

import com.herobrine.future.init.Init;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeJungle;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTrees;

import java.util.Random;

public class WorldGenBamboo extends WorldGenAbstractTree {

    public static final IBlockState JUNGLE_LOG = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
    public static final IBlockState JUNGLE_LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);
    public static final IBlockState OAK_LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);

    public WorldGenBamboo(boolean notify) {
        super(notify);
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {
        if(world.getBlockState(pos).getBlock().isReplaceable(world, pos) && Init.BAMBOO_STALK.canPlaceBlockAt(world, pos)) {
            world.setBlockState(pos, Init.BAMBOO_STALK.getDefaultState());
        }
        for (int i = 0; i < 10 - rand.nextInt(3); i++) {
            if(world.getBlockState(pos).getBlock() == Init.BAMBOO_STALK && Init.BAMBOO_STALK.canGrow(world, pos, world.getBlockState(pos), false)) {
                Init.BAMBOO_STALK.grow(world, rand, pos, true);
            }
        }
        return true;
    }

    public static WorldGenAbstractTree getTree(Random rand) {
        if (rand.nextInt(3) == 0) {
            return Init.BAMBOO_FEATURE;
        }
        else if (rand.nextInt(2) == 0) {
            return new WorldGenShrub(JUNGLE_LOG, OAK_LEAF);
        }
        else if (rand.nextBoolean()) {
            return new WorldGenTrees(false);
        }
        else {
            return rand.nextInt(3) == 0 ? new WorldGenMegaJungle(false, 10, 20, JUNGLE_LOG, JUNGLE_LEAF) : new WorldGenTrees(false, 4 + rand.nextInt(7), JUNGLE_LOG, JUNGLE_LEAF, true);
        }
    }

    public static final Biome BIOME_BAMBOO_JUNGLE = new BiomeJungle(false, new Biome.BiomeProperties("Bamboo Jungle").setTemperature(0.95F).setRainfall(0.9F)) {
        @Override
        public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
            return WorldGenBamboo.getTree(rand);
        }
    };
    public static final WorldGenBamboo BAMBOO_FEATURE = new WorldGenBamboo(false);
}