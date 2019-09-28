package thedarkcolour.core.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import thedarkcolour.futuremc.FutureMC;

public class ItemModeled extends Item implements Modeled {
    public ItemModeled(String regName) {
        setTranslationKey(FutureMC.ID + "." + regName);
        setRegistryName(regName);
        addModel();
    }

    @Override
    public ItemModeled setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }
}