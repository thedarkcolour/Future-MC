
package com.herobrine.future.blocks;

import com.herobrine.future.utils.Config;
import com.herobrine.future.utils.Init;
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
    //public static final PropertyInteger lit = PropertyInteger.create("lit", 0, 1);
    public static final PropertyBool lit = PropertyBool.func_177716_a("lit");
    public Campfire() {
        super(Material.field_151575_d);
        func_149663_c(Init.MODID + ".Campfire");
        setRegistryName("Campfire");
        func_149647_a(Init.futuretab);
        func_149672_a(SoundType.field_185848_a);
        func_149711_c(2.0F);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(lit, Boolean.valueOf(true)));
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @SideOnly(Side.CLIENT)
    public void func_180655_c(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        double d0 = (double)pos.func_177958_n() + rand.nextDouble() * 0.5D + 0.2D;
        double d1 = (double)pos.func_177956_o() + rand.nextDouble() * 0.35D + 0.2D;
        double d2 = (double)pos.func_177952_p() + rand.nextDouble() * 0.5D + 0.2D;
        if(worldIn.func_180495_p(pos) == this.func_176223_P()){
            worldIn.func_175688_a(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            if(rand.nextInt(20) == 0) {
                worldIn.func_175688_a(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public int func_149750_m(IBlockState state) {
        if (state.func_177229_b(lit)) {
            return 9;
        } else {
            return 0;
        }
    }

    @Override
    public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        Block block = worldIn.func_180495_p(pos.func_177984_a()).func_177230_c();
        if (block instanceof BlockLiquid | block instanceof BlockFluidBase) {
            return super.func_180642_a(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).func_177226_a(lit, false);
        } else {
            return super.func_180642_a(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).func_177226_a(lit, true);
        }
    }

    @Override
    public void func_180634_a(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if(Config.campfiredmg) {
            if (entityIn instanceof EntityLivingBase) {
                entityIn.func_70097_a(DamageSource.field_76372_a, 1.0F);
            }
        }
    }

    @Override
    public void func_176213_c(World worldIn, BlockPos pos, IBlockState state) {
        this.func_189540_a(state, worldIn, pos, worldIn.func_180495_p(pos).func_177230_c(), pos);
    }

    @Override
    public boolean func_180639_a(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.func_184586_b(hand);
        Block block = worldIn.func_180495_p(pos.func_177984_a()).func_177230_c();
        if (worldIn.func_180495_p(pos) != this.func_176223_P()) {
            if (stack.func_77973_b() == Items.field_151033_d) {
                if (!(block instanceof BlockLiquid | block instanceof BlockFluidBase)) {
                    worldIn.func_175656_a(pos, withLit(true));
                    stack.func_77972_a(1, playerIn);
                }
            }
        } return false;
    }

    @Override
    public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        Block block = worldIn.func_180495_p(pos.func_177984_a()).func_177230_c();
        if (block instanceof BlockFluidBase | block instanceof BlockLiquid) {
            worldIn.func_175656_a(pos, withLit(false));
        }
    }

    @Override
    public Item func_180660_a(IBlockState state, Random rand, int fortune) {
        return Items.field_151044_h;
    }

    @Override
    public boolean func_176196_c(World worldIn, BlockPos pos) {
        return worldIn.func_180495_p(pos.func_177977_b()).func_185896_q();
    }

    @Override
    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, lit);
    }

    @Override
    public IBlockState func_176203_a(int meta) {
        return this.func_176223_P();
    }

    @Override
    public int func_176201_c(IBlockState state) {
        if (state.func_177229_b(lit)) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess source, BlockPos pos) {
        return false;
    }

    @Override
    public boolean func_149637_q(IBlockState state) {
        return false;
    }

    @Override
    public boolean func_149730_j(IBlockState state) {
        return false;
    }

    @Override
    public boolean func_149662_c(IBlockState state) {
        return false;
    }

    @Override
    public boolean func_185481_k(IBlockState state) {
        return false;
    }

    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public BlockRenderLayer func_180664_k() {
        return BlockRenderLayer.CUTOUT;
    }

    public IBlockState withLit(boolean lit) {
        return this.func_176223_P().func_177226_a(this.lit, lit);
    }

    public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
        return boundingBox;
    }

    public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) { return BlockFaceShape.UNDEFINED; }
}
