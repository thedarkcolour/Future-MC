package com.herobrine.future.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FlintAndSteelPatch extends ItemFlintAndSteel {
    public FlintAndSteelPatch() {
        setUnlocalizedName("flintAndSteel");
        setRegistryName(new ResourceLocation("flint_and_steel"));
        setCreativeTab(CreativeTabs.TOOLS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        pos = pos.offset(facing);
        ItemStack itemstack = player.getHeldItem(hand);
        BlockFire block = Blocks.FIRE;

        if (!player.canPlayerEdit(pos, facing, itemstack)) {
            return EnumActionResult.FAIL;
        }
        else if(canPlaceFireAt(worldIn, pos)) {
            if (worldIn.isAirBlock(pos)) {
                worldIn.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
                worldIn.setBlockState(pos, block.getDefaultState());
                itemstack.damageItem(1, player);
                return EnumActionResult.SUCCESS;
            }
            if (player instanceof EntityPlayerMP) {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, itemstack);
            }
        }
        return EnumActionResult.FAIL;
    }

    private boolean canPlaceFireAt(World world, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.values()) {
            Block block = world.getBlockState(pos.offset(facing)).getBlock();
            if (block.isFullBlock(world.getBlockState(pos.offset(facing))) || world.getBlockState(pos).getBlock().isFlammable(world, pos, facing)) {
                return true;
            }
        }
        return world.getBlockState(pos).getBlock().isTopSolid(world.getBlockState(pos));
    }
}
