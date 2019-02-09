package com.herobrine.future.blocks;

import com.herobrine.future.utils.Init;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StrippedLog extends BlockLog {

    public StrippedLog(String variant) {
        super();
        func_180632_j(field_176227_L.func_177621_b().func_177226_a(field_176299_a, BlockLog.EnumAxis.Y));
        func_149663_c(Init.MODID + ".stripped_" + variant + "_log");
        setRegistryName("stripped_" + variant + "_log");
        func_149647_a(Init.futuretab);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public void func_149666_a(CreativeTabs itemIn, NonNullList<ItemStack> items) {
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
    public IBlockState func_176203_a(int meta) {
        IBlockState state = this.func_176223_P();

        switch (meta & 12) {
            case 0:
                state = state.func_177226_a(field_176299_a, BlockLog.EnumAxis.Y);
                break;
            case 4:
                state = state.func_177226_a(field_176299_a, BlockLog.EnumAxis.X);
                break;
            case 8:
                state = state.func_177226_a(field_176299_a, BlockLog.EnumAxis.Z);
                break;
            default:
                state = state.func_177226_a(field_176299_a, BlockLog.EnumAxis.NONE);
        } return state;
    }

    @Override
    public int func_176201_c(IBlockState state) {
        int meta = 0;

        switch (state.func_177229_b(field_176299_a)) {
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
    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] {field_176299_a});
    }
}
