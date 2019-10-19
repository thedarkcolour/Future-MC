package thedarkcolour.futuremc.container;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import thedarkcolour.futuremc.init.Init;
import thedarkcolour.futuremc.sound.Sounds;
import thedarkcolour.futuremc.world.IWorldPosCallable;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.stream.Collectors;

public class ContainerGrindstoneNew extends Container {
    private final ItemStackHandler handler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            if(slot < 2) {
                handleCrafting();
            }
        }
    };
    private final IWorldPosCallable worldPosCallable;
    private final InventoryPlayer player;

    public ContainerGrindstoneNew(InventoryPlayer playerInv, IWorldPosCallable worldPosCallable) {
        this.worldPosCallable = worldPosCallable;
        this.player = playerInv;

        addOwnSlots();
        addPlayerSlots(playerInv);
    }

    // Should probably have an abstract class with this method.
    private void addOwnSlots() {
        addSlotToContainer(new SlotItemHandler(handler, 0, 49, 19) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return stack.isItemStackDamageable() || stack.getItem() == Items.ENCHANTED_BOOK || stack.isItemEnchanted();
            }
        });
        addSlotToContainer(new SlotItemHandler(handler, 1, 49, 40) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return stack.isItemStackDamageable() || stack.getItem() == Items.ENCHANTED_BOOK || stack.isItemEnchanted();
            }
        });
        addSlotToContainer(new SlotItemHandler(handler, 2, 129, 34) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return false;
            }

            @Override
            public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
                worldPosCallable.consume((worldIn, pos) -> {
                    if(!worldIn.isRemote) {
                        int i = this.rewardEXP(worldIn);

                        while(i > 0) {
                            int il = EntityXPOrb.getXPSplit(i);
                            i -= il;
                            worldIn.spawnEntity(new EntityXPOrb(worldIn, pos.getX(), pos.getY() + 0.5D, pos.getZ(), il));
                        }

                        worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), Sounds.GRINDSTONE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                    }
                });
                ContainerGrindstoneNew.this.handler.setStackInSlot(0, ItemStack.EMPTY);
                ContainerGrindstoneNew.this.handler.setStackInSlot(1, ItemStack.EMPTY);

                return stack;
            }

            private int rewardEXP(World worldIn) {
                int xp = 0;
                xp = xp + this.getEXPForItem(ContainerGrindstoneNew.this.handler.getStackInSlot(0));
                xp = xp + this.getEXPForItem(ContainerGrindstoneNew.this.handler.getStackInSlot(1));
                if (xp > 0) {
                    int exp = (int)Math.ceil((double)xp / 2.0D);
                    return exp + worldIn.rand.nextInt(exp);
                } else {
                    return 0;
                }
            }

            private int getEXPForItem(ItemStack p_216943_1_) {
                int l = 0;
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(p_216943_1_);

                for(Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                    Enchantment enchantment = entry.getKey();
                    Integer integer = entry.getValue();
                    if (!enchantment.isCurse()) {
                        l += enchantment.getMinEnchantability(integer);
                    }
                }

                return l;
            }
        });
    }

    public void handleCrafting() {
        //detectAndSendChanges();

        ItemStack stack = handler.getStackInSlot(0);
        ItemStack stack1 = handler.getStackInSlot(1);

        boolean flag = !stack.isEmpty() || !stack1.isEmpty();
        boolean flag1 = !stack.isEmpty() && !stack1.isEmpty();

        if(!flag) {
            handler.setStackInSlot(2, ItemStack.EMPTY);
        } else {
            boolean flag2 = !stack.isEmpty() && stack.getItem() != Items.ENCHANTED_BOOK && stack.isItemEnchanted() || !stack1.isEmpty() && stack1.getItem() != Items.ENCHANTED_BOOK && !stack1.isItemEnchanted();
            if(stack.getCount() > 1 || stack1.getCount() > 1 || !flag1 && flag2) {
                handler.setStackInSlot(2, ItemStack.EMPTY);
                detectAndSendChanges();
                return;
            }

            int count = 1;
            int sum;
            ItemStack outItem;
            if(flag1) {
                if(stack.getItem() != stack1.getItem()) {
                    handler.setStackInSlot(2, ItemStack.EMPTY);
                    detectAndSendChanges();
                    return;
                }

                outItem = combineEnchants(stack, stack1);
                sum = (stack.getMaxDamage() - stack.getItemDamage()) + (stack.getMaxDamage() - stack1.getItemDamage());

                sum += Math.floor(sum * 0.2);
                if(sum > stack.getMaxDamage()) {
                    sum = stack.getMaxDamage();
                }

                if(!outItem.isItemStackDamageable() || !outItem.getItem().isRepairable()) {
                    if(!ItemStack.areItemStacksEqual(stack, stack1)) {
                        handler.setStackInSlot(2, ItemStack.EMPTY);
                        detectAndSendChanges();
                        return;
                    }

                    count = 2;
                }
                sum = stack.getMaxDamage() - sum;
            } else {
                boolean flag3 = !stack.isEmpty();
                sum = flag3 ? stack.getItemDamage() : stack1.getItemDamage();
                outItem = flag3 ? stack : stack1;
            }

            this.handler.setStackInSlot(2, makeOutput(outItem, sum , count));
        }
    }

    private ItemStack combineEnchants(ItemStack stack, ItemStack stack1) {
        ItemStack stack2 = stack.copy();
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack1);

        for(Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
            Enchantment e = entry.getKey();
            if (!e.isCurse() || EnchantmentHelper.getEnchantmentLevel(e, stack2) == 0) {
                stack2.addEnchantment(e, entry.getValue());
            }
        }

        return stack2;
    }

    private ItemStack makeOutput(ItemStack stack, int damage, int count) {
        ItemStack itemstack = stack.copy();
        itemstack.removeSubCompound("ench");
        itemstack.removeSubCompound("StoredEnchantments");

        if (damage > 0) {
            itemstack.setItemDamage(damage);
        }

        itemstack.setCount(count);
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack).entrySet().stream()
                .filter((entry) -> entry.getKey().isCurse())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        EnchantmentHelper.setEnchantments(map, itemstack);
        itemstack.setRepairCost(0);
        if (itemstack.getItem() == Items.ENCHANTED_BOOK && map.size() == 0) {
            itemstack = new ItemStack(Items.BOOK);
            if (stack.hasDisplayName()) {
                itemstack.setStackDisplayName(stack.getDisplayName());
            }
        }

        for(int i = 0; i < map.size(); ++i) {
            itemstack.setRepairCost(itemstack.getRepairCost() * 2 + 1);
        }

        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        worldPosCallable.consume((worldIn, pos) -> {
            if(!playerIn.isEntityAlive() || playerIn instanceof EntityPlayerMP && ((EntityPlayerMP)playerIn).hasDisconnected()) {
                for(int i = 0; i < 2; ++i) {
                    playerIn.dropItem(removeFromHandler(i), false);
                }
            } else {
                for(int i = 0; i < 2; ++i) {
                    playerIn.inventory.placeItemBackInInventory(worldIn, removeFromHandler(i));
                }
            }
        });
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return isUsableWithinDistance(worldPosCallable, playerIn, Init.GRINDSTONE);
    }

    private void addPlayerSlots(InventoryPlayer playerInv) {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }

    // Move to abstract class and use an abstract getHandler() method
    private ItemStack removeFromHandler(int slot) {
        ItemStack stack = handler.getStackInSlot(slot);
        if(stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            handler.setStackInSlot(slot, ItemStack.EMPTY);
            return stack;
        }
    }

    private boolean isUsableWithinDistance(IWorldPosCallable posCallable, EntityPlayer playerIn, Block block) {
        return posCallable.applyOrElse((world, pos) -> world.getBlockState(pos).getBlock() == block && playerIn.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D, true);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            ItemStack itemstack2 = this.handler.getStackInSlot(0);
            ItemStack itemstack3 = this.handler.getStackInSlot(1);
            if (index == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 0 && index != 1) {
                if (!itemstack2.isEmpty() && !itemstack3.isEmpty()) {
                    if (index >= 3 && index < 30) {
                        if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.mergeItemStack(itemstack1, 0, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    public InventoryPlayer getPlayerInv() {
        return player;
    }

    @SideOnly(Side.CLIENT)
    public boolean isRecipeInvalid() {
        return (!handler.getStackInSlot(0).isEmpty() || !handler.getStackInSlot(1).isEmpty()) && handler.getStackInSlot(2).isEmpty();
    }
}