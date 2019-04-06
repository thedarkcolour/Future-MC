package com.herobrine.future.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy implements IProxy {
    public static final String PATH = "com.herobrine.future.proxy.ServerProxy";

    @Override
    public void preInit(FMLPreInitializationEvent e) {

    }

    @Override
    public void init(FMLInitializationEvent e) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {

    }

    public static final String getPath() {
        return ServerProxy.class.getCanonicalName();
    }
}
