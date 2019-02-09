package com.herobrine.future.utils;

import com.herobrine.future.blocks.NewWall;
import com.herobrine.future.blocks.StrippedLog;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Init {
    public static final Item.ToolMaterial TRIDENT = EnumHelper.addToolMaterial("TRIDENT", 3, 250, 9f, 5.0f, 22);
    public static final String MODID = "minecraftfuture";
    public static final List<String> variantsLog = Arrays.asList("oak", "spruce", "birch", "jungle", "acacia", "dark_oak");
    public static final List<String> variantsWall = Arrays.asList("brick", "granite", "andesite", "diorite", "sandstone", "red_sandstone", "stone_brick", "mossy_stone", "nether_brick", "nether_brick_red", "end_stone", "prismarine"); //12 values
    public static List<Block> strippedLogs = new ArrayList<>();
    public static List<Block> newwall = new ArrayList<>();
    public static CreativeTabs futuretab = new CreativeTabs("Future") {
        @Override    //New creative tab
        public ItemStack func_78016_d() {
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

    @GameRegistry.ObjectHolder("minecraftfuture:berrybush")
    public static com.herobrine.future.blocks.BerryBush berrybush;

    @GameRegistry.ObjectHolder("minecraftfuture:sweetberry")
    public static com.herobrine.future.items.ItemBerry sweetberry;

    @GameRegistry.ObjectHolder("minecraftfuture:campfire")
    public static com.herobrine.future.blocks.Campfire campfire;

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
        if (Config.berrybush) berrybush.initModel();
        if (Config.berrybush) sweetberry.initModel();
        if (Config.campfire) campfire.initModel();
        if (Config.striplog) {
            for (Block block : Init.strippedLogs) {
                ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(block), 0, new ModelResourceLocation( Item.func_150898_a(block).getRegistryName(), "inventory"));
            }
        }
        if (Config.newwall) {
            for (Block block : Init.newwall) {
                ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(block), 0, new ModelResourceLocation( Item.func_150898_a(block).getRegistryName(), "inventory"));
            }
        }
    }
}
