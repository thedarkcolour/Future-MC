package thedarkcolour.futuremc.item;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import thedarkcolour.core.item.Modeled;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.init.Init;

public class ItemBamboo extends ItemBlock implements Modeled {
    public ItemBamboo() {
        super(Init.BAMBOO_STALK);
        setTranslationKey(FutureMC.ID + ".Bamboo");
        setRegistryName("Bamboo");
        addModel();
    }

    @Override
    public int getItemBurnTime(ItemStack itemStack) {
        return 50;
    }
}