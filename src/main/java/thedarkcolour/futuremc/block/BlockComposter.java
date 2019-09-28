package thedarkcolour.futuremc.block;

import net.minecraft.block.Block;
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
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import thedarkcolour.core.block.BlockBase;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.init.FutureConfig;
import thedarkcolour.futuremc.init.Init;
import thedarkcolour.futuremc.sound.Sounds;
import thedarkcolour.futuremc.tile.TileComposter;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;


public class BlockComposter extends BlockBase {
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 8);
    protected static final AxisAlignedBB AABB_LEGS = makeAABB(0D, 0D, 0D, 16D, 2D, 16D);
    protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
    protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);

    public BlockComposter() {
        super("Composter", Material.WOOD);
        setSoundType(SoundType.WOOD);
        setDefaultState(getDefaultState().withProperty(LEVEL, 0));
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.MISC : FutureMC.TAB);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LEVEL);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) {
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
        if(worldIn.isRemote) return;
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

        if(worldIn.getTileEntity(pos) instanceof TileComposter) {
            TileComposter te = (TileComposter) worldIn.getTileEntity(pos);
            if(canCompost(stack, state)) {
                spawnBonemealParticles(worldIn, pos);
                if(!worldIn.isRemote) {
                    te.addItem(stack, !playerIn.isCreative());
                    return true;
                }
            }
            if(!worldIn.isRemote) {
                if(state.getValue(LEVEL) == 8) {
                    te.extractBoneMeal();
                }
            }
        }
        return true;
    }

    public void spawnBonemealParticles(World worldIn, BlockPos pos) {
        Random random = worldIn.rand;
        double d0 = 0.53125D;
        double d1 = 0.13125F;
        double d2 = 0.7375F;

        for(int i = 0; i < 10; ++i) {
            double d3 = random.nextGaussian() * 0.02D;
            double d4 = random.nextGaussian() * 0.02D;
            double d5 = random.nextGaussian() * 0.02D;
            worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, pos.getX() + d1 + d2 * (double)random.nextFloat(), (double)pos.getY() + d0 + (double)random.nextFloat() * (1.0D - d0), (double)pos.getZ() + d1 + d2 * (double)random.nextFloat(), d3, d4, d5);
        }
    }


    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if(!worldIn.isRemote) {
            if(worldIn.getBlockState(pos).getValue(LEVEL) == 7) {
                worldIn.setBlockState(pos, getDefaultState().withProperty(LEVEL, 8));
                worldIn.playSound(null, pos, Sounds.COMPOSTER_READY, SoundCategory.BLOCKS, 1F, 1F);
                ((TileComposter)worldIn.getTileEntity(pos)).getBuffer().setStackInSlot(0, new ItemStack(Items.DYE, 1, 15));
            }
        }
    }

    public static boolean canCompost(ItemStack stack, IBlockState state) {
        return ItemsForComposter.getChance(stack) != -1 && !isFull(state);
    }

    public static boolean isFull(IBlockState state) {
        return state.getValue(LEVEL) >= 7;
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

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World worldIn, IBlockState state) {
        return new TileComposter();
    }

    public static final class ItemsForComposter {
        private static HashMap<ItemStack, Integer> VALID_ITEMS = new HashMap<>();

        static  {
            if(FutureConfig.general.composter) init(); // Adds only if Composter is enabled. Should save a bit of RAM.
        }

        private static void init() {
            // COMMON
            add(Items.BEETROOT_SEEDS, Rarity.COMMON);
            add(new ItemStack(Blocks.TALLGRASS, 1,1), Rarity.COMMON);
            add(new ItemStack(Blocks.LEAVES, 1, 0), Rarity.COMMON);
            add(new ItemStack(Blocks.LEAVES, 1, 1), Rarity.COMMON);
            add(new ItemStack(Blocks.LEAVES, 1, 2), Rarity.COMMON);
            add(new ItemStack(Blocks.LEAVES, 1, 3), Rarity.COMMON);
            add(new ItemStack(Blocks.LEAVES2, 1, 0), Rarity.COMMON);
            add(new ItemStack(Blocks.LEAVES2, 1, 1), Rarity.COMMON);
            add(Items.MELON_SEEDS, Rarity.COMMON);
            add(Items.PUMPKIN_SEEDS, Rarity.COMMON);
            add(new ItemStack(Blocks.SAPLING, 1, 0), Rarity.COMMON);
            add(new ItemStack(Blocks.SAPLING, 1, 1), Rarity.COMMON);
            add(new ItemStack(Blocks.SAPLING, 1, 2), Rarity.COMMON);
            add(new ItemStack(Blocks.SAPLING, 1, 3), Rarity.COMMON);
            add(new ItemStack(Blocks.SAPLING, 1, 4), Rarity.COMMON);
            add(new ItemStack(Blocks.SAPLING, 1, 5), Rarity.COMMON);
            add(Init.SWEET_BERRY, Rarity.COMMON);
            add(Items.WHEAT_SEEDS, Rarity.COMMON);

            // UNCOMMON
            add(Items.MELON, Rarity.UNCOMMON);
            add(Items.REEDS, Rarity.UNCOMMON);
            add(Blocks.CACTUS, Rarity.UNCOMMON);
            add(Blocks.VINE, Rarity.UNCOMMON);
            add(new ItemStack(Blocks.DOUBLE_PLANT, 1, 2), Rarity.UNCOMMON);

            // RARE
            add(Items.APPLE, Rarity.RARE);
            add(Items.BEETROOT, Rarity.RARE);
            add(Items.CARROT, Rarity.RARE);
            add(new ItemStack(Items.DYE, 1, 3), Rarity.RARE);
            add(new ItemStack(Blocks.TALLGRASS, 1,2), Rarity.RARE);
            add(new ItemStack(Blocks.DOUBLE_PLANT, 1, 3), Rarity.RARE);
            add(Blocks.RED_FLOWER, Rarity.RARE);
            add(Blocks.YELLOW_FLOWER, Rarity.RARE);
            add(Init.LILY_OF_VALLEY, Rarity.RARE);
            add(Init.CORNFLOWER, Rarity.RARE);
            add(Init.WITHER_ROSE, Rarity.RARE);
            add(Blocks.DOUBLE_PLANT, Rarity.RARE);
            add(new ItemStack(Blocks.DOUBLE_PLANT, 1, 1), Rarity.RARE);
            add(new ItemStack(Blocks.DOUBLE_PLANT, 1, 4), Rarity.RARE);
            add(new ItemStack(Blocks.DOUBLE_PLANT, 1, 5), Rarity.RARE);
            add(Blocks.WATERLILY, Rarity.RARE);
            add(Blocks.MELON_BLOCK, Rarity.RARE);
            add(Blocks.BROWN_MUSHROOM, Rarity.RARE);
            add(Blocks.RED_MUSHROOM, Rarity.RARE);
            add(Items.POTATO, Rarity.RARE);
            add(Blocks.PUMPKIN, Rarity.RARE);
            add(Items.WHEAT, Rarity.RARE);

            // EPIC
            add(Items.BAKED_POTATO, Rarity.EPIC);
            add(Items.BREAD, Rarity.EPIC);
            add(Items.COOKIE, Rarity.EPIC);
            add(Blocks.HAY_BLOCK, Rarity.EPIC);

            // LEGENDARY
            add(Items.CAKE, Rarity.LEGENDARY);
            add(Items.PUMPKIN_PIE, Rarity.LEGENDARY);
        }

        private static void add(IForgeRegistryEntry.Impl<?> registryObject, Rarity rarity) {
            if(registryObject instanceof Block) {
                add(new ItemStack((Block) registryObject), rarity);
            } else {
                add(new ItemStack((Item) registryObject), rarity);
            }
        }

        public static void add(ItemStack stack, Rarity rarity) {
            add(stack, rarity.getChance());
        }

        public static void add(ItemStack stack, int rarity) {
            VALID_ITEMS.put(stack, rarity);
        }

        public static int getChance(ItemStack stack) {
            if(stack.isEmpty()) return -1;
            Optional<ItemStack> item = VALID_ITEMS.keySet().stream().filter((itemStack) -> itemStack.isItemEqual(stack)).findFirst();
            return item.isPresent() ? VALID_ITEMS.get(item.get()) : -1;
        }

        public static void remove(ItemStack stack) {
            VALID_ITEMS.remove(stack);
        }

        public enum Rarity {
            COMMON(30), UNCOMMON(50), RARE(65), EPIC(85), LEGENDARY(100);
            final int chance;

            Rarity(int chance) {
                this.chance = chance;
            }

            public int getChance() {
                return this.chance;
            }
        }
    }
}