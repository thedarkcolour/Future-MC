package com.herobrine.future.blocks;

import com.herobrine.future.utils.FutureJava;
import com.herobrine.future.utils.Init;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
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

public class Lantern extends Block {

    public static final ResourceLocation lantern = new ResourceLocation(FutureJava.MODID, "Lantern");
    public static final PropertyDirection FACING = PropertyDirection.func_177714_a("facing");
    private static final AxisAlignedBB SITTING_AABB = new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 0.5625, 0.6875);
    private static final AxisAlignedBB HANGING_AABB = new AxisAlignedBB(0.3125,0.0625,0.3125,0.6875,0.625,0.6875);

    public Lantern() {
        super(Material.field_151573_f);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(FACING, EnumFacing.UP));
        func_149663_c(Init.MODID + ".Lantern");
        setRegistryName("Lantern");
        func_149647_a(Init.futuretab);
        func_149715_a(1);
        func_149711_c(5);
        func_149672_a(SoundType.field_185852_e);
        setHarvestLevel("pickaxe", 1);
    }


    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    //Overrides
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (worldIn.func_180495_p(pos.func_177977_b()).func_177230_c() == Blocks.field_150350_a) {
            return this.func_176223_P().func_177226_a(FACING, EnumFacing.DOWN);
        } else {
            for (EnumFacing enumfacing : EnumFacing.Plane.VERTICAL) {
                if (this.canAttachTo(worldIn, pos.func_177972_a(enumfacing.func_176734_d()), enumfacing)) {
                    return this.func_176223_P().func_177226_a(FACING, enumfacing);
                }
            }
            return this.func_176223_P();
        }
    }

    @Override
    protected BlockStateContainer func_180661_e() { return new BlockStateContainer(this, FACING); }

    @Override
    public IBlockState func_176203_a(int meta) { return this.func_176223_P().func_177226_a(FACING, EnumFacing.func_82600_a(meta & 7)); }

    @Override
    public int func_176201_c(IBlockState state) { return state.func_177229_b(FACING).func_176745_a(); }


    @Override
    public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!func_176196_c(worldIn, pos)) {
            this.func_176226_b(worldIn, pos, state, 0);
            worldIn.func_175698_g(pos);
        }
    }

    @Override
    public boolean func_149730_j(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess source, BlockPos pos) { return false; }

    @Override
    public boolean func_149686_d(IBlockState state) {
        return false;
    }

    @Override
    public boolean func_149662_c(IBlockState state) { return false; }

    @Override
    public boolean func_185481_k(IBlockState state) {
        return false;
    }

    @Override
    public BlockRenderLayer func_180664_k() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override    /**Place-able on given side**/
    public boolean func_176196_c(World worldIn, BlockPos pos) {
        return this.canPlaceAt(worldIn, pos);
    }
    //private booleans
    private boolean canAttachTo(World worldIn, BlockPos pos, EnumFacing facing) {
        IBlockState state = worldIn.func_180495_p(pos);
        boolean flag = func_193382_c(state.func_177230_c());
        return !flag && this.func_176196_c(worldIn, pos);
    }

    private boolean canPlaceAt(World worldIn, BlockPos pos) {
        BlockPos blockpos = pos.func_177977_b();
        IBlockState state = worldIn.func_180495_p(blockpos);
        Block block = state.func_177230_c();
        Block blockdown = worldIn.func_180495_p(blockpos).func_177230_c();
        return block != this && !((worldIn.func_180495_p(blockpos).func_177230_c() == Blocks.field_150350_a || BlockBush.class.isAssignableFrom(blockdown.getClass())) & worldIn.func_180495_p(pos.func_177984_a()).func_177230_c() == Blocks.field_150350_a);
    }

    public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch(state.func_177229_b(FACING)) {
            case UP:
                return SITTING_AABB;
            case DOWN:
                return HANGING_AABB;
            default:
                return SITTING_AABB;
        }
    }

    public BlockFaceShape func_193383_a(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) { return BlockFaceShape.UNDEFINED; }
}
