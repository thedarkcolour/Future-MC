package com.herobrine.future.blocks;

import com.herobrine.future.init.Init;
import net.minecraft.block.SoundType;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

import java.util.Random;

public class BlockLilyOfValley extends BlockFlower { //Adds white flower
    public BlockLilyOfValley() {
        super("FlowerWhite");
        setCreativeTab(Init.futuretab);
        setSoundType(SoundType.PLANT);
    }

    @Override
    public boolean isBiomeValid(Biome biome) {
        return biome == Biomes.FOREST || biome == Biomes.MUTATED_FOREST;
    }

    @Override
    public boolean getSpawnChance(Random random) {
        return random.nextInt(100) > 95;
    }

    @Override
    public boolean getChunkChance(Random random) {
        return random.nextInt(100) > 93;
    }
}