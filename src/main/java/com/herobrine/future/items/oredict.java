package com.herobrine.future.items;

import net.minecraft.init.Items;
import net.minecraftforge.oredict.OreDictionary;

public class oredict {
    public static void registerOres() {
        OreDictionary.registerOre("dyeBlue", futureItems.dyeblue);
        OreDictionary.registerOre("dyeWhite", futureItems.dyewhite);
        OreDictionary.registerOre("dyeBlack", futureItems.dyeblack);
    }
}
