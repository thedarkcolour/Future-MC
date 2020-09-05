package thedarkcolour.futuremc.block.buzzybees

import net.minecraft.block.BlockDispenser
import net.minecraft.dispenser.IBlockSource
import net.minecraft.entity.EntityLiving
import net.minecraft.init.Bootstrap.BehaviorDispenseOptional
import net.minecraft.item.ItemStack
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldServer
import net.minecraftforge.common.IShearable
import thedarkcolour.futuremc.registry.FSounds
import thedarkcolour.futuremc.tile.BeeHiveTile

/**
 * Shears stuff (beehive and sheep)
 */
class ShearDispenserBehaviour : BehaviorDispenseOptional() {
    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    override fun dispenseStack(source: IBlockSource, stack: ItemStack): ItemStack {
        val world = source.world

        if (!world.isRemote) {
            val pos = source.blockPos.offset(source.blockState.getValue(BlockDispenser.FACING))

            successful = shearBeehive(world as WorldServer, pos) || shearSheep(world, pos, stack)

            if (successful && stack.attemptDamageItem(1, world.rand, null)) {
                stack.count = 0
            }
        }

        return stack
    }

    private fun shearBeehive(worldIn: WorldServer, pos: BlockPos): Boolean {
        val tile = worldIn.getTileEntity(pos)

        if (tile is BeeHiveTile) {
            val honeyLevel = tile.honeyLevel

            if (honeyLevel >= 5) {
                worldIn.playSound(null, pos, FSounds.BEEHIVE_SHEAR, SoundCategory.BLOCKS, 1.0f, 1.0f)
                tile.dropHoneyCombs(worldIn, pos)
                tile.emptyHoney(worldIn, pos, null)
                return true
            }
        }

        return false
    }

    private fun shearSheep(worldIn: WorldServer, pos: BlockPos, shears: ItemStack): Boolean {
        for (entity in worldIn.getEntitiesWithinAABB(EntityLiving::class.java, AxisAlignedBB(pos))) {
            if (entity is IShearable) {
                if (entity.isShearable(shears, worldIn, pos)) {
                    entity.onSheared(shears, worldIn, pos, net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.FORTUNE, shears))
                }
            }
        }

        return false
    }
}