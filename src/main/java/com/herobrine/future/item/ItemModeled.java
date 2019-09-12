package com.herobrine.future.item;

import com.herobrine.future.FutureMC;
import com.herobrine.future.client.Modeled;
import net.minecraft.item.Item;

public class ItemModeled extends Item implements Modeled {
    /*public ItemModeled() {
        addModel();
    }*/

    public ItemModeled(String regName) {
        setUnlocalizedName(FutureMC.ID + "." + regName);
        setRegistryName(regName);
        addModel();
    }
}