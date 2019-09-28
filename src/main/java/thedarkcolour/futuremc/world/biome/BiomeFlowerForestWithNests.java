package thedarkcolour.futuremc.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeForest;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

public class BiomeFlowerForestWithNests extends BiomeForest {
    public BiomeFlowerForestWithNests() {
        super(BiomeForest.Type.FLOWER, (new Biome.BiomeProperties("Flower Forest")).setBaseBiome("forest").setHeightVariation(0.4F).setTemperature(0.7F).setRainfall(0.8F));
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        // Use default trees to prevent a bunch of deformed trees.
        return (rand.nextInt(10) == 0 ? BIG_TREE_FEATURE : (rand.nextFloat() < 0.01 ? BiomePlainsWithNests.SMALL : TREE_FEATURE));
    }
}