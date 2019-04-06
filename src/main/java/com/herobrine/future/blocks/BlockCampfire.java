package com.herobrine.future.blocks;

import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.tile.campfire.TileCampfire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockCampfire extends BlockBase {
    private static final AxisAlignedBB boundingBoxOld = new AxisAlignedBB(0,0,0,1,0.3125,1);
    private static final AxisAlignedBB boundingBoxNew = new AxisAlignedBB(0,0,0,1,0.375,1);
    public static final PropertyBool NEW = PropertyBool.create("new");
    public static final PropertyBool LIT = PropertyBool.create("lit");

    public BlockCampfire() {
        super(new BlockProperties("Campfire", Material.WOOD));
        setSoundType(SoundType.WOOD);
        setHardness(2.0F);
        this.setDefaultState(getBlockState().getBaseState().withProperty(LIT, true).withProperty(NEW, FutureConfig.general.oldCampfire));
    }

    @SuppressWarnings("ConstantConditions")
    public void model() {
        if(FutureConfig.general.oldCampfire) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
        }
        else {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName() + "_new", "inventory"));
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LIT, NEW);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        double d0 = (double)pos.getX() + rand.nextDouble() * 0.5D + 0.2D;
        double d1 = (double)pos.getY() + rand.nextDouble() * 0.35D + 0.2D;
        double d2 = (double)pos.getZ() + rand.nextDouble() * 0.5D + 0.2D;
        if(worldIn.getBlockState(pos) == this.getDefaultState()){
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            if(rand.nextInt(20) == 0) {
                worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public int getLightValue(IBlockState state) {
        return state.getValue(LIT) ? 15 : 0;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        if (block instanceof BlockLiquid | block instanceof BlockFluidBase) {
            return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(LIT, false).withProperty(NEW, !FutureConfig.general.oldCampfire);
        } else {
            return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(LIT, true).withProperty(NEW, !FutureConfig.general.oldCampfire);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        if (worldIn.getBlockState(pos) != this.getDefaultState()) {
            if (stack.getItem() == Items.FLINT_AND_STEEL) {
                if (!(block instanceof BlockLiquid | block instanceof BlockFluidBase)) {
                    worldIn.setBlockState(pos, this.getBlockState().getBaseState().withProperty(LIT, true).withProperty(NEW, state.getValue(NEW)));
                    stack.damageItem(1, playerIn);
                }
            }
        }
        return false;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        if (block instanceof BlockFluidBase | block instanceof BlockLiquid) {
            worldIn.setBlockState(pos, this.getBlockState().getBaseState().withProperty(LIT, false).withProperty(NEW, state.getValue(NEW)));
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return !FutureConfig.general.oldCampfire ? boundingBoxNew : boundingBoxOld;
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if(FutureConfig.general.campfireDMG) {
            if (entityIn instanceof EntityLivingBase) {
                if(worldIn.getBlockState(pos) == this.getDefaultState()) {
                    entityIn.attackEntityFrom(DamageSource.IN_FIRE, 1.0F);
                }
            }
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) { // Handles properties
        switch (meta) {
            default: return this.getBlockState().getBaseState().withProperty(LIT, false).withProperty(NEW, false);
            case 1: return this.getBlockState().getBaseState().withProperty(LIT, true).withProperty(NEW, false);
            case 2: return this.getBlockState().getBaseState().withProperty(LIT, false).withProperty(NEW, true);
            case 3: return this.getBlockState().getBaseState().withProperty(LIT, true).withProperty(NEW, true);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if(state.getValue(NEW)) {
            return state.getValue(LIT) ? 3 : 2;
        }
        else {
            return state.getValue(LIT) ? 1 : 0;
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.neighborChanged(state, worldIn, pos, worldIn.getBlockState(pos).getBlock(), pos);
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess source, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isTopSolid(IBlockState state) {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileCampfire();
    }

    public boolean canStayLit(World world, BlockPos pos) {
        if(world.getBiome(pos).canRain() && world.isRaining() && world.canSeeSky(pos)) {
            return false;
        }
        return !(world.getBlockState(pos.up()).getBlock() instanceof BlockFluidBase);
    }
}