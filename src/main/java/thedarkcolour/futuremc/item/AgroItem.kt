package thedarkcolour.futuremc.item

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import thedarkcolour.core.item.ModeledItem
import thedarkcolour.futuremc.entity.bee.EntityBee

class AgroItem : ModeledItem("bee_agro") {
    init {
        setMaxStackSize(1)
    }

    override fun itemInteractionForEntity(
        stack: ItemStack,
        playerIn: EntityPlayer,
        target: EntityLivingBase,
        hand: EnumHand
    ): Boolean {
        val world = target.world

        for (bee in world.getEntities<EntityBee>(EntityBee::class.java) { !it!!.isAngry }) {
            bee.setBeeAttacker(target, 40000)
        }

        return false
    }
}