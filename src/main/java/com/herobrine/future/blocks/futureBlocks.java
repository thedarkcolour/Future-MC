package com.herobrine.future.blocks;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class futureBlocks {
    public static final String MODID = "minecraftfuture";

    @GameRegistry.ObjectHolder("minecraftfuture:lantern")
    public static com.herobrine.future.blocks.lantern lantern;          //Lantern

    @GameRegistry.ObjectHolder("minecraftfuture:stonecutter")
    public static com.herobrine.future.blocks.stonecutter stonecutter;  //Stonecutter

    @GameRegistry.ObjectHolder("minecraftfuture:flowerblue")
    public static com.herobrine.future.blocks.flowerblue flowerblue;    //Cornflower

    @GameRegistry.ObjectHolder("minecraftfuture:flowerwhite")
    public static com.herobrine.future.blocks.flowerwhite flowerwhite;  //Lily of the Valley

    @GameRegistry.ObjectHolder("minecraftfuture:flowerblack")
    public static com.herobrine.future.blocks.flowerblack flowerblack;  //Wither Rose

    @SideOnly(Side.CLIENT)
    public static void initModel() {
        stonecutter.initModel();   //renders blocks
        lantern.initModel();
        flowerblue.initModel();
        flowerwhite.initModel();
        flowerblack.initModel();
    }
}