package thedarkcolour.futuremc.container

/*
class GrindstoneContainer(private val playerInv: InventoryPlayer, private val worldIn: World, private val pos: BlockPos) : FContainer() {
    private val inventory = object : DarkInventory(3) {
        override fun onContentsChanged(slot: Int) {
            if (slot != 2) {
                updateResult()
                detectAndSendChanges()
            }
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return slot != 2
        }
    }

    private fun updateResult() {

    }

    override fun getGuiContainer(): GuiContainer {
        TO DO("not implemented")
    }

    override fun canInteractWith(playerIn: EntityPlayer): Boolean {
        TO DO("not implemented")
    }

}*/