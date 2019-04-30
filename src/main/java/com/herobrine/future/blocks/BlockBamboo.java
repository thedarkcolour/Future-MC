package com.herobrine.future.blocks;

import com.herobrine.future.init.Init;
import com.herobrine.future.sound.Sounds;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockBamboo extends BlockBase implements IPlantable, IGrowable {
    public static final PropertyBool THICK = PropertyBool.create("thick");
    public static final PropertyEnum<BlockBamboo.EnumLeaves> LEAVES = PropertyEnum.create("leaves", BlockBamboo.EnumLeaves.class);
    private static final AxisAlignedBB THICK_AABB = makeAABB(6.5, 0, 6.5, 9.5, 16, 9.5);
    private static final AxisAlignedBB THIN_AABB = makeAABB(7, 0, 7, 9, 16, 9);
    private static final AxisAlignedBB LARGE_AABB = makeAABB(5, 0, 5, 11, 16, 11);
    private static final AxisAlignedBB SMALL_AABB = makeAABB(5.5, 0, 5.5, 10.5, 16, 10.5);

    public BlockBamboo() {
        super(new BlockProperties("Bamboo", Material.PLANTS, Sounds.BAMBOO));
        setDefaultState(getBlockState().getBaseState().withProperty(THICK, false).withProperty(LEAVES, EnumLeaves.NO_LEAVES));
        setTickRandomly(true);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if(worldIn.getBlockState(pos.up()).getBlock() == Init.BAMBOO_STALK) {
            worldIn.destroyBlock(pos.up(), true);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        neighborChanged(worldIn, pos);
    }

    /**
     * Returns true if block can stay
     */
    public void neighborChanged(World world, BlockPos pos) {
        if (!this.canPlaceBlockAt(world, pos)) {
            world.destroyBlock(pos, true);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos.down());
        Block block = state.getBlock();
        if (block.canSustainPlant(state, worldIn, pos.down(), EnumFacing.UP, this)) return true;

        if (block == this) {
            return true;
        }
        return isBlockValidForPlacement(block);
    }

    public boolean isBlockValidForPlacement(Block block) {
        return (block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.SAND || block == Blocks.MYCELIUM);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumLeaves propLeaves = EnumLeaves.NO_LEAVES;
        boolean isThick = meta % 2 != 0;

        switch (meta) {
            case 2:
            case 3: propLeaves = EnumLeaves.LARGE_LEAVES;
            break;
            case 4:
            case 5: propLeaves = EnumLeaves.SMALL_LEAVES;
        }
        return getBlockState().getBaseState().withProperty(LEAVES, propLeaves).withProperty(THICK, isThick);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        boolean b = state.getValue(THICK);
        switch (state.getValue(LEAVES)) {
            case NO_LEAVES: {
                meta = b ? 1 : 0;
            }
            break;
            case LARGE_LEAVES: {
                meta = b ? 3 : 2;
            }
            break;
            case SMALL_LEAVES: {
                meta = b ? 5 : 4;
            }
        }
        return meta;
    }

    @Override public boolean isFullBlock(IBlockState state) { return false; }
    @Override public boolean isOpaqueCube(IBlockState state) { return false; }
    @Override public boolean isFullCube(IBlockState state) { return false; }
    @Override public boolean canEntitySpawn(IBlockState state, Entity entityIn) { return false; }
    @Override public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) { return false; }
    @Override public boolean isTopSolid(IBlockState state) { return false; }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, THICK, LEAVES);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public enum EnumLeaves implements IStringSerializable {
        NO_LEAVES("none"), SMALL_LEAVES("small"), LARGE_LEAVES("large");
        public String name;

        EnumLeaves(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Plains;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return this.getDefaultState();
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) { // Why is there an "isClient" param?
        Block self = worldIn.getBlockState(pos).getBlock();

        return self == Init.BAMBOO_STALK && !state.getValue(THICK) && (worldIn.getBlockState(pos.up()).getBlock().isReplaceable(worldIn, pos.up()) || worldIn.getBlockState(pos.up()).getBlock() == Init.BAMBOO_STALK);
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return this.canGrow(worldIn, pos, state, false);
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        grow(worldIn, rand, pos, false);
    }

    /**
     * Added override to make the bamboo thick always if param isOld is true
     */
    public void grow(World worldIn, Random rand, BlockPos pos, boolean isOld) {
        boolean old = isOld || worldIn.getBlockState(pos.up().up().up().up().up()).getBlock() == Init.BAMBOO_STALK;

        int growNumber = old ? rand.nextInt(7) + getStalkBlockPositions(worldIn, pos).length : rand.nextInt(7);

        for(int i = 0; i < growNumber; i++) {
            BlockPos posIn = new BlockPos(pos.getX(), pos.getY() + i + 1, pos.getZ());

            if(worldIn.getBlockState(posIn).getBlock().isReplaceable(worldIn, posIn)) {
                worldIn.setBlockState(posIn, getDefaultState().withProperty(THICK, old).withProperty(LEAVES, getLeaves(i + 1, rand)));
            }
        }
        if(old) {
            for (BlockPos posIn : getStalkBlockPositions(worldIn, pos)) {
                worldIn.setBlockState(posIn, worldIn.getBlockState(posIn).withProperty(THICK, true));
            }
        }
    }

    /**
     * Logic for leaves on the bamboo stalks
     */
    public EnumLeaves getLeaves(int i, Random random) {
        EnumLeaves leaves = EnumLeaves.NO_LEAVES;
        if(i > (5 - random.nextInt(3) + random.nextInt(2))) {
            leaves = EnumLeaves.LARGE_LEAVES;
        }
        else if(i > (2 - random.nextInt(1))) {
            leaves = EnumLeaves.SMALL_LEAVES;
        }
        return leaves;
    }

    /**
     * Gets the entire stalk of bamboo, somewhat RAM hungry
     */
    public BlockPos[] getStalkBlockPositions(World world, BlockPos pos) {
        List<BlockPos> list = new ArrayList<>();

        list.add(pos);
        int i = 0, j = 0;
        while (world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1 + i, pos.getZ())).getBlock() == Init.BAMBOO_STALK) {
            list.add(new BlockPos(pos.getX(), pos.getY() + 1 + i, pos.getZ()));
            i++;
        }
        while (world.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1 - j, pos.getZ())).getBlock() == Init.BAMBOO_STALK) {
            list.add(new BlockPos(pos.getX(), pos.getY() - 1 - j, pos.getZ()));
            j++;
        }

        return list.toArray(new BlockPos[0]);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isAreaLoaded(pos, 1)) return; // prevent growing cactus from loading unloaded chunks with block update - from BlockCactus

        boolean canGrow = rand.nextInt(2) == 0 && canGrow(worldIn, pos, state, false);

        if(ForgeHooks.onCropsGrowPre(worldIn, pos, state, canGrow)) {
            grow(worldIn, rand, pos, false);
            ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.getValue(THICK) ? THICK_AABB : THIN_AABB;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        if(state.getValue(LEAVES) == EnumLeaves.NO_LEAVES) {
            return getCollisionBoundingBox(state, world, pos);
        }
        else {
            return state.getValue(LEAVES) == EnumLeaves.LARGE_LEAVES ? LARGE_AABB : SMALL_AABB;
        }
    }
}