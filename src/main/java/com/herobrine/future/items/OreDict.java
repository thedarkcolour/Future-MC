package com.herobrine.future.items;

import com.herobrine.future.init.Init;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDict {
    public static void registerOres() {
        OreDictionary.registerOre("dyeWhite", new ItemStack(Init.DYES, 1,0));
        OreDictionary.registerOre("dyeBlue", new ItemStack(Init.DYES, 1,1));
        OreDictionary.registerOre("dyeBrown", new ItemStack(Init.DYES, 1,2));
        OreDictionary.registerOre("dyeBlack", new ItemStack(Init.DYES, 1, 3));
        OreDictionary.registerOre("seedSweetBerry", Init.SWEET_BERRY);
        OreDictionary.registerOre("cropSweetBerry", Init.SWEET_BERRY);
        /*if (FutureConfig.general.strippedLogs) {
            for (Block block : Init.strippedLogs) {
                OreDictionary.registerOre("logWood", Item.getItemFromBlock(block));
            }
        }*/
    }
}