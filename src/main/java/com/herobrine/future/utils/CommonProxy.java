package com.herobrine.future.utils;

import com.herobrine.future.blocks.*;
import com.herobrine.future.items.dyeblack;
import com.herobrine.future.items.dyeblue;
import com.herobrine.future.items.dyewhite;
import com.herobrine.future.items.trident;
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

import java.io.File;

@Mod.EventBusSubscriber
public class CommonProxy {
    public static Configuration config;

    public void preInit(FMLPreInitializationEvent e) {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "minecraftfuture.cfg"));
        Config.readConfig();
    }
    public void init(FMLInitializationEvent e) { }
    public void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
                config.save();
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {    //registers blocks
        if (Config.lanternenabled) event.getRegistry().register(new lantern());
        if (Config.stonecutterenabled) event.getRegistry().register(new stonecutter());
        if (Config.cornflowerenabled) event.getRegistry().register(new flowerblue());
        if (Config.lilyenabled) event.getRegistry().register(new flowerwhite());
        if (Config.witherroseenabled) event.getRegistry().register(new flowerblack());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {  //registers items
        if (Config.lanternenabled) event.getRegistry().register(new ItemBlock(futureBlocks.lantern).setRegistryName(futureBlocks.lantern.getRegistryName()));
        if (Config.stonecutterenabled) event.getRegistry().register(new ItemBlock(futureBlocks.stonecutter).setRegistryName(futureBlocks.stonecutter.getRegistryName()));
        if (Config.cornflowerenabled) event.getRegistry().register(new ItemBlock(futureBlocks.flowerblue).setRegistryName(futureBlocks.flowerblue.getRegistryName()));
        if (Config.lilyenabled) event.getRegistry().register(new ItemBlock(futureBlocks.flowerwhite).setRegistryName(futureBlocks.flowerwhite.getRegistryName()));
        if (Config.witherroseenabled) event.getRegistry().register(new ItemBlock(futureBlocks.flowerblack).setRegistryName(futureBlocks.flowerblack.getRegistryName()));
        if (Config.tridentenabled) event.getRegistry().register(new trident());
        if (Config.dyesenabled && Config.dyeblueenabled) event.getRegistry().register(new dyeblue());
        if (Config.dyesenabled && Config.dyewhiteenabled) event.getRegistry().register(new dyewhite());
        if (Config.dyesenabled && Config.dyeblackenabled) event.getRegistry().register(new dyeblack());
    }
}