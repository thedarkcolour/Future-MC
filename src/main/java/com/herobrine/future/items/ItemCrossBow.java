package com.herobrine.future.items;

import com.herobrine.future.MainFuture;
import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.init.Init;
import com.herobrine.future.sound.Sounds;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.Objects;

public class ItemCrossBow extends Item {
    public ItemCrossBow(String registryName) {
        setUnlocalizedName(Init.MODID + "." + registryName);
        setRegistryName(registryName);
        setMaxStackSize(1);
        setMaxDamage(326);
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.COMBAT : Init.FUTURE_MC_TAB);
        this.addPropertyOverride(new ResourceLocation("pull"), ((stack, worldIn, entityIn) -> entityIn == null || entityIn.getActiveItemStack().getItem() != Init.CROSSBOW || isLoaded(stack) ? 0.0F : (float)(stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F));
        this.addPropertyOverride(new ResourceLocation("pulling"), (stack, worldIn, entityIn) -> !isLoaded(stack) && entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F);
        this.addPropertyOverride(new ResourceLocation("loaded"), (stack, worldIn, entityIn) -> isLoaded(stack) ? 1.0F : 0.0F);
    }

    @SideOnly(Side.CLIENT)
    public void model() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(Objects.requireNonNull(getRegistryName()), "inventory"));
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return isLoaded(stack) ? I18n.format("item.minecraftfuture.Crossbow_Loaded.name") : I18n.format("item.minecraftfuture.Crossbow.name");
    }

    public void createNBT(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        setIsLoaded(stack, false);
        setIsLoading(stack, false);
        setLoadedItem(stack, ItemStack.EMPTY);
    }

    public void setIsLoaded(ItemStack stack, boolean bool) {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setBoolean("isLoaded", bool);

        stack.setTagCompound(tag);
    }

    public void setIsLoading(ItemStack stack, boolean bool) {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setBoolean("isLoading", bool);

        stack.setTagCompound(tag);
    }

    public void setLoadedItem(ItemStack stack, ItemStack... stacks) {
        if(stacks.length != 3 && stacks.length != 1) {
            System.out.println("Failed to set item compound: Too " + (stacks.length > 3 ? "many" : "few") + " items");
            return;
        }

        NonNullList<ItemStack> loadItems = NonNullList.create();
        loadItems.addAll(Arrays.asList(stacks));

        if(stack.getTagCompound() != null) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("LoadedItems", ItemStackHelper.saveAllItems(tag, loadItems));

            stack.setTagCompound(tag);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack stack = playerIn.getHeldItem(hand);

        createNBT(stack);

        //if(!)

        if (!worldIn.isRemote && isLoaded(stack)) {
            ItemStack arrowStack = findAmmo(playerIn).isEmpty() ? new ItemStack(Items.ARROW) : findAmmo(playerIn);

            EntityArrow entityArrow = ((ItemArrow)Items.ARROW).createArrow(worldIn, arrowStack, playerIn);
            entityArrow.pickupStatus = playerIn.isCreative() ? EntityArrow.PickupStatus.CREATIVE_ONLY : EntityArrow.PickupStatus.ALLOWED;

            entityArrow.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 2.5F + 0F * 0.5F, 0.5F);
            entityArrow.setIsCritical(true);
            setIsLoaded(stack, false);
            stack.damageItem(1, playerIn);
            entityArrow.playSound(Sounds.CROSSBOW_FIRE, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 0.7F * 0.5F);
            boolean flag1 = playerIn.capabilities.isCreativeMode || (arrowStack.getItem() instanceof ItemArrow && isInfinite(arrowStack, stack));
            if (!flag1 && !playerIn.capabilities.isCreativeMode) {
                arrowStack.shrink(1);
                if (arrowStack.isEmpty()) {
                    playerIn.inventory.deleteStack(arrowStack);
                }
            }
            System.out.println("Fired arrow?");
            return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
        }

        //if (!playerIn.isCreative() && this.findAmmo(playerIn).isEmpty()) {
         //   return new ActionResult<>(EnumActionResult.FAIL, stack);
        //}
        //playerIn.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    public ItemStack findAmmo(EntityPlayer player) { // From ItemBow
        if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND))) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND))) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        else if(!player.isCreative()){
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isArrow(itemstack)) {
                    return itemstack;
                }
            }
            return ItemStack.EMPTY;
        }
        else {
            return new ItemStack(Items.ARROW);
        }
    }

    public boolean isArrow(ItemStack stack) { // From Arrow
        return stack.getItem() instanceof ItemArrow;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer playerIn = (EntityPlayer) entityLiving;
            float rand = 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 0.7F * 0.5F;
            int i = this.getUseDuration(stack, timeLeft);
            if (i >= 20 && !isLoaded(stack)) {
                if(!this.findAmmo(playerIn).isEmpty() || playerIn.isCreative()) {
                    ItemStack item = findAmmo(playerIn).isEmpty() ? new ItemStack(Items.ARROW) : findAmmo(playerIn);
                    setItemCompound(stack.getTagCompound(), item);
                    setIsLoaded(stack, true);
                }
                worldIn.playSound((EntityPlayer) entityLiving, entityLiving.getPosition(), Sounds.CROSSBOW_CHARGE, SoundCategory.PLAYERS, 1.0F, rand);
                worldIn.playSound((EntityPlayer) entityLiving, entityLiving.getPosition(), Sounds.CROSSBOW_LOAD, SoundCategory.PLAYERS, 1.0F, rand);
            }
        }
    }

    private int getUseDuration(ItemStack stack, int timeLeft) {
        return stack.getMaxItemUseDuration() - timeLeft;
    }

    /**
     * Null-safe check to check if the stack (crossbow) is loaded
     */
    private static boolean isLoaded(ItemStack stack) {
        if(stack.getTagCompound() == null) {
            return false;
        }
        return stack.getTagCompound().getBoolean("isLoaded");
    }

    /**
     * Null-safe check to check if the player is loading the weapon
     */
    private static boolean isLoading(ItemStack stack) {
        if(stack.getTagCompound() == null) {
            return false;
        }
        return stack.getTagCompound().getBoolean("isLoading");
    }

    /**
     * Static version of {@link ItemArrow#isInfinite(ItemStack, ItemStack, EntityPlayer)}
     */
    public static boolean isInfinite(ItemStack stack, ItemStack bow) {
        int enchant = net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.INFINITY, bow);
        return enchant > 0 && stack.getItem() instanceof ItemArrow;
    }

    /**
     * Saves the item to the crossbow so I can get the stack for shooting
     */
    public static void setItemCompound(NBTTagCompound compound, ItemStack... stacks) {
        if(compound == null) {
            return;
        }
        if(stacks.length != 3 && stacks.length != 1) {
            MainFuture.logger.log(Level.ERROR, "Error: Invalid item compound for bow.");
            return;
        }
        NonNullList<ItemStack> list = NonNullList.create();
        list.addAll(Arrays.asList(stacks));
        ItemStackHelper.saveAllItems(compound, list, false);
    }
}