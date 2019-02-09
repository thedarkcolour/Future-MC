package com.herobrine.future.items;

import com.google.common.collect.Multimap;
import com.herobrine.future.entity.EntityTrident;
import com.herobrine.future.utils.Config;
import com.herobrine.future.utils.Init;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.properties.EntityPropertyManager;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.minecraft.item.Item.ToolMaterial;

public class Trident extends ItemSword {
    @SuppressWarnings("unused")
    private final float attackDamage;
    public Trident(String name, ToolMaterial material, CreativeTabs tab) {
        super(material);
        setRegistryName(name);
        func_77655_b(Init.MODID + ".Trident");
        func_77637_a(tab);
        this.attackDamage = material.func_78000_c();

        if (Config.tridentanimation) {
            this.func_185043_a(new ResourceLocation("holding"), (stack, worldIn, entityIn) ->
                    entityIn instanceof EntityPlayer && !entityIn.func_184587_cr() && entityIn.func_184614_ca() == stack ||
                            !entityIn.func_184587_cr() && entityIn != null && entityIn.func_184592_cb() == stack ? 1.0F : 0F);
            this.func_185043_a(new ResourceLocation("throwing"), (stack, worldIn, entityIn) -> entityIn != null && entityIn.func_184587_cr() && entityIn.func_184607_cu() == stack ? 1.0F : 0F);
        }
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public EnumAction func_77661_b(ItemStack stack) {
        if(Config.tridentanimation) {
            return EnumAction.BOW;
        } else return EnumAction.NONE;
    }

    @Override
    public int func_77626_a(ItemStack stack) {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> func_77659_a(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackHeld = playerIn.func_184586_b(hand);
        playerIn.func_184598_c(hand);
        if (Config.tridentanimation) {
            return new ActionResult(EnumActionResult.PASS, itemStackHeld);
        } else {
            return new ActionResult(EnumActionResult.FAIL, itemStackHeld);
        }
    }

    @Override
    public void func_77615_a(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        int useTime = func_77626_a(stack) - timeLeft;
        EntityPlayer entityplayer = (EntityPlayer)entityLiving;
        if (useTime > 20 && Config.tridentanimation) {
            if(!worldIn.field_72995_K && entityLiving instanceof EntityPlayer) {
                EntityArrow trident = new EntityTrident(worldIn, entityplayer, 1.5F);
                trident.func_184547_a(entityplayer, entityplayer.field_70125_A, entityplayer.field_70177_z, 0.0F, 1.5F, 1.0F);
                trident.func_70239_b(9.0F);
            }
        }
    }
}
