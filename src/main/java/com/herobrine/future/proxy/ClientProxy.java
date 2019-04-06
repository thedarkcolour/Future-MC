package com.herobrine.future.proxy;

import com.herobrine.future.entity.Entities;
import com.herobrine.future.init.InitModels;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy implements IProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        Entities.initModels();
    }

    @Override
    public void init(FMLInitializationEvent e) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {

    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        InitModels.initModel();
    }
}