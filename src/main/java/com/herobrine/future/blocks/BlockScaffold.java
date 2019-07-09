package com.herobrine.future.blocks;

public class BlockScaffold {}/*extends BlockBase {
    public static final PropertyInteger DISTANCE = PropertyInteger.create("distance", 0, 7);
    public static final PropertyBool BOTTOM = PropertyBool.create("bottom");
    private static final AxisAlignedBB TOP_AABB = makeAABB(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private AxisAlignedBB BOTTOM_AABB = makeAABB(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

    public BlockScaffold() {
        super(new BlockProperties("scaffolding", Material.CIRCUITS, Sounds.SCAFFOLDING));
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
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
                worldIn.spawnEntity(new EntityFallingBlock(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, state));
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
}*/