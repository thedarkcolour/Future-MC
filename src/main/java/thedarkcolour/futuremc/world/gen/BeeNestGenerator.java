package thedarkcolour.futuremc.world.gen;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import thedarkcolour.futuremc.block.Blocks;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public final class BeeNestGenerator extends AbstractTreeFeature {
    @SuppressWarnings("unchecked")
    public BeeNestGenerator(Function p_i49920_1_, boolean doBlockNofityOnPlace) {
        super(p_i49920_1_, doBlockNofityOnPlace);
    }

    @Override
    protected boolean place(Set changedBlocks, IWorldGenerationReader worldIn, Random rand, BlockPos position, MutableBoundingBox boundsIn) {
        return false;
    }

    private static final BlockState BEE_NEST = Blocks.BEE_NEST.getDefaultState();
    private static final Direction[] VALID_OFFSETS = new Direction[] {Direction.SOUTH, Direction.WEST, Direction.EAST};

    /**
     * Called by ASM.
     * See beenestssmalltrees.js in the resources folder.
     */
    @SuppressWarnings("unused")
    public static void generateBeeNestForSmallTrees(IWorldGenerationReader worldGenerationReader, Random rand, final BlockPos position, int height) {
        World worldIn = ((IWorld) worldGenerationReader).getWorld();
        Biome biome = worldIn.getBiome(position);
        if (biome == Biomes.PLAINS) {
            if (rand.nextFloat() < 0.05) {
                Direction offset = VALID_OFFSETS[rand.nextInt(3)];
                BlockPos pos = position.up(height - 4);
                if (func_214587_a(worldGenerationReader, pos)) {

                }
            }
        }
        //Objects.equal(Reflection.getCallerClass(4), Tree.class) ||
        //if (!(biome instanceof PlainsBiome || biome instanceof SunflowerPlainsBiome || biome instanceof FlowerForestBiome)) {
        //    return;
        //}
        //boolean shouldGenerate = rand.nextFloat() < (biome == Biomes.FLOWER_FOREST ? 0.01 : 0.005);
        //if (shouldGenerate) {
        //    return;
        //}
        //Direction offset = VALID_OFFSETS[rand.nextInt(3)];
        //BlockPos pos = position.up(height - 4).offset(offset);
        //if (func_214587_a(worldIn, pos) && worldIn.getBlockState(pos.south(1)).getBlock().isAir(worldIn.getBlockState(pos.south(1)), worldIn, pos.south(1))) {
        //    worldIn.setBlockState(pos, BEE_NEST, 3);
//
        //    TileEntity te = worldIn.getTileEntity(pos);
//
        //    if (te instanceof BeeHiveTileEntity) {
        //        for (int j = 0; j < 3; ++j) {
        //            // Check this if stuff fucks up
        //            ((BeeHiveTileEntity) te).tryEnterHive(EntityTypes.BEE.create(worldIn.getWorld()), false, rand.nextInt(599));
        //        }
        //    }
        //}
    }

    /**
     * Called by ASM.
     * See beenestsbigtrees.js in the resources folder.
     */
    @SuppressWarnings("unused")
    public static void generateBeeNestForBigTrees(IWorldGenerationReader worldGenerationReader, Random rand, final BlockPos position, int height) {
        World worldIn = ((IWorld) worldGenerationReader).getWorld();
        Biome biome = worldIn.getBiome(position);
        if (biome == Biomes.PLAINS) {
            if (rand.nextFloat() < 0.05) {

            }
        }
        //Objects.equal(Reflection.getCallerClass(4), BigTree.class) ||
        //if (!(biome instanceof PlainsBiome || biome instanceof SunflowerPlainsBiome || biome instanceof FlowerForestBiome)) {
        //    return;
        //}
        //boolean shouldGenerate = rand.nextFloat() < (biome == Biomes.FLOWER_FOREST ? 0.01 : 0.005);
        //if (shouldGenerate) {
        //    return;
        //}
        //Direction offset = VALID_OFFSETS[rand.nextInt(3)];
        //BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(position.offset(offset));
        //for (int i = 0; i < height; ++i) {
        //    if (!worldIn.getBlockState(pos.up()).getBlock().isAir(worldIn.getBlockState(pos.up()), worldIn, pos)) {
        //        if (worldIn.getBlockState(pos).getBlock().isAir(worldIn.getBlockState(pos), worldIn, pos) && worldIn.getBlockState(pos.south(1)).getBlock().isAir(worldIn.getBlockState(pos.south(1)), worldIn, pos.south(1))) {
        //            worldIn.setBlockState(pos, BEE_NEST, 3);
//
        //            TileEntity te = worldIn.getTileEntity(pos);
//
        //            if (te instanceof BeeHiveTileEntity) {
        //                for (int j = 0; j < 3; ++j) {
        //                    // Check this if stuff fucks up
        //                    ((BeeHiveTileEntity) te).tryEnterHive(EntityTypes.BEE.create(worldIn), false, rand.nextInt(599));
        //                }
        //            }
        //        }
        //        return;
        //    }
        //    pos.move(Direction.UP, 1);
        //}
    }
}
