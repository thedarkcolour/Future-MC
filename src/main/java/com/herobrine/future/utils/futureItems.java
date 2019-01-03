package com.herobrine.future.utils;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class futureItems {
    public static final String MODID = "minecraftfuture";
    public static CreativeTabs futuretab = new CreativeTabs("Future") {
        @Override    //New creative tab
        public ItemStack getTabIconItem() {
            return new ItemStack(futureBlocks.lantern);
        }
    };

    @GameRegistry.ObjectHolder("minecraftfuture:trident")
    public static com.herobrine.future.items.trident trident;    //Trident

    @GameRegistry.ObjectHolder("minecraftfuture:dyeblue")
    public static com.herobrine.future.items.dyeblue dyeblue;    //Blue dye

    @GameRegistry.ObjectHolder("minecraftfuture:dyewhite")
    public static com.herobrine.future.items.dyewhite dyewhite;

    @GameRegistry.ObjectHolder("minecraftfuture:dyeblack")
    public static com.herobrine.future.items.dyeblack dyeblack;

    @GameRegistry.ObjectHolder("minecraftfuture:suspiciousstew")
    public static com.herobrine.future.items.suspiciousstew suspiciousstew;

    @SideOnly(Side.CLIENT)
    public static void initModel() {
        trident.initModel();
        dyeblue.initModel();
        dyewhite.initModel();
        dyeblack.initModel();
        //suspiciousstew.initModel();
    }
}