package thedarkcolour.futuremc.enchantment;

import net.minecraft.enchantment.Enchantment;

public class EnchantmentMultishot extends EnchantmentCrossbowBase {
    public EnchantmentMultishot() {
        super(Rarity.RARE, "multishot");
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return super.canApplyTogether(ench) && ench != Enchantments.PIERCING;
    }
}