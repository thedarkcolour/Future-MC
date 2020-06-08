package thedarkcolour.futuremc.item

import com.google.common.collect.Lists
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.IProjectile
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.init.Items
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemArrow
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent
import thedarkcolour.core.item.ModeledItem
import thedarkcolour.core.util.getOrCreateTag
import thedarkcolour.futuremc.enchantment.EnchantHelper
import thedarkcolour.futuremc.enchantment.Enchantments
import thedarkcolour.futuremc.entity.horizontal_firework.EntityHorizontalFireworksRocket
import thedarkcolour.futuremc.entity.trident.EntityModArrow
import thedarkcolour.futuremc.entity.trident.EntityPiercingArrow
import thedarkcolour.futuremc.registry.FItems
import thedarkcolour.futuremc.registry.FSounds
import java.util.*
import kotlin.math.PI
import kotlin.math.cos

class CrossbowItem : ModeledItem("crossbow") {
    private var isLoadingStart = false
    private var isLoadingMiddle = false

    init {
        setMaxStackSize(1)
        maxDamage = 326
        setCreativeTab(CreativeTabs.COMBAT)
        addPropertyOverride(ResourceLocation("pull")) { stack, _, entityIn ->
            if (entityIn != null && stack.item == this) {
                if (Companion.isCharged(stack)) {
                    0.0f
                } else {
                    (stack.maxItemUseDuration - entityIn.itemInUseCount).toFloat() / getChargeTime(stack).toFloat()
                }
            } else {
                0.0f
            }
        }
        addPropertyOverride(ResourceLocation("pulling")) { stack, _, entityIn ->
            if (!Companion.isCharged(stack) && entityIn != null && entityIn.isHandActive && entityIn.activeItemStack == stack) {
                1.0f
            } else {
                0.0f
            }
        }
        addPropertyOverride(ResourceLocation("charged")) { stack, _, _ ->
            if (Companion.isCharged(stack)) 1.0f else 0.0f
        }
        addPropertyOverride(ResourceLocation("firework")) { stack, _, entityIn ->
            if (entityIn != null && Companion.isCharged(stack) && hasChargedProjectile(stack)) {
                1.0f
            } else {
                0.0f
            }
        }
    }

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        val itemstack = playerIn.getHeldItem(handIn)
        return if (Companion.isCharged(itemstack)) {
            fireProjectiles(worldIn, playerIn, handIn, itemstack, getSoundPitch(itemstack), 1.0f)
            setCharged(itemstack, false)
            ActionResult(EnumActionResult.SUCCESS, itemstack)
        } else if (!findAmmo(playerIn).isEmpty) {
            if (!Companion.isCharged(itemstack)) {
                isLoadingStart = false
                isLoadingMiddle = false
                playerIn.activeHand = handIn
            }

            ActionResult(EnumActionResult.SUCCESS, itemstack)
        } else {
            ActionResult(EnumActionResult.FAIL, itemstack)
        }
    }

    override fun onPlayerStoppedUsing(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase, timeLeft: Int) {
        val i = getMaxItemUseDuration(stack) - timeLeft
        val f = getCharge(i, stack)
        if (f >= 1.0f && !Companion.isCharged(stack) && hasAmmo(entityLiving, stack)) {
            setCharged(stack, true)
            val soundcategory = if (entityLiving is EntityPlayer) SoundCategory.PLAYERS else SoundCategory.HOSTILE
            worldIn.playSound(
                null,
                entityLiving.posX,
                entityLiving.posY,
                entityLiving.posZ,
                FSounds.CROSSBOW_LOADING_END,
                soundcategory,
                1.0f,
                1.0f / (itemRand.nextFloat() * 0.5f + 1.0f) + 0.2f
            )
        }
    }

    private fun hasAmmo(entityIn: EntityLivingBase, crossbow: ItemStack): Boolean {
        val i = EnchantmentHelper.getEnchantmentLevel(Enchantments.MULTISHOT, crossbow)
        val j = if (i == 0) 1 else 3
        val isCreative = entityIn is EntityPlayer && entityIn.capabilities.isCreativeMode
        var itemstack = findAmmo(entityIn)
        var itemstack1 = itemstack.copy()

        for (k in 0 until j) {
            if (k > 0) {
                itemstack = itemstack1.copy()
            }

            if (itemstack.isEmpty && isCreative) {
                itemstack = ItemStack(Items.ARROW)
                itemstack1 = itemstack.copy()
            }

            if (!consumeAmmo(entityIn, crossbow, itemstack, k > 0, isCreative)) {
                return false
            }
        }

        return true
    }

    private fun consumeAmmo(
        entity: EntityLivingBase,
        crossbow: ItemStack,
        ammo: ItemStack,
        isMultishot: Boolean,
        isCreative: Boolean
    ): Boolean {
        if (ammo.isEmpty) {
            return false
        } else {
            val flag = isCreative && ammo.item is ItemArrow
            val stack: ItemStack
            if (!flag && !isCreative && !isMultishot) {
                stack = ammo.splitStack(1)
                if (ammo.isEmpty && entity is EntityPlayer) {
                    entity.inventory.deleteStack(ammo)
                }
            } else {
                stack = ammo.copy()
            }

            addChargedProjectile(crossbow, stack)
            return true
        }
    }

    private fun setCharged(stack: ItemStack, chargedIn: Boolean) {
        val tag = stack.getOrCreateTag()
        tag.setBoolean("Charged", chargedIn)
    }

    private fun addChargedProjectile(crossbow: ItemStack, projectile: ItemStack) {
        val nbt = crossbow.getOrCreateTag()
        val tagList: NBTTagList
        tagList = if (nbt.hasKey("ChargedProjectiles", 9)) {
            nbt.getTagList("ChargedProjectiles", 10)
        } else {
            NBTTagList()
        }

        val nbt1 = NBTTagCompound()
        projectile.writeToNBT(nbt1)
        tagList.appendTag(nbt1)
        nbt.setTag("ChargedProjectiles", tagList)
    }

    private fun getChargedProjectiles(stack: ItemStack): List<ItemStack> {
        val list = Lists.newArrayList<ItemStack>()
        val tag = stack.tagCompound
        if (tag?.hasKey("ChargedProjectiles", 9) == true) {
            val tagList = tag.getTagList("ChargedProjectiles", 10)
            for (i in 0 until tagList.tagCount()) {
                val nbt = tagList.getCompoundTagAt(i)
                list.add(ItemStack(nbt))
            }
        }

        return list
    }

    private fun clearProjectiles(stack: ItemStack) {
        stack.tagCompound?.let {
            val list = it.getTagList("ChargedProjectiles", 9)
            val iterator = list.iterator()
            while (iterator.hasNext()) {
                iterator.next()
                iterator.remove()
            }
        }
    }

    private fun hasChargedProjectile(stack: ItemStack): Boolean {
        return getChargedProjectiles(stack).stream().anyMatch { item -> item.item == Items.FIREWORKS }
    }

    private fun fireProjectile(
        worldIn: World,
        shooter: EntityLivingBase,
        hand: EnumHand,
        crossbow: ItemStack,
        projectile: ItemStack,
        soundPitch: Float,
        isCreative: Boolean,
        velocity: Float,
        inaccuracy: Float,
        projectileAngle: Float
    ) {
        if (!worldIn.isRemote) {
            val isFireworks = projectile.item == Items.FIREWORKS
            val a: IProjectile
            if (isFireworks) {
                a = EntityHorizontalFireworksRocket(
                    worldIn,
                    projectile,
                    shooter.posX,
                    shooter.posY + shooter.eyeHeight.toDouble() - 0.15f.toDouble(),
                    shooter.posZ
                )
            } else {
                a = createArrow(worldIn, shooter, crossbow, projectile)
                if (isCreative || projectileAngle != 0.0F) {
                    a.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY
                }
            }

            // Used for Pillagers
            //
            //if (shooter is ICrossbowUser) {
            //    val icrossbowuser = shooter as ICrossbowUser
            //    icrossbowuser.shoot(icrossbowuser.getAttackTarget(), crossbow, a, projectileAngle)
            //} else {
            val vec3d = shooter.getLook(1.0F)
            val f = projectileAngle * (PI / 180)
            vec3d.func214905a(vec3d.x * f, vec3d.y * f, vec3d.z * f, cos(f))
            a.shoot(vec3d.x, vec3d.y, vec3d.z, velocity, inaccuracy)
            //}

            crossbow.damageItem(if (isFireworks) 3 else 1, shooter)
            worldIn.spawnEntity(a as Entity)
            worldIn.playSound(
                null,
                shooter.posX,
                shooter.posY,
                shooter.posZ,
                FSounds.CROSSBOW_SHOOT,
                SoundCategory.PLAYERS,
                1.0f,
                soundPitch
            )
        }
    }

    private fun createArrow(
        worldIn: World,
        shooter: EntityLivingBase,
        crossbow: ItemStack,
        ammo: ItemStack
    ): EntityModArrow {
        val modArrow = EntityPiercingArrow(worldIn, ammo, shooter)
        if (shooter is EntityPlayer) {
            modArrow.isCritical = true
        }

        modArrow.func_213865_o(true)
        val i = EnchantmentHelper.getEnchantmentLevel(Enchantments.PIERCING, crossbow)
        if (i > 0) {
            modArrow.setPierceLevel(i.toByte())
        }

        return modArrow
    }

    private fun fireProjectiles(
        worldIn: World,
        shooter: EntityLivingBase,
        handIn: EnumHand,
        stack: ItemStack,
        velocityIn: Float,
        inaccuracyIn: Float
    ) {
        val list = getChargedProjectiles(stack)
        val afloat = getRandomSoundPitches(shooter.rng)

        for (i in list.indices) {
            val itemstack = list[i]
            val flag = shooter is EntityPlayer && shooter.capabilities.isCreativeMode
            if (!itemstack.isEmpty) {
                when (i) {
                    0 -> fireProjectile(
                        worldIn,
                        shooter,
                        handIn,
                        stack,
                        itemstack,
                        afloat[i],
                        flag,
                        velocityIn,
                        inaccuracyIn,
                        0.0f
                    )
                    1 -> fireProjectile(
                        worldIn,
                        shooter,
                        handIn,
                        stack,
                        itemstack,
                        afloat[i],
                        flag,
                        velocityIn,
                        inaccuracyIn,
                        -10.0f
                    )
                    2 -> fireProjectile(
                        worldIn,
                        shooter,
                        handIn,
                        stack,
                        itemstack,
                        afloat[i],
                        flag,
                        velocityIn,
                        inaccuracyIn,
                        10.0f
                    )
                }
            }
        }

        clearProjectiles(stack)
    }

    private fun getRandomSoundPitches(rand: Random): FloatArray {
        val flag = rand.nextBoolean()
        return floatArrayOf(1.0f, getRandomSoundPitch(flag), getRandomSoundPitch(!flag))
    }

    private fun getRandomSoundPitch(flagIn: Boolean): Float {
        val f = if (flagIn) 0.63f else 0.43f
        return 1.0f / (itemRand.nextFloat() * 0.5f + 1.8f) + f
    }

    fun update(event: LivingEntityUseItemEvent) {
        val entityLiving = event.entityLiving
        val worldIn = entityLiving.world
        val stack = entityLiving.activeItemStack
        val count = stack.count

        if (!worldIn.isRemote) {
            val level = EnchantHelper.getQuickCharge(stack)
            val sound = getSoundEvent(level)
            val sound1 = if (level == 0) {
                FSounds.CROSSBOW_LOADING_MIDDLE
            } else {
                null
            }
            val f = (stack.maxItemUseDuration - count).toFloat() / getChargeTime(stack).toFloat()
            if (f < 0.2) {
                isLoadingStart = false
                isLoadingMiddle = false
            }

            if (f >= 0.2 && !isLoadingStart) {
                isLoadingStart = true
                entityLiving.playSound(sound, 0.5F, 1F)
            }

            if (f >= 0.5 && sound1 != null && !isLoadingMiddle) {
                isLoadingMiddle = true
                entityLiving.playSound(sound1, 0.5F, 1F)
            }
        }
    }

    override fun getMaxItemUseDuration(stack: ItemStack): Int {
        return getChargeTime(stack) + 3
    }

    private fun getChargeTime(stack: ItemStack): Int {
        val quickCharge = EnchantHelper.getQuickCharge(stack)
        return if (quickCharge == 0) {
            25
        } else {
            25 - 5 * quickCharge
        }
    }

    override fun getItemUseAction(stack: ItemStack): EnumAction {
        return EnumAction.BOW
    }

    private fun getSoundEvent(level: Int): SoundEvent {
        return when (level) {
            1 -> FSounds.CROSSBOW_QUICK_CHARGE_I
            2 -> FSounds.CROSSBOW_QUICK_CHARGE_II
            3 -> FSounds.CROSSBOW_QUICK_CHARGE_III
            else -> FSounds.CROSSBOW_LOADING_START
        }
    }

    private fun getCharge(useTime: Int, stack: ItemStack): Float {
        var f = useTime.toFloat() / getChargeTime(stack).toFloat()
        if (f > 1.0f) {
            f = 1.0f
        }

        return f
    }

    private fun findAmmo(entityIn: EntityLivingBase): ItemStack {
        return if (entityIn is EntityPlayer) {
            if (ARROWS_AND_FIREWORKS(entityIn.heldItemMainhand)) {
                entityIn.heldItemMainhand
            } else if (ARROWS_AND_FIREWORKS(entityIn.heldItemOffhand)) {
                entityIn.heldItemOffhand
            } else {
                var stack = ItemStack.EMPTY
                for (i in 0 until entityIn.inventory.sizeInventory) {
                    stack = entityIn.inventory.getStackInSlot(i)
                    if (ARROWS(stack)) {
                        break
                    }
                }
                stack
            }
        } else if (entityIn is EntityMob) {
            when {
                ARROWS_AND_FIREWORKS(entityIn.heldItemMainhand) -> entityIn.heldItemMainhand
                ARROWS_AND_FIREWORKS(entityIn.heldItemOffhand) -> entityIn.heldItemOffhand
                else -> ItemStack(Items.ARROW)
            }
        } else {
            ItemStack.EMPTY
        }
    }

    private fun getSoundPitch(stack: ItemStack): Float {
        return if (stack.item == FItems.CROSSBOW && hasChargedProjectile(stack)) {
            1.6F
        } else {
            3.15F
        }
    }

    companion object {
        private val ARROWS: (ItemStack) -> Boolean = { stack ->
            stack.item is ItemArrow
        }

        private val ARROWS_AND_FIREWORKS: (ItemStack) -> Boolean = { stack ->
            ARROWS(stack) || stack.item == Items.FIREWORKS
        }

        fun isCharged(stack: ItemStack): Boolean {
            val tag = stack.tagCompound
            return tag?.getBoolean("Charged") ?: false
        }
    }
}