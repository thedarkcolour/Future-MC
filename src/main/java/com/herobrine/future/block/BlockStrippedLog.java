package com.herobrine.future.block;

import com.herobrine.future.FutureMC;
import com.herobrine.future.init.FutureConfig;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;

import java.util.Arrays;
import java.util.List;

public class BlockStrippedLog extends BlockLog {
    public static List<String> variants = Arrays.asList("acacia", "jungle", "birch", "oak", "spruce", "dark_oak");
    public BlockStrippedLog(String variant) {
        super();
        setUnlocalizedName(FutureMC.ID + ".stripped_" + variant + "_log");
        setRegistryName("stripped_" + variant + "_log");
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.BUILDING_BLOCKS : FutureMC.CREATIVE_TAB);
        this.setDefaultState(getBlockState().getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
    }

    @SuppressWarnings("ConstantConditions")
    public void model() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Item.getItemFromBlock(this).getRegistryName(), "inventory"));
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this));
    }

    @Override
    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return true;
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 5;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = this.getDefaultState();

        switch (meta & 12) {
            case 0:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
                break;
            case 4:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
                break;
            case 8:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
                break;
            default:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
        } return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;

        switch (state.getValue(LOG_AXIS)) {
            case X:
                meta |= 4;
                break;
            case Z:
                meta |= 8;
                break;
            case NONE:
                meta |= 12;
        } return meta;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LOG_AXIS);
    }
}
