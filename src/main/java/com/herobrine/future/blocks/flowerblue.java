package com.herobrine.future.blocks;

import com.herobrine.future.items.futureItems;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class flowerblue extends BlockBush { //Adds blue flower
    public flowerblue() {
        super(Material.PLANTS);
        setRegistryName("flowerblue");
        setUnlocalizedName(futureItems.MODID + ".flowerblue");
        setCreativeTab(futureItems.futuretab);
        setSoundType(SoundType.PLANT);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
