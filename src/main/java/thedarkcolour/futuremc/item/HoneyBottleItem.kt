package thedarkcolour.futuremc.item

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Items
import net.minecraft.init.MobEffects
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraft.stats.StatList
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World
import thedarkcolour.core.util.setItemModel
import thedarkcolour.core.util.setItemName
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig

// work with apple skin and quark
class HoneyBottleItem : ItemFood(6, 0.8f, false) {
    init {
        // use functions from ItemModeled.kt
        setItemName(this, "honey_bottle")
        setItemModel(this, 0)
        setMaxStackSize(16)
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.FOOD else FutureMC.GROUP
        containerItem = Items.GLASS_BOTTLE
    }

    override fun getItemUseAction(stack: ItemStack) = EnumAction.DRINK

    override fun getMaxItemUseDuration(stack: ItemStack) = 40

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        playerIn.activeHand = handIn
        return ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn))
    }

    override fun onItemUseFinish(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase): ItemStack {
        super.onItemUseFinish(stack, worldIn, entityLiving)
        if (entityLiving is EntityPlayerMP) {
            CriteriaTriggers.CONSUME_ITEM.trigger(entityLiving, stack)
            entityLiving.addStat(StatList.getObjectUseStats(this)!!)
        }

        if (!worldIn.isRemote) {
            entityLiving.removePotionEffect(MobEffects.POISON)
        }

        return if (stack.isEmpty) {
            ItemStack(Items.GLASS_BOTTLE)
        } else {
            val bottle = ItemStack(Items.GLASS_BOTTLE)

            if (entityLiving is EntityPlayer && !entityLiving.capabilities.isCreativeMode) {
                if (!entityLiving.inventory.addItemStackToInventory(bottle)) {
                    entityLiving.dropItem(bottle, false)
                }
            }

            stack
        }
    }
}