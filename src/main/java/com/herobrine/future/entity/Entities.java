package com.herobrine.future.entity;

import com.herobrine.future.FutureJava;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class Entities {

    public static void init() {
        int id = 1;

        EntityRegistry.registerModEntity(new ResourceLocation("minecraftfuture:trident"), EntityTrident.class, "Trident", id++, FutureJava.instance, 32, 1, true);
    }
}
