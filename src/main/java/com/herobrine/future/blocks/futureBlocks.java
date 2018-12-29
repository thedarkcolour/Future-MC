package com.herobrine.future.blocks;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class futureBlocks {

    @GameRegistry.ObjectHolder("minecraftfuture:lantern")
    public static com.herobrine.future.blocks.lantern lantern;    //Lantern

    @GameRegistry.ObjectHolder("minecraftfuture:stonecutter")
    public static com.herobrine.future.blocks.stonecutter stonecutter;    //Stonecutter

    @SideOnly(Side.CLIENT)
    public static void initModel() {
        stonecutter.initModel();   //renders blocks
        lantern.initModel();
    }
}
