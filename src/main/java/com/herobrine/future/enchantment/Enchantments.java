package com.herobrine.future.enchantment;

import com.herobrine.future.items.ItemTrident;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Enchantments {
    public static final EnumEnchantmentType TRIDENT = EnumHelper.addEnchantmentType("weapons", (item) -> (item instanceof ItemTrident));

    public static final Enchantment LOYALTY = new EnchantLoyalty();
    public static final Enchantment CHANNELING = new EnchantmentChanneling();
    //public static final Enchantment RIPTIDE = new EnchantmentRiptide();
    public static final Enchantment IMPALING = new EnchantmentImpaling();

    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        event.getRegistry().registerAll(CHANNELING, IMPALING, LOYALTY);
    }
}
