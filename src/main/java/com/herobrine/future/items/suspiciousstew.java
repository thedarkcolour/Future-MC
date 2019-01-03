package com.herobrine.future.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class suspiciousstew extends ItemFood {
    public suspiciousstew(String name, int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation,isWolfFood);
        setMaxStackSize(1);
        setRegistryName(name);
        setUnlocalizedName(name);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
