package thedarkcolour.futuremc.item

import com.google.common.collect.Multimap
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.MoverType
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.item.ItemModeled
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.enchantment.EnchantHelper
import thedarkcolour.futuremc.entity.trident.EntityTrident
import thedarkcolour.futuremc.registry.FSounds
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class ItemTrident : ItemModeled("Trident") {
    override fun canDestroyBlockInCreative(
        world: World,
        pos: BlockPos,
        stack: ItemStack,
        player: EntityPlayer
    ): Boolean {
        return false
    }

    override fun getItemUseAction(stack: ItemStack): EnumAction = EnumAction.BOW

    override fun getMaxItemUseDuration(stack: ItemStack): Int = 72000

    override fun hasEffect(stack: ItemStack): Boolean = false

    override fun isEnchantable(stack: ItemStack): Boolean = true

    override fun onPlayerStoppedUsing(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase, timeLeft: Int) {
        if (entityLiving is EntityPlayer) {
            val i = getUseDuration(stack, timeLeft)
            if (i >= 10) {
                val j = EnchantHelper.getRiptide(stack)
                if (j <= 0 || entityLiving.isWet) {
                    if (!worldIn.isRemote) {
                        stack.damageItem(1, entityLiving)
                        if (j == 0) {
                            val trident = EntityTrident(worldIn, entityLiving, stack)
                            trident.shoot(
                                entityLiving,
                                entityLiving.rotationPitch,
                                entityLiving.rotationYaw,
                                0.0f,
                                2.5f + j.toFloat() * 0.5f,
                                1.0f
                            )
                            if (entityLiving.capabilities.isCreativeMode) {
                                trident.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY
                            }
                            worldIn.spawnEntity(trident)
                            if (!entityLiving.capabilities.isCreativeMode) {
                                entityLiving.inventory.deleteStack(stack)
                            }
                        }
                    }
                    var sound = FSounds.TRIDENT_THROW
                    if (j > 0) {
                        val f = entityLiving.rotationYaw
                        val f1 = entityLiving.rotationPitch
                        var f2 = -sin(f * (PI.toFloat() / 180f)) * cos(f1 * (PI.toFloat() / 180f))
                        var f3 = -sin(f1 * (PI.toFloat() / 180f))
                        var f4 = cos(f * (PI.toFloat() / 180f)) * cos(f1 * (PI.toFloat() / 180f))
                        val f5 = sqrt(f2 * f2 + f3 * f3 + f4 * f4)
                        val f6 = 3.0f * ((1.0f + j.toFloat()) / 4.0f)
                        f2 *= (f6 / f5)
                        f3 *= (f6 / f5)
                        f4 *= (f6 / f5)
                        entityLiving.addVelocity(f2.toDouble(), f3.toDouble(), f4.toDouble())
                        sound = when {
                            j >= 3 -> {
                                FSounds.TRIDENT_RIPTIDE_III
                            }
                            j == 2 -> {
                                FSounds.TRIDENT_RIPTIDE_II
                            }
                            else -> {
                                FSounds.TRIDENT_RIPTIDE_I
                            }
                        }
                        if (entityLiving.onGround) {
                            entityLiving.move(MoverType.SELF, 0.0, 1.1999999, 0.0)
                        }
                    }
                    worldIn.playSound(
                        null,
                        entityLiving.posX,
                        entityLiving.posY,
                        entityLiving.posZ,
                        sound,
                        SoundCategory.PLAYERS,
                        1.0f,
                        1.0f
                    )
                }
            }
        }
    }

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val stack = playerIn.getHeldItem(hand)
        return if (stack.itemDamage >= stack.maxDamage) {
            ActionResult(EnumActionResult.FAIL, stack)
        } else if (EnchantHelper.getRiptide(stack) > 0 && !playerIn.isWet) {
            ActionResult(EnumActionResult.FAIL, stack)
        } else {
            playerIn.activeHand = hand
            ActionResult(EnumActionResult.SUCCESS, stack)
        }
    }

    override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
        stack.damageItem(1, attacker)
        return true
    }

    private fun getUseDuration(stack: ItemStack, timeLeft: Int): Int {
        return stack.maxItemUseDuration - timeLeft
    }

    override fun onBlockDestroyed(
        stack: ItemStack,
        worldIn: World,
        state: IBlockState,
        pos: BlockPos,
        entityLiving: EntityLivingBase
    ): Boolean {
        if (state.getBlockHardness(worldIn, pos).toDouble() != 0.0) {
            stack.damageItem(2, entityLiving)
        }
        return true
    }

    override fun getItemAttributeModifiers(equipmentSlot: EntityEquipmentSlot): Multimap<String, AttributeModifier> {
        val multimap = super.getItemAttributeModifiers(equipmentSlot)
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(
                SharedMonsterAttributes.ATTACK_DAMAGE.name,
                AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 8.0, 0)
            )
            multimap.put(
                SharedMonsterAttributes.ATTACK_SPEED.name,
                AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (-2.9f).toDouble(), 0)
            )
        }
        return multimap
    }

    override fun getItemEnchantability(): Int = 1

    override fun getItemStackLimit(): Int = 1

    //val tileEntityItemStackRendererr: TileEntityItemStackRenderer
    //    get() = SpecialItemRenderer.instance

    init {
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.COMBAT else FutureMC.TAB
        maxDamage = 250
        setMaxStackSize(1)
    }
}