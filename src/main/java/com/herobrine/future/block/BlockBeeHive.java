package com.herobrine.future.block;

import com.herobrine.future.entity.bee.EntityBee;
import com.herobrine.future.init.Init;
import com.herobrine.future.tile.TileBeeHive;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thedarkcolour.core.block.InteractionBlock;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBeeHive extends InteractionBlock {
    public static final PropertyInteger HONEY_LEVEL = PropertyInteger.create("honey_level", 0, 5);
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockBeeHive(String regName) {
        super(regName);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileBeeHive();
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileBeeHive) {
            TileBeeHive hive = (TileBeeHive) worldIn.getTileEntity(pos);

            return hive.getHoneyLevel();
        }

        return 0;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        if (!worldIn.isRemote) {
            if (te instanceof TileBeeHive && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0) {
                ((TileBeeHive)te).angerBees(player, TileBeeHive.BeeState.field_20429);
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            List<EntityBee> nearbyBees = worldIn.getEntitiesWithinAABB(EntityBee.class, (new AxisAlignedBB(pos)).expand(8.0D, 6.0D, 8.0D));
            if (!nearbyBees.isEmpty()) {
                List<EntityPlayer> nearbyPlayers = worldIn.getEntitiesWithinAABB(EntityPlayer.class, (new AxisAlignedBB(pos)).expand(8.0D, 6.0D, 8.0D));
                int int_1 = nearbyPlayers.size();

                for (EntityBee bee : nearbyBees) {
                    if (bee.getAttackTarget() == null) {
                        bee.setBeeAttacker(nearbyPlayers.get(worldIn.rand.nextInt(int_1)));
                    }
                }
            }
        }
    }

    public static void emptyHoney(World worldIn, IBlockState state, BlockPos pos, @Nullable EntityPlayer playerIn) {
        worldIn.setBlockState(pos, state.withProperty(HONEY_LEVEL, 0), 3);
        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileBeeHive) {
            TileBeeHive hive = (TileBeeHive)tile;
            hive.angerBees(playerIn, TileBeeHive.BeeState.field_20429);
        }
    }

    public static void dropHoneyComb(World worldIn, BlockPos pos) {
        for (int i = 0; i < 3; ++i) {
            spawnAsEntity(worldIn, pos, new ItemStack(Init.HONEY_COMB));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileBeeHive) {
            TileBeeHive hive = (TileBeeHive) worldIn.getTileEntity(pos);

            return state.withProperty(HONEY_LEVEL, hive.getHoneyLevel());
        }
        return state;
    }
}