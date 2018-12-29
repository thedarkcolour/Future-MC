package com.herobrine.future.proxy;

import com.herobrine.future.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) { }
    public void init(FMLInitializationEvent e) { }
    public void postInit(FMLPostInitializationEvent e) {

    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new lantern());//registers blocks
        event.getRegistry().register(new stonecutter());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(futureBlocks.lantern).setRegistryName(futureBlocks.lantern.getRegistryName()));    //registry of blocks as items
        event.getRegistry().register(new ItemBlock(futureBlocks.stonecutter).setRegistryName(futureBlocks.stonecutter.getRegistryName()));
    }
}