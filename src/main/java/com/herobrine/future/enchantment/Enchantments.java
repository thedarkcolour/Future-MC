package com.herobrine.future.enchantment;

import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.items.ItemCrossBow;
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
    public static final EnumEnchantmentType CROSSBOW = EnumHelper.addEnchantmentType("weapons", (item) -> (item instanceof ItemCrossBow));

    public static final Enchantment LOYALTY = new EnchantLoyalty();
    public static final Enchantment CHANNELING = new EnchantChanneling();
    //public static final Enchantment RIPTIDE = new EnchantmentRiptide(); TODO - Riptide enchantment
    public static final Enchantment IMPALING = new EnchantImpaling();

    public static final Enchantment QUICK_CHARGE = new EnchantQuickCharge();
    public static final Enchantment MULTISHOT = new EnchantMultishot();
    public static final Enchantment PIERCING = new EnchantPiercing();

    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        if(FutureConfig.general.trident) event.getRegistry().registerAll(CHANNELING, IMPALING, LOYALTY);
        //if(FutureConfig.general.crossbow) event.getRegistry().registerAll(QUICK_CHARGE, MULTISHOT);
    }
}
