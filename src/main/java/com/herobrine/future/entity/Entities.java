package com.herobrine.future.entity;

import com.herobrine.future.MainFuture;
import com.herobrine.future.entity.trident.EntityTrident;
import com.herobrine.future.entity.trident.RenderTrident;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Entities {
    public static void init() {
        int id = 1;

        EntityRegistry.registerModEntity(new ResourceLocation("minecraftfuture:trident"), EntityTrident.class, "trident", id++, MainFuture.instance, 32, 1, true);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        RenderingRegistry.registerEntityRenderingHandler(EntityTrident.class, RenderTrident.FACTORY);
    }
}
