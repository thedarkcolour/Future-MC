package com.herobrine.future.sound;

import com.herobrine.future.init.Init;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Sounds {
    public static final SoundEvent TRIDENT_THROW = makeSoundEvent("throw_trident");
    public static final SoundEvent TRIDENT_PIERCE = makeSoundEvent("pierce_trident");
    public static final SoundEvent TRIDENT_IMPACT = makeSoundEvent("impact_trident");
    public static final SoundEvent TRIDENT_CHANNELING = makeSoundEvent("channeling_trident");
    public static final SoundEvent TRIDENT_LOYALTY = makeSoundEvent("loyalty");
    public static final SoundEvent BELL_RING = makeSoundEvent("bell_ring");

    private static SoundEvent makeSoundEvent(String name) {
        ResourceLocation loc = new ResourceLocation(Init.MODID, name);
        return new SoundEvent(loc).setRegistryName(loc);
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(TRIDENT_THROW, TRIDENT_PIERCE, TRIDENT_IMPACT, TRIDENT_CHANNELING,
                TRIDENT_LOYALTY);//, TRIDENT_RIPTIDE_I, TRIDENT_RIPTIDE_II, TRIDENT_RIPTIDE_III);
    }
}