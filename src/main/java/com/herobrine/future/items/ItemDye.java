package com.herobrine.future.items;

import com.herobrine.future.utils.IModel;
import com.herobrine.future.utils.proxy.Init;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDye extends Item implements IModel {
    public ItemDye() {
        setHasSubtypes(true);
        setMaxDamage(0);
        setUnlocalizedName(Init.MODID + "dye");
        setRegistryName("dye");
        setCreativeTab(Init.futuretab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void models() {
        model(this, 0, new ModelResourceLocation(getRegistryName() + "white", "inventory"));
        model(this, 1, new ModelResourceLocation(getRegistryName() + "blue", "inventory"));
        model(this, 2, new ModelResourceLocation(getRegistryName() + "brown", "inventory"));
        model(this, 3, new ModelResourceLocation(getRegistryName() + "black", "inventory"));
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case 1:
                return "Blue Dye";
            case 2:
                return "Brown Dye";
            case 3:
                return "Black Dye";
            default:
                return "White Dye";
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(this.isInCreativeTab(tab)) {
            for(int i = 0; i < 4; ++i) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }
}
