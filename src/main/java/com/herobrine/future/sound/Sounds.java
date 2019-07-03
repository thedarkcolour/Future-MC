package com.herobrine.future.sound;

import com.herobrine.future.init.Init;
import net.minecraft.block.SoundType;
import net.minecraft.init.SoundEvents;
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
    public static final SoundEvent CROSSBOW_CHARGE = makeSoundEvent("charge");
    public static final SoundEvent CROSSBOW_FIRE = makeSoundEvent("crossbow_fire");
    public static final SoundEvent CROSSBOW_QUICK_CHARGE = makeSoundEvent("quick_charge");
    public static final SoundEvent CROSSBOW_LOAD = makeSoundEvent("crossbow_load");
    public static final SoundEvent GRINDSTONE_USE = makeSoundEvent("grindstone_use");
    public static final SoundEvent COMPOSTER_EMPTY = makeSoundEvent("composter_empty");
    public static final SoundEvent COMPOSTER_FILL = makeSoundEvent("composter_fill");
    public static final SoundEvent COMPOSTER_FILL_SUCCESS = makeSoundEvent("composter_fill_success");
    public static final SoundEvent COMPOSTER_READY = makeSoundEvent("composter_ready");

    public static final SoundType BAMBOO = new SoundType(1.0F, 1.12F, SoundEvents.BLOCK_WOOD_BREAK, SoundEvents.BLOCK_WOOD_STEP, SoundEvents.BLOCK_WOOD_PLACE, SoundEvents.BLOCK_WOOD_HIT, SoundEvents.BLOCK_WOOD_FALL);


    private static SoundEvent makeSoundEvent(String name) {
        ResourceLocation loc = new ResourceLocation(Init.MODID, name);
        return new SoundEvent(loc).setRegistryName(loc);
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(TRIDENT_THROW, TRIDENT_PIERCE, TRIDENT_IMPACT, TRIDENT_CHANNELING,
                TRIDENT_LOYALTY, CROSSBOW_CHARGE, CROSSBOW_FIRE, CROSSBOW_QUICK_CHARGE, CROSSBOW_LOAD,
                COMPOSTER_EMPTY, COMPOSTER_FILL, COMPOSTER_FILL_SUCCESS, COMPOSTER_READY);
    }
}