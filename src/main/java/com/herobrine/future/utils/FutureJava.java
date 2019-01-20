package com.herobrine.future.utils;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = FutureJava.MODID,
        name = FutureJava.MODNAME,
        version = FutureJava.VERSION,
        dependencies = "required-after:forge@[14.23.5.2768,)", useMetadata = true
)

public class FutureJava {

    public static final String MODID = "minecraftfuture";
    public static final String MODNAME = "Minecraft Future";
    public static final String VERSION = "0.0.7";

    @SidedProxy(clientSide = "com.herobrine.future.utils.ClientProxy",
                serverSide = "com.herobrine.future.utils.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static FutureJava instance;

    public static Logger logger;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        logger = event.getModLog();
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent e) {
        proxy.init(e);
        CommonProxy.init(e);
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}