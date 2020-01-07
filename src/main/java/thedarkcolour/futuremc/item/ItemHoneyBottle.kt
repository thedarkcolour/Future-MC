package thedarkcolour.futuremc.item

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Items
import net.minecraft.init.MobEffects
import net.minecraft.init.SoundEvents
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.world.World
import thedarkcolour.core.item.ItemModeled
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig

class ItemHoneyBottle : ItemModeled("honey_bottle") {
    init {
        setMaxStackSize(16)
        setCreativeTab(if (FConfig.useVanillaCreativeTabs) CreativeTabs.FOOD else FutureMC.TAB)
        containerItem = Items.GLASS_BOTTLE
    }

    override fun getItemUseAction(stack: ItemStack): EnumAction = EnumAction.DRINK

    override fun getMaxItemUseDuration(stack: ItemStack): Int = 40

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        playerIn.activeHand = handIn
        return ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn))
    }

    override fun onItemUseFinish(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase): ItemStack {
        if (entityLiving is EntityPlayerMP) {
            CriteriaTriggers.CONSUME_ITEM.trigger(entityLiving, stack)
            entityLiving.foodStats.addStats(6, 0.8f)
            stack.shrink(1)
            worldIn.playSound(null, entityLiving.position, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5f, worldIn.rand.nextFloat() * 0.1f + 0.9f)
        }

        if (!worldIn.isRemote) {
            entityLiving.removePotionEffect(MobEffects.POISON)
        }

        val isCreative = entityLiving is EntityPlayer && entityLiving.isCreative

        return if (stack.isEmpty && !isCreative) {
            ItemStack(Items.GLASS_BOTTLE)
        } else {
            stack.grow(1)
            stack
        }
    }
}