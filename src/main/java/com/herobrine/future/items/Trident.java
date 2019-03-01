package com.herobrine.future.items;

import com.google.common.collect.Multimap;
import com.herobrine.future.entity.EntityTrident;
import com.herobrine.future.utils.blocks.IModel;
import com.herobrine.future.utils.proxy.Init;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Trident extends Item implements IModel {
    public Trident() {
        setRegistryName("trident");
        setUnlocalizedName(Init.MODID + ".Trident");
        setCreativeTab(Init.futuretab);
        setMaxDamage(250);
        setMaxStackSize(1);

        if (/*FutureConfig.c.tridentthrow*/false) {
            this.addPropertyOverride(new ResourceLocation("throwing"), (stack, worldIn, entityIn) ->
                    entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void models() {
        model(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
        return false;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        if(/*FutureConfig.c.tridentthrow*/false) {
            return EnumAction.BOW;
        } else return EnumAction.NONE;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackHeld = playerIn.getHeldItem(hand);
        playerIn.setActiveHand(hand);
        if (/*FutureConfig.c.tridentthrow*/false) {
            return new ActionResult(EnumActionResult.PASS, itemStackHeld);
        } else {
            return new ActionResult(EnumActionResult.FAIL, itemStackHeld);
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (/*entityLiving instanceof EntityPlayer && FutureConfig.c.tridentthrow*/false) { //Not fully implemented
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            int i = this.getUseDuration(stack, timeLeft);
            if (i >= 10) {
                if (entityplayer.isWet()) {
                    if (!worldIn.isRemote) {
                        stack.damageItem(1, entityplayer);
                            EntityTrident entitytrident = new EntityTrident(worldIn, entityplayer, stack);
                            entitytrident.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, 2.5F + (float)0 * 0.5F, 1.0F);
                            if (entityplayer.capabilities.isCreativeMode) {
                                entitytrident.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                            }

                            worldIn.spawnEntity(entitytrident);
                            if (!entityplayer.capabilities.isCreativeMode) {
                                entityplayer.inventory.deleteStack(stack);
                            }
                    }

                    SoundEvent soundevent = SoundEvents.ENTITY_ARROW_SHOOT;
                        float f = entityplayer.rotationYaw;
                        float f1 = entityplayer.rotationPitch;
                        float f2 = -MathHelper.sin(f * ((float)Math.PI / 180F)) * MathHelper.cos(f1 * ((float)Math.PI / 180F));
                        float f3 = -MathHelper.sin(f1 * ((float)Math.PI / 180F));
                        float f4 = MathHelper.cos(f * ((float)Math.PI / 180F)) * MathHelper.cos(f1 * ((float)Math.PI / 180F));
                        float f5 = MathHelper.sqrt(f2 * f2 + f3 * f3 + f4 * f4);
                        float f6 = 3.0F * ((1.0F + (float)0) / 4.0F);
                        f2 = f2 * (f6 / f5);
                        f3 = f3 * (f6 / f5);
                        f4 = f4 * (f6 / f5);
                        entityplayer.addVelocity((double)f2, (double)f3, (double)f4);

                        if (entityplayer.onGround) {
                            entityplayer.move(MoverType.SELF, 0.0D, (double)1.1999999F, 0.0D);
                        }

                    worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, soundevent, SoundCategory.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }

    public int getUseDuration(ItemStack stack, int timeLeft) {
        return stack.getMaxItemUseDuration() - timeLeft;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if ((double)state.getBlockHardness(worldIn, pos) != 0.0D) {
            stack.damageItem(2, entityLiving);
        }

        return true;
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 8.0D, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)-2.9F, 0));
        }

        return multimap;
    }

    public int getItemEnchantability() {
        return 22;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemStackLimit() {
        return 1;
    }
}