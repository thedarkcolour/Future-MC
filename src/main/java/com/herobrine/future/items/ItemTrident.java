package com.herobrine.future.items;

import com.google.common.collect.Multimap;
import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.enchantment.EnchantHelper;
import com.herobrine.future.enchantment.EnchantImpaling;
import com.herobrine.future.entity.trident.EntityTrident;
import com.herobrine.future.init.Init;
import com.herobrine.future.sound.Sounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTrident extends Item {
    public ItemTrident() {
        setRegistryName("Trident");
        setUnlocalizedName(Init.MODID + ".Trident");
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.COMBAT : Init.FUTURE_MC_TAB);
        setMaxDamage(250);
        setMaxStackSize(1);
    }

    @SideOnly(Side.CLIENT)
    public void model() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
        return false;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemstack = playerIn.getHeldItem(hand);

        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, hand, true);
        if(ret != null) {
            return ret;
        }

        if(itemstack.getItemDamage() >= itemstack.getMaxDamage()) {
            return new ActionResult<>(EnumActionResult.FAIL, itemstack);
        }
        else {
            playerIn.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
        }
    }



    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer)entityLiving;
            int i = this.getUseDuration(stack, timeLeft);
            if (i >= 10) {
                if (!worldIn.isRemote) {
                    stack.damageItem(1, entityPlayer);
                    EntityTrident entitytrident = new EntityTrident(worldIn, entityPlayer, stack);
                    entitytrident.setDamage(EnchantImpaling.getDamageForTrident(EnchantHelper.getImpaling(entitytrident.getThrownStack())));

                    entitytrident.shoot(entityPlayer, entityPlayer.rotationPitch, entityPlayer.rotationYaw, 0.0F, 2.5F + 0F * 0.5F, 1.0F);
                    if (entityPlayer.capabilities.isCreativeMode) {
                        entitytrident.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                    }

                    worldIn.spawnEntity(entitytrident);
                    if (!entityPlayer.capabilities.isCreativeMode) {
                        entityPlayer.inventory.deleteStack(stack);
                    }
                }

                SoundEvent soundevent = Sounds.TRIDENT_THROW; // TO-DO RIPTIDE
                //float f = entityPlayer.rotationYaw;
                //float f1 = entityPlayer.rotationPitch;
                //float f2 = -MathHelper.sin(f * ((float) Math.PI / 180F)) * MathHelper.cos(f1 * ((float) Math.PI / 180F));
                //float f3 = -MathHelper.sin(f1 * ((float) Math.PI / 180F));
                //float f4 = MathHelper.cos(f * ((float) Math.PI / 180F)) * MathHelper.cos(f1 * ((float) Math.PI / 180F));
                //float f5 = MathHelper.sqrt(f2 * f2 + f3 * f3 + f4 * f4);
                //float f6 = 3.0F * ((1.0F + (float) 0) / 4.0F);
                //f2 = f2 * (f6 / f5);
                //f3 = f3 * (f6 / f5);
                //f4 = f4 * (f6 / f5);
                //entityPlayer.addVelocity((double)f2, (double)f3, (double)f4);

                //if (entityPlayer.onGround) {
                //entityPlayer.move(MoverType.SELF, 0.0D, (double)1.1999999F, 0.0D);
                //}

                worldIn.playSound(null, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, soundevent, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }

    private int getUseDuration(ItemStack stack, int timeLeft) {
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

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }
}