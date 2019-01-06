package com.herobrine.future.utils;

import com.herobrine.future.blocks.*;
import com.herobrine.future.blocks.blocks.TileEntityBarrel;
import com.herobrine.future.items.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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
        NetworkRegistry.INSTANCE.registerGuiHandler(futurejava.instance, new GuiHandler());
    }
    public static void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
                config.save();
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {    //registers blocks
        if (Config.lant) event.getRegistry().register(new lantern());
        if (Config.stonec) event.getRegistry().register(new stonecutter());
        if (Config.barl) event.getRegistry().register(new barrel());
        if (Config.bluef) event.getRegistry().register(new flowerblue());
        if (Config.lily) event.getRegistry().register(new flowerwhite());
        if (Config.wrose) event.getRegistry().register(new flowerblack());
        if (Config.barl) GameRegistry.registerTileEntity(TileEntityBarrel.class, init.MODID + "_testcontainerblock");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {  //registers items
        if (Config.trident) event.getRegistry().register(new trident("trident", init.TRIDENT, init.futuretab));
        if (Config.lant) event.getRegistry().register(new ItemBlock(init.lantern).setRegistryName(init.lantern.getRegistryName()));
        if (Config.bluef) event.getRegistry().register(new ItemBlock(init.flowerblue).setRegistryName(init.flowerblue.getRegistryName()));
        if (Config.lily) event.getRegistry().register(new ItemBlock(init.flowerwhite).setRegistryName(init.flowerwhite.getRegistryName()));
        if (Config.wrose) event.getRegistry().register(new ItemBlock(init.flowerblack).setRegistryName(init.flowerblack.getRegistryName()));
        if (Config.susstew) event.getRegistry().register(new suspiciousstew("suspiciousstew", 6, 0.6F, false));
        if (Config.dyes && Config.dyeb) event.getRegistry().register(new dyeblue());
        if (Config.dyes && Config.dyew) event.getRegistry().register(new dyewhite());
        if (Config.dyes && Config.dyebk) event.getRegistry().register(new dyeblack());
        if (Config.stonec) event.getRegistry().register(new ItemBlock(init.stonecutter).setRegistryName(init.stonecutter.getRegistryName()));
        if (Config.barl) event.getRegistry().register(new ItemBlock(init.barrel).setRegistryName(init.barrel.getRegistryName()));
    }
}