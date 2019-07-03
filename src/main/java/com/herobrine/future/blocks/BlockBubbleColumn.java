package com.herobrine.future.blocks;

import com.herobrine.future.init.Init;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

import java.util.Random;

public class BlockBubbleColumn extends BlockBase {
    public static final PropertyBool DRAG = PropertyBool.create("drag");

    public BlockBubbleColumn() {
        super(new BlockProperties("bubble_column", Material.WATER));
        this.setUnlocalizedName(Init.MODID + ".BubbleColumn");
        this.setDefaultState(this.getDefaultState().withProperty(DRAG, true));
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DRAG);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return meta == 1 ? this.getDefaultState() : this.getDefaultState().withProperty(DRAG, false);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(DRAG) ? 1 : 0;
    }

    public static void placeBubbleColumn(World world, BlockPos pos, boolean drag) {
        //if (canHoldBubbleColumn(world, pos)) {
            //world.setBlockState(pos, getDefaultState().withProperty(DRAG, drag), 2);
        //}
    }

    public static boolean canHoldBubbleColumn(World world, BlockPos pos) {
        //IBlockState state = world.getBlockState(pos);
        //if(world.getBlockState(pos).getBlock() != Blocks.WATER) return false;
        //return state.getValue(BlockFluidClassic.LEVEL) >= 8;
        return true;
    }

    @Override
    public void model() {
        IStateMapper mapper = new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return new ModelResourceLocation(getRegistryName(), "inventory");
            }
        };
        ModelLoader.setCustomStateMapper(this, mapper);
    }

    /**
     * Allows SoulSand to place bubble columns
     */
    public static class BlockSoulsandOverride extends BlockSoulSand {
        public BlockSoulsandOverride() {
            setRegistryName(new ResourceLocation("soul_sand"));
            setSoundType(SoundType.SAND);
        }

        @Override
        public String getUnlocalizedName() {
            return "tile.hellsand";
        }

        @Override
        public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
            BlockBubbleColumn.placeBubbleColumn(worldIn, pos.up(), false);
        }

        @Override
        public int tickRate(World worldIn) {
            return 20;
        }

        public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }

        @Override
        public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
            this.neighborChanged(state, worldIn, pos, this, null);
        }
    }
}