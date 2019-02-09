package com.herobrine.future.blocks;

import com.herobrine.future.blocks.tile.TileEntityStonecutter;
import com.herobrine.future.utils.Config;
import com.herobrine.future.utils.FutureJava;
import com.herobrine.future.utils.Init;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Stonecutter extends Block implements ITileEntityProvider {
    private static final AxisAlignedBB boundingBox = new AxisAlignedBB(0,0,0,1,0.5625,1);
    public static final PropertyDirection FACING = BlockHorizontal.field_185512_D;
    public static final ResourceLocation stonecutter = new ResourceLocation(FutureJava.MODID, "Stonecutter");
    public static final int GUI_ID = 2;

    public Stonecutter() {
        super(Material.field_151576_e);
        func_149663_c(Init.MODID + ".Stonecutter");
        setRegistryName("Stonecutter");
        func_149647_a(Init.futuretab);
        func_180632_j(field_176227_L.func_177621_b().func_177226_a(FACING, EnumFacing.NORTH));
        setHarvestLevel("pickaxe", 0);
        func_149711_c(5.0F);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.func_176223_P().func_177226_a(FACING, placer.func_174811_aO().func_176734_d());
    }

    @Override
    public boolean func_180639_a(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (Config.stonecutterfunctions) {
            if(worldIn.field_72995_K) {
                return true;
            } else {
                TileEntity tileEntity = worldIn.func_175625_s(pos);

                //if (tileEntity instanceof TileEntityStonecutter) {
                    playerIn.func_180468_a((TileEntityStonecutter)tileEntity);
                //}

                return false;
            }
        } else return false;
    }

    @Override
    public IBlockState func_176203_a(int meta) {
        return this.func_176223_P().func_177226_a(FACING, EnumFacing.func_82600_a(meta & 7));
    }

    @Override
    public int func_176201_c(IBlockState state) {
        return state.func_177229_b(FACING).func_176745_a();
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
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public BlockRenderLayer func_180664_k() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean func_185481_k(IBlockState state) {
        return false;
    }

    @Override
    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public TileEntity func_149915_a(World world, int meta) {
        if (Config.stonecutterfunctions) {
            return new TileEntityStonecutter();
        } else {
            return null;
        }
    }

    public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
        return boundingBox;
    }

    public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) { return BlockFaceShape.UNDEFINED; }
}
