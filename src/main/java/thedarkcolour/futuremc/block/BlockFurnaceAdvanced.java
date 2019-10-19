package thedarkcolour.futuremc.block;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thedarkcolour.core.block.BlockBase;
import thedarkcolour.core.gui.Gui;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.init.FutureConfig;
import thedarkcolour.futuremc.init.Init;
import thedarkcolour.futuremc.tile.TileFurnaceAdvanced;

import java.util.ArrayList;
import java.util.Random;

public class BlockFurnaceAdvanced extends BlockBase implements ITileEntityProvider {
    public static final PropertyBool LIT = PropertyBool.create("lit");
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private FurnaceType type;

    public BlockFurnaceAdvanced(FurnaceType type) {
        super(type.getName());
        setHardness(3.5F);
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.DECORATIONS : FutureMC.TAB);

        this.type = type;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        if(type == FurnaceType.SMOKER) {
            return new TileFurnaceAdvanced.TileSmoker();
        }
        if(type == FurnaceType.BLAST_FURNACE) {
            return new TileFurnaceAdvanced.TileBlastFurnace();
        }
        return null;
    }

    @Override
    public int getLightValue(IBlockState state) {
        return state.getValue(LIT) ? 13 : 0;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        TileEntity te = worldIn.getTileEntity(pos);
        if (!(te instanceof TileFurnaceAdvanced)) {
            return false;
        }
        Block block = worldIn.getBlockState(pos).getBlock();

        if (block == Init.BLAST_FURNACE) {
            if (!(te instanceof TileFurnaceAdvanced.TileBlastFurnace)) {
                return false;
            }
        }
        if (block == Init.SMOKER) {
            if (!(te instanceof TileFurnaceAdvanced.TileSmoker)) {
                return false;
            }
        }

        Gui.FURNACE.open(playerIn, worldIn, pos);
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, LIT);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(LIT, (meta & 4) != 0).withProperty(FACING, EnumFacing.byHorizontalIndex(meta & -5));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(LIT) ? 4 : 0) | state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(LIT, false);
    }

    public static void setState(boolean active, World world, BlockPos pos) {
        if(world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileFurnaceAdvanced) {
            world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockFurnaceAdvanced.LIT, active));
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileFurnaceAdvanced) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileFurnaceAdvanced) tile);
        }
        worldIn.removeTileEntity(pos);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.getValue(LIT)) {
            EnumFacing enumfacing = stateIn.getValue(FACING);
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
            double d2 = (double)pos.getZ() + 0.5D;
            double d3 = rand.nextDouble() * 0.6D - 0.3D;

            if (rand.nextDouble() < 0.1D) {
                worldIn.playSound((double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            if (worldIn.getBlockState(pos).getBlock() == Init.SMOKER) {
                switch (enumfacing) {
                    case WEST:
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.52D, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
                        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - 0.52D, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
                        break;
                    case EAST:
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.52D, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
                        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.52D, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
                        break;
                    case NORTH:
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                        break;
                    case SOUTH:
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
                        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);

                }
            }
        }
    }

    public enum FurnaceType {
        SMOKER("Smoker"),
        BLAST_FURNACE("Blast_Furnace");

        private String name;

        FurnaceType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public boolean canCraft(ItemStack stack) {
            if (this == BLAST_FURNACE) {
                return Recipes.BLAST_FURNACE_RECIPES.stream().anyMatch(recipe -> recipe.input.isItemEqual(stack));
            } else if (this == SMOKER) {
                return Recipes.SMOKER_RECIPES.stream().anyMatch(recipe -> recipe.input.isItemEqual(stack));
            } else {
                throw new IllegalStateException("Invalid Smoker/Blast Furnace state");
            }
        }
    }
    public static final class Recipes {
        private static final ArrayList<Recipe> SMOKER_RECIPES = Lists.newArrayListWithExpectedSize(8);
        private static final ArrayList<Recipe> BLAST_FURNACE_RECIPES = Lists.newArrayListWithExpectedSize(8);

        public static void init() {
            smokerRecipe(new ItemStack(Items.FISH), new ItemStack(Items.COOKED_FISH));
            smokerRecipe(new ItemStack(Items.CHICKEN), new ItemStack(Items.COOKED_CHICKEN));
            smokerRecipe(new ItemStack(Items.PORKCHOP), new ItemStack(Items.COOKED_PORKCHOP));
            smokerRecipe(new ItemStack(Items.FISH, 1, 1), new ItemStack(Items.COOKED_FISH, 1, 1));
            smokerRecipe(new ItemStack(Items.BEEF), new ItemStack(Items.COOKED_BEEF));
            smokerRecipe(new ItemStack(Items.MUTTON), new ItemStack(Items.COOKED_MUTTON));
            smokerRecipe(new ItemStack(Items.RABBIT), new ItemStack(Items.COOKED_RABBIT));
            smokerRecipe(new ItemStack(Items.POTATO), new ItemStack(Items.BAKED_POTATO));
            blastFurnaceRecipe(new ItemStack(Blocks.COAL_ORE), new ItemStack(Items.COAL));
            blastFurnaceRecipe(new ItemStack(Blocks.IRON_ORE), new ItemStack(Items.IRON_INGOT));
            blastFurnaceRecipe(new ItemStack(Blocks.DIAMOND_ORE), new ItemStack(Items.DIAMOND));
            blastFurnaceRecipe(new ItemStack(Blocks.EMERALD_ORE), new ItemStack(Items.EMERALD));
            blastFurnaceRecipe(new ItemStack(Blocks.GOLD_ORE), new ItemStack(Items.GOLD_INGOT));
            blastFurnaceRecipe(new ItemStack(Blocks.LAPIS_ORE), new ItemStack(Items.DYE, 1, 4));
            blastFurnaceRecipe(new ItemStack(Blocks.REDSTONE_ORE), new ItemStack(Items.REDSTONE));
            blastFurnaceRecipe(new ItemStack(Blocks.QUARTZ_ORE), new ItemStack(Items.QUARTZ));
        }

        public static void blastFurnaceRecipe(ItemStack input, ItemStack output) {
            BLAST_FURNACE_RECIPES.add(new Recipe(input, output));
        }

        public static void smokerRecipe(ItemStack input, ItemStack output) {
            SMOKER_RECIPES.add(new Recipe(input, output));
        }

        public static ArrayList<Recipe> getSmokerRecipes() {
            return SMOKER_RECIPES;
        }

        public static ArrayList<Recipe> getBlastFurnaceRecipes() {
            return BLAST_FURNACE_RECIPES;
        }

        public static void removeSmokerRecipe(ItemStack input) {
            SMOKER_RECIPES.removeIf(recipe -> recipe.input.isItemEqual(input));
        }

        public static void removeBlastFurnaceRecipe(ItemStack input) {
            BLAST_FURNACE_RECIPES.removeIf(recipe -> recipe.input.isItemEqual(input));
        }

        public static void clearSmoker() {
            SMOKER_RECIPES.clear();
        }

        public static void clearBlastFurnace() {
            BLAST_FURNACE_RECIPES.clear();
        }
    }

    public static class Recipe {
        public final ItemStack input;
        public final ItemStack output;

        public Recipe(ItemStack input, ItemStack output) {
            this.input = input;
            this.output = output;
        }
    }
/*
    public static final class ValidItemExceptionsForSmoker {
        private static final ArrayList<ItemStack> VALID_ITEMS = new ArrayList<>();
        private static final ArrayList<ItemStack> BLACKLIST = new ArrayList<>();

        public static boolean isItemValid(ItemStack stack) {
            if(stack.isEmpty() || VALID_ITEMS.isEmpty()) {
                return false;
            }
            return VALID_ITEMS.stream().anyMatch(stack1 -> stack1.isItemEqualIgnoreDurability(stack));
        }

        public static boolean isBlacklisted(ItemStack stack) {
            return BLACKLIST.stream().anyMatch(stack1 -> stack1.isItemEqualIgnoreDurability(stack));
        }

        public static void addSmeltableItem(ItemStack stack) {
            if(isBlacklisted(stack)) {
                FutureMC.logger.log(Level.WARN, "Tried to add valid Smoker input that was removed by ZenScript " + stack.getItem().getRegistryName());
            }
            VALID_ITEMS.add(stack);
        }

        public static void removeSmeltableItem(ItemStack stack) {
            if(isItemValid(stack)) {
                FutureMC.logger.log(Level.WARN, "Tried to remove valid Smoker input that was added by ZenScript " + stack.getItem().getRegistryName());
            }
            BLACKLIST.add(stack);
        }
    }

    public static final class ValidItemExceptionsForBlastFurnace {
        private static final ArrayList<ItemStack> VALID_ITEMS = new ArrayList<>();
        private static final ArrayList<ItemStack> BLACKLIST = new ArrayList<>();

        public static boolean isItemValid(ItemStack stack) {
            if(stack.isEmpty() || VALID_ITEMS.isEmpty()) {
                return false;
            }
            return VALID_ITEMS.stream().anyMatch(stack1 -> stack1.isItemEqualIgnoreDurability(stack));
        }

        public static boolean isBlacklisted(ItemStack stack) {
            return BLACKLIST.stream().anyMatch(stack1 -> stack1.isItemEqualIgnoreDurability(stack));
        }

        public static void addSmeltableItem(ItemStack stack) {
            if(isBlacklisted(stack)) {
                FutureMC.logger.log(Level.WARN, "Tried to add valid BlastFurnace input that was removed by ZenScript " + stack.getItem().getRegistryName());
            }

            VALID_ITEMS.add(stack);
        }

        public static void removeSmeltableItem(ItemStack stack) {
            if(isItemValid(stack)) {
                FutureMC.logger.log(Level.WARN, "Tried to remove valid BlastFurnace input that was added by ZenScript " + stack.getItem().getRegistryName());
            }
            BLACKLIST.add(stack);
        }
    }*/
}