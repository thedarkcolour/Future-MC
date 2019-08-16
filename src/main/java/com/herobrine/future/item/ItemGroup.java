package com.herobrine.future.item;

import com.herobrine.future.init.FutureConfig;
import com.herobrine.future.init.Init;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemGroup extends CreativeTabs {
    public ItemGroup() {
        super("Future");
    }

    @Override
    public ItemStack getTabIconItem() {
        return FutureConfig.general.lantern ? new ItemStack(Init.LANTERN) : FutureConfig.general.bamboo ? new ItemStack(Init.BAMBOO_ITEM) : new ItemStack(Blocks.GRASS);
    }

    @Override
    public void displayAllRelevantItems(NonNullList<ItemStack> p_78018_1_) {
        super.displayAllRelevantItems(p_78018_1_);
    }
}