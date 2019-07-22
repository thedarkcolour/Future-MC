package com.herobrine.future.world.gen.feature;

import com.herobrine.future.init.Init;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

public class FeatureBambooStalk extends WorldGenAbstractTree {

    public FeatureBambooStalk() {
        super(false);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos pos) {
        if(worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && Init.BAMBOO_STALK.canPlaceBlockAt(worldIn, pos)) {
            worldIn.setBlockState(pos, Init.BAMBOO_STALK.getDefaultState());
        }
        for (int i = 0; i < 10 - rand.nextInt(3); i++) {
            if(worldIn.getBlockState(pos).getBlock() == Init.BAMBOO_STALK && Init.BAMBOO_STALK.canGrow(worldIn, pos, worldIn.getBlockState(pos), false)) {
                Init.BAMBOO_STALK.grow(worldIn, rand, pos, null);
            }
        }
        return true;
    }
}