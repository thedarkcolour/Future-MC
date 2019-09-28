package thedarkcolour.futuremc.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public abstract class AbstractContainerBase extends Container {
    protected final InventoryPlayer playerInv;

    public AbstractContainerBase(InventoryPlayer playerInv) {
        this.playerInv = playerInv;
    }

    public final InventoryPlayer getPlayerInv() {
        return playerInv;
    }

    protected final void addPlayerSlots() {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }
}