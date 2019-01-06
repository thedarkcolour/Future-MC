package com.herobrine.future.utils;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class init {
    public static final Item.ToolMaterial TRIDENT = EnumHelper.addToolMaterial("TRIDENT", 3, 250, 9f, 5.0f, 22);
    public static final String MODID = "minecraftfuture";
    public static CreativeTabs futuretab = new CreativeTabs("Future") {
        @Override    //New creative tab
        public ItemStack getTabIconItem() {
            return new ItemStack(init.lantern);
        }};

    @GameRegistry.ObjectHolder("minecraftfuture:trident")
    public static com.herobrine.future.items.trident trident;    //Trident

    @GameRegistry.ObjectHolder("minecraftfuture:lantern")
    public static com.herobrine.future.blocks.lantern lantern;          //Lantern

    @GameRegistry.ObjectHolder("minecraftfuture:flowerblue")
    public static com.herobrine.future.blocks.flowerblue flowerblue;    //Cornflower

    @GameRegistry.ObjectHolder("minecraftfuture:flowerwhite")
    public static com.herobrine.future.blocks.flowerwhite flowerwhite;  //Lily of the Valley

    @GameRegistry.ObjectHolder("minecraftfuture:flowerblack")
    public static com.herobrine.future.blocks.flowerblack flowerblack;  //Wither Rose

    @GameRegistry.ObjectHolder("minecraftfuture:suspiciousstew")
    public static com.herobrine.future.items.suspiciousstew suspiciousstew;  //Suspicious stew

    @GameRegistry.ObjectHolder("minecraftfuture:dyeblue")
    public static com.herobrine.future.items.dyeblue dyeblue;    //Blue dye

    @GameRegistry.ObjectHolder("minecraftfuture:dyewhite")
    public static com.herobrine.future.items.dyewhite dyewhite; //White Dye

    @GameRegistry.ObjectHolder("minecraftfuture:dyeblack")
    public static com.herobrine.future.items.dyeblack dyeblack; //Black Dye

    @GameRegistry.ObjectHolder("minecraftfuture:stonecutter")
    public static com.herobrine.future.blocks.stonecutter stonecutter;  //Stonecutter

    @GameRegistry.ObjectHolder("minecraftfuture:barrel")
    public static com.herobrine.future.blocks.barrel barrel;    //Barrel

    @SideOnly(Side.CLIENT)
    public static void initModel() {//renders everything
        if (Config.trident) trident.initModel();
        if (Config.lant) lantern.initModel();
        if (Config.bluef) flowerblue.initModel();
        if (Config.lily) flowerwhite.initModel();
        if (Config.wrose) flowerblack.initModel();
        if (Config.susstew) suspiciousstew.initModel();
        if (Config.dyeb) dyeblue.initModel();
        if (Config.dyew) dyewhite.initModel();
        if (Config.dyebk) dyeblack.initModel();
        if (Config.stonec) stonecutter.initModel();
        if (Config.barl) barrel.initModel();
    }
}