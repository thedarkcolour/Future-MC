package com.herobrine.future.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

public abstract class ContainerTEBase<T extends TileEntity> extends AbstractContainerBase {
    protected final T tile;

    public ContainerTEBase(InventoryPlayer playerInv, T tile) {
        super(playerInv);
        this.tile = tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
    }
}