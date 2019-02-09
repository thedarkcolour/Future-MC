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
    public static final PropertyInteger AGE = PropertyInteger.func_177719_a("age", 0, 3);
    protected static final AxisAlignedBB AGE0 = new AxisAlignedBB(0.3, 0.0D, 0.3, 0.7, 0.5, 0.7);
    protected static final AxisAlignedBB AGE3 = new AxisAlignedBB(0.1, 0, 0.1, 0.9, 0.975,0.9);

    public BerryBush() {
        super(Material.field_151585_k);
        func_149663_c(Init.MODID + ".berrybush");
        setRegistryName("berrybush");
        func_149672_a(SoundType.field_185850_c);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(this.getAgeProperty(), Integer.valueOf(0)));
        this.func_149675_a(true);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, AGE);
    }

    @Override
    public int func_176201_c(IBlockState state) {
        return state.func_177229_b(AGE);
    }

    @Override
    public IBlockState func_176203_a(int meta) {
        return this.func_176223_P();
    }

    @Override
    public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch(state.func_177229_b(AGE)) {
            case 0: return AGE0;
            default: return AGE3;
        }
    }

    public int getMaxAge() { return 7; }

    public boolean isMaxAge(IBlockState state) {
        return state.func_177229_b(this.getAgeProperty()) >= this.getMaxAge();
    }

    protected int getAge(IBlockState state) {
        return state.func_177229_b(this.getAgeProperty()).intValue();
    }

    public IBlockState withAge(int age) {
        return this.func_176223_P().func_177226_a(this.getAgeProperty(), age);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Init.sweetberry);
    }

    @Override
    public boolean func_176473_a(World worldIn, BlockPos pos, IBlockState state, boolean isClient) { return !this.isMaxAge(state); }

    @Override
    public boolean func_180670_a(World worldIn, Random rand, BlockPos pos, IBlockState state) { return true; }

    @Override
    public void func_176474_b(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        int age = this.getAge(state);
        int mage = this.getMaxAge();

        if (age > mage) {
            age = mage;
        }
        worldIn.func_175656_a(pos, this.withAge(age + 1));
    }

    @Override
    public void func_180650_b(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        boolean canGrow = (rand.nextInt(20) == 0);

        if (worldIn.func_175671_l(pos) >= 8) {
            if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, canGrow)) {
                int age = state.func_177229_b(AGE).intValue();
                if (age < 3) {
                    worldIn.func_180501_a(pos, this.func_176223_P().func_177226_a(AGE, Integer.valueOf(age + 1)), 2);
                }
                ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.func_180495_p(pos));
            }
        }
    }

    protected PropertyInteger getAgeProperty() { return AGE; }

    @Override
    public boolean func_176196_c(World worldIn, BlockPos pos) {
        return super.func_176196_c(worldIn, pos);
    }

    @Override
    public boolean func_180639_a(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.field_72995_K) {
            return true;
        }
        Item item = playerIn.func_184586_b(EnumHand.MAIN_HAND).func_77973_b();
        if (item != Items.field_151100_aR)
            if (item != Init.sweetberry) {
                if (worldIn.func_180495_p(pos).func_177230_c().func_176201_c(state) == 2) {
                    worldIn.func_175656_a(pos, withAge(1));
                    func_180635_a(worldIn, pos, new ItemStack(Init.sweetberry));
                }
            }
        if (worldIn.func_180495_p(pos).func_177230_c().func_176201_c(state) == 3) {
            worldIn.func_175656_a(pos, withAge(1));
            func_180635_a(worldIn, pos, new ItemStack(Init.sweetberry, 3));
        }
        return false;
    }
}
