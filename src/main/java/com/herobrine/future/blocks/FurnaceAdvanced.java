package com.herobrine.future.blocks;

import com.herobrine.future.FutureJava;
import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.tile.advancedfurnace.TileEntityAdvancedFurnace;
import com.herobrine.future.tile.advancedfurnace.TileEntityBlastFurnace;
import com.herobrine.future.tile.advancedfurnace.TileEntitySmoker;
import com.herobrine.future.utils.proxy.Init;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class FurnaceAdvanced extends Block implements ITileEntityProvider {
    public static final PropertyBool LIT = PropertyBool.create("lit");
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private FurnaceType type;
    private static final int GUI_ID = 2;

    public FurnaceAdvanced(FurnaceType type) {
        super(Material.ROCK);
        setUnlocalizedName(FutureJava.MODID + "." + type.getRegName());
        setRegistryName(type.getRegName());
        setCreativeTab(Init.futuretab);
        setHardness(3);
        this.type = type;
        //this.setDefaultState(this.blockState.getBaseState().withProperty(LIT, false).withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        if(type == FurnaceType.SMOKER) {
            return new TileEntitySmoker();
        }
        if(type == FurnaceType.BLAST_FURNACE) {
            return new TileEntityBlastFurnace();
        }
        return null;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        TileEntity te = worldIn.getTileEntity(pos);
        if(!(te instanceof TileEntityAdvancedFurnace)) {
            if (FutureConfig.c.debug) System.out.println("Invalid tile Entity '" + te + "'");
            return false;
        }
        else if(type == FurnaceType.SMOKER) {
            if (!(te instanceof TileEntitySmoker)) {
                if (FutureConfig.c.debug) System.out.println("Invalid tile Entity '" + te + "'");
                return false;
            }
        }
        else if(type == FurnaceType.BLAST_FURNACE) {
            if (!(te instanceof TileEntityBlastFurnace)) {
                if (FutureConfig.c.debug) System.out.println("Invalid tile Entity '" + te + "'");
                return false;
            }
        }

        playerIn.openGui(FutureJava.instance, GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    //
    //  BlockState stuff
    //

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, LIT);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        switch (meta) {
            case 1: {
                return blockState.getBaseState().withProperty(FACING, EnumFacing.EAST).withProperty(LIT, false);
            }
            case 2: {
                return blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH).withProperty(LIT, false);
            }
            case 3: {
                return blockState.getBaseState().withProperty(FACING, EnumFacing.WEST).withProperty(LIT, false);
            }
            case 4: {
                return blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LIT, true);
            }
            case 5: {
                return blockState.getBaseState().withProperty(FACING, EnumFacing.EAST).withProperty(LIT, true);
            }
            case 6: {
                return blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH).withProperty(LIT, true);
            }
            case 7: {
                return blockState.getBaseState().withProperty(FACING, EnumFacing.WEST).withProperty(LIT, true);
            }
            default: {
                return blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LIT, false);
            }
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getValue(LIT)) {
            //return state.getValue(FACING).getIndex() + 8;
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
            //return state.getValue(FACING).getIndex();
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

        public boolean canCraft(ItemStack stack) {
            if(this == BLAST_FURNACE) {
                return isOre(stack);
            }
            else if(this == SMOKER) {
                return isFood(stack);
            }
            else {
                System.out.println("Error: cannot craft because type is not valid");
                return false;
            }
        }

        public boolean isFood(ItemStack stack) {
            return stack.getItem() instanceof ItemFood;
        }

        public boolean isOre(ItemStack stack) {
            return getOreName(stack).startsWith("ORE");
        }

        private String getOreName(ItemStack stack) {
            int[] ids = OreDictionary.getOreIDs(stack);
            if (ids.length >= 1) {//ids != null &&
                return OreDictionary.getOreName(ids[0]);
            }
            return "";
        }
    }

    public IBlockState getBaseBlockState() {
        return this.blockState.getBaseState();
    }

    public static void setState(boolean active, World world, BlockPos pos) {
        FurnaceAdvanced furnace = (FurnaceAdvanced) world.getBlockState(pos).getBlock();

        if(world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityAdvancedFurnace) {
            EnumFacing facing = world.getBlockState(pos).getValue(FurnaceAdvanced.FACING);

            if(active) {
                System.out.println("blockState is now ACTIVE");
                world.setBlockState(pos, furnace.getBaseBlockState().withProperty(FurnaceAdvanced.FACING, facing).withProperty(FurnaceAdvanced.LIT, true));
            }
            else {
                world.setBlockState(pos, furnace.getBaseBlockState().withProperty(FurnaceAdvanced.FACING, facing).withProperty(FurnaceAdvanced.LIT, false));
            }
        }
    }
}