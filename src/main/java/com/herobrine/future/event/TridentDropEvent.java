package com.herobrine.future.event;

import com.herobrine.future.init.Init;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class TridentDropEvent { // Allows the Trident to drop from the Elder Guardian
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onMobDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();

        if(entity instanceof EntityElderGuardian) {
            entity.getEntityWorld().spawnEntity(new EntityItem(entity.getEntityWorld(), entity.posX, entity.posY, entity.posZ, new ItemStack(Init.TRIDENT, 1)));
        }
    }
}
