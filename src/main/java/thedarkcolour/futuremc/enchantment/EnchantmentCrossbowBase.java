package thedarkcolour.futuremc.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentCrossbowBase extends Enchantment{
    public EnchantmentCrossbowBase(Enchantment.Rarity rarity, String name) {
        super(rarity, Enchantments.CROSSBOW, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
        setRegistryName(name);
        setName(name);
    }
}
