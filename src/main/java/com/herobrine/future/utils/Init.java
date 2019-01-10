package com.herobrine.future.utils;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Init {
    public static final Item.ToolMaterial TRIDENT = EnumHelper.addToolMaterial("TRIDENT", 3, 250, 9f, 5.0f, 22);
    public static final String MODID = "minecraftfuture";
    public static CreativeTabs futuretab = new CreativeTabs("Future") {
        @Override    //New creative tab
        public ItemStack getTabIconItem() {
            return new ItemStack(Init.lantern);
        }};

    @GameRegistry.ObjectHolder("minecraftfuture:Trident")
    public static com.herobrine.future.items.Trident trident;    //Trident

    @GameRegistry.ObjectHolder("minecraftfuture:Lantern")
    public static com.herobrine.future.blocks.Lantern lantern;          //Lantern

    @GameRegistry.ObjectHolder("minecraftfuture:FlowerBlue")
    public static com.herobrine.future.blocks.FlowerBlue flowerblue;    //Cornflower

    @GameRegistry.ObjectHolder("minecraftfuture:FlowerWhite")
    public static com.herobrine.future.blocks.FlowerWhite flowerwhite;  //Lily of the Valley

    @GameRegistry.ObjectHolder("minecraftfuture:FlowerBlack")
    public static com.herobrine.future.blocks.FlowerBlack flowerblack;  //Wither Rose

    @GameRegistry.ObjectHolder("minecraftfuture:SuspiciousStew")
    public static com.herobrine.future.items.SuspiciousStew suspiciousstew;  //Suspicious stew

    @GameRegistry.ObjectHolder("minecraftfuture:DyeBlue")
    public static com.herobrine.future.items.DyeBlue dyeblue;    //Blue dye

    @GameRegistry.ObjectHolder("minecraftfuture:DyeWhite")
    public static com.herobrine.future.items.DyeWhite dyewhite; //White Dye

    @GameRegistry.ObjectHolder("minecraftfuture:DyeBrown")
    public static com.herobrine.future.items.DyeBrown dyebrown;    //Brown Dye

    @GameRegistry.ObjectHolder("minecraftfuture:DyeBlack")
    public static com.herobrine.future.items.DyeBlack dyeblack; //Black Dye

    @GameRegistry.ObjectHolder("minecraftfuture:Stonecutter")
    public static com.herobrine.future.blocks.Stonecutter stonecutter;  //Stonecutter

    @GameRegistry.ObjectHolder("minecraftfuture:loom")
    public static com.herobrine.future.blocks.Loom loom;

    @GameRegistry.ObjectHolder("minecraftfuture:Barrel")
    public static com.herobrine.future.blocks.Barrel barrel;    //Barrel

    @SubscribeEvent //renders everything
    public static void initModel() {   /**      REMEMBER TO CHECK COMMON PROXY IF GAME CRASHES      **/
        if (Config.trident) trident.initModel();
        if (Config.lant) lantern.initModel();
        if (Config.bluef) flowerblue.initModel();
        if (Config.lily) flowerwhite.initModel();
        if (Config.wrose) flowerblack.initModel();
        if (Config.susstew) suspiciousstew.initModel();
        if (Config.dyeb) dyeblue.initModel();
        if (Config.dyew) dyewhite.initModel();
        if (Config.dyebr) dyebrown.initModel();
        if (Config.dyebk) dyeblack.initModel();
        if (Config.stonec) stonecutter.initModel();
        if (Config.loom) loom.initModel();
        if (Config.barl) barrel.initModel();
        }
    }