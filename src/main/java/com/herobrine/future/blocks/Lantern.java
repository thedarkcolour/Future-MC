package com.herobrine.future.blocks;

import com.herobrine.future.FutureJava;
import com.herobrine.future.utils.blocks.IModel;
import com.herobrine.future.utils.proxy.Init;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class Lantern extends Block implements IModel {
    public static final ResourceLocation lantern = new ResourceLocation(FutureJava.MODID, "Lantern");
    private static final PropertyBool HANGING = PropertyBool.create("hanging");
    private static final AxisAlignedBB SITTING_AABB = new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 0.5625, 0.6875);
    private static final AxisAlignedBB HANGING_AABB = new AxisAlignedBB(0.3125,0.0625,0.3125,0.6875,0.625,0.6875);

    public Lantern() {
        super(Material.IRON);
        setDefaultState(this.blockState.getBaseState().withProperty(HANGING, false));
        setUnlocalizedName(Init.MODID + ".Lantern");
        setRegistryName("Lantern");
        setCreativeTab(Init.futuretab);
        setLightLevel(1);
        setHardness(5);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void models() {
        model(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {

        if (!isBlockInvalid(worldIn, pos.down()) && !isBlockInvalid(worldIn, pos.up())) { // Case if both top/bottom blocks are valid

            if (facing == EnumFacing.DOWN) {
                return this.getDefaultState().withProperty(HANGING, true); // Only hang if top block is right clicked
            }
            else {
                return this.getDefaultState().withProperty(HANGING, false); // Else, do not hang
            }
        } // Does not return if one of the blocks is invalid

        if (isBlockInvalid(worldIn, pos.down())) { // If that invalid block is the bottom block, hang.
            return this.getDefaultState().withProperty(HANGING, true);
        }
        else { // If the invalid block is the top block, do not hang.
            return this.getDefaultState().withProperty(HANGING, false);
        }
    }


    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!canBlockStay(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        else if (isBlockInvalid(worldIn, pos.down())) {
            if(worldIn.getBlockState(pos.up()).getBlock() == this) {

            }
            else {
                worldIn.setBlockState(pos, state.withProperty(HANGING, true));
            }
        }
        else if (isBlockInvalid(worldIn, pos.up())) {
            worldIn.setBlockState(pos, state.withProperty(HANGING, false));
        }
    }

    private boolean isBlockInvalid(World world, BlockPos blockPos) {
        IBlockState state = world.getBlockState(blockPos);
        Block block = state.getBlock();
        return (BlockBush.class.isAssignableFrom(block.getClass())
                || world.isAirBlock(blockPos))
                || isPiston(block);
    }

    private boolean isPiston(Block block) {
        return (block==Blocks.PISTON || block==Blocks.PISTON_EXTENSION || block==Blocks.PISTON_HEAD || block==Blocks.STICKY_PISTON);
    }

    @Override    /*Place-able on given side**/
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }

    private boolean canBlockStay(World worldIn, BlockPos pos) {
        return (!isBlockInvalid(worldIn, pos.down())
                || (!isBlockInvalid(worldIn, pos.up())));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HANGING);
    }


    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (meta != 1) {
            return this.getDefaultState().withProperty(HANGING, true);
        }
        else {
            return this.getDefaultState().withProperty(HANGING, false);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getValue(HANGING)) {
            return 1;
        } else {
            return 0;
        }
    }


    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }


    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess source, BlockPos pos) {
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


    @Override
    public boolean isTopSolid(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(HANGING) ? HANGING_AABB : SITTING_AABB;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return blockState.getValue(HANGING) ? 15 : 1;
    }
}