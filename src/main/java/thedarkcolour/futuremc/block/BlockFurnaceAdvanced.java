package thedarkcolour.futuremc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockOre;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import thedarkcolour.core.block.BlockBase;
import thedarkcolour.core.gui.Gui;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.compat.oredict.OreDict;
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
        switch (meta) {
            case 1: {
                return shortState(EnumFacing.EAST, false);
            }
            case 2: {
                return shortState(EnumFacing.SOUTH, false);
            }
            case 3: {
                return shortState(EnumFacing.WEST, false);
            }
            case 4: {
                return shortState(EnumFacing.NORTH, true);
            }
            case 5: {
                return shortState(EnumFacing.EAST, true);
            }
            case 6: {
                return shortState(EnumFacing.SOUTH, true);
            }
            case 7: {
                return shortState(EnumFacing.WEST, true);
            }
            default: {
                return shortState(EnumFacing.NORTH, false);
            }
        }
    }

    @Override // TODO Copy from BeeHive
    public int getMetaFromState(IBlockState state) {
        if (state.getValue(LIT)) {
            switch (state.getValue(FACING)) {
                case EAST: {
                    return 5;
                }
                case SOUTH: {
                    return 6;
                }
                case WEST: {
                    return 7;
                }
                default: {
                    return 4;
                }
            }
        } else {
            switch (state.getValue(FACING)) {
                case EAST: {
                    return 1;
                }
                case SOUTH: {
                    return 2;
                }
                case WEST: {
                    return 3;
                }
                default: {
                    return 0;
                }
            }
        }
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

    public IBlockState shortState(EnumFacing facing, boolean isLit) {
        return getDefaultState().withProperty(FACING, facing).withProperty(LIT, isLit);
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

            if(worldIn.getBlockState(pos).getBlock() == Init.SMOKER) {
                switch (enumfacing) {
                    case WEST: {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.52D, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
                        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - 0.52D, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
                        break;
                    }
                    case EAST: {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.52D, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
                        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.52D, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
                        break;
                    }
                    case NORTH: {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                        break;
                    }
                    case SOUTH: {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
                        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
                    }
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
                return isOre(stack);
            } else if (this == SMOKER) {
                return isFood(stack);
            } else {
                throw new IllegalStateException("Invalid Smoker/Blast Furnace state");
            }
        }

        public boolean isFood(ItemStack stack) {
            if (stack.getItem() instanceof ItemFood || FurnaceRecipes.instance().getSmeltingResult(stack).getItem() instanceof ItemFood) {
                return !ValidItemExceptionsForSmoker.isBlacklisted(stack);
            }
            return ValidItemExceptionsForSmoker.isItemValid(stack);
        }

        public boolean isOre(ItemStack stack) {
            if (OreDict.getOreName(stack).startsWith("ore") || OreDict.getOreName(FurnaceRecipes.instance().getSmeltingResult(stack)).startsWith("ingot") || Block.getBlockFromItem(stack.getItem()) instanceof BlockOre) {
                return !ValidItemExceptionsForBlastFurnace.isBlacklisted(stack);
            }
            return ValidItemExceptionsForBlastFurnace.isItemValid(stack);
        }
    }

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
    }
}