package com.herobrine.future.blocks;

import com.herobrine.future.utils.IModel;
import com.herobrine.future.utils.proxy.Init;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Loom extends BlockHorizontalBase implements IModel {
    private static final PropertyDirection FACING = BlockHorizontal.FACING;
    public Loom() {
        super(Material.WOOD);
        setUnlocalizedName(Init.MODID + ".Loom");
        setRegistryName("Loom");
        setCreativeTab(Init.futuretab);
        setSoundType(SoundType.WOOD);
        setHardness(2.0F);

        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void models() {
        model(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return true;
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 5;
    }
}