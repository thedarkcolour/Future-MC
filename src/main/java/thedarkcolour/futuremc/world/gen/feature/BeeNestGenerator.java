package thedarkcolour.futuremc.world.gen.feature;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import sun.reflect.Reflection;
import thedarkcolour.futuremc.block.BeeHiveBlock;
import thedarkcolour.futuremc.config.FConfig;
import thedarkcolour.futuremc.entity.bee.BeeEntity;
import thedarkcolour.futuremc.registry.FBlocks;
import thedarkcolour.futuremc.tile.BeeHiveTile;

import java.util.Random;

public final class BeeNestGenerator {
    public static final IBlockState BEE_NEST = FBlocks.INSTANCE.getBEE_NEST().getDefaultState().withProperty(BeeHiveBlock.FACING, EnumFacing.SOUTH);
    public static final EnumFacing[] VALID_OFFSETS = new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST};
    public static final Object2DoubleMap<Biome> BIOMES_AND_CHANCES = new Object2DoubleOpenHashMap();

    /**
     * Called with ASM.
     *
     * @see thedarkcolour.futuremc.asm.CoreTransformer
     */
    @SuppressWarnings("unused")
    public static void generateBeeNestsForSmallTrees(World worldIn, Random rand, final BlockPos position, int height, WorldGenAbstractTree trees) {
        if (!FConfig.INSTANCE.getBuzzyBees().bee.enabled || (Reflection.getCallerClass(3) != BiomeDecorator.class)) {
            return;
        }
        Biome biome = worldIn.getBiome(position);

        if (!(rand.nextFloat() < FConfig.INSTANCE.getBuzzyBees().beeNestChance * BIOMES_AND_CHANCES.getDouble(biome))) {
            return;
        }
        EnumFacing offset = VALID_OFFSETS[rand.nextInt(3)];
        BlockPos pos = position.up(height - 4).offset(offset);
        if (trees.isReplaceable(worldIn, pos) && worldIn.getBlockState(pos.south(1)).getBlock().isAir(worldIn.getBlockState(pos.south(1)), worldIn, pos.south(1))) {
            worldIn.setBlockState(pos, BEE_NEST);

            TileEntity te = worldIn.getTileEntity(pos);

            if (te instanceof BeeHiveTile) {
                for (int j = 0; j < 3; ++j) {
                    BeeEntity bee = new BeeEntity(worldIn);
                    ((BeeHiveTile) te).tryEnterHive(bee, false, rand.nextInt(599));
                }
            }
        }
    }

    /**
     * Called with ASM.
     *
     * @see thedarkcolour.futuremc.asm.CoreTransformer
     */
    @SuppressWarnings("unused")
    public static void generateBeeNestsForBigTrees(World worldIn, Random rand, BlockPos position, final int height, WorldGenAbstractTree tree) {
        if (!FConfig.INSTANCE.getBuzzyBees().bee.enabled || (Reflection.getCallerClass(3) != BiomeDecorator.class)) {
            return;
        }
        Biome biome = worldIn.getBiome(position);

        if (!(rand.nextFloat() < FConfig.INSTANCE.getBuzzyBees().beeNestChance * BIOMES_AND_CHANCES.getDouble(biome))) {
            return;
        }
        EnumFacing offset = VALID_OFFSETS[rand.nextInt(3)];
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(position.offset(offset));
        for (int i = 0; i < height; ++i) {
            if (!worldIn.getBlockState(pos.up()).getBlock().isAir(worldIn.getBlockState(pos.up()), worldIn, pos)) {
                if (worldIn.getBlockState(pos).getBlock().isAir(worldIn.getBlockState(pos), worldIn, pos) && worldIn.getBlockState(pos.south(1)).getBlock().isAir(worldIn.getBlockState(pos.south(1)), worldIn, pos.south(1))) {
                    worldIn.setBlockState(pos, BEE_NEST);

                    TileEntity te = worldIn.getTileEntity(pos);

                    if (te instanceof BeeHiveTile) {
                        for (int j = 0; j < 3; ++j) {
                            BeeEntity bee = new BeeEntity(worldIn);
                            ((BeeHiveTile) te).tryEnterHive(bee, false, rand.nextInt(599));
                        }
                    }
                }
                return;
            }
            pos.move(EnumFacing.UP, 1);
        }
    }

    public static void refresh() {
        for (String entry : FConfig.INSTANCE.getBuzzyBees().validBiomesForBeeNest) {
            String[] parts = entry.split(":");
            ResourceLocation loc = new ResourceLocation(parts[0], parts[1]);
            Biome biome = ForgeRegistries.BIOMES.getValue(loc);
            double chance = Double.parseDouble(parts[2]);
            BIOMES_AND_CHANCES.put(biome, chance);
        }
    }
}