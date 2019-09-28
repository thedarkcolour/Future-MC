package thedarkcolour.futuremc.entity;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thedarkcolour.core.util.RegistryHelper;
import thedarkcolour.futuremc.entity.bee.EntityBee;
import thedarkcolour.futuremc.entity.bee.RenderBee;
import thedarkcolour.futuremc.entity.drowned.EntityDrowned;
import thedarkcolour.futuremc.entity.panda.EntityPanda;
import thedarkcolour.futuremc.entity.panda.RenderPanda;
import thedarkcolour.futuremc.entity.trident.EntityTrident;
import thedarkcolour.futuremc.entity.trident.RenderTrident;
import thedarkcolour.futuremc.init.FutureConfig;

public final class Entities {
    public static void init() {
        if (FutureConfig.general.trident) {
            RegistryHelper.registerEntity("trident", EntityTrident.class, 32, 1);
        }
        if (false) {
            RegistryHelper.registerEntity("drowned", EntityDrowned.class, 36, 4);
        }
        if (FutureConfig.general.panda && FutureConfig.general.bamboo) {
            RegistryHelper.registerEntity("panda", EntityPanda.class, 36, 2,15198183, 1776418);
            EntityRegistry.addSpawn(EntityPanda.class, 1, 1, 2, EnumCreatureType.CREATURE, Biomes.JUNGLE, Biomes.JUNGLE_HILLS, Biomes.MUTATED_JUNGLE);
        }
        if (FutureConfig.general.bee) {
            RegistryHelper.registerEntity("bee", EntityBee.class,32, 3, 16770398, 2500144);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        if (FutureConfig.general.trident) RenderingRegistry.registerEntityRenderingHandler(EntityTrident.class, RenderTrident::new);
        if (FutureConfig.general.panda && FutureConfig.general.bamboo) RenderingRegistry.registerEntityRenderingHandler(EntityPanda.class, RenderPanda::new);
        if (FutureConfig.general.bee) RegistryHelper.registerEntityModel(EntityBee.class, RenderBee::new);
    }
}