package com.herobrine.future.utils;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) { super.preInit(e); }

    @Override
    public void init(FMLInitializationEvent e) {
        oredict.registerOres();
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        futureBlocks.initModel();   //calls model init
        futureItems.initModel();
    }
}

