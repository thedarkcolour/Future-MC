package com.herobrine.future.blocks;

import com.herobrine.future.FutureMC;
import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.init.Init;
import com.herobrine.future.tile.GuiHandler;
import com.herobrine.future.tile.advancedfurnace.TileFurnaceAdvanced;
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
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemFood;
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
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Random;

public class BlockFurnaceAdvanced extends BlockBase implements ITileEntityProvider {
    public static final PropertyBool LIT = PropertyBool.create("lit");
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private FurnaceType type;

    public BlockFurnaceAdvanced(FurnaceType type) {
        super(new BlockProperties(type.getRegName()));
        setHardness(3.5F);
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.DECORATIONS : Init.FUTURE_MC_TAB);

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
        if(!(te instanceof TileFurnaceAdvanced)) {
            return false;
        }
        Block block = worldIn.getBlockState(pos).getBlock();

        if(block == Init.BLAST_FURNACE) {
            if (!(te instanceof TileFurnaceAdvanced.TileBlastFurnace)) {
                return false;
            }
        }
        if(block == Init.SMOKER) {
            if (!(te instanceof TileFurnaceAdvanced.TileSmoker)) {
                return false;
            }
        }

        playerIn.openGui(FutureMC.instance, GuiHandler.GUI_FURNACE, worldIn, pos.getX(), pos.getY(), pos.getZ());
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

    @Override
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
        }
        else {
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
        BlockFurnaceAdvanced furnace = (BlockFurnaceAdvanced) world.getBlockState(pos).getBlock();

        if(world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileFurnaceAdvanced) {
            EnumFacing facing = world.getBlockState(pos).getValue(BlockFurnaceAdvanced.FACING);

            world.setBlockState(pos, furnace.getDefaultState().withProperty(BlockFurnaceAdvanced.FACING, facing).withProperty(BlockFurnaceAdvanced.LIT, active));
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
                worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
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
                    case NORTH:{
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

        String regName;

        FurnaceType(String name) {
            this.regName = name;
        }

        public String getRegName() {
            return regName;
        }

        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        public boolean canCraft(ItemStack stack) {
            if(stack.isEmpty()) return false;

            if(this == BLAST_FURNACE) {
                return isOre(stack);
            } else if(this == SMOKER) {
                return isFood(stack);
            } else {
                FutureMC.LOGGER.log(Level.ERROR, "Error: cannot craft because type is not valid");
                return false;
            }
        }

        public boolean isFood(ItemStack stack) {
            if(stack.getItem() instanceof ItemFood) {
                return !ValidItemExceptionsForSmoker.isBlacklisted(stack);
            }
            return ValidItemExceptionsForSmoker.isItemValid(stack);
        }

        public boolean isOre(ItemStack stack) {
            if(getOreName(stack).startsWith("ore")) {
                return !ValidItemExceptionsForBlastFurnace.isBlacklisted(stack);
            }
            return ValidItemExceptionsForBlastFurnace.isItemValid(stack);
        }

        private String getOreName(ItemStack stack) {
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

    public static class ValidItemExceptionsForSmoker {
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
                FutureMC.LOGGER.log(Level.WARN, "Tried to add valid Smoker input that was removed by ZenScript " + stack.getItem().getRegistryName());
            }
            VALID_ITEMS.add(stack);
        }

        public static void removeSmeltableItem(ItemStack stack) {
            if(isItemValid(stack)) {
                FutureMC.LOGGER.log(Level.WARN, "Tried to remove valid Smoker input that was added by ZenScript " + stack.getItem().getRegistryName());
            }
            BLACKLIST.add(stack);
        }
    }

    public static class ValidItemExceptionsForBlastFurnace {
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
                FutureMC.LOGGER.log(Level.WARN, "Tried to add valid BlastFurnace input that was removed by ZenScript " + stack.getItem().getRegistryName());
            }

            VALID_ITEMS.add(stack);
        }

        public static void removeSmeltableItem(ItemStack stack) {
            if(isItemValid(stack)) {
                FutureMC.LOGGER.log(Level.WARN, "Tried to remove valid BlastFurnace input that was added by ZenScript " + stack.getItem().getRegistryName());
            }
            BLACKLIST.add(stack);
        }
    }
}