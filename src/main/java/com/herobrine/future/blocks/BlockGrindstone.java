package com.herobrine.future.blocks;

import com.herobrine.future.FutureMC;
import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.init.Init;
import com.herobrine.future.tile.GuiHandler;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGrindstone extends BlockBase {
    private static final PropertyEnum<BlockGrindstone.EnumAttachment> ATTACHMENT = PropertyEnum.create("face", BlockGrindstone.EnumAttachment.class);
    private static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockGrindstone() {
        super(new BlockProperties("Grindstone"));
        setHardness(3.5F);
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.DECORATIONS : Init.FUTURE_MC_TAB);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) {
            return true;
        }
        else {
            playerIn.openGui(FutureMC.instance, GuiHandler.GUI_GRINDSTONE, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return createBoundingBox(state.getValue(ATTACHMENT),state.getValue(FACING));
    }

    public enum EnumAttachment implements IStringSerializable {
        WALL("wall"), FLOOR("floor"), CEILING("ceiling");

        public String name;

        EnumAttachment(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        public static EnumAttachment getFromFacing(EnumFacing facing) {
            if(facing == EnumFacing.DOWN) {
                return CEILING;
            }
            if(facing == EnumFacing.UP) {
                return FLOOR;
            }
            return WALL;
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ATTACHMENT, FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        switch (meta) {
            default: {
                return getBlockState().getBaseState().withProperty(ATTACHMENT, EnumAttachment.FLOOR).withProperty(FACING, EnumFacing.NORTH);
            }
            case 1: {
                return getBlockState().getBaseState().withProperty(ATTACHMENT, EnumAttachment.FLOOR).withProperty(FACING, EnumFacing.EAST);
            }
            case 2: {
                return getBlockState().getBaseState().withProperty(ATTACHMENT, EnumAttachment.FLOOR).withProperty(FACING, EnumFacing.SOUTH);
            }
            case 3: {
                return getBlockState().getBaseState().withProperty(ATTACHMENT, EnumAttachment.FLOOR).withProperty(FACING, EnumFacing.WEST);
            }
            case 4: {
                return getBlockState().getBaseState().withProperty(ATTACHMENT, EnumAttachment.WALL).withProperty(FACING, EnumFacing.NORTH);
            }
            case 5: {
                return getBlockState().getBaseState().withProperty(ATTACHMENT, EnumAttachment.WALL).withProperty(FACING, EnumFacing.EAST);
            }
            case 6: {
                return getBlockState().getBaseState().withProperty(ATTACHMENT, EnumAttachment.WALL).withProperty(FACING, EnumFacing.SOUTH);
            }
            case 7: {
                return getBlockState().getBaseState().withProperty(ATTACHMENT, EnumAttachment.WALL).withProperty(FACING, EnumFacing.WEST);
            }
            case 8: {
                return getBlockState().getBaseState().withProperty(ATTACHMENT, EnumAttachment.CEILING).withProperty(FACING, EnumFacing.NORTH);
            }
            case 9: {
                return getBlockState().getBaseState().withProperty(ATTACHMENT, EnumAttachment.CEILING).withProperty(FACING, EnumFacing.EAST);
            }
            case 10: {
                return getBlockState().getBaseState().withProperty(ATTACHMENT, EnumAttachment.CEILING).withProperty(FACING, EnumFacing.SOUTH);
            }
            case 11: {
                return getBlockState().getBaseState().withProperty(ATTACHMENT, EnumAttachment.CEILING).withProperty(FACING, EnumFacing.WEST);
            }
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        switch (state.getValue(ATTACHMENT)) {
            case WALL: {
                switch (state.getValue(FACING)) {
                    default: return 4;
                    case EAST: return 5;
                    case SOUTH: return 6;
                    case WEST: return 7;
                }
            }
            case CEILING: {
                switch (state.getValue(FACING)) {
                    default: return 8;
                    case EAST: return 9;
                    case SOUTH: return 10;
                    case WEST: return 11;
                }
            }
            default: {
                switch (state.getValue(FACING)) {
                    default: return 0;
                    case EAST: return 1;
                    case SOUTH: return 2;
                    case WEST: return 3;
                }
            }
        }
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumAttachment attachment = EnumAttachment.getFromFacing(facing);
        EnumFacing finalFacing = placer.getHorizontalFacing();
        if(attachment == EnumAttachment.WALL) {
            finalFacing = facing;
        }

        return getBlockState().getBaseState().withProperty(ATTACHMENT, attachment).withProperty(FACING, finalFacing);
    }

    public static AxisAlignedBB createBoundingBox(EnumAttachment attachment,EnumFacing facing) {
        AxisAlignedBB FLOOR_X = makeAABB(2D, 4D, 4D, 14D, 16D, 12D);
        AxisAlignedBB FLOOR_Z = makeAABB(4D, 4D, 2D, 12D, 16D, 14D);
        AxisAlignedBB CEILING_X = makeAABB(2D, 0D, 4D, 14D, 12D, 12D);
        AxisAlignedBB CEILING_Z = makeAABB(4D, 0D, 2D, 12D, 12D, 14D);
        AxisAlignedBB WALL_NORTH = makeAABB(4D, 2D, 0D, 12D, 14D, 12D);
        AxisAlignedBB WALL_WEST = makeAABB(0D, 2d, 4D, 12D, 14D, 12D);
        AxisAlignedBB WALL_SOUTH = makeAABB(4D, 2D, 4D, 12D, 14D, 16D);
        AxisAlignedBB WALL_EAST = makeAABB(4D, 2D, 4D, 16D, 14D, 12D);

        switch (attachment) {
            case FLOOR: {
                switch (facing) {
                    case NORTH:
                    case SOUTH:
                        return FLOOR_Z;
                    case EAST:
                    case WEST:
                        return FLOOR_X;
                }
            }
            case WALL: {
                switch (facing) {
                    case EAST: return WALL_EAST;
                    case WEST: return WALL_WEST;
                    case NORTH: return WALL_NORTH;
                    case SOUTH: return WALL_SOUTH;
                }
            }
            case CEILING: {
                switch (facing) {
                    case NORTH:
                    case SOUTH:
                    return CEILING_Z;
                    case EAST:
                    case WEST:
                    return CEILING_X;
                }
            }
        }

        return NULL_AABB;
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
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
}