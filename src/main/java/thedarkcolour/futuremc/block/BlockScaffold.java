package thedarkcolour.futuremc.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thedarkcolour.core.block.BlockBase;
import thedarkcolour.futuremc.init.Init;
import thedarkcolour.futuremc.sound.Sounds;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockScaffold extends BlockBase {
    public static final PropertyInteger DISTANCE = PropertyInteger.create("distance", 0, 7);
    public static final PropertyBool BOTTOM = PropertyBool.create("bottom");
    private static final AxisAlignedBB TOP_AABB = makeAABB(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private AxisAlignedBB BOTTOM_AABB = makeAABB(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

    public BlockScaffold() {
        super("scaffolding", Material.CIRCUITS, Sounds.SCAFFOLDING);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        int i = getDistance(worldIn, pos);
        return this.getDefaultState().withProperty(DISTANCE, i).withProperty(BOTTOM, this.func_220116_a(worldIn, pos, i));
    }

    private boolean func_220116_a(World worldIn, BlockPos pos, int i) {
        return i > 0 && worldIn.getBlockState(pos.down()).getBlock() != this;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if(!worldIn.isRemote) {
            worldIn.scheduleUpdate(pos, this, 1);
        }
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        if (shouldBlock(entityIn)) {
            for(AxisAlignedBB box : boundingBoxesA) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, box);
            }
        } else if(state.getValue(DISTANCE) != 0 && state.getValue(BOTTOM)) {
            for(AxisAlignedBB box : boundingBoxesB) {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, box);
            }
        }
    }

    private boolean shouldBlock(Entity entityIn) {
        return entityIn != null && !entityIn.isSneaking() && entityIn instanceof EntityPlayer;
    }

    public static int getDistance(World worldIn, BlockPos pos) {
        BlockPos.MutableBlockPos blockPos = (new BlockPos.MutableBlockPos(pos)).move(EnumFacing.DOWN);
        IBlockState blockstate = worldIn.getBlockState(blockPos);
        int i = 7;
        if (blockstate.getBlock() == Init.SCAFFOLDING) {
            i = blockstate.getValue(DISTANCE);
        } else if (blockstate.getBlock().isSideSolid(blockstate, worldIn, blockPos, EnumFacing.UP)) {
            return 0;
        }

        for(EnumFacing direction : EnumFacing.Plane.HORIZONTAL) {
            IBlockState blockstate1 = worldIn.getBlockState(blockPos.setPos(pos).move(direction));
            if (blockstate1.getBlock() == Init.SCAFFOLDING) {
                i = Math.min(i, blockstate1.getValue(DISTANCE) + 1);
                if (i == 1) {
                    break;
                }
            }
        }

        return i;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote) {
            worldIn.scheduleUpdate(pos, this, 1);
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int i = getDistance(worldIn, pos);
        IBlockState blockstate = state.withProperty(DISTANCE, i).withProperty(BOTTOM, this.func_220116_a(worldIn, pos, i));
        if (blockstate.getValue(DISTANCE) == 7) {
            if (state.getValue(DISTANCE) == 7) {
                worldIn.spawnEntity(new EntityFallingBlock(worldIn, (double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D, state));
            } else {
                worldIn.destroyBlock(pos, true);
            }
        } else if (state != blockstate) {
            worldIn.setBlockState(pos, blockstate, 3);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return getDistance(worldIn, pos) < 7;
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DISTANCE, BOTTOM);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean bottom = meta > 7;
        int distance = bottom ? meta - 8 : meta;
        return getDefaultState().withProperty(BOTTOM, bottom).withProperty(DISTANCE, distance);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BOTTOM) ? state.getValue(DISTANCE) + 8 : state.getValue(DISTANCE);
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    static AxisAlignedBB[] boundingBoxesA;
    static AxisAlignedBB[] boundingBoxesB;

    static {
        boundingBoxesA = new AxisAlignedBB[5];
        boundingBoxesA[0] = makeAABB(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D);
        boundingBoxesA[1] = makeAABB(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 2.0D);
        boundingBoxesA[2] = makeAABB(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D);
        boundingBoxesA[3] = makeAABB(0.0D, 0.0D, 14.0D, 2.0D, 16.0D, 16.0D);
        boundingBoxesA[4] = makeAABB(14.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D);

        boundingBoxesB = new AxisAlignedBB[4];
        boundingBoxesB[0] = makeAABB(0.0D, 0.0D, 0.0D, 2.0D, 2.0D, 16.0D);
        boundingBoxesB[1] = makeAABB(14.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
        boundingBoxesB[2] = makeAABB(0.0D, 0.0D, 14.0D, 16.0D, 2.0D, 16.0D);
        boundingBoxesB[3] = makeAABB(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 2.0D);
    }
}