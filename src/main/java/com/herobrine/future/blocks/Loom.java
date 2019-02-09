package com.herobrine.future.blocks;

import com.herobrine.future.utils.Init;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Loom extends Block {
    public static final PropertyDirection FACING = BlockHorizontal.field_185512_D;
    public Loom() {
        super(Material.field_151575_d);
        func_149663_c(Init.MODID + ".Loom");
        setRegistryName("Loom");
        func_149647_a(Init.futuretab);
        func_149672_a(SoundType.field_185848_a);
        func_149711_c(2.0F);
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
    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public IBlockState func_176203_a(int meta) {
        return this.func_176223_P().func_177226_a(FACING, EnumFacing.func_82600_a(meta & 7));
    }

    @Override
    public int func_176201_c(IBlockState state) {
        return state.func_177229_b(FACING).func_176745_a();
    }
}
