package com.herobrine.future.blocks;

import com.herobrine.future.init.Init;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBase extends Block {
    public BlockProperties properties;

    public BlockBase(BlockProperties properties) {
        super(properties.material());
        this.properties = properties;
        setUnlocalizedName(Init.MODID + "." + properties.registryName());
        setRegistryName(properties.registryName());
        setCreativeTab(Init.futuretab);
        setSoundType(properties.soundType());
        setHardness(3.0F);
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    public void model() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
