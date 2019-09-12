package com.herobrine.future.init.proxy;

import com.herobrine.future.client.particle.EnumParticleType;
import com.herobrine.future.client.tesr.bell.TESRBell;
import com.herobrine.future.entity.Entities;
import com.herobrine.future.init.InitModels;
import com.herobrine.future.tile.TileBell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.ParticleManager;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.Map;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @SubscribeEvent
    public static void registerModels(final ModelRegistryEvent event) {
        InitModels.initModel();
    }

    @SubscribeEvent
    public static void registerTextureAtlasSprites(final TextureStitchEvent event) {

    }

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        Entities.initModels();
        ClientRegistry.bindTileEntitySpecialRenderer(TileBell.class, new TESRBell());
    }

    @Override
    public void init(FMLInitializationEvent e) {
        try {
            Field field = ParticleManager.class.getDeclaredField("particleTypes");
            field.setAccessible(true);
            //noinspection unchecked
            int id = ((Map<Integer, IParticleFactory>) field.get(Minecraft.getMinecraft().effectRenderer)).size();
            Minecraft.getMinecraft().effectRenderer.registerParticle(++id, EnumParticleType.CAMPFIRE_COZY_SMOKE.getFactory());
            Minecraft.getMinecraft().effectRenderer.registerParticle(++id, EnumParticleType.CAMPFIRE_SIGNAL_SMOKE.getFactory());
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }
}