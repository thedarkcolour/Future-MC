package com.herobrine.future.items;

import com.herobrine.future.utils.Init;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
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
    public ItemBerry(int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation,isWolfFood);
        func_77655_b(Init.MODID + ".sweetberry");
        setRegistryName("sweetberry");
        func_77637_a(Init.futuretab);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public EnumActionResult func_180614_a(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        pos = pos.func_177972_a(facing);
        ItemStack itemstack = player.func_184586_b(hand);
        Block block = Init.berrybush;

        if (!player.func_175151_a(pos, facing, itemstack)) {
            return EnumActionResult.FAIL;
        } else if (!player.func_71043_e(false)) {
            if(block.func_176196_c(worldIn, pos)) {
                if (worldIn.func_175623_d(pos)) {
                    worldIn.func_184133_a(player, pos, SoundEvents.field_187577_bU, SoundCategory.BLOCKS, 1.0F, 0.8F);
                    worldIn.func_175656_a(pos, block.func_176223_P());
                    itemstack.func_190918_g(1);
                    return EnumActionResult.SUCCESS;
                }
            }
            if (player instanceof EntityPlayerMP) {
                CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)player, pos, itemstack);
            }
        }
        return EnumActionResult.SUCCESS;
    }
}
