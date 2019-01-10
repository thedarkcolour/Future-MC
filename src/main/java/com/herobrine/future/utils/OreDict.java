package com.herobrine.future.utils;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDict {
    public static void registerOreDictEntries() {
        OreDictionary.registerOre("listAllFlower",  new ItemStack(Blocks.RED_FLOWER, 1, OreDictionary.WILDCARD_VALUE));
    }

    public static void registerOres() {
        OreDictionary.registerOre("dyeBlue", Init.dyeblue);
        OreDictionary.registerOre("dyeWhite", Init.dyewhite);
        OreDictionary.registerOre("dyeBlack", Init.dyeblack);
        OreDictionary.registerOre("listAllFlower", Init.flowerblack);
        OreDictionary.registerOre("listAllFlower", Init.flowerblue);
        OreDictionary.registerOre("listAllFlower", Init.flowerwhite);
    }
}