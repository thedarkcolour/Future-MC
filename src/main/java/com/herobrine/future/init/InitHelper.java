package com.herobrine.future.init;

import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public final class InitHelper {
    /**
     * Checks if a specific item from Charm is loaded
     * This works for registry because Charm registers before me
     */
    public static boolean isCharmItemLoaded(String registryName) {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation("charm:" + registryName)) != Blocks.AIR;
    }
}