package com.herobrine.future.blocks;

import com.herobrine.future.utils.Init;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BerryBush extends BlockBush implements IGrowable {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);
    protected static final AxisAlignedBB AGE0 = new AxisAlignedBB(0.3, 0.0D, 0.3, 0.7, 0.5, 0.7);
    protected static final AxisAlignedBB AGE3 = new AxisAlignedBB(0.1, 0, 0.1, 0.9, 0.975,0.9);

    public BerryBush() {
        super(Material.PLANTS);
        setUnlocalizedName(Init.MODID + ".berrybush");
        setRegistryName("berrybush");
        setSoundType(SoundType.PLANT);
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.getAgeProperty(), Integer.valueOf(0)));
        this.setTickRandomly(true);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AGE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch(state.getValue(AGE)) {
            case 0: return AGE0;
            default: return AGE3;
        }
    }

    public int getMaxAge() { return 7; }

    public boolean isMaxAge(IBlockState state) {
        return state.getValue(this.getAgeProperty()) >= this.getMaxAge();
    }

    protected int getAge(IBlockState state) {
        return state.getValue(this.getAgeProperty()).intValue();
    }

    public IBlockState withAge(int age) {
        return this.getDefaultState().withProperty(this.getAgeProperty(), Integer.valueOf(age));
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Init.sweetberry);
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) { return !this.isMaxAge(state); }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) { return true; }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        int age = this.getAge(state);
        int mage = this.getMaxAge();

        if (age > mage) {
            age = mage;
        }
        worldIn.setBlockState(pos, this.withAge(age + 1));
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        boolean canGrow = (rand.nextInt(20) == 0);

        if (worldIn.getLightFromNeighbors(pos) >= 8) {
            if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, canGrow)) {
                int age = state.getValue(AGE).intValue();
                if (age < 3) {
                    worldIn.setBlockState(pos, this.getDefaultState().withProperty(AGE, Integer.valueOf(age + 1)), 2);
                }
                ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
            }
        }
    }

    protected PropertyInteger getAgeProperty() { return AGE; }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        Item item = playerIn.getHeldItem(EnumHand.MAIN_HAND).getItem();
        if (item != Items.DYE)
            if (item != Init.sweetberry) {
                if (worldIn.getBlockState(pos).getBlock().getMetaFromState(state) == 2) {
                    worldIn.setBlockState(pos, withAge(1));
                    spawnAsEntity(worldIn, pos, new ItemStack(Init.sweetberry));
                }
            }
        if (worldIn.getBlockState(pos).getBlock().getMetaFromState(state) == 3) {
            worldIn.setBlockState(pos, withAge(1));
            Random r = new Random();
            spawnAsEntity(worldIn, pos, new ItemStack(Init.sweetberry, 3));
        }
        return false;
    }
}
