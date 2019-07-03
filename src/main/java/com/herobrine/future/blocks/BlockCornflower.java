package com.herobrine.future.blocks;

import com.herobrine.future.config.FutureConfig;
import net.minecraft.block.SoundType;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

public class BlockCornflower extends BlockFlower { //Adds blue flower
    public BlockCornflower() {
        super("FlowerBlue");
        setSoundType(SoundType.PLANT);
    }

    @Override
    public boolean isBiomeValid(Biome biome) {
        return biome == Biomes.PLAINS || biome == Biomes.MUTATED_FOREST;
    }

    @Override
    public int getFlowerChance() {
        return FutureConfig.modFlowers.cornflowerChance;
    }
}