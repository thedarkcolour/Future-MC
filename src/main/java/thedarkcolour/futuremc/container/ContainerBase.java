package thedarkcolour.futuremc.container;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import thedarkcolour.futuremc.world.IWorldPosCallable;

public abstract class ContainerBase extends AbstractContainerBase {
    protected final IWorldPosCallable posCallable;
    private final Block acceptableBlockIn;

    public ContainerBase(InventoryPlayer playerInv, IWorldPosCallable posCallable, Block acceptableBlockIn) {
        super(playerInv);
        this.posCallable = posCallable;
        this.acceptableBlockIn = acceptableBlockIn;
        addOwnSlots();
        addPlayerSlots();
    }

    protected abstract void addOwnSlots();

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return posCallable.applyOrElse((world, pos) -> world.getBlockState(pos).getBlock() == acceptableBlockIn && playerIn.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D, true);
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        posCallable.consume((worldIn, pos) -> {
            if (!playerIn.isEntityAlive() || playerIn instanceof EntityPlayerMP && ((EntityPlayerMP)playerIn).hasDisconnected()) {
                for (int i = 0; i < 2; ++i) {
                    playerIn.dropItem(inventorySlots.get(i).getStack(), false);
                }
            } else {
                for (int i = 0; i < 2; ++i) {
                    playerIn.inventory.placeItemBackInInventory(worldIn, inventorySlots.get(i).getStack());
                }
            }
        });
    }
}