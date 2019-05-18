package com.herobrine.future.blocks;

import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.init.Init;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockComposter extends BlockBase {
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 8);
    protected static final AxisAlignedBB AABB_LEGS = makeAABB(0D, 0D, 0D, 16D, 2D, 16D);
    protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
    protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);

    public BlockComposter() {
        super(new BlockProperties("Composter", Material.WOOD));
        setSoundType(SoundType.WOOD);
        setDefaultState(getBlockState().getBaseState().withProperty(LEVEL, 0));
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.MISC : Init.FUTURE_MC_TAB);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LEVEL);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LEGS);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_WEST);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_NORTH);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_EAST);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_SOUTH);
        if(state.getValue(LEVEL) > 0) {
            double level = state.getValue(LEVEL) == 8D ? 6D : state.getValue(LEVEL) - 1D;
            addCollisionBoxToList(pos, entityBox, collidingBoxes, makeAABB(2D,2D,2D, 14D, 3D + (2D * level), 14D));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(LEVEL, meta);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if(worldIn.getBlockState(pos).getValue(LEVEL) == 8) {
            EntityItem item = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 0.6D, pos.getZ() + 0.5D);
            item.setItem(new ItemStack(Items.DYE, 1, 15));
            worldIn.spawnEntity(item);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LEVEL);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);

        if(itemComposted(stack.getItem(), state)) { // Composts when the check succeeds.
            if (worldIn.isRemote) {
                return true;
            }
            fill(worldIn, pos, stack, ItemsForComposter.getChance(stack.getItem().getRegistryName()));
            if(worldIn.getBlockState(pos).getValue(LEVEL) == 7) {
                worldIn.scheduleBlockUpdate(pos, this, 30, 1);
            }
        }
        if(state.getValue(LEVEL) == 8) {
            if(worldIn.isRemote) return true;
            EntityItem item = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 0.6D, pos.getZ() + 0.5D);
            item.setItem(new ItemStack(Items.DYE, 1, 15));

            worldIn.setBlockState(pos, getDefaultState());
            worldIn.spawnEntity(item);
        }
        return false;
    }

    /**
     * Tries to fill the composter and consumes the player's item.
     */
    public void fill(World world, BlockPos pos, ItemStack stack, int chance) {
        int level = world.getBlockState(pos).getValue(LEVEL);
        if(world.rand.nextInt(100) <= chance) {
            world.setBlockState(pos, getDefaultState().withProperty(LEVEL, level + 1));
        }
        stack.shrink(1);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if(worldIn.getBlockState(pos).getValue(LEVEL) == 7) {
            worldIn.setBlockState(pos, getDefaultState().withProperty(LEVEL, 8));
        }
    }

    /**
     * Tries to compost item and returns true if it can.
     */
    public boolean itemComposted(Item item, IBlockState state) {
        ResourceLocation resourceLocation = item.getRegistryName();

        return ItemsForComposter.VALID_ITEMS.containsKey(resourceLocation) && state.getValue(LEVEL) < 7;
    }

    public static final class ItemsForComposter {
        private static Map<ResourceLocation, Rarity> VALID_ITEMS = new HashMap<>();

        public static void init() {
            // COMMON
            VALID_ITEMS.put(Items.WHEAT_SEEDS.getRegistryName(), Rarity.COMMON);
            VALID_ITEMS.put(Items.MELON_SEEDS.getRegistryName(), Rarity.COMMON);
            VALID_ITEMS.put(Items.PUMPKIN_SEEDS.getRegistryName(), Rarity.COMMON);
            VALID_ITEMS.put(Items.BEETROOT_SEEDS.getRegistryName(), Rarity.COMMON);
            VALID_ITEMS.put(Blocks.TALLGRASS.getRegistryName(), Rarity.COMMON);
            VALID_ITEMS.put(Blocks.LEAVES.getRegistryName(), Rarity.COMMON);
            VALID_ITEMS.put(Blocks.LEAVES2.getRegistryName(), Rarity.COMMON);
            VALID_ITEMS.put(Blocks.SAPLING.getRegistryName(), Rarity.COMMON);
            VALID_ITEMS.put(Init.SWEET_BERRY.getRegistryName(), Rarity.COMMON);

            // UNCOMMON
            VALID_ITEMS.put(Items.MELON.getRegistryName(), Rarity.UNCOMMON);
            VALID_ITEMS.put(Items.REEDS.getRegistryName(), Rarity.UNCOMMON);
            VALID_ITEMS.put(Blocks.CACTUS.getRegistryName(), Rarity.UNCOMMON);
            VALID_ITEMS.put(Blocks.VINE.getRegistryName(), Rarity.UNCOMMON);
            VALID_ITEMS.put(Blocks.DOUBLE_PLANT.getRegistryName(), Rarity.UNCOMMON);

            // RARE
            VALID_ITEMS.put(Items.APPLE.getRegistryName(), Rarity.RARE);
            VALID_ITEMS.put(Items.BEETROOT.getRegistryName(), Rarity.RARE);
            VALID_ITEMS.put(Items.CARROT.getRegistryName(), Rarity.RARE);
            VALID_ITEMS.put(Blocks.RED_FLOWER.getRegistryName(), Rarity.RARE);
            VALID_ITEMS.put(Blocks.YELLOW_FLOWER.getRegistryName(), Rarity.RARE);
            VALID_ITEMS.put(Blocks.WATERLILY.getRegistryName(), Rarity.RARE);
            VALID_ITEMS.put(Blocks.MELON_BLOCK.getRegistryName(), Rarity.RARE);
            VALID_ITEMS.put(Blocks.BROWN_MUSHROOM.getRegistryName(), Rarity.RARE);
            VALID_ITEMS.put(Blocks.RED_MUSHROOM.getRegistryName(), Rarity.RARE);
            VALID_ITEMS.put(Blocks.PUMPKIN.getRegistryName(), Rarity.RARE);
            VALID_ITEMS.put(Items.WHEAT.getRegistryName(), Rarity.RARE);

            // EPIC
            VALID_ITEMS.put(Items.BAKED_POTATO.getRegistryName(), Rarity.EPIC);
            VALID_ITEMS.put(Items.BREAD.getRegistryName(), Rarity.EPIC);
            VALID_ITEMS.put(Items.COOKIE.getRegistryName(), Rarity.EPIC);
            VALID_ITEMS.put(Blocks.HAY_BLOCK.getRegistryName(), Rarity.EPIC);
            VALID_ITEMS.put(Blocks.BROWN_MUSHROOM_BLOCK.getRegistryName(), Rarity.EPIC);
            VALID_ITEMS.put(Blocks.RED_MUSHROOM_BLOCK.getRegistryName(), Rarity.EPIC);

            // LEGENDARY
            VALID_ITEMS.put(Items.CAKE.getRegistryName(), Rarity.LEGENDARY);
            VALID_ITEMS.put(Items.PUMPKIN_PIE.getRegistryName(), Rarity.LEGENDARY);
        }

        public static boolean isItemValid(ResourceLocation loc) {
            return VALID_ITEMS.containsKey(loc);
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

        /**
         * Planned CraftTweaker function
         */
        @SuppressWarnings("unused")
        public static void remove(ResourceLocation loc) {
            VALID_ITEMS.remove(loc);
        }

        /**
         * Planned CraftTweaker function
         */
        public static void add(ResourceLocation loc, String rarity) {
            Rarity rare = Rarity.COMMON;
            switch (rarity) {
                case "uncommon": rare = Rarity.UNCOMMON; break;
                case "rare": rare = Rarity.RARE; break;
                case "epic": rare = Rarity.EPIC; break;
                case "legendary": rare = Rarity.LEGENDARY;
            }

            VALID_ITEMS.put(loc, rare);
        }

        static {
            init();
        }

        public enum Rarity {
            COMMON, UNCOMMON, RARE, EPIC, LEGENDARY
        }
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isTopSolid(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
}