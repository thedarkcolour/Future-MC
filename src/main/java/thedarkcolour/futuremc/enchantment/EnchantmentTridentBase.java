package thedarkcolour.futuremc.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentTridentBase extends Enchantment {
    public EnchantmentTridentBase(Rarity rarity, String name) {
        super(rarity, Enchantments.TRIDENT, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
        setRegistryName(name);
        setName(name);
    }
}
