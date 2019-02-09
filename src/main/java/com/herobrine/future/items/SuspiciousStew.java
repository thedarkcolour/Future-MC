package com.herobrine.future.items;

import com.herobrine.future.utils.Init;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SuspiciousStew extends ItemFood {
    public SuspiciousStew(String name, int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation,isWolfFood);
        func_77625_d(1);
        setRegistryName(name);
        func_77655_b(Init.MODID + ".SuspiciousStew");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    public ItemStack func_77654_b(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        super.func_77654_b(stack, worldIn, entityLiving);
        return new ItemStack(Items.field_151054_z);
    }

    @Override
    public void func_77622_d(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        NBTTagCompound nbt = stack.func_77978_p();
        if(nbt == null) {stack.func_77982_d(new NBTTagCompound());}
        if(nbt.func_82582_d()) {
            nbt.func_74778_a("effect", "NULL");
        }
    }

    protected void func_77849_c(ItemStack stack, World worldIn, EntityPlayer player) {
            if(!worldIn.field_72995_K & stack.func_77942_o()) {
            String tag = (stack.func_77978_p().func_74779_i("effect"));
            switch(tag) {
                case "REGEN": player.func_70690_d(new PotionEffect(MobEffects.field_76428_l, 140, 1)); break;
                case "JUMP": player.func_70690_d(new PotionEffect(MobEffects.field_76430_j, 100, 1)); break;
                case "POISON": player.func_70690_d(new PotionEffect(MobEffects.field_76436_u, 220, 1)); break;
                case "WITHER": player.func_70690_d(new PotionEffect(MobEffects.field_82731_v, 140, 1)); break;
                case "WEAKNESS":player.func_70690_d(new PotionEffect(MobEffects.field_76437_t, 160, 1)); break;
                case "BLINDNESS":player.func_70690_d(new PotionEffect(MobEffects.field_76440_q, 140, 1)); break;
                case "FIRE_RESISTANCE":player.func_70690_d(new PotionEffect(MobEffects.field_76426_n, 60, 1)); break;
                case "SATURATION":player.func_70690_d(new PotionEffect(MobEffects.field_76443_y, 100, 1)); break;
                case "SPEED":player.func_70690_d(new PotionEffect(MobEffects.field_76424_c, 100, 1));
            }
        }
    }
}
