package thedarkcolour.futuremc.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomePlains;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import thedarkcolour.futuremc.world.gen.feature.FeatureTreeWithHive;

import java.util.Random;

public class BiomePlainsWithNests extends BiomePlains {
    public static final FeatureTreeWithHive SMALL = new FeatureTreeWithHive();

    public BiomePlainsWithNests(boolean sunflowers, Biome.BiomeProperties properties) {
        super(sunflowers, properties);
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        // Use the default trees because my trees mess up. This reduces the amount of odd trees.
        return (rand.nextInt(3) == 0 ? BIG_TREE_FEATURE : (rand.nextFloat() < 0.05 ? SMALL : TREE_FEATURE));
    }
}