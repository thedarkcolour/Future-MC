package com.herobrine.future.utils;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class oredict {
    public static void registerOres() {
        OreDictionary.registerOre("dyeBlue", init.dyeblue);
        OreDictionary.registerOre("dyeWhite", init.dyewhite);
        OreDictionary.registerOre("dyeBlack", init.dyeblack);
        OreDictionary.registerOre("listAllFlower",  new ItemStack(Blocks.RED_FLOWER, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("listAllFlower", init.flowerblack);
        OreDictionary.registerOre("listAllFlower", init.flowerblue);
        OreDictionary.registerOre("listAllFlower", init.flowerwhite);
    }
}
