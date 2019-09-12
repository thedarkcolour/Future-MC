package com.herobrine.future.block;

import com.herobrine.future.FutureMC;
import com.herobrine.future.init.FutureConfig;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import java.util.Random;

public class BlockBlueIce extends Block {
    public BlockBlueIce() {
        super(Material.ICE);
        setUnlocalizedName(FutureMC.ID + ".BlueIce");
        setRegistryName("blue_ice");
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.BUILDING_BLOCKS : FutureMC.TAB);
        setDefaultSlipperiness(0.989F);
        setHardness(2.8F);
        setSoundType(SoundType.GLASS);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }
}