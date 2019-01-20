package com.herobrine.future.blocks;

import com.herobrine.future.utils.Config;
import com.herobrine.future.utils.FutureJava;
import com.herobrine.future.utils.Init;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

public class Campfire extends Block {
    private static final AxisAlignedBB boundingBox = new AxisAlignedBB(0,0,0,1,0.3125,1);
    public static final PropertyInteger lit = PropertyInteger.create("lit", 0, 1);
    public Campfire() {
        super(Material.WOOD);
        setUnlocalizedName(Init.MODID + ".Campfire");
        setRegistryName("Campfire");
        setCreativeTab(Init.futuretab);
        setSoundType(SoundType.WOOD);
        setHardness(2.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(lit, Integer.valueOf(1)));
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        double d0 = (double)pos.getX() + rand.nextDouble() * 0.5D + 0.2D;
        double d1 = (double)pos.getY() + rand.nextDouble() * 0.31D + 0.2D;
        double d2 = (double)pos.getZ() + rand.nextDouble() * 0.5D + 0.2D;
        if(worldIn.getBlockState(pos) == this.getDefaultState()){
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            if(rand.nextInt(20) == 0) {
                worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        this.setLightLevel(0.5625F);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if(Config.campfiredmg) {
            if (entityIn instanceof EntityLivingBase) {
                entityIn.attackEntityFrom(DamageSource.IN_FIRE, 1.0F);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        if (worldIn.getBlockState(pos) != this.getDefaultState()) {
            if (stack.getItem() == Items.FLINT_AND_STEEL) {
                if (!(block instanceof BlockLiquid | block instanceof BlockFluidBase)) {
                    worldIn.setBlockState(pos, withLit(1));
                    stack.damageItem(1, playerIn);
                    this.setLightLevel(0.5625F);
                }
            }
        } return false;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        if (block instanceof BlockFluidBase | block instanceof BlockLiquid) {
            worldIn.setBlockState(pos, withLit(0));
            this.setLightLevel(0F);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.COAL;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).isTopSolid();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, lit);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(lit);
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess source, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
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
    public boolean isTopSolid(IBlockState state) {
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

    public IBlockState withLit(int lit) {
        return this.getDefaultState().withProperty(this.lit, lit);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return boundingBox;
    }
}