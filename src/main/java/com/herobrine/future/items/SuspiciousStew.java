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
        setMaxStackSize(1);
        setRegistryName(name);
        setUnlocalizedName(Init.MODID + ".SuspiciousStew");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);
        return new ItemStack(Items.BOWL);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        NBTTagCompound nbt = stack.getTagCompound();
        if(nbt == null) {stack.setTagCompound(new NBTTagCompound());}
        if(nbt.hasNoTags()) {
            nbt.setString("effect", "NULL");
        }
    }

    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
            if(!worldIn.isRemote & stack.hasTagCompound()) {
            String tag = (stack.getTagCompound().getString("effect"));
            switch(tag) {
                case "REGEN": player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 140, 1)); break;
                case "JUMP": player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 100, 1)); break;
                case "POISON": player.addPotionEffect(new PotionEffect(MobEffects.POISON, 220, 1)); break;
                case "WITHER": player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 140, 1)); break;
                case "WEAKNESS":player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 160, 1)); break;
                case "BLINDNESS":player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 140, 1)); break;
                case "FIRE_RESISTANCE":player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 60, 1)); break;
                case "SATURATION":player.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 100, 1)); break;
                case "SPEED":player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100, 1));
            }
        }
    }
}