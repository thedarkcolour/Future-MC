package thedarkcolour.futuremc.block.buzzybees

import net.minecraft.block.BlockDispenser
import net.minecraft.dispenser.BehaviorDefaultDispenseItem
import net.minecraft.dispenser.IBehaviorDispenseItem
import net.minecraft.dispenser.IBlockSource
import net.minecraft.init.Bootstrap.BehaviorDispenseOptional
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntityDispenser
import net.minecraft.world.World
import thedarkcolour.futuremc.registry.FItems
import thedarkcolour.futuremc.tile.BeeHiveTile

object BottleDispenserBehavior : BehaviorDispenseOptional()  {
    private val default = BehaviorDefaultDispenseItem()

    override fun dispenseStack(source: IBlockSource, stack: ItemStack): ItemStack {
        val state = source.blockState
        successful = false
        val world = source.world
        val pos = source.blockPos.offset(state.getValue(BlockDispenser.FACING))
        val te = world.getTileEntity(pos)
        TODO()
    }

    private fun fillBottle(source: IBlockSource, bottle: ItemStack, filled: ItemStack) {
        bottle.shrink(1)
        if (bottle.isEmpty) {
            //source.world.
        }
    }

    fun dispense(world: World, source: IBlockSource, stack: ItemStack, existing: IBehaviorDispenseItem?): ItemStack {
        val state = source.blockState

        // remove, world should always be remote
        if (!world.isRemote) {
            val pos = source.blockPos.offset(state.getValue(BlockDispenser.FACING))
            val te = world.getTileEntity(pos)

            if (te is BeeHiveTile && te.honeyLevel >= 5) {
                te.emptyHoney(world, pos, null)
                stack.shrink(1)

                if (stack.isEmpty) {
                    return ItemStack(FItems.HONEY_BOTTLE)
                } else {
                    // if glass bottle stack is not empty and inventory has no more room
                    if (source.getBlockTileEntity<TileEntityDispenser>().addItemStack(ItemStack(FItems.HONEY_BOTTLE)) < 0) {
                        // spit onto the ground
                        existing?.dispense(source, stack)
                    }

                    return stack//ItemStack(FItems.HONEY_BOTTLE)
                }

            }
        }

        // Other dispenser stuff
        playDispenseSound(source)
        spawnDispenseParticles(source, state.getValue(BlockDispenser.FACING))

        // Return the item to keep in the slot
        return stack
    }
}