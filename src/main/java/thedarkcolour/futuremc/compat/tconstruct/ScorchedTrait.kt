package thedarkcolour.futuremc.compat.tconstruct

import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import slimeknights.tconstruct.library.traits.AbstractTrait

class ScorchedTrait : AbstractTrait("scorched", 0x4A2940) {
    override fun damage(
        tool: ItemStack?,
        player: EntityLivingBase?,
        target: EntityLivingBase?,
        damage: Float,
        newDamage: Float,
        isCritical: Boolean
    ): Float {
        return super.damage(tool, player, target, damage, newDamage, isCritical)
    }
}