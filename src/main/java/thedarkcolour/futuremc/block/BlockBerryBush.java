package thedarkcolour.futuremc.block;

import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import thedarkcolour.futuremc.entity.bee.EntityBee;
import thedarkcolour.futuremc.init.FutureConfig;
import thedarkcolour.futuremc.init.Init;

import java.util.Random;

public class BlockBerryBush extends BlockFlower implements IGrowable, IPlantable {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);
    private static final AxisAlignedBB YOUNG = new AxisAlignedBB(0.3D, 0.0D, 0.3D, 0.7D, 0.5D, 0.7D);
    private static final AxisAlignedBB MATURE = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.975D,0.9D);
    public static final DamageSource BERRY_BUSH_DAMAGE = new DamageSource("berryBush");

    public BlockBerryBush() {
        super("BerryBush");
        setSoundType(SoundType.PLANT);
        setDefaultState(getBlockState().getBaseState().withProperty(AGE, 1));
        setTickRandomly(true);
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        Item item = playerIn.getHeldItem(EnumHand.MAIN_HAND).getItem();
        if (item != Items.DYE) {
            if (item != Init.SWEET_BERRY) {
                if (worldIn.getBlockState(pos).getBlock().getMetaFromState(state) == 2) {
                    worldIn.setBlockState(pos, this.blockState.getBaseState().withProperty(AGE, 1));
                    spawnAsEntity(worldIn, pos, new ItemStack(Init.SWEET_BERRY));
                }
                if (worldIn.getBlockState(pos).getBlock().getMetaFromState(state) == 3) {
                    worldIn.setBlockState(pos, this.blockState.getBaseState().withProperty(AGE, 1));
                    spawnAsEntity(worldIn, pos, new ItemStack(Init.SWEET_BERRY, 3));
                }
            }
        }
        return false;
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
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (entityIn instanceof EntityLivingBase && !(entityIn instanceof EntityBee)) {
            entityIn.fallDistance = 0.0F;
            entityIn.motionX *= 0.800000011920929D;
            entityIn.motionY *= 0.75D;
            entityIn.motionZ *= 0.800000011920929D;

            if (!worldIn.isRemote && state.getValue(AGE) > 0 && (entityIn.prevPosX != entityIn.posX || entityIn.prevPosZ != entityIn.posZ)) {
                double double_1 = Math.abs(entityIn.posX - entityIn.prevPosX);
                double double_2 = Math.abs(entityIn.posZ - entityIn.prevPosZ);

                if (double_1 >= 0.003000000026077032D || double_2 >= 0.003000000026077032D) {
                    entityIn.attackEntityFrom(BERRY_BUSH_DAMAGE, 1.0F);
                }
            }
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if(!worldIn.isRemote) {
            if (worldIn.getBlockState(pos).getBlock().getMetaFromState(state) == 2) {
                worldIn.setBlockState(pos, this.blockState.getBaseState().withProperty(AGE, 1));
                spawnAsEntity(worldIn, pos, new ItemStack(Init.SWEET_BERRY));
            }
            else if (worldIn.getBlockState(pos).getBlock().getMetaFromState(state) == 3) {
                worldIn.setBlockState(pos, this.blockState.getBaseState().withProperty(AGE, 1));
                spawnAsEntity(worldIn, pos, new ItemStack(Init.SWEET_BERRY, 3));
            }
        }
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
        return state.getValue(AGE) == 0 ? YOUNG : MATURE;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Init.SWEET_BERRY);
    }

    @Override
    public boolean isBiomeValid(Biome biome) {
        return biome == Biomes.TAIGA || biome == Biomes.TAIGA_HILLS || biome == Biomes.COLD_TAIGA ||
                biome == Biomes.COLD_TAIGA_HILLS || biome == Biomes.MUTATED_REDWOOD_TAIGA || biome == Biomes.MUTATED_REDWOOD_TAIGA_HILLS ||
                biome == Biomes.REDWOOD_TAIGA || biome == Biomes.REDWOOD_TAIGA_HILLS || biome == Biomes.MUTATED_TAIGA_COLD;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return state.getValue(AGE) < 3;
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return state.getValue(AGE) < 3;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        int age = state.getValue(AGE);
        worldIn.setBlockState(pos, this.blockState.getBaseState().withProperty(AGE, age + 1));
    }

    @Override
    public int getFlowerChance() {
        return FutureConfig.general.berryBushChance;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Plains;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return getBlockState().getBaseState().withProperty(AGE, 0);
    }

}