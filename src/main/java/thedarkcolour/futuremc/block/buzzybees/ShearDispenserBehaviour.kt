package thedarkcolour.futuremc.block.buzzybees

import net.minecraft.block.BlockDispenser
import net.minecraft.dispenser.IBehaviorDispenseItem
import net.minecraft.dispenser.IBlockSource
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.Bootstrap.BehaviorDispenseOptional
import net.minecraft.item.ItemStack
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import net.minecraftforge.common.IShearable
import thedarkcolour.futuremc.registry.FSounds
import thedarkcolour.futuremc.tile.BeeHiveTile

/**
 * Shears stuff (beehive)
 */
object ShearDispenserBehaviour : BehaviorDispenseOptional() {
    fun dispense(world: World, source: IBlockSource, stack: ItemStack, existing: IBehaviorDispenseItem?): ItemStack {
        val state = source.blockState

        if (!world.isRemote) {
            val pos = source.blockPos.offset(state.getValue(BlockDispenser.FACING))

            successful = shearBeehive(world, pos) || shearSheep(world as WorldServer, pos, stack)

            if (!successful && existing != null) {
                return stack
            }

            // Remove item if it's broken
            if (successful && stack.attemptDamageItem(1, world.rand, null)) {
                stack.count = 0
            }
        }

        playDispenseSound(source)
        spawnDispenseParticles(source, state.getValue(BlockDispenser.FACING))

        // Return the item to keep in the slot
        return stack
    }

    private fun shearBeehive(worldIn: World, pos: BlockPos): Boolean {
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
                    val drops = entity.onSheared(shears, worldIn, pos, net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.FORTUNE, shears))
                    val rand = worldIn.rand

                    // Copied from EntitySheep
                    for (stack in drops) {
                        val f = 0.7f
                        val itemEntity = EntityItem(worldIn,
                            pos.x + (rand.nextFloat() * f) + (1.0f - f) * 0.5,
                            pos.y + (rand.nextFloat() * f) + (1.0f - f) * 0.5,
                            pos.z + (rand.nextFloat() * f) + (1.0f - f) * 0.5,
                            stack)
                        itemEntity.setDefaultPickupDelay()
                        worldIn.spawnEntity(itemEntity)
                    }
                    return true
                }
            }
        }

        return false
    }
}