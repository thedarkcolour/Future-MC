package com.herobrine.future.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockComposter extends BlockBase {
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 8);

    public BlockComposter() {
        super(new BlockProperties("Composter"));
        setSoundType(SoundType.WOOD);
        setDefaultState(getBlockState().getBaseState().withProperty(LEVEL, 0));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LEVEL);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(LEVEL, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LEVEL);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        System.out.println(playerIn.getHeldItem(hand).getItem());
        {
            if (worldIn.isRemote) {
                return true;
            }
            itemComposted(playerIn.getHeldItem(hand).getItem());
        }
        return false;
    }

    public void itemComposted(Item item) {

    }

    public static final class ItemsForComposter {
        private static Map<ResourceLocation, Rarity> VALID_ITEMS = new HashMap<>();

        public static void init() {
            // COMMON
            VALID_ITEMS.put(Blocks.TALLGRASS.getRegistryName(), Rarity.COMMON);

            // UNCOMMON
            VALID_ITEMS.put(Blocks.CACTUS.getRegistryName(), Rarity.UNCOMMON);
            VALID_ITEMS.put(Items.MELON.getRegistryName(), Rarity.UNCOMMON);

            // RARE
            VALID_ITEMS.put(Items.APPLE.getRegistryName(), Rarity.RARE);

            // EPIC
            VALID_ITEMS.put(Items.BAKED_POTATO.getRegistryName(), Rarity.EPIC);

            // LEGENDARY
            VALID_ITEMS.put(Items.CAKE.getRegistryName(), Rarity.LEGENDARY);
            VALID_ITEMS.put(Items.PUMPKIN_PIE.getRegistryName(), Rarity.LEGENDARY);
        }

        public static boolean isItemValid(ResourceLocation loc) {
            System.out.println(VALID_ITEMS.containsKey(loc));
            return VALID_ITEMS.containsKey(loc);
        }

        public static void remove(ResourceLocation loc) {
            VALID_ITEMS.remove(loc);
        }

        public static int getChance(ResourceLocation location) {
            if(isItemValid(location)) {
                Rarity rarity = VALID_ITEMS.get(location);

                switch (rarity) {
                    case COMMON: return 30;
                    case UNCOMMON: return 50;
                    case RARE: return 65;
                    case EPIC: return 85;
                    case LEGENDARY: return 100;
                }
            }
            return -1;
        }

        static {
            init();
        }

        public enum Rarity {
            COMMON, UNCOMMON, RARE, EPIC, LEGENDARY
        }
    }
}