package com.herobrine.future.blocks;

import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.init.Init;
import net.minecraft.block.BlockPurpurSlab;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Random;

import static net.minecraft.block.BlockPurpurSlab.VARIANT;

public abstract class BlockNewSlab extends BlockSlab {
    public BlockNewSlab() {
        super(Material.ROCK);
        setHardness(2.0F);
    }

    protected abstract Item getSlab();

    @Override
    public String getUnlocalizedName(int meta) {
        return super.getUnlocalizedName();
    }

    @Override
    public IProperty<?> getVariantProperty() {
        return VARIANT;
    }

    @Override
    public Comparable<?> getTypeForItem(ItemStack stack) {
        return BlockPurpurSlab.Variant.DEFAULT;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return isDouble() ? new BlockStateContainer(this, VARIANT) : new BlockStateContainer(this, HALF, VARIANT);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return isDouble() ? this.getDefaultState() : getBlockState().getBaseState().withProperty(HALF, meta == 1 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return isDouble() ? 0 : (state.getValue(HALF) == EnumBlockHalf.TOP ? 1 : 0);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(getSlab());
    }

    public static class Half extends BlockNewSlab {
        public Half(String variant) {
            setUnlocalizedName(Init.MODID + "." + variant + "_slab");
            setRegistryName(variant + "_slab");
            setLightOpacity(0);
        }

        @Override
        public boolean isDouble() {
            return false;
        }

        @Override
        protected Item getSlab() {
            return Item.getItemFromBlock(this);
        }

        @Override
        public boolean isOpaqueCube(IBlockState state) {
            return false;
        }
    }

    public static class Double extends BlockNewSlab {
        public Double(String variant) {
            setUnlocalizedName(Init.MODID + "." + variant + "_double_slab");
            setRegistryName(variant + "_double_slab");
            this.variant = variant;
        }

        @Override
        public boolean isDouble() {
            return true;
        }

        String variant;

        @Override
        protected Item getSlab() {
            return ForgeRegistries.ITEMS.getValue(new ResourceLocation(Init.MODID + ":" + variant + "_slab"));
        }

        @Override
        public Item getItemDropped(IBlockState state, Random rand, int fortune) {
            return getSlab();
        }

        @Override
        public int quantityDropped(Random random) {
            return 2;
        }
    }
}