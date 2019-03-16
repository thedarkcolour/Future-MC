package com.herobrine.future.items;

import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.utils.proxy.Init;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDict {
    public static void registerOres() {
        OreDictionary.registerOre("dyeWhite", new ItemStack(Init.dye, 1,0));
        OreDictionary.registerOre("dyeBlue", new ItemStack(Init.dye, 1,1));
        OreDictionary.registerOre("dyeBrown", new ItemStack(Init.dye, 1,2));
        OreDictionary.registerOre("dyeBlack", new ItemStack(Init.dye, 1, 3));
        OreDictionary.registerOre("seedCranberry", Init.sweetberry);
        OreDictionary.registerOre("cropCranberry", Init.sweetberry);
        if (FutureConfig.a.strippedlogs) {
            for (Block block : Init.strippedLogs) {
                OreDictionary.registerOre("logWood", Item.getItemFromBlock(block));
            }
        }
    }
}