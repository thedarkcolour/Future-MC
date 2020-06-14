package thedarkcolour.futuremc.command;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.Set;

public class BiomeLocator {

    /**
     * We need this because Kotlin doesn't allow for-i loops
     * and this would be horribly inefficient if it used {@code while} loops.
     *
     * @param biomes the biome(s) to look for when checking the world
     *
     * @return a nullable position of a biome in {@code biomes}
     */
    @Nullable
    public static BlockPos locateBiome(BiomeProvider biomeProvider, int x, int y, int z, int radius, int grid, Set<Biome> biomes, Random rand, boolean findFirst) {
        int i = x >> 2;
        int j = z >> 2;
        int k = radius >> 2;
        int l = y >> 2;
        BlockPos blockPos = null;
        int i1 = 0;
        int j1 = findFirst ? 0 : k;

        for(int k1 = j1; k1 <= k; k1 += grid) {
            for(int l1 = -k1; l1 <= k1; l1 += grid) {
                boolean bl2 = Math.abs(l1) == k1;

                for(int i2 = -k1; i2 <= k1; i2 += grid) {
                    if (findFirst) {
                        boolean bl3 = Math.abs(i2) == k1;
                        if (!bl3 && !bl2) {
                            continue;
                        }
                    }

                    int j2 = i + i2;
                    int k2 = j + l1;
                    if (biomes.contains(biomeProvider.getBiomeForNoiseGen(j2, l, k2))) {
                        if (blockPos == null || rand.nextInt(i1 + 1) == 0) {
                            blockPos = new BlockPos(j2 << 2, y, k2 << 2);
                            if (findFirst) {
                                return blockPos;
                            }
                        }

                        ++i1;
                    }
                }
            }
        }

        return blockPos;
    }
}
