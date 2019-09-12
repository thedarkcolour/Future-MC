package com.herobrine.future.entity;

import com.herobrine.future.FutureMC;
import com.herobrine.future.entity.bee.EntityBee;
import com.herobrine.future.entity.panda.EntityPanda;
import com.herobrine.future.entity.panda.RenderPanda;
import com.herobrine.future.entity.trident.EntityTrident;
import com.herobrine.future.entity.trident.RenderTrident;
import com.herobrine.future.init.FutureConfig;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thedarkcolour.core.util.RegistryHelper;

@SuppressWarnings("UnusedAssignment")
public final class Entities {
    public static void init() {
        int id = 0;
/*
        if (FutureConfig.general.trident) {
            EntityRegistry.registerModEntity(new ResourceLocation(FutureMC.ID,"trident"), EntityTrident.class,
                    "trident", id++, FutureMC.instance, 32, 1, true);
        }*/
        if (FutureConfig.general.trident) {
            RegistryHelper.registerEntity("trident", EntityTrident.class, 32, ++id);
        }
        /*
        if (false) {
            EntityRegistry.registerModEntity(new ResourceLocation(FutureMC.ID, "drowned"), EntityDrowned.class,
                    "drowned", id++, FutureMC.instance, 36, 1, true);
        }*/
        if (FutureConfig.general.bee) {
            EntityRegistry.registerModEntity(new ResourceLocation(FutureMC.ID, "bee"), EntityBee.class,
                    "bee", id++, FutureMC.instance, 32, 1, true);
        }
        if (FutureConfig.general.panda && FutureConfig.general.bamboo) {
            EntityRegistry.registerModEntity(new ResourceLocation(FutureMC.ID, "panda"), EntityPanda.class,
                    "panda", id++, FutureMC.instance, 36, 1, true, 15198183, 1776418);
            EntityRegistry.addSpawn(EntityPanda.class, 1, 1, 2, EnumCreatureType.CREATURE, Biomes.JUNGLE, Biomes.JUNGLE_HILLS, Biomes.MUTATED_JUNGLE);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        if(FutureConfig.general.trident) RenderingRegistry.registerEntityRenderingHandler(EntityTrident.class, RenderTrident::new);
        if(FutureConfig.general.panda && FutureConfig.general.bamboo) RenderingRegistry.registerEntityRenderingHandler(EntityPanda.class, RenderPanda::new);
    }
}