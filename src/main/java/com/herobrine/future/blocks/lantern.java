package com.herobrine.future.blocks;

import com.herobrine.future.utils.futurejava;
import com.herobrine.future.utils.init;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class lantern extends Block {

    public static final ResourceLocation lantern = new ResourceLocation(futurejava.MODID, "lantern");
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    private static final AxisAlignedBB SITTING_AABB = new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 0.5625, 0.6875);
    private static final AxisAlignedBB HANGING_AABB = new AxisAlignedBB(0.3125,0.0625,0.3125,0.6875,0.625,0.6875);

    public lantern() {
        super(Material.IRON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        setUnlocalizedName(init.MODID + ".lantern");
        setRegistryName("lantern");
        setCreativeTab(init.futuretab);
        setLightLevel(1);
        setHardness(5);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 1);
    }


    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    //Overrides
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (worldIn.getBlockState(pos.down()).getBlock() == Blocks.AIR) {
            return this.getDefaultState().withProperty(FACING, EnumFacing.DOWN);
        } else {
            for (EnumFacing enumfacing : EnumFacing.Plane.VERTICAL) {
                if (this.canAttachTo(worldIn, pos.offset(enumfacing.getOpposite()), enumfacing)) {
                    return this.getDefaultState().withProperty(FACING, enumfacing);
                }
            }
            return this.getDefaultState();
        }
    }

    @Override
    protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, FACING); }


    @Override
    public IBlockState getStateFromMeta(int meta) { return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7)); }

    @Override
    public int getMetaFromState(IBlockState state) { return state.getValue(FACING).getIndex(); }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!canPlaceBlockAt(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess source, BlockPos pos) { return false; }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) { return false; }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override    /**Place-able on given side**/
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return this.canPlaceAt(worldIn, pos);
    }
    //private booleans
    private boolean canAttachTo(World worldIn, BlockPos pos, EnumFacing facing) {
        IBlockState state = worldIn.getBlockState(pos);
        boolean flag = isExceptBlockForAttachWithPiston(state.getBlock());
        return !flag && this.canPlaceBlockAt(worldIn, pos);
    }

    private boolean canPlaceAt(World worldIn, BlockPos pos) {
        BlockPos blockpos = pos.down();
        IBlockState state = worldIn.getBlockState(blockpos);
        Block block = state.getBlock();
        Block blockdown = worldIn.getBlockState(blockpos).getBlock();
        return block != this && !((worldIn.getBlockState(blockpos).getBlock() == Blocks.AIR || BlockBush.class.isAssignableFrom(blockdown.getClass())) & worldIn.getBlockState(pos.up()).getBlock() == Blocks.AIR);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch(state.getValue(FACING)) {
            case UP:
                return SITTING_AABB;
            case DOWN:
                return HANGING_AABB;
            default:
                return SITTING_AABB;
        }
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) { return BlockFaceShape.UNDEFINED; }
}