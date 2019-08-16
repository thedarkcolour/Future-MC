package com.herobrine.future.item;

import com.herobrine.future.FutureMC;
import com.herobrine.future.block.BlockBerryBush;
import com.herobrine.future.client.Modeled;
import com.herobrine.future.init.FutureConfig;
import com.herobrine.future.init.Init;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBerry extends ItemFood implements Modeled {
    public ItemBerry() {
        super(2, 0.2F, false);
        setUnlocalizedName(FutureMC.ID + ".SweetBerry");
        setRegistryName("SweetBerry");
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.FOOD : FutureMC.CREATIVE_TAB);
        addModel();
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        pos = pos.offset(facing);
        ItemStack itemstack = player.getHeldItem(hand);
        BlockBerryBush block = (BlockBerryBush) Init.BERRY_BUSH;

        if (!player.canPlayerEdit(pos, facing, itemstack)) {
            return EnumActionResult.FAIL;
        }
        if(block.canPlaceBlockAt(worldIn, pos)) {
            if (worldIn.isAirBlock(pos)) {
                worldIn.playSound(player, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
                worldIn.setBlockState(pos, block.getBlockState().getBaseState().withProperty(BlockBerryBush.AGE, 0));
                itemstack.shrink(1);

                return EnumActionResult.SUCCESS;
            }
        }
        if (player instanceof EntityPlayerMP) {
            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, itemstack);
        }

        return EnumActionResult.FAIL;
    }
}
