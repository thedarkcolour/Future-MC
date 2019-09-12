package com.herobrine.future.event;

import com.herobrine.future.init.Init;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public final class EatHoneyBottleEvent {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void eatHoneyBottleEvent(final LivingEntityUseItemEvent.Tick event) {
        if (event.getItem().getItem() == Init.HONEY_BOTTLE) {
            event.setCanceled(true);
        }
    }
}