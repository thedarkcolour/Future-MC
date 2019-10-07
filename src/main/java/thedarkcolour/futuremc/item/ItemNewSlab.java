package thedarkcolour.futuremc.item;

import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import thedarkcolour.core.item.Modeled;
import thedarkcolour.futuremc.block.BlockNewSlab;

import java.util.ArrayList;
import java.util.List;

public class ItemNewSlab extends ItemSlab implements Modeled {
    private final BlockSlab singleSlab;

    public ItemNewSlab(BlockNewSlab.Half singleSlab, BlockNewSlab.Double doubleSlab) {
        super(singleSlab, singleSlab, doubleSlab);
        setRegistryName(singleSlab.getRegistryName());
        setTranslationKey(singleSlab.getTranslationKey());
        //setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.BUILDING_BLOCKS : Init.CREATIVE_TAB);

        this.singleSlab = singleSlab;
        addModel();
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return singleSlab.getTranslationKey();
    }

    public static final class Slabs {
        private static final String[] VARIANTS = new String[]{
                "granite","andesite","diorite", // DONE
                "polished_granite","polished_andesite","polished_diorite", // DONE
                "stone","prismarine","dark_prismarine","prismarine_brick", // DONE
                "smooth_red_sandstone","smooth_sandstone","mossy_stone_brick",
                "mossy_stone","smooth_quartz","red_nether_brick","end_stone_brick"
        };

        public static List<BlockNewSlab.Half> slabHalves = new ArrayList<>();
        public static List<BlockNewSlab.Double> slabDoubles = new ArrayList<>();
        public static List<ItemNewSlab> slabItems = new ArrayList<>();

        public static void initSlab() {
            for (String variant : VARIANTS) {
                BlockNewSlab.Half half = new BlockNewSlab.Half(variant);
                BlockNewSlab.Double full = new BlockNewSlab.Double(variant);
                slabHalves.add(half);
                slabDoubles.add(full);
                slabItems.add(new ItemNewSlab(half, full));
            }
        }

        public static void clean() {
            slabDoubles = null;
            slabHalves = null;
            slabItems = null;
        }
    }
}