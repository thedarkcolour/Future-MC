package com.herobrine.future.item;

import com.herobrine.future.FutureMC;
import com.herobrine.future.client.Modeled;
import com.herobrine.future.init.Init;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBamboo extends ItemBlock implements Modeled {
    public ItemBamboo() {
        super(Init.BAMBOO_STALK);
        setUnlocalizedName(FutureMC.MODID + ".Bamboo");
        setRegistryName("Bamboo");
        addModel();
    }

    @Override
    public int getItemBurnTime(ItemStack itemStack) {
        return 50;
    }
}