package com.herobrine.future.items;

import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.init.Init;
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

import java.util.Random;

public class ItemSuspiciousStew extends ItemFood {
    public ItemSuspiciousStew() {
        super(6, 0.6F, false);
        setMaxStackSize(1);
        setRegistryName("SuspiciousStew");
        setUnlocalizedName(Init.MODID + ".SuspiciousStew");
        setCreativeTab(Init.FUTURE_MC_TAB);
    }

    @SideOnly(Side.CLIENT)
    public void model() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);
        return new ItemStack(Items.BOWL);
    }

    public void createNBT(ItemStack stack) {
        NBTTagCompound nbt = new NBTTagCompound();
        if(FutureConfig.modFlowers.suspiciousStewEffect && !FutureConfig.modFlowers.isSuspiciousStewRandom) {
            nbt.setString("effect", "NULL");
        }
        stack.setTagCompound(nbt);
    }

    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        if(!worldIn.isRemote && FutureConfig.modFlowers.suspiciousStewEffect) {
            if(stack.getTagCompound() == null) {
                createNBT(stack);
            }
            if(FutureConfig.modFlowers.isSuspiciousStewRandom)  {
                Random random = new Random();
                switch (random.nextInt(9)) {
                    case 0: {
                        player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 140, 1));
                    } break;
                    case 1: {
                        player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 100, 1));
                    } break;
                    case 2: {
                        player.addPotionEffect(new PotionEffect(MobEffects.POISON, 220, 1));
                    } break;
                    case 3: {
                        player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 140, 1));
                    } break;
                    case 4: {
                        player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 160, 1));
                    } break;
                    case 5: {
                        player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 140, 1));
                    } break;
                    case 6: {
                        player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 60, 1));
                    } break;
                    case 7: {
                        player.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 100, 1));
                    } break;
                    case 8: {
                        player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100, 1));
                    }
                }
            }
            else if (stack.hasTagCompound()) {
                String tag = (stack.getTagCompound().getString("effect"));

                switch(tag) {
                    case "REGEN": {
                        player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 140, 1));
                    } break;
                    case "JUMP": {
                        player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 100, 1));
                    } break;
                    case "POISON": {
                        player.addPotionEffect(new PotionEffect(MobEffects.POISON, 220, 1));
                    } break;
                    case "WITHER": {
                        player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 140, 1));
                    } break;
                    case "WEAKNESS": {
                        player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 160, 1));
                    } break;
                    case "BLINDNESS": {
                        player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 140, 1));
                    } break;
                    case "FIRE_RESISTANCE": {
                        player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 60, 1));
                    } break;
                    case "SATURATION": {
                        player.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 100, 1));
                    } break;
                    case "SPEED": {
                        player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100, 1));
                    } break;
                    case "NULL": {

                    }
                }
            }
        }
    }
}