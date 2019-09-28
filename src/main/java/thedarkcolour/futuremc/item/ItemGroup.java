package thedarkcolour.futuremc.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import thedarkcolour.futuremc.init.FutureConfig;
import thedarkcolour.futuremc.init.Init;

public class ItemGroup extends CreativeTabs {
    public ItemGroup() {
        super("Future");
    }

    @Override
    public ItemStack createIcon() {
        return FutureConfig.general.lantern ? new ItemStack(Init.LANTERN) : FutureConfig.general.bamboo ? new ItemStack(Init.BAMBOO_ITEM) : new ItemStack(Blocks.GRASS);
    }

    @Override
    public void displayAllRelevantItems(NonNullList<ItemStack> p_78018_1_) {
        super.displayAllRelevantItems(p_78018_1_);
    }
}