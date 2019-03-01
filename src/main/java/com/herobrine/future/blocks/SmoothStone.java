package com.herobrine.future.blocks;

import com.herobrine.future.FutureJava;
import com.herobrine.future.utils.blocks.IModel;
import com.herobrine.future.utils.proxy.Init;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SmoothStone extends Block implements IModel {
    public SmoothStone() {
        super(Material.ROCK);
        setUnlocalizedName(FutureJava.MODID + ".SmoothStone");
        setRegistryName("SmoothStone");
        setCreativeTab(Init.futuretab);
        setSoundType(SoundType.STONE);
        setHardness(3.0F);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void models() {
        model(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
