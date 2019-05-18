package com.herobrine.future.blocks;

import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.init.Init;
import com.herobrine.future.tile.stonecutter.TileStonecutter;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockStonecutter extends BlockBase implements ITileEntityProvider {
    protected static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool OLD = PropertyBool.create("old");
    private static final AxisAlignedBB boundingBox = new AxisAlignedBB(0D,0D,0D,1D,0.5625D,1D);

    public BlockStonecutter() {
        super(new BlockProperties("Stonecutter"));
        setHardness(3.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OLD, FutureConfig.general.stonecutterOld));
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.DECORATIONS : Init.FUTURE_MC_TAB);
    }

    @SuppressWarnings("ConstantConditions")
    public void model() {
        if(!FutureConfig.general.stonecutterOld) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
        }
        else {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName() + "_old", "inventory"));
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, OLD);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        switch (meta) {
            case 1: {
                return this.blockState.getBaseState().withProperty(FACING, EnumFacing.EAST).withProperty(OLD, false);
            }
            case 2: {
                return this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH).withProperty(OLD, false);
            }
            case 3: {
                return this.blockState.getBaseState().withProperty(FACING, EnumFacing.WEST).withProperty(OLD, false);
            }
            case 4: {
                return this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OLD, true);
            }
            case 5: {
                return this.blockState.getBaseState().withProperty(FACING, EnumFacing.EAST).withProperty(OLD, true);
            }
            case 6: {
                return this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH).withProperty(OLD, true);
            }
            case 7: {
                return this.blockState.getBaseState().withProperty(FACING, EnumFacing.WEST).withProperty(OLD, true);
            }
            default: {
                return this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OLD, false);
            }
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        EnumFacing facing = state.getValue(FACING);
        if(!state.getValue(OLD)) {
            switch (facing) {
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
        else {
            switch (facing) {
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
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(OLD, FutureConfig.general.stonecutterOld);
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
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
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isTopSolid(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return getMetaFromState(state) < 4 ? boundingBox : FULL_BLOCK_AABB;
    }

    @Override
    public boolean hasCustomBreakingProgress(IBlockState state) {
        return true;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileStonecutter();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        //if(worldIn.isRemote) {
          //  return true; //false
        //}
       // else {
          //  playerIn.openGui(MainFuture.instance, GuiHandler.GUI_STONECUTTER, worldIn, pos.getX(), pos.getY(), pos.getZ());
          //  return true; //false
        //}
        return false;
    }
}