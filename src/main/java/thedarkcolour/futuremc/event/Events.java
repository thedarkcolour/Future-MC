package thedarkcolour.futuremc.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thedarkcolour.futuremc.init.Init;
import thedarkcolour.futuremc.sound.Sounds;

@Mod.EventBusSubscriber
public final class Events {
    @SubscribeEvent
    public static void eatHoneyBottleEvent(final PlaySoundAtEntityEvent event) {
        if (event.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase e = (EntityLivingBase) event.getEntity();
            if (e.getActiveItemStack().getItem() == Init.HONEY_BOTTLE) {
                event.setSound(Sounds.HONEY_BOTTLE_DRINK);
            }
        }
    }
}