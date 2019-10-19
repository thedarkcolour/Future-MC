package thedarkcolour.core.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import thedarkcolour.core.block.BlockRotatable;
import thedarkcolour.futuremc.block.BlockBamboo;
import thedarkcolour.futuremc.block.BlockBerryBush;
import thedarkcolour.futuremc.block.BlockCampfire;
import thedarkcolour.futuremc.block.BlockComposter;
import thedarkcolour.futuremc.entity.bee.EntityBee;
import thedarkcolour.futuremc.init.Init;
import thedarkcolour.futuremc.tile.TileBeeHive;
import thedarkcolour.futuremc.tile.TileCampfire;
import thedarkcolour.futuremc.tile.TileComposter;

public class ItemDebugger extends ItemModeled {
    public ItemDebugger() {
        super("debugger");
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();

        if (worldIn.getTileEntity(pos) instanceof TileBeeHive) {
            TileBeeHive hive = (TileBeeHive) worldIn.getTileEntity(pos);
            if (!worldIn.isRemote) {
                if (player.isSneaking()) {
                    hive.setHoneyLevel(5, true);
                } else {
                    player.sendMessage(new TextComponentString("Bees: " + hive.getNumberOfBees() + ", HoneyLevel: " + hive.getHoneyLevel() + ", " + pos));
                }
            }
        } else if (worldIn.getTileEntity(pos) instanceof TileCampfire) {
            TileCampfire campfire = (TileCampfire) worldIn.getTileEntity(pos);

            if (!worldIn.isRemote) {
                if (player.isSneaking()) {
                    for (int i = 0; i < 4; ++i) {
                        player.sendMessage(new TextComponentString("Current: " + campfire.getBuffer().getStackInSlot(i).toString() + ", Result: " + TileCampfire.Recipes.getRecipe(campfire.getBuffer().getStackInSlot(i)).output.toString()));
                    }
                } else {
                    for (int i = 0; i < 4; ++i) {
                        player.sendMessage(new TextComponentString("Progress: " + campfire.getCookingTimes()[i] + ", Total: " + campfire.getCookingTotalTimes()[i]));
                    }
                }
            }
        } else if (block == Init.COMPOSTER) {
            if (player.isSneaking()) {
                if (!BlockComposter.isFull(state)) {
                    ((TileComposter) worldIn.getTileEntity(pos)).addLayer();
                }
            } else {
                if (!worldIn.isRemote) {
                    player.sendStatusMessage(new TextComponentString("Layers: " + worldIn.getBlockState(pos).getValue(BlockComposter.LEVEL)), true);
                }
            }
        } else if (block instanceof BlockRotatable || state.getPropertyKeys().contains(BlockHorizontal.FACING)) {
            int i = worldIn.getBlockState(pos).getValue(BlockHorizontal.FACING).getHorizontalIndex();

            if (i == 3) {
                i = 0;
            } else {
                ++i;
            }

            worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(BlockHorizontal.FACING, EnumFacing.byHorizontalIndex(i)));
        } else if (block == Init.CAMPFIRE) {
            worldIn.setBlockState(pos, state.withProperty(BlockCampfire.LIT, !state.getValue(BlockCampfire.LIT)));
        } else if (block == Init.BAMBOO_STALK) {
            if (player.isSneaking()) {
                worldIn.setBlockState(pos, state.withProperty(BlockBamboo.THICK, !state.getValue(BlockBamboo.THICK)));
            } else {
                int i = state.getValue(BlockBamboo.LEAVES).ordinal();
                if (i > 2) {
                    i = 0;
                } else {
                    ++i;
                }

                worldIn.setBlockState(pos, state.withProperty(BlockBamboo.LEAVES, BlockBamboo.EnumLeaves.values()[i]));
            }
        } else if (block == Init.SWEET_BERRY_BUSH) {
            int i = state.getValue(BlockBerryBush.AGE);
            if (i > 3) {
                i = 0;
            } else {
                ++i;
            }
            worldIn.setBlockState(pos, state.withProperty(BlockBerryBush.AGE, i));
        } else {
            return EnumActionResult.FAIL;
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (target instanceof EntityBee && !playerIn.world.isRemote) {
            EntityBee bee = (EntityBee) target;

            playerIn.sendMessage(new TextComponentString("Hive: " + bee.getHivePos() + ", Flower: " + bee.getFlowerPos()));
        }

        return false;
    }
}