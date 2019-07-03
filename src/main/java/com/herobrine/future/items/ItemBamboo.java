package com.herobrine.future.items;

import com.herobrine.future.init.Init;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

public class ItemBamboo extends ItemBlock {
    public ItemBamboo() {
        super(Init.BAMBOO_STALK);
        setUnlocalizedName(Init.MODID + ".Bamboo");
        setRegistryName("Bamboo");
    }

    @Override
    public int getItemBurnTime(ItemStack itemStack) {
        return 50;
    }

    @SideOnly(Side.CLIENT)
    public void model() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(Objects.requireNonNull(getRegistryName()), "inventory"));
    }
}