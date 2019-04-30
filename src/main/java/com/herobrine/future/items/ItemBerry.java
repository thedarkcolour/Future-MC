package com.herobrine.future.items;

import com.herobrine.future.blocks.BlockBerryBush;
import com.herobrine.future.init.Init;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBerry extends ItemFood {
    public ItemBerry() {
        super(2, 0.2F, false);
        setUnlocalizedName(Init.MODID + ".SweetBerry");
        setRegistryName("SweetBerry");
        setCreativeTab(Init.FUTURE_MC_TAB);
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    public void model() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        pos = pos.offset(facing);
        ItemStack itemstack = player.getHeldItem(hand);
        BlockBerryBush block = (BlockBerryBush) Init.BERRY_BUSH;

        if (!player.canPlayerEdit(pos, facing, itemstack)) {
            return EnumActionResult.FAIL;
        }
        else if (!player.canEat(false)) {
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
        }
        return EnumActionResult.FAIL;
    }
}
