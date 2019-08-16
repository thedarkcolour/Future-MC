package com.herobrine.future.item;

import com.google.common.collect.Multimap;
import com.herobrine.future.FutureMC;
import com.herobrine.future.client.Modeled;
import com.herobrine.future.enchantment.EnchantHelper;
import com.herobrine.future.entity.trident.EntityTrident;
import com.herobrine.future.init.FutureConfig;
import com.herobrine.future.sound.Sounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemTrident extends Item implements Modeled {
    public ItemTrident() {
        setRegistryName("Trident");
        setUnlocalizedName(FutureMC.ID + ".Trident");
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.COMBAT : FutureMC.CREATIVE_TAB);
        setMaxDamage(250);
        setMaxStackSize(1);
        addModel();
    }

    @Override
    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
        return false;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer)entityLiving;
            int i = getUseDuration(stack, timeLeft);
            if (i >= 10) {
                int j = EnchantHelper.getRiptide(stack);
                if(j <= 0 || entityPlayer.isWet()) {
                    if (!worldIn.isRemote) {
                        stack.damageItem(1, entityPlayer);
                        if(j == 0) {
                            EntityTrident trident = new EntityTrident(worldIn, entityPlayer, stack);
                            trident.shoot(entityLiving, entityPlayer.rotationPitch, entityPlayer.rotationYaw, 0.0F, 2.5F + (float)j * 0.5F, 1.0F);

                            if (entityPlayer.capabilities.isCreativeMode) {
                                trident.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                            }

                            worldIn.spawnEntity(trident);
                            if (!entityPlayer.capabilities.isCreativeMode) {
                                entityPlayer.inventory.deleteStack(stack);
                            }
                        }
                    }

                    SoundEvent soundevent = Sounds.TRIDENT_THROW;
                    if (j > 0) {
                        float f = entityPlayer.rotationYaw;
                        float f1 = entityPlayer.rotationPitch;
                        float f2 = -MathHelper.sin(f * ((float)Math.PI / 180F)) * MathHelper.cos(f1 * ((float)Math.PI / 180F));
                        float f3 = -MathHelper.sin(f1 * ((float)Math.PI / 180F));
                        float f4 = MathHelper.cos(f * ((float)Math.PI / 180F)) * MathHelper.cos(f1 * ((float)Math.PI / 180F));
                        float f5 = MathHelper.sqrt(f2 * f2 + f3 * f3 + f4 * f4);
                        float f6 = 3.0F * ((1.0F + (float)j) / 4.0F);
                        f2 = f2 * (f6 / f5);
                        f3 = f3 * (f6 / f5);
                        f4 = f4 * (f6 / f5);
                        entityPlayer.addVelocity(f2, f3, f4);
                        if (j >= 3) {
                            soundevent = Sounds.TRIDENT_RIPTIDE_2;
                        } else if (j == 2) {
                            soundevent = Sounds.TRIDENT_RIPTIDE_1;
                        } else {
                            soundevent = Sounds.TRIDENT_RIPTIDE_0;
                        }

                        if (entityPlayer.onGround) {
                            entityPlayer.move(MoverType.SELF, 0.0D, (double)1.1999999F, 0.0D);
                        }
                    }

                    worldIn.playSound(null, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, soundevent, SoundCategory.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        if (itemstack.getItemDamage() >= itemstack.getMaxDamage()) {
            return new ActionResult<>(EnumActionResult.FAIL, itemstack);
        } else if (EnchantHelper.getRiptide(itemstack) > 0 && !playerIn.isWet()) {
            return new ActionResult<>(EnumActionResult.FAIL, itemstack);
        } else {
            playerIn.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
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
        if (state.getBlockHardness(worldIn, pos) != 0.0D) {
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
        return 1;
    }

    @Override
    public int getItemStackLimit() {
        return 1;
    }
}