package com.herobrine.future.compat.oredict;

import com.herobrine.future.init.FutureConfig;
import com.herobrine.future.init.Init;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class OreDict {
    public static void registerOres() {
        if(FutureConfig.modFlowers.dyes) {
            OreDictionary.registerOre("dyeWhite", new ItemStack(Init.DYES, 1,0));
            OreDictionary.registerOre("dyeBlue", new ItemStack(Init.DYES, 1,1));
            OreDictionary.registerOre("dyeBrown", new ItemStack(Init.DYES, 1,2));
            OreDictionary.registerOre("dyeBlack", new ItemStack(Init.DYES, 1, 3));
        }

        if (FutureConfig.general.berryBush) {
            OreDictionary.registerOre("seedSweetBerry", Init.SWEET_BERRY);
            OreDictionary.registerOre("cropSweetBerry", Init.SWEET_BERRY);
        }

        if (FutureConfig.general.bamboo) {
            OreDictionary.registerOre("cropBamboo", Init.BAMBOO_ITEM);
        }

        if (FutureConfig.general.strippedLogs) {
            OreDictionary.registerOre("logWood", Init.STRIPPED_ACACIA_LOG);
            OreDictionary.registerOre("logWood", Init.STRIPPED_BIRCH_LOG);
            OreDictionary.registerOre("logWood", Init.STRIPPED_DARK_OAK_LOG);
            OreDictionary.registerOre("logWood", Init.STRIPPED_JUNGLE_LOG);
            OreDictionary.registerOre("logWood", Init.STRIPPED_OAK_LOG);
            OreDictionary.registerOre("logWood", Init.STRIPPED_SPRUCE_LOG);
        }
    }

    public static String getOreName(ItemStack stack) {
        if(stack == null || stack.isEmpty()) {
            return "";
        }
        int[] ids = OreDictionary.getOreIDs(stack);
        if (ids.length >= 1) {
            return OreDictionary.getOreName(ids[0]);
        }
        return "";
    }
}