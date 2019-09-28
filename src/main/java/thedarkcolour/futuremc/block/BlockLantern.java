package thedarkcolour.futuremc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thedarkcolour.core.block.BlockBase;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.init.FutureConfig;

public class BlockLantern extends BlockBase {
    private static final PropertyBool HANGING = PropertyBool.create("hanging");
    private static final AxisAlignedBB SITTING_AABB = makeAABB(5, 0, 5, 11, 9, 11);
    private static final AxisAlignedBB HANGING_AABB = makeAABB(5,1,5,11,10,11);

    public BlockLantern() {
        super("Lantern", Material.IRON);
        setLightLevel(1);
        setHardness(5);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 0);
        setDefaultState(this.blockState.getBaseState().withProperty(HANGING, false));
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.DECORATIONS : FutureMC.TAB);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (!isBlockInvalid(worldIn, pos.down()) && !isBlockInvalid(worldIn, pos.up())) { // don't touch
            if (facing == EnumFacing.DOWN) {
                return this.getDefaultState().withProperty(HANGING, true);
            }
            else {
                return this.getDefaultState().withProperty(HANGING, false);
            }
        }

        if (isBlockInvalid(worldIn, pos.down())) {
            return this.getDefaultState().withProperty(HANGING, true);
        }
        else {
            return this.getDefaultState().withProperty(HANGING, false);
        }
    }

    private boolean isBlockInvalid(World world, BlockPos blockPos) {
        IBlockState state = world.getBlockState(blockPos);
        Block block = state.getBlock();
        return (block instanceof BlockBush) || world.isAirBlock(blockPos) || isPiston(block);
    }

    private boolean isPiston(Block block) {
        return (block == Blocks.PISTON || block == Blocks.PISTON_EXTENSION || block == Blocks.PISTON_HEAD || block == Blocks.STICKY_PISTON);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }

    private boolean canBlockStay(World worldIn, BlockPos pos) {
        return (!isBlockInvalid(worldIn, pos.down())
                || (!isBlockInvalid(worldIn, pos.up())));
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!canBlockStay(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        else if (isBlockInvalid(worldIn, pos.down())) {
            if(worldIn.getBlockState(pos.up()).getBlock() != this) {
                worldIn.setBlockState(pos, state.withProperty(HANGING, true));
            }
        }
        else if (isBlockInvalid(worldIn, pos.up())) {
            worldIn.setBlockState(pos, state.withProperty(HANGING, false));
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HANGING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(HANGING, meta != 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(HANGING) ? 1 : 0;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return blockState.getValue(HANGING) ? 15 : 0;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(HANGING) ? HANGING_AABB : SITTING_AABB;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override public boolean isFullBlock(IBlockState state) { return false; }
    @Override public boolean isNormalCube(IBlockState state, IBlockAccess source, BlockPos pos) { return false; }
    @Override public boolean isFullCube(IBlockState state) { return false; }
    @Override public boolean isOpaqueCube(IBlockState state) { return false; }
    @Override public boolean isTopSolid(IBlockState state) { return false; }
}