package com.herobrine.future.blocks.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotStonecutterOutput extends SlotItemHandler {
    private int removeCount;

    public SlotStonecutterOutput(IItemHandler inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean func_75214_a(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack func_75209_a(int amount) {
        if (this.func_75216_d()) {
            this.removeCount += Math.min(amount, this.func_75211_c().func_190916_E());
        } return super.func_75209_a(amount);
    }

    @Override
    public ItemStack func_190901_a(EntityPlayer playerIn, ItemStack stack) {
        this.func_75208_c(stack);
        super.func_190901_a(playerIn, stack);
        return stack;
    }
}
