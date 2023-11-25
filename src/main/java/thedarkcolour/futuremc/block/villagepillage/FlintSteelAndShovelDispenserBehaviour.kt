package thedarkcolour.futuremc.block.villagepillage

import net.minecraft.block.BlockDispenser
import net.minecraft.block.BlockLiquid
import net.minecraft.dispenser.IBehaviorDispenseItem
import net.minecraft.dispenser.IBlockSource
import net.minecraft.init.Bootstrap.BehaviorDispenseOptional
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.fluids.BlockFluidBase

object FlintSteelAndShovelDispenserBehaviour : BehaviorDispenseOptional() {

    fun dispense(world: World, source: IBlockSource, stack: ItemStack, existing: IBehaviorDispenseItem): ItemStack {
        val state = source.blockState

        if (!world.isRemote) {
            val pos = source.blockPos.offset(state.getValue(BlockDispenser.FACING))
            val campfireState = world.getBlockState(pos)
            if (campfireState.block is CampfireBlock) {
                if (!campfireState.getValue(CampfireBlock.LIT)) {
                    if (stack.item == Items.FLINT_AND_STEEL || stack.item == Items.FIRE_CHARGE) {
                        if (!((campfireState.block is BlockLiquid) || (campfireState.block is BlockFluidBase))) {
                            CampfireBlock.setLit(world, pos, true)
                            if (stack.attemptDamageItem(1,world.rand,null)) {
                                stack.count = 0
                            }
                        }
                    }
                } else if (stack.item.getToolClasses(stack).contains("shovel")) {
                    CampfireBlock.setLit(world, pos, false)
                }
            }
        }

        playDispenseSound(source)
        spawnDispenseParticles(source, state.getValue(BlockDispenser.FACING))

        // Return the item to keep in the slot
        return stack
    }
}