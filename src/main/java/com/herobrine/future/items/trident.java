package com.herobrine.future.items;

import com.herobrine.future.futurejava;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class trident extends Item {
    private final float attackDamage;

    public trident() {
        setRegistryName("trident");
        setUnlocalizedName(futurejava.MODID + ".trident");
        setCreativeTab(futureItems.futuretab);
        this.attackDamage = 9F;
        }

        @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}