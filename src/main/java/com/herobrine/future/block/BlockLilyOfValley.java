package com.herobrine.future.block;

import com.herobrine.future.init.FutureConfig;
import net.minecraft.block.SoundType;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

public class BlockLilyOfValley extends BlockFlower { //Adds white flower
    public BlockLilyOfValley() {
        super("FlowerWhite");
        setSoundType(SoundType.PLANT);
    }

    @Override
    public boolean isBiomeValid(Biome biome) {
        return biome == Biomes.FOREST || biome == Biomes.MUTATED_FOREST;
    }

    @Override
    public int getFlowerChance() {
        return FutureConfig.modFlowers.lilyChance;
    }
}