package com.herobrine.future.event;

import com.herobrine.future.init.Init;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class WitherRoseEvent {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onMobDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        Entity attacker = event.getSource().getTrueSource();

        if(attacker instanceof EntityWither) {
            entity.getEntityWorld().spawnEntity(new EntityItem(entity.getEntityWorld(), entity.posX, entity.posY, entity.posZ, new ItemStack(Init.WITHER_ROSE, 1)));
        }
    }
}