package thedarkcolour.core.util;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import thedarkcolour.core.block.IProjectileDispenserBehaviour;
import thedarkcolour.futuremc.FutureMC;

public final class RegistryHelper {
    public static void registerDispenserBehaviour(Item item, IProjectileDispenserBehaviour behaviour) {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(item, behaviour);
    }

    public static void registerEntity(String name, Class<? extends Entity> entity, int trackingRange, int id) {
        EntityRegistry.registerModEntity(new ResourceLocation(FutureMC.ID, name), entity, name, id, FutureMC.instance, trackingRange, 1, true);
    }

    public static void registerEntity(String name, Class<? extends Entity> entity, int trackingRange, int id, int primary, int secondary) {
        EntityRegistry.registerModEntity(new ResourceLocation(FutureMC.ID, name), entity, name, id, FutureMC.instance, trackingRange, 1, true, primary, secondary);
    }

    public static <T extends Entity> void registerEntityModel(Class<T> entityBeeClass, IRenderFactory<T> factory) {
        RenderingRegistry.registerEntityRenderingHandler(entityBeeClass, factory);
    }
}