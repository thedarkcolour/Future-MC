package thedarkcolour.futuremc.compat.tconstruct

import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import slimeknights.tconstruct.library.utils.ToolHelper
import slimeknights.tconstruct.smeltery.TinkerSmeltery
import slimeknights.tconstruct.tools.melee.item.BattleAxe
import slimeknights.tconstruct.tools.tools.Hatchet
import slimeknights.tconstruct.tools.tools.LumberAxe
import slimeknights.tconstruct.tools.tools.Mattock
import thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipes

/**
 * Tinkers construct mod compatibility.
 *
 * @author TheDarkColour
 *
 * @see thedarkcolour.futuremc.compat.checkTConstruct
 */
object TConstructCompat {

    /**
     * Determines if the stack is an axe-like tool from Tinkers Construct. Used in log stripping.
     */
    fun isTinkersAxe(stack: ItemStack): Boolean {
        val item = stack.item
        return item is Hatchet || item is Mattock || item is BattleAxe || item is LumberAxe
    }

    /**
     * Damages a Tinkers Construct tool.
     */
    fun damageTool(stack: ItemStack, amount: Int, entityIn: EntityLivingBase) {
        ToolHelper.damageTool(stack, amount, entityIn)
    }

    /**
     * TConstruct specific stonecutter recipes.
     */
    fun registerStonecutterRecipes() {
        // seared stone variants
        val stack = ItemStack(TinkerSmeltery.searedBlock, 1, 32767)

        for (i in 0..11) {
            StonecutterRecipes.addRecipe(stack, ItemStack(TinkerSmeltery.searedBlock, 1, i))
        }
    }
}