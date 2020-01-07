package thedarkcolour.futuremc.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraftforge.common.util.EnumHelper;
import thedarkcolour.futuremc.item.ItemCrossbow;
import thedarkcolour.futuremc.item.ItemTrident;

public final class Enchantments {
    public static final EnumEnchantmentType TRIDENT = EnumHelper.addEnchantmentType("weapons", (item) -> (item instanceof ItemTrident));
    public static final EnumEnchantmentType CROSSBOW = EnumHelper.addEnchantmentType("weapons", (item) -> (item instanceof ItemCrossbow));

    public static final Enchantment LEALTAD = new EnchantmentLoyalty();
    public static final Enchantment CONDUCTIVIDAD = new EnchantmentChanneling();
    public static final Enchantment RIPTIDE = new EnchantmentRiptide();
    public static final Enchantment EMPALAMIENTO = new EnchantmentImpaling();

    public static final Enchantment QUICK_CHARGE = new EnchantmentQuickCharge();
    public static final Enchantment MULTISHOT = new EnchantmentMultishot();
    public static final Enchantment PIERCING = new EnchantmentPiercing();
}
