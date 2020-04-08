package thedarkcolour.futuremc.entity.trident

import com.google.common.collect.Sets
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.projectile.EntityTippedArrow
import net.minecraft.init.Items
import net.minecraft.init.PotionTypes
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.potion.PotionEffect
import net.minecraft.potion.PotionUtils
import net.minecraft.util.EnumParticleTypes
import net.minecraft.world.World

class EntityPiercingArrow : EntityModArrow {
    private var potion = PotionTypes.EMPTY
    private val customPotionEffects: MutableSet<PotionEffect> = Sets.newHashSet()
    private var fixedColor = false

    @Suppress("unused")
    constructor(world: World) : super(world)

    // unused
    // constructor(world: World, x: Double, y: Double, z: Double) : super(world, x, y, z)

    constructor(world: World, stack: ItemStack, shooter: EntityLivingBase) : super(world, shooter) {
        if (stack.item == Items.TIPPED_ARROW) {
            potion = PotionUtils.getPotionFromItem(stack)
            val c = PotionUtils.getFullEffectsFromItem(stack)

            if (c.isNotEmpty()) {
                for (potionEffect in c) {
                    customPotionEffects.add(PotionEffect(potionEffect))
                }
            }

            val i = EntityTippedArrow.getCustomColor(stack)

            if (i == -1) {
                refreshColor()
            } else {
                fixedColor = true
                dataManager.set(COLOR, i)
            }
        } else if (stack.item == Items.ARROW) {
            potion = PotionTypes.EMPTY
            customPotionEffects.clear()
            dataManager.set(COLOR, -1)
        }
    }

    private fun refreshColor() {
        fixedColor = false
        updateColor()
    }

    private fun updateColor() {
        dataManager.set(
            COLOR,
            PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(potion, customPotionEffects))
        )
    }

    fun addEffect(effect: PotionEffect) {
        customPotionEffects.add(effect)
        updateColor()
    }

    override fun entityInit() {
        super.entityInit()
        dataManager.register(COLOR, -1)
    }

    override fun onUpdate() {
        super.onUpdate()
    }

    fun spawnPotionParticles(particleCount: Int) {
        val i = getColor()

        if (i != -1 && particleCount > 0) {
            val d0 = (i shr 16 and 255).toDouble() / 255.0
            val d1 = (i shr 8 and 255).toDouble() / 255.0
            val d2 = (i shr 0 and 255).toDouble() / 255.0

            for (j in 0 until particleCount) {
                world.spawnParticle(
                    EnumParticleTypes.SPELL_MOB,
                    posX + (rand.nextDouble() - 0.5) * width.toDouble(),
                    posY + rand.nextDouble() * height.toDouble(),
                    posZ + (rand.nextDouble() - 0.5) * width.toDouble(),
                    d0,
                    d1,
                    d2
                )
            }
        }
    }

    private fun getColor(): Int {
        return dataManager.get(COLOR)
    }

    override fun getArrowStack(): ItemStack {
        return if (customPotionEffects.isEmpty() && potion == PotionTypes.EMPTY) {
            ItemStack(Items.ARROW)
        } else {
            val itemstack = ItemStack(Items.TIPPED_ARROW)
            PotionUtils.addPotionToItemStack(itemstack, potion)
            PotionUtils.appendEffects(itemstack, customPotionEffects)
            if (fixedColor) {
                var nbttagcompound = itemstack.tagCompound
                if (nbttagcompound == null) {
                    nbttagcompound = NBTTagCompound()
                    itemstack.tagCompound = nbttagcompound
                }
                nbttagcompound.setInteger("CustomPotionColor", getColor())
            }
            itemstack
        }
    }

    override fun handleStatusUpdate(id: Byte) {
        if (id == 0.toByte()) {
            val i = fixedColor
        }
    }

    companion object {
        private val COLOR = EntityDataManager.createKey(EntityTippedArrow::class.java, DataSerializers.VARINT)
    }
}