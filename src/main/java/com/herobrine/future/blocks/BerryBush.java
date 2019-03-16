package com.herobrine.future.blocks;

import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.utils.proxy.Init;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeHooks;

import java.util.Random;

public class BerryBush extends BlockFlower implements IGrowable {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);
    private static final AxisAlignedBB YOUNG = new AxisAlignedBB(0.3, 0.0D, 0.3, 0.7, 0.5, 0.7);
    private static final AxisAlignedBB MATURE = new AxisAlignedBB(0.1, 0, 0.1, 0.9, 0.975,0.9);
    public BlockStateContainer state = blockState;

    public BerryBush() {
        setUnlocalizedName(Init.MODID + ".berrybush");
        setRegistryName("berrybush");
        setSoundType(SoundType.PLANT);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 1));
        this.setTickRandomly(true);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AGE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (meta < 4) {
            return this.blockState.getBaseState().withProperty(AGE, meta);
        }
        else {
            return this.blockState.getBaseState().withProperty(AGE, 0);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch(state.getValue(AGE)) {
            case 0: return YOUNG;
            default: return MATURE;
        }
    }

    private int getMaxAge() {
        return 7;
    }

    private boolean isMaxAge(IBlockState state) {
        return state.getValue(AGE) >= this.getMaxAge();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Init.sweetberry);
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return !this.isMaxAge(state);
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return true;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        int age = state.getValue(AGE);
        int mAge = this.getMaxAge();

        if (age > mAge) {
            age = mAge;
        }

        worldIn.setBlockState(pos, this.blockState.getBaseState().withProperty(AGE, age + 1));
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        boolean canGrow = (rand.nextInt(20) == 0);

        if (worldIn.getLightFromNeighbors(pos) >= 8) {
            if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, canGrow)) {
                int age = state.getValue(AGE);
                if (age < 3) {
                    worldIn.setBlockState(pos, this.blockState.getBaseState().withProperty(AGE, age + 1), 2);
                }
                ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        if (FutureConfig.a.sweetberry) {
            Item item = playerIn.getHeldItem(EnumHand.MAIN_HAND).getItem();
            if (item != Items.DYE) {
                if (item != Init.sweetberry) {
                    if (worldIn.getBlockState(pos).getBlock().getMetaFromState(state) == 2) {
                        worldIn.setBlockState(pos, this.blockState.getBaseState().withProperty(AGE, 1));
                        spawnAsEntity(worldIn, pos, new ItemStack(Init.sweetberry));
                    }
                    if (worldIn.getBlockState(pos).getBlock().getMetaFromState(state) == 3) {
                        worldIn.setBlockState(pos, this.blockState.getBaseState().withProperty(AGE, 1));
                        spawnAsEntity(worldIn, pos, new ItemStack(Init.sweetberry, 3));
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isBiomeValid(Biome biome) {
        return biome == Biomes.TAIGA || biome == Biomes.TAIGA_HILLS || biome == Biomes.COLD_TAIGA ||
                biome == Biomes.COLD_TAIGA_HILLS || biome == Biomes.MUTATED_REDWOOD_TAIGA || biome == Biomes.MUTATED_REDWOOD_TAIGA_HILLS ||
                biome == Biomes.REDWOOD_TAIGA || biome == Biomes.REDWOOD_TAIGA_HILLS || biome == Biomes.MUTATED_TAIGA_COLD;
    }

    @Override
    public boolean getSpawnChance(Random random) {
        return random.nextInt(100) > 96;
    }

    @Override
    public boolean getChunkChance(Random random) {
        return random.nextInt(100) > 93;
    }
}