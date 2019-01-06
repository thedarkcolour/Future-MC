package com.herobrine.future.items;

import com.herobrine.future.utils.init;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSword;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class trident extends ItemSword {
    private final float attackDamage;
    public trident(String name, ToolMaterial material, CreativeTabs tab) {
        super(material);
        setRegistryName(name);
        setUnlocalizedName(init.MODID + ".trident");
        setCreativeTab(tab);
        this.attackDamage = material.getAttackDamage();
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}