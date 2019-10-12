package thedarkcolour.futuremc.world.gen.feature;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomePlains;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import sun.reflect.Reflection;
import thedarkcolour.futuremc.block.BlockBeeHive;
import thedarkcolour.futuremc.entity.bee.EntityBee;
import thedarkcolour.futuremc.init.FutureConfig;
import thedarkcolour.futuremc.init.Init;
import thedarkcolour.futuremc.tile.TileBeeHive;

import java.util.Random;

public final class BeeNestGenerator {
    protected static final IBlockState BEE_NEST = Init.BEE_NEST.getDefaultState().withProperty(BlockBeeHive.FACING, EnumFacing.SOUTH);
    protected static final EnumFacing[] VALID_OFFSETS = new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST};

    public static double getBeeNestChance() {
        return FutureConfig.general.beeNestChance;
    }

    /**
     * Called with ASM.
     * @see thedarkcolour.futuremc.asm.CoreTransformer
     */
    @SuppressWarnings("unused")
    public static void generateBeeNestsForSmallTrees(World worldIn, Random rand, final BlockPos position, int height, WorldGenAbstractTree trees) {
        if (!FutureConfig.general.bee || (Reflection.getCallerClass(3) != BiomeDecorator.class || (!(worldIn.getBiome(position) instanceof BiomePlains) && worldIn.getBiome(position) != Biomes.MUTATED_FOREST))) {
            return;
        }
        Biome biome = worldIn.getBiome(position);
        boolean shouldGenerate = rand.nextFloat() < (biome == Biomes.MUTATED_FOREST ? (getBeeNestChance() / 5) : getBeeNestChance());
        if (!shouldGenerate) {
            return;
        }
        EnumFacing offset = VALID_OFFSETS[rand.nextInt(3)];
        BlockPos pos = position.up(height - 4).offset(offset);
        if (trees.isReplaceable(worldIn, pos) && worldIn.getBlockState(pos.south(1)).getBlock().isAir(worldIn.getBlockState(pos.south(1)), worldIn, pos.south(1))) {
            worldIn.setBlockState(pos, BEE_NEST);

            TileEntity te = worldIn.getTileEntity(pos);

            if (te instanceof TileBeeHive) {
                for (int j = 0; j < 3; ++j) {
                    EntityBee bee = new EntityBee(worldIn);
                    ((TileBeeHive) te).tryEnterHive(bee, false, rand.nextInt(599));
                }
            }
        }
    }

    /**
     * Called with ASM.
     * @see thedarkcolour.futuremc.asm.CoreTransformer
     */
    @SuppressWarnings("unused")
    public static void generateBeeNestsForBigTrees(World worldIn, Random rand, BlockPos position, final int height, WorldGenAbstractTree tree) {
        if (Reflection.getCallerClass(3) != BiomeDecorator.class || (!(worldIn.getBiome(position) instanceof BiomePlains) && worldIn.getBiome(position) != Biomes.MUTATED_FOREST)) {
            return;
        }
        Biome biome = worldIn.getBiome(position);
        boolean shouldGenerate = rand.nextFloat() < (biome == Biomes.MUTATED_FOREST ? (FutureConfig.general.beeNestChance / 5) : FutureConfig.general.beeNestChance);
        if (!shouldGenerate) {
            return;
        }
        EnumFacing offset = VALID_OFFSETS[rand.nextInt(3)];
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(position.offset(offset));
        for (int i = 0; i < height; ++i) {
            if (!worldIn.getBlockState(pos.up()).getBlock().isAir(worldIn.getBlockState(pos.up()), worldIn, pos)) {
                if (worldIn.getBlockState(pos).getBlock().isAir(worldIn.getBlockState(pos), worldIn, pos) && worldIn.getBlockState(pos.south(1)).getBlock().isAir(worldIn.getBlockState(pos.south(1)), worldIn, pos.south(1))) {
                    worldIn.setBlockState(pos, BEE_NEST);

                    TileEntity te = worldIn.getTileEntity(pos);

                    if (te instanceof TileBeeHive) {
                        for (int j = 0; j < 3; ++j) {
                            EntityBee bee = new EntityBee(worldIn);
                            ((TileBeeHive) te).tryEnterHive(bee, false, rand.nextInt(599));
                        }
                    }
                }
                return;
            }
            pos.move(EnumFacing.UP, 1);
        }
    }
}