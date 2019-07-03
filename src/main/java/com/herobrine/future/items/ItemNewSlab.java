package com.herobrine.future.items;

import com.herobrine.future.blocks.BlockNewSlab;
import net.minecraft.block.BlockSlab;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemNewSlab extends ItemSlab {
    private final BlockSlab singleSlab;

    public ItemNewSlab(BlockNewSlab.Half singleSlab, BlockNewSlab.Double doubleSlab) {
        super(singleSlab, singleSlab, doubleSlab);
        setRegistryName(Objects.requireNonNull(singleSlab.getRegistryName()));
        setUnlocalizedName(singleSlab.getUnlocalizedName());
        //setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.BUILDING_BLOCKS : Init.FUTURE_MC_TAB);

        this.singleSlab = singleSlab;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return singleSlab.getUnlocalizedName();
    }

    @SideOnly(Side.CLIENT)
    public void model() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(Objects.requireNonNull(getRegistryName()), "inventory"));
    }

    public static final class Slabs {
        private static final String[] VARIANTS = new String[]{
                "granite","andesite","diorite", // DONE
                "polished_granite","polished_andesite","polished_diorite", // DONE
                "stone","prismarine","dark_prismarine","prismarine_brick", // DONE
                "smooth_red_sandstone","smooth_sandstone","mossy_stone_brick",
                "mossy_stone","smooth_quartz","red_nether_brick","end_stone_brick"
        };

        public static final List<BlockNewSlab.Half> SLAB_HALVES = new ArrayList<>();
        public static final List<BlockNewSlab.Double> SLAB_DOUBLES = new ArrayList<>();
        public static final List<ItemNewSlab> SLAB_ITEMS = new ArrayList<>();

        public static void initSlab() {
            for (String variant : VARIANTS) {
                BlockNewSlab.Half half = new BlockNewSlab.Half(variant);
                BlockNewSlab.Double full = new BlockNewSlab.Double(variant);
                SLAB_HALVES.add(half);
                SLAB_DOUBLES.add(full);
                SLAB_ITEMS.add(new ItemNewSlab(half, full));
            }
        }
    }
}