package com.herobrine.future.block;

import com.herobrine.future.sound.Sounds;
import com.herobrine.future.tile.TileBell;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBell extends BlockRotatable {
    public static final PropertyEnum<BellAttachment> ATTACHMENT = PropertyEnum.create("attachment", BellAttachment.class);

    public BlockBell() {
        super("bell", Material.IRON, SoundType.ANVIL);
        setHardness(5.0F);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileBell();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return ring(worldIn, state, worldIn.getTileEntity(pos), hitY, facing, pos);
    }

    private boolean ring(World worldIn, IBlockState state, TileEntity te, float hitY, EnumFacing facing, BlockPos pos) {
        boolean flag = func_220129_a(state, facing, hitY - pos.getY());
        if(!worldIn.isRemote && te instanceof TileBell && flag) {
            ((TileBell)te).func_213939_a(facing);
            this.playRingSound(worldIn, pos);
        }

        return true;
    }

    private boolean func_220129_a(IBlockState state, EnumFacing facing, float v) {
        if (facing.getAxis() != EnumFacing.Axis.Y && !(v > 0.8124F)) {
            EnumFacing direction = state.getValue(FACING);
            BellAttachment bellattachment = state.getValue(ATTACHMENT);
            switch(bellattachment) {
                case FLOOR:
                    return direction.getAxis() == facing.getAxis();
                case SINGLE_WALL:
                case DOUBLE_WALL:
                    return direction.getAxis() != facing.getAxis();
                case CEILING:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    private void playRingSound(World worldIn, BlockPos pos) {
        worldIn.playSound(null, pos, Sounds.BELL_RING, SoundCategory.BLOCKS, 2.0F, 1.0F);
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
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ATTACHMENT, FACING);
    }

    public enum BellAttachment implements IStringSerializable {
        FLOOR("floor"),
        CEILING("ceiling"),
        SINGLE_WALL("single_wall"),
        DOUBLE_WALL("double_wall");

        private final String name;

        BellAttachment(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing.Axis axis = facing.getAxis();
        if (axis == EnumFacing.Axis.Y) {
            IBlockState blockstate = this.getDefaultState().withProperty(ATTACHMENT, facing == EnumFacing.DOWN ? BellAttachment.CEILING : BellAttachment.FLOOR).withProperty(FACING, placer.getHorizontalFacing());
            if (blockstate.getBlock().canPlaceBlockAt(worldIn, pos)) {
                return blockstate;
            }
        } else {
            boolean flag = axis == EnumFacing.Axis.X && isSideSolid(worldIn.getBlockState(pos.west()), worldIn, pos.west(), EnumFacing.EAST) && isSideSolid(worldIn.getBlockState(pos.east()), worldIn, pos.east(), EnumFacing.WEST) || axis == EnumFacing.Axis.Z && isSideSolid(worldIn.getBlockState(pos.north()), worldIn, pos.north(), EnumFacing.SOUTH) && isSideSolid(worldIn
                    .getBlockState(pos.south()), worldIn, pos.south(), EnumFacing.NORTH);
            IBlockState blockstate1 = this.getDefaultState().withProperty(FACING, facing.getOpposite()).withProperty(ATTACHMENT, flag ? BellAttachment.DOUBLE_WALL : BellAttachment.SINGLE_WALL);
            if (blockstate1.getBlock().canPlaceBlockAt(worldIn, pos)) {
                return blockstate1;
            }

            boolean flag1 = isSideSolid(worldIn.getBlockState(pos.down()), worldIn, pos.down(), EnumFacing.UP);
            blockstate1 = blockstate1.withProperty(ATTACHMENT, flag1 ? BellAttachment.FLOOR : BellAttachment.CEILING);
            if (blockstate1.getBlock().canPlaceBlockAt (worldIn, pos)) {
                return blockstate1;
            }
        }

        return null;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(ATTACHMENT, BellAttachment.values()[meta / 4]).withProperty(FACING, EnumFacing.getHorizontal(meta % 4));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ATTACHMENT).ordinal() * 4 + state.getValue(ATTACHMENT).ordinal();
    }
}