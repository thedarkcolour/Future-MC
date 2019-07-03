package com.herobrine.future.blocks;

import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.init.Init;
import com.herobrine.future.sound.Sounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import scala.actors.threadpool.Arrays;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockBamboo extends BlockBase implements IPlantable, IGrowable {
    public static final PropertyBool THICK = PropertyBool.create("thick");
    public static final PropertyEnum<BlockBamboo.EnumLeaves> LEAVES = PropertyEnum.create("leaves", BlockBamboo.EnumLeaves.class);
    public static final PropertyBool MATURE = PropertyBool.create("mature");
    private static final AxisAlignedBB THICK_AABB = makeAABB(6.5, 0, 6.5, 9.5, 16, 9.5);
    private static final AxisAlignedBB THIN_AABB = makeAABB(7, 0, 7, 9, 16, 9);
    private static final AxisAlignedBB LARGE_AABB = makeAABB(5, 0, 5, 11, 16, 11);
    private static final AxisAlignedBB SMALL_AABB = makeAABB(5.5, 0, 5.5, 10.5, 16, 10.5);


    public BlockBamboo() {
        super(new BlockProperties("Bamboo", Material.PLANTS, Sounds.BAMBOO));
        setDefaultState(getBlockState().getBaseState().withProperty(THICK, false).withProperty(LEAVES, EnumLeaves.NO_LEAVES).withProperty(MATURE, false));
        setTickRandomly(true);
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.MISC : Init.FUTURE_MC_TAB);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if(worldIn.getBlockState(pos.up()).getBlock() == Init.BAMBOO_STALK) {
            worldIn.destroyBlock(pos.up(), true);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.canPlaceBlockAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
        if(worldIn.getBlockState(pos.up()).getBlock() == Init.BAMBOO_STALK) {
            if(worldIn.getBlockState(pos.up()).getValue(THICK) && !worldIn.getBlockState(pos).getValue(THICK)) {
                if(worldIn instanceof World) {
                    worldIn.setBlockState(pos, getDefaultState()
                            .withProperty(THICK, true)
                            .withProperty(MATURE, worldIn.getBlockState(pos).getValue(MATURE))
                            .withProperty(LEAVES, worldIn.getBlockState(pos).getValue(LEAVES)));
                }
            }
        }
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if(worldIn.getBlockState(pos.offset(facing, -1)).getBlock() == Init.BAMBOO_STALK) {
            boolean thick = worldIn.getBlockState(pos.offset(facing, -1)).getValue(THICK);
            return this.getDefaultState().withProperty(THICK, thick);
        }
        else return getStateFromMeta(meta);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return isBlockValidForPlacement(worldIn.getBlockState(pos.down()).getBlock());
    }

    public boolean isBlockValidForPlacement(Block block) {
        return (block == Blocks.GRASS || block instanceof BlockDirt || block == Blocks.SAND || block == Blocks.GRAVEL || block == Init.BAMBOO_STALK || block == Blocks.MYCELIUM);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if(meta > 5) {
            if(meta > 8) {
                return getDefaultState().withProperty(LEAVES, (EnumLeaves) Arrays.asList(EnumLeaves.values()).get(meta - 9)).withProperty(MATURE, true).withProperty(THICK, true);
            } else {
                return getDefaultState().withProperty(LEAVES, (EnumLeaves) Arrays.asList(EnumLeaves.values()).get(meta - 6)).withProperty(MATURE, true).withProperty(THICK, false);
            }
        } else {
            if(meta > 2) {
                return getDefaultState().withProperty(LEAVES, (EnumLeaves) Arrays.asList(EnumLeaves.values()).get(meta - 3)).withProperty(MATURE, false).withProperty(THICK, true);
            } else {
                return getDefaultState().withProperty(LEAVES, (EnumLeaves) Arrays.asList(EnumLeaves.values()).get(meta)).withProperty(MATURE, false).withProperty(THICK, false);}
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;

        if(state.getValue(MATURE)) meta += 6;
        switch (state.getValue(LEAVES)) {
            case NO_LEAVES: break;
            case SMALL_LEAVES: meta += 1; break;
            case LARGE_LEAVES: meta += 2;
        }
        if(state.getValue(THICK)) meta += 3;

        return meta;
    }

    @Override public boolean isFullBlock(IBlockState state) { return false; }
    @Override public boolean isOpaqueCube(IBlockState state) { return false; }
    @Override public boolean isFullCube(IBlockState state) { return false; }
    @Override public boolean canEntitySpawn(IBlockState state, Entity entityIn) { return false; }
    @Override public boolean isSideSolid(IBlockState base_state, IBlockAccess worldIn, BlockPos pos, EnumFacing side) { return false; }
    @Override public boolean isTopSolid(IBlockState state) { return false; }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, THICK, LEAVES, MATURE);
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
    public EnumPlantType getPlantType(IBlockAccess worldIn, BlockPos pos) {
        return EnumPlantType.Plains;
    }

    @Override
    public IBlockState getPlant(IBlockAccess worldIn, BlockPos pos) {
        return this.getDefaultState();
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) { // Why is there an "isClient" param?
        int i = this.numOfAboveBamboo(worldIn, pos);
        int j = this.numOfBelowBamboo(worldIn, pos);
        return i + j + 1 < 16 && !worldIn.getBlockState(pos.up(i)).getValue(MATURE)
                && worldIn.isAirBlock(pos.up(i + 1));
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return true;
    }

    @Override
    public void grow(World worldIn, Random random, BlockPos pos, @Nullable IBlockState state) {
        int i = numOfAboveBamboo(worldIn, pos);
        int j = numOfBelowBamboo(worldIn, pos);
        int k = i + j + 1;
        int l = 1 + random.nextInt(2);

        for(int i1 = 0; i1 < l; ++i1) {
            BlockPos blockpos = pos.up(i);
            IBlockState blockstate = worldIn.getBlockState(blockpos);
            if (k >= 16 || blockstate.getValue(MATURE) || !worldIn.isAirBlock(blockpos.up())) {
                return;
            }

            updateStalk(worldIn, blockpos, random, k);
            ++i;
            ++k;
        }
    }

    protected void updateStalk(World worldIn, BlockPos pos, Random random, int blocks) {
        IBlockState down = worldIn.getBlockState(pos.down());
        IBlockState down2 = worldIn.getBlockState(pos.down(2));
        EnumLeaves leaves = EnumLeaves.NO_LEAVES;
        if (blocks >= 1) {
            if (down.getBlock() == Init.BAMBOO_STALK && down.getValue(LEAVES) != EnumLeaves.NO_LEAVES) {
                if (down.getBlock() == Init.BAMBOO_STALK && down.getValue(LEAVES) != EnumLeaves.NO_LEAVES) {
                    leaves = EnumLeaves.LARGE_LEAVES;
                    if (down2.getBlock() == Init.BAMBOO_STALK) {
                        worldIn.setBlockState(pos.down(), down.withProperty(LEAVES, EnumLeaves.SMALL_LEAVES), 3);
                        worldIn.setBlockState(pos.down(2), down2.withProperty(LEAVES, EnumLeaves.NO_LEAVES), 3);
                    }
                }
            } else {
                leaves = EnumLeaves.SMALL_LEAVES;
            }
        }

        boolean thick = numOfBelowBamboo(worldIn, pos) > 2;
        if(down.getBlock() == Init.BAMBOO_STALK) thick = numOfBelowBamboo(worldIn, pos) > 2 || down.getValue(THICK);
        boolean mature = (blocks < 11 || !(random.nextFloat() < 0.25F)) && blocks != 15;
        worldIn.setBlockState(pos.up(), getDefaultState()
                .withProperty(THICK, thick).withProperty(LEAVES, leaves).withProperty(MATURE, !mature), 3);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        if (!canPlaceBlockAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        } else if (!state.getValue(MATURE)) {
            if (random.nextInt(3) == 0 && worldIn.isAirBlock(pos.up()) && worldIn.getLightFromNeighbors(pos.up()) >= 9) {
                int i = numOfBelowBamboo(worldIn, pos) + 1;
                if (i < 16) {
                    updateStalk(worldIn, pos, random, i);
                }
            }

        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        Vec3d vec3d = state.getOffset(worldIn, pos);
        return state.getValue(THICK) ? THICK_AABB.offset(vec3d) : THIN_AABB.offset(vec3d);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if(state.getValue(LEAVES) == EnumLeaves.NO_LEAVES) {
            return getCollisionBoundingBox(state, worldIn, pos);
        }
        else {
            Vec3d vec3d = state.getOffset(worldIn, pos);
            return state.getValue(LEAVES) == EnumLeaves.LARGE_LEAVES ? LARGE_AABB.offset(vec3d) : SMALL_AABB.offset(vec3d);
        }
    }

    @Override
    public EnumOffsetType getOffsetType() {
        return EnumOffsetType.XZ;
    }

    protected int numOfAboveBamboo(World worldIn, BlockPos pos) {
        int i = 0;
        while (i < 16 && worldIn.getBlockState(pos.up(i + 1)).getBlock() == Init.BAMBOO_STALK) {
            ++i;
        }

        return i;
    }

    protected int numOfBelowBamboo(World worldIn, BlockPos pos) {
        int i = 0;
        while (i < 16 && worldIn.getBlockState(pos.down(i + 1)).getBlock() == Init.BAMBOO_STALK) {
            ++i;
        }

        return i;
    }
}