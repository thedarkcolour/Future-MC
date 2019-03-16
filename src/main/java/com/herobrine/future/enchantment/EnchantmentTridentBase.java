package com.herobrine.future.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;

@SuppressWarnings("all")
public class EnchantmentTridentBase extends Enchantment {
    public EnchantmentTridentBase(Rarity rarity, String name) {
        super(rarity, Enchantments.TRIDENT, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
        setRegistryName(name);
        setName(name);
    }
}
