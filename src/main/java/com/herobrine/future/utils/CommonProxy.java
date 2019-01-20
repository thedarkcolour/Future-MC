package com.herobrine.future.utils;

import com.herobrine.future.blocks.*;
import com.herobrine.future.blocks.tile.TileEntityBarrel;
import com.herobrine.future.items.*;
import com.herobrine.future.utils.worldgen.WorldGenFlower;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;

@Mod.EventBusSubscriber
public class CommonProxy {
    public static Configuration config;

    public static void preInit(FMLPreInitializationEvent e) {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "minecraftfuture.cfg"));
        Config.readConfig();
    }
    public static void init(FMLInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(FutureJava.instance, new GuiHandler());
        GameRegistry.registerWorldGenerator(new WorldGenFlower(), 0);
    }
    public static void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
                config.save();
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {    //registers tile
        if (Config.lant) event.getRegistry().register(new Lantern());
        if (Config.stonec) event.getRegistry().register(new Stonecutter());
        if (Config.barl) event.getRegistry().register(new Barrel());
        if (Config.bluef) event.getRegistry().register(new FlowerBlue());
        if (Config.lily) event.getRegistry().register(new FlowerWhite());
        if (Config.wrose) event.getRegistry().register(new FlowerBlack());
        if (Config.berrybush) event.getRegistry().register(new BerryBush());
        if (Config.loom) event.getRegistry().register(new Loom());
        if (Config.barl) GameRegistry.registerTileEntity(TileEntityBarrel.class, Init.MODID + ":containerbarrel");
        if (Config.campfire) event.getRegistry().register(new Campfire());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {  //registers items
        if (Config.trident) event.getRegistry().register(new Trident("Trident", Init.TRIDENT, Init.futuretab));
        if (Config.lant) event.getRegistry().register(new ItemBlock(Init.lantern).setRegistryName(Init.lantern.getRegistryName()));
        if (Config.bluef) event.getRegistry().register(new ItemBlock(Init.flowerblue).setRegistryName(Init.flowerblue.getRegistryName()));
        if (Config.lily) event.getRegistry().register(new ItemBlock(Init.flowerwhite).setRegistryName(Init.flowerwhite.getRegistryName()));
        if (Config.wrose) event.getRegistry().register(new ItemBlock(Init.flowerblack).setRegistryName(Init.flowerblack.getRegistryName()));
        if (Config.susstew) event.getRegistry().register(new SuspiciousStew("SuspiciousStew", 6, 0.6F, false));
        if (Config.berrybush) event.getRegistry().register(new ItemBerry(2, 0.2F, false));
        if (Config.dyes && Config.dyeb) event.getRegistry().register(new DyeBlue());
        if (Config.dyes && Config.dyew) event.getRegistry().register(new DyeWhite());
        if (Config.dyes && Config.dyebr) event.getRegistry().register(new DyeBrown());
        if (Config.dyes && Config.dyebk) event.getRegistry().register(new DyeBlack());
        if (Config.stonec) event.getRegistry().register(new ItemBlock(Init.stonecutter).setRegistryName(Init.stonecutter.getRegistryName()));
        if (Config.loom) event.getRegistry().register(new ItemBlock(Init.loom).setRegistryName(Init.loom.getRegistryName()));
        if (Config.barl) event.getRegistry().register(new ItemBlock(Init.barrel).setRegistryName(Init.barrel.getRegistryName()));
        if (Config.campfire) event.getRegistry().register(new ItemBlock(Init.campfire).setRegistryName(Init.campfire.getRegistryName()));
    }
}