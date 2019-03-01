package com.herobrine.future.utils.proxy;

import com.herobrine.future.blocks.NewWall;
import com.herobrine.future.blocks.StrippedLog;
import com.herobrine.future.utils.config.FutureConfig;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Init {
    public static final String MODID = "minecraftfuture";
    public static final List<String> variantsLog = Arrays.asList("oak", "spruce", "birch", "jungle", "acacia", "dark_oak");
    private static final List<String> variantsWall = Arrays.asList("brick", "granite", "andesite", "diorite", "sandstone", "red_sandstone", "stone_brick", "mossy_stone", "nether_brick", "nether_brick_red", "end_stone", "prismarine"); //12 values
    public static List<Block> strippedLogs = new ArrayList<>();
    public static List<Block> newwall = new ArrayList<>();
    public static CreativeTabs futuretab = new CreativeTabs("Future") {
        @Override    //New creative tab
        public ItemStack getTabIconItem() {
            return new ItemStack(Init.lantern);
        }
    };

    public static void init() {
        for (String variant: variantsLog) {
            strippedLogs.add(new StrippedLog(variant));
        }
        for (String variant : variantsWall) {
            newwall.add(new NewWall(variant));
        }
    }

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
    public static com.herobrine.future.items.SuspiciousStew suspiciousstew;

    @GameRegistry.ObjectHolder("minecraftfuture:Stonecutter")
    public static com.herobrine.future.blocks.Stonecutter stonecutter;

    @GameRegistry.ObjectHolder("minecraftfuture:loom")
    public static com.herobrine.future.blocks.Loom loom;

    @GameRegistry.ObjectHolder("minecraftfuture:Barrel")
    public static com.herobrine.future.blocks.Barrel barrel;

    @GameRegistry.ObjectHolder("minecraftfuture:berrybush")
    public static com.herobrine.future.blocks.BerryBush berrybush;

    @GameRegistry.ObjectHolder("minecraftfuture:sweetberry")
    public static com.herobrine.future.items.ItemBerry sweetberry;

    @GameRegistry.ObjectHolder("minecraftfuture:campfire")
    public static com.herobrine.future.blocks.Campfire campfire;

    @GameRegistry.ObjectHolder("minecraftfuture:dye")
    public static com.herobrine.future.items.ItemDye dye;

    @GameRegistry.ObjectHolder("minecraftfuture:smoothstone")
    public static com.herobrine.future.blocks.SmoothStone smoothstone;

    private static void model(Item item, int metadata, ModelResourceLocation model) { //Shortened method
        ModelLoader.setCustomModelResourceLocation(item, metadata, model);
    }

    @SubscribeEvent //renders everything
    public static void initModel() {   /*      REMEMBER TO CHECK COMMON PROXY IF GAME CRASHES      */
        if (FutureConfig.a.trident) trident.models();
        if (FutureConfig.a.lantern) lantern.models();
        if (FutureConfig.b.cornflower) flowerblue.models();
        if (FutureConfig.b.lily) flowerwhite.models();
        if (FutureConfig.b.witherrose) flowerblack.models();
        if (FutureConfig.b.suspiciousstew) suspiciousstew.models();

        if (FutureConfig.a.stonecutter) {
            if(!FutureConfig.a.oldstonecutter) {
                stonecutter.models();
            }/*
            else {
                OldStonecutter.models();
            }*/
        }

        if (FutureConfig.a.loom) loom.models();
        if (FutureConfig.a.barrel) barrel.models();
        if (FutureConfig.a.berrybush) berrybush.models();
        if (FutureConfig.a.sweetberry && FutureConfig.a.berrybush) sweetberry.models();
        if (FutureConfig.a.campfire) campfire.models();
        if (FutureConfig.b.dyes) dye.models();
        if (FutureConfig.a.smoothstone) smoothstone.models();

        if (FutureConfig.a.strippedlogs) {
            for (Block block : Init.strippedLogs) {
                model(Item.getItemFromBlock(block), 0, new ModelResourceLocation( Item.getItemFromBlock(block).getRegistryName(), "inventory"));
            }
        }
        if (FutureConfig.a.newwallvariants) {
            for (Block block : Init.newwall) {
                model(Item.getItemFromBlock(block), 0, new ModelResourceLocation( Item.getItemFromBlock(block).getRegistryName(), "inventory"));
            }
        }
    }
}