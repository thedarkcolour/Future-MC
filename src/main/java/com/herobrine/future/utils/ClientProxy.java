package com.herobrine.future.utils;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public static void preInit(FMLPreInitializationEvent e) {
        preInit(e);
    }

    public static void init(FMLInitializationEvent e) {
        OreDict.registerOres();
        OreDict.registerOreDictEntries();
        NetworkRegistry.INSTANCE.registerGuiHandler(FutureJava.instance, new GuiHandler());
    }

    public static void postInit(FMLPostInitializationEvent e) {
        postInit(e);
    }



    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        Init.initModel();   //calls model Init
    }
}