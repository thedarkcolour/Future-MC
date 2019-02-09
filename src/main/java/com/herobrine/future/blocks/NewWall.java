package com.herobrine.future.blocks;

import com.herobrine.future.utils.Init;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class NewWall extends Block {
    public static final PropertyBool UP = PropertyBool.func_177716_a("up");
    public static final PropertyBool NORTH = PropertyBool.func_177716_a("north");
    public static final PropertyBool EAST = PropertyBool.func_177716_a("east");
    public static final PropertyBool SOUTH = PropertyBool.func_177716_a("south");
    public static final PropertyBool WEST = PropertyBool.func_177716_a("west");
    protected static final AxisAlignedBB[] AABB_BY_INDEX = new AxisAlignedBB[] {new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.3125D, 0.0D, 0.0D, 0.6875D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.3125D, 1.0D, 0.875D, 0.6875D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
    protected static final AxisAlignedBB[] CLIP_AABB_BY_INDEX = new AxisAlignedBB[] {AABB_BY_INDEX[0].func_186666_e(1.5D), AABB_BY_INDEX[1].func_186666_e(1.5D), AABB_BY_INDEX[2].func_186666_e(1.5D), AABB_BY_INDEX[3].func_186666_e(1.5D), AABB_BY_INDEX[4].func_186666_e(1.5D), AABB_BY_INDEX[5].func_186666_e(1.5D), AABB_BY_INDEX[6].func_186666_e(1.5D), AABB_BY_INDEX[7].func_186666_e(1.5D), AABB_BY_INDEX[8].func_186666_e(1.5D), AABB_BY_INDEX[9].func_186666_e(1.5D), AABB_BY_INDEX[10].func_186666_e(1.5D), AABB_BY_INDEX[11].func_186666_e(1.5D), AABB_BY_INDEX[12].func_186666_e(1.5D), AABB_BY_INDEX[13].func_186666_e(1.5D), AABB_BY_INDEX[14].func_186666_e(1.5D), AABB_BY_INDEX[15].func_186666_e(1.5D)};

    public NewWall(String variant) {
        super(Material.field_151576_e);
        func_149663_c(Init.MODID+ "." + variant + "_wall");
        setRegistryName(variant + "_wall");
        func_149672_a(SoundType.field_185851_d);
        func_149647_a(Init.futuretab);
    }

    private static int getAABBIndex(IBlockState state) {
        int i = 0;

        if (state.func_177229_b(NORTH).booleanValue()) {
            i |= 1 << EnumFacing.NORTH.func_176736_b();
        }

        if (state.func_177229_b(EAST).booleanValue()) {
            i |= 1 << EnumFacing.EAST.func_176736_b();
        }

        if (state.func_177229_b(SOUTH).booleanValue()) {
            i |= 1 << EnumFacing.SOUTH.func_176736_b();
        }

        if (state.func_177229_b(WEST).booleanValue()) {
            i |= 1 << EnumFacing.WEST.func_176736_b();
        } return i;
    }

    @Override
    public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
        state = this.func_176221_a(state, source, pos);
        return AABB_BY_INDEX[getAABBIndex(state)];
    }

    @Override
    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] {UP, NORTH, EAST, WEST, SOUTH});
    }

    @Override
    public boolean func_149662_c(IBlockState state) {
        return false;
    }

    @Override
    public boolean func_149686_d(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean func_176225_a(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.DOWN ? super.func_176225_a(blockState, blockAccess, pos, side) : true;
    }

    @Override
    public void func_185477_a(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        if (!isActualState) {
            state = this.func_176221_a(state, worldIn, pos);
        }

        func_185492_a(pos, entityBox, collidingBoxes, CLIP_AABB_BY_INDEX[getAABBIndex(state)]);
    }

    @Override
    public IBlockState func_176221_a(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        boolean flag =  canWallConnectTo(worldIn, pos, EnumFacing.NORTH);
        boolean flag1 = canWallConnectTo(worldIn, pos, EnumFacing.EAST);
        boolean flag2 = canWallConnectTo(worldIn, pos, EnumFacing.SOUTH);
        boolean flag3 = canWallConnectTo(worldIn, pos, EnumFacing.WEST);
        boolean flag4 = flag && !flag1 && flag2 && !flag3 || !flag && flag1 && !flag2 && flag3;
        return state.func_177226_a(UP, Boolean.valueOf(!flag4 || !worldIn.func_175623_d(pos.func_177984_a()))).func_177226_a(NORTH, Boolean.valueOf(flag)).func_177226_a(EAST, Boolean.valueOf(flag1)).func_177226_a(SOUTH, Boolean.valueOf(flag2)).func_177226_a(WEST, Boolean.valueOf(flag3));
    }

    @Override
    public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        return canConnectTo(world, pos.func_177972_a(facing), facing.func_176734_d());
    }

    private boolean canWallConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        BlockPos other = pos.func_177972_a(facing);
        Block block = world.func_180495_p(other).func_177230_c();
        return block.canBeConnectedTo(world, other, facing.func_176734_d()) || canConnectTo(world, other, facing.func_176734_d());
    }

    private boolean canConnectTo(IBlockAccess worldIn, BlockPos pos, EnumFacing p_176253_3_) {
        IBlockState iblockstate = worldIn.func_180495_p(pos);
        Block block = iblockstate.func_177230_c();
        BlockFaceShape blockfaceshape = iblockstate.func_193401_d(worldIn, pos, p_176253_3_);
        boolean flag = blockfaceshape == BlockFaceShape.MIDDLE_POLE_THICK || blockfaceshape == BlockFaceShape.MIDDLE_POLE && block instanceof BlockFenceGate;
        return !isExceptBlockForAttachWithPiston(block) && blockfaceshape == BlockFaceShape.SOLID || flag;
    }

    protected static boolean isExceptBlockForAttachWithPiston(Block block) {
        return Block.func_193382_c(block) || block == Blocks.field_180401_cv || block == Blocks.field_150440_ba || block == Blocks.field_150423_aK || block == Blocks.field_150428_aP;
    }

    @Override
    public IBlockState func_176203_a(int meta) {
        return this.func_176223_P();
    }

    @Override
    public int func_176201_c(IBlockState state) {
        return 0;
    }
}
