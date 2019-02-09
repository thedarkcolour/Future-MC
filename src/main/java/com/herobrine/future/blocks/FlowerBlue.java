package com.herobrine.future.blocks;

import com.herobrine.future.utils.Init;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FlowerBlue extends BlockBush { //Adds blue flower
    public FlowerBlue() {
        super(Material.field_151585_k);
        setRegistryName("FlowerBlue");
        func_149663_c(Init.MODID + ".FlowerBlue");
        func_149647_a(Init.futuretab);
        func_149672_a(SoundType.field_185850_c);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public boolean func_176196_c(World worldIn, BlockPos pos) {
        return super.func_176196_c(worldIn, pos);
    }
}
