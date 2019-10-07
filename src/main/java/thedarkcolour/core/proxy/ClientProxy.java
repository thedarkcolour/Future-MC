package thedarkcolour.core.proxy;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thedarkcolour.futuremc.client.tesr.campfire.TESRCampfire;
import thedarkcolour.futuremc.entity.Entities;
import thedarkcolour.futuremc.init.InitModels;
import thedarkcolour.futuremc.tile.TileCampfire;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @SubscribeEvent
    public static void registerModels(final ModelRegistryEvent event) {
        InitModels.initModel();
    }

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(ClientProxy.class);
        Entities.initModels();
        //ClientRegistry.bindTileEntitySpecialRenderer(TileBell.class, new TESRBell());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCampfire.class, new TESRCampfire());
    }
/*
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
    }*/
}