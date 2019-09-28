package thedarkcolour.futuremc.block;

import net.minecraft.block.SoundType;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import thedarkcolour.futuremc.init.FutureConfig;

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