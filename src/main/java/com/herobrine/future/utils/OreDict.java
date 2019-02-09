package com.herobrine.future.utils;

import net.minecraftforge.oredict.OreDictionary;

public class OreDict {
    public static void registerOres() {
        OreDictionary.registerOre("dyeBlue", Init.dyeblue);
        OreDictionary.registerOre("dyeWhite", Init.dyewhite);
        OreDictionary.registerOre("dyeBlack", Init.dyeblack);
        OreDictionary.registerOre("listAllFlower", Init.flowerblack);
        OreDictionary.registerOre("listAllFlower", Init.flowerblue);
        OreDictionary.registerOre("listAllFlower", Init.flowerwhite);
        if (Config.newwall) {
            for (int i = 0; i > 6; i++) {
                OreDictionary.registerOre("logWood", Init.strippedLogs.get(i));
            }
        }
    }
}
