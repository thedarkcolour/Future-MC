package com.herobrine.future.entity;

import com.herobrine.future.MainFuture;
import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.entity.drowned.EntityDrowned;
import com.herobrine.future.entity.trident.EntityTrident;
import com.herobrine.future.entity.trident.RenderTrident;
import com.herobrine.future.init.Init;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Entities {
    public static void init() {
        int id = 1;

        if(FutureConfig.general.trident) EntityRegistry.registerModEntity(new ResourceLocation(Init.MODID,"trident"), EntityTrident.class, "trident", id++, MainFuture.instance, 32, 1, true);
        //noinspection ConstantConditions
        if(false) EntityRegistry.registerModEntity(new ResourceLocation(Init.MODID, "drowned"), EntityDrowned.class, "drowned", id, MainFuture.instance, 36, 1, true);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        if(FutureConfig.general.trident) RenderingRegistry.registerEntityRenderingHandler(EntityTrident.class, RenderTrident.FACTORY);
    }
}
