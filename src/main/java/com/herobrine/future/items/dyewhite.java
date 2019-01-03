package com.herobrine.future.items;

import com.herobrine.future.utils.futureItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class dyewhite extends Item {
    public dyewhite() {
        setUnlocalizedName(futureItems.MODID + ".dyewhite");
        setRegistryName("dyewhite");
        setCreativeTab(futureItems.futuretab);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}