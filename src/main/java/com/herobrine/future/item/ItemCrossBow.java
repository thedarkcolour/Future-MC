package com.herobrine.future.item;

import com.google.common.collect.Lists;
import com.herobrine.future.FutureMC;
import com.herobrine.future.client.Modeled;
import com.herobrine.future.enchantment.EnchantHelper;
import com.herobrine.future.entity.horizontal_firework.EntityHorizontalFireworksRocket;
import com.herobrine.future.init.FutureConfig;
import com.herobrine.future.init.Init;
import com.herobrine.future.sound.Sounds;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ItemCrossBow extends Item implements Modeled {
    public ItemCrossBow(String registryName) {
        setUnlocalizedName(FutureMC.MODID + "." + registryName);
        setRegistryName(registryName);
        setMaxStackSize(1);
        setMaxDamage(326);
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.COMBAT : FutureMC.CREATIVE_TAB);
        this.addPropertyOverride(new ResourceLocation("pull"), ((stack, worldIn, entityIn) -> entityIn == null || entityIn.getActiveItemStack().getItem() != Init.CROSSBOW || isLoaded(stack) ? 0.0F : (float)(stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F));
        this.addPropertyOverride(new ResourceLocation("pulling"), (stack, worldIn, entityIn) -> !isLoaded(stack) && entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F);
        this.addPropertyOverride(new ResourceLocation("loaded"), (stack, worldIn, entityIn) -> isLoaded(stack) ? 1.0F : 0.0F);
        addModel();
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return isLoaded(stack) ? I18n.format("item.minecraftfuture.Crossbow_Loaded.name") : I18n.format("item.minecraftfuture.Crossbow.name");
    }

    public void setIsLoaded(ItemStack stack, boolean isLoading) {
        NBTTagCompound tag = stack.getTagCompound();
        tag.setBoolean("isLoaded", isLoading);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entity, int timeLeft) {
        int i = getUseDuration(stack, timeLeft);
        float f = func_220031_a(i, stack);
        if(f >= 1.0F && !isLoaded(stack) && func_220021_a(entity, stack)) {
            setIsLoaded(stack, true);
            SoundCategory category = entity instanceof EntityPlayer ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            worldIn.playSound(null, entity.posX, entity.posY, entity.posZ, Sounds.CROSSBOW_LOAD, category, 1.0F, 1.0F / (itemRand.nextFloat() * 0.5F + 1.0F) + 0.2F);
        }
    }

    private static boolean func_220021_a(EntityLivingBase entity, ItemStack stack) {
        boolean flag = EnchantHelper.hasMultishot(stack);
        int j = flag ? 1 : 3;
        boolean flag1 = entity instanceof EntityPlayer && ((EntityPlayer)entity).isCreative();
        ItemStack ammo = findAmmo(entity);
        ItemStack itemstack1 = ammo.copy();

        for(int k = 0; k < j; ++k) {
            if (k > 0) {
                ammo = itemstack1.copy();
            }

            if (ammo.isEmpty() && flag1) {
                ammo = new ItemStack(Items.ARROW);
                itemstack1 = ammo.copy();
            }

            if (!loadCrossbow(entity, stack, ammo, k > 0, flag1)) {
                return false;
            }
        }

        return true;
    }

    private static boolean loadCrossbow(EntityLivingBase entity, ItemStack crossbow, ItemStack ammo, boolean isCopy, boolean isCreative) {
        if (ammo.isEmpty()) {
            return false;
        } else {
            boolean flag = isCreative && ammo.getItem() instanceof ItemArrow;
            ItemStack itemstack;
            if (!flag && !isCreative && !isCopy) {
                itemstack = ammo.splitStack(1);
                if (ammo.isEmpty() && entity instanceof EntityPlayer) {
                    ((EntityPlayer)entity).inventory.deleteStack(ammo);
                }
            } else {
                itemstack = ammo.copy();
            }

            addStacksToCrossbow(crossbow, itemstack);
            return true;
        }
    }

    private static void addStacksToCrossbow(ItemStack crossbow, ItemStack stack) {
        NBTTagCompound tag = getOrCreateTag(crossbow);
        NBTTagList nbtList;
        if (tag.hasKey("ChargedProjectiles", 9)) {
            nbtList = tag.getTagList("ChargedProjectiles", 10);
        } else {
            nbtList = new NBTTagList();
        }

        NBTTagCompound tag1 = new NBTTagCompound();
        stack.setTagCompound(tag1);
        nbtList.appendTag(tag1);
        tag.setTag("ChargedProjectiles", nbtList);
    }

    private static NBTTagCompound getOrCreateTag(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }

        return stack.getTagCompound();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack crossbow = playerIn.getHeldItem(hand);
        if (isLoaded(crossbow)) {
            func_220014_a(worldIn, playerIn, hand, crossbow, func_220013_l(crossbow), 1.0F);
            setIsLoaded(crossbow, false);
            return new ActionResult<>(EnumActionResult.SUCCESS, crossbow);
        } else if (!findAmmo(playerIn).isEmpty()) {
            if (!isLoaded(crossbow)) {
                //this.field_220034_c = false;
                //this.field_220035_d = false;
                playerIn.setActiveHand(hand);
            }

            return new ActionResult<>(EnumActionResult.SUCCESS, crossbow);
        } else {
            return new ActionResult<>(EnumActionResult.FAIL, crossbow);
        }
    }

    public static void func_220014_a(World worldIn, EntityLivingBase entity, EnumHand hand, ItemStack crossbow, float p_220014_4_, float p_220014_5_) {
        List<ItemStack> list = getLoadedAmmo(crossbow);
        float[] rng = getDirectionsForArrows(entity.getRNG());

        for(int i = 0; i < list.size(); ++i) {
            ItemStack ammo = list.get(i);
            boolean isCreative = entity instanceof EntityPlayer && ((EntityPlayer)entity).isCreative();
            if (!ammo.isEmpty()) {
                if (i == 0) {
                    func_220016_a(worldIn, entity, hand, crossbow, ammo, rng[i], isCreative, p_220014_4_, p_220014_5_, 0.0F);
                } else if (i == 1) {
                    func_220016_a(worldIn, entity, hand, crossbow, ammo, rng[i], isCreative, p_220014_4_, p_220014_5_, -10.0F);
                } else if (i == 2) {
                    func_220016_a(worldIn, entity, hand, crossbow, ammo, rng[i], isCreative, p_220014_4_, p_220014_5_, 10.0F);
                }
            }
        }

        func_220027_k(crossbow);
    }

    private static void func_220027_k(ItemStack crossbow) {
        NBTTagCompound tag = crossbow.getTagCompound();
        if (tag != null) {
            NBTTagList tagList = tag.getTagList("ChargedProjectiles", 9);
            for(int i = 0; i < tagList.tagCount(); i++) tagList.removeTag(i);
            tag.setTag("ChargedProjectiles", tagList);
        }

    }

    private static void func_220016_a(World worldIn, EntityLivingBase entity, EnumHand hand, ItemStack crossbow, ItemStack ammo, float p_220016_5_, boolean isCreative, float p_220016_7_, float p_220016_8_, float offset) {
        if (!worldIn.isRemote) {
            boolean flag = ammo.getItem() == Items.FIREWORKS;
            IProjectile iprojectile;
            if (flag) {
                iprojectile = new EntityHorizontalFireworksRocket(worldIn, ammo, entity.posX, entity.posY + (double)entity.getEyeHeight() - (double)0.15F, entity.posZ);
            } else {
                iprojectile = createArrow(worldIn, entity, crossbow, ammo);
                if (isCreative || offset != 0.0F) {
                    ((EntityArrow)iprojectile).pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                }
            }

            //Vec3d vec3d1 = entity.func_213286_i(1.0F);
            //Quaternion quaternion = new Quaternion(new Vector3f(vec3d1), offset, true);
            //Vec3d vec3d = entity.getLook(1.0F);
            //Vector3f vector3f = new Vector3f(vec3d);
            //vector3f.func_214905_a(quaternion);
            //iprojectile.shoot((double)vector3f.getX(), (double)vector3f.getY(), (double)vector3f.getZ(), p_220016_7_, p_220016_8_);
//
            //crossbow.func_222118_a(flag ? 3 : 1, entity, (p_220017_1_) -> p_220017_1_.func_213334_d(hand));
            //worldIn.func_217376_c((Entity)iprojectile);
            //worldIn.playSound(null, entity.posX, entity.posY, entity.posZ, Sounds.CROSSBOW_FIRE, SoundCategory.PLAYERS, 1.0F, p_220016_5_);
        }
    }

    private static EntityArrow createArrow(World worldIn, EntityLivingBase shooter, ItemStack crossbow, ItemStack ammo) {
        ItemArrow arrowitem = (ItemArrow)(ammo.getItem() instanceof ItemArrow ? ammo.getItem() : Items.ARROW);
        EntityArrow arrow = arrowitem.createArrow(worldIn, crossbow, shooter);
        if (shooter instanceof EntityPlayer) {
            arrow.setIsCritical(true);
        }
        arrow.setIsCritical(true);

        return arrow;
    }

    private static float[] getDirectionsForArrows(Random random) {
        boolean flag = random.nextBoolean();
        return new float[]{1.0F, getDirection(flag), getDirection(!flag)};
    }

    private static float getDirection(boolean isLeft) {
        float f = isLeft ? 0.63F : 0.43F;
        return 1.0F / (itemRand.nextFloat() * 0.5F + 1.8F) + f;
    }

    private static List<ItemStack> getLoadedAmmo(ItemStack crossbow) {
        List<ItemStack> list = Lists.newArrayList();
        NBTTagCompound compoundnbt = crossbow.getTagCompound();
        if (compoundnbt != null && compoundnbt.hasKey("ChargedProjectiles", 9)) {
            NBTTagList listnbt = compoundnbt.getTagList("ChargedProjectiles", 10);
            if (listnbt != null) {
                for(int i = 0; i < listnbt.tagCount(); ++i) {
                    NBTTagCompound compoundnbt1 = listnbt.getCompoundTagAt(i);
                    list.add(read(compoundnbt1));
                }
            }
        }

        return list;
    }

    public static ItemStack read(NBTTagCompound compound) {
        try {
            return new ItemStack(compound);
        } catch (RuntimeException runtimeexception) {
            FutureMC.LOGGER.debug("Tried to load invalid item: {}", compound, runtimeexception);
            return ItemStack.EMPTY;
        }
    }

    public static ItemStack findAmmo(EntityLivingBase entity) { // From ItemBow
        if(!(entity instanceof EntityPlayer || entity instanceof EntityMob)) return ItemStack.EMPTY;

        if(entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (isAmmo(itemstack)) {
                    return itemstack;
                }
            }
        }
        else if(entity instanceof EntityMob) {
            return new ItemStack(Items.ARROW);
        }
        return ItemStack.EMPTY;
    }

    public static boolean isAmmo(ItemStack stack) { // From Arrow
        return stack.getItem() instanceof ItemArrow;
    }

    private static float func_220031_a(int p_220031_0_, ItemStack stack) {
        float f = (float)p_220031_0_ / (float)EnchantHelper.getQuickCharge(stack);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    private int getUseDuration(ItemStack stack, int timeLeft) {
        return stack.getMaxItemUseDuration() - timeLeft;
    }

    private static boolean isLoaded(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.getBoolean("isLoaded");
    }

    private static boolean func_220019_a(ItemStack p_220019_0_) {
        return getLoadedAmmo(p_220019_0_).stream().anyMatch((p_220010_1_) -> p_220010_1_.getItem() == Items.FIREWORKS);
    }

    private static float func_220013_l(ItemStack p_220013_0_) {
        return p_220013_0_.getItem() == Init.CROSSBOW && func_220019_a(p_220013_0_) ? 1.6F : 3.15F;
    }
}