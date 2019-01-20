package com.herobrine.future.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        Init.initModel();   //calls model Init
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent event) {
        ResourceLocation sprite = new ResourceLocation("minecraftfuture", "particle/campfire");
        event.getMap().registerSprite(sprite);
    }
}