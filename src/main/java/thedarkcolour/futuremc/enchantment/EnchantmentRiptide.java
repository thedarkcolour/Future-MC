package thedarkcolour.futuremc.enchantment;

import net.minecraft.enchantment.Enchantment;

public class EnchantmentRiptide extends EnchantmentTridentBase {
    public EnchantmentRiptide() {
        super(Rarity.RARE, "riptide");
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + enchantmentLevel * 7;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return super.canApplyTogether(ench) && ench != Enchantments.LEALTAD && ench != Enchantments.CONDUCTIVIDAD;
    }
}