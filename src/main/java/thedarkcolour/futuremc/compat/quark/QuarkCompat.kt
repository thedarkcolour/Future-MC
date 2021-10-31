package thedarkcolour.futuremc.compat.quark

import net.minecraft.block.state.IBlockState
import sun.reflect.Reflection
import thedarkcolour.futuremc.registry.FBlocks
import vazkii.quark.automation.block.BlockColorSlime
import vazkii.quark.base.module.ModuleLoader
import vazkii.quark.building.block.BlockBark
import vazkii.quark.building.feature.BarkBlocks
import vazkii.quark.client.feature.ShowInvalidSlots
import vazkii.quark.decoration.feature.MoreBanners

/**
 * Inter-mod compatibility with Quark.
 *
 * @author TheDarkColour
 *
 * @see thedarkcolour.futuremc.compat.checkQuark
 */
object QuarkCompat {
    private val BARK_2_STRIPPED_WOOD = hashMapOf<IBlockState, IBlockState>()

    /**
     * Checks if a block is colored slime.
     *
     * Used in honey blocks for checking piston behaviour.
     */
    fun isColoredSlime(state: IBlockState): Boolean {
        return state.block is BlockColorSlime
    }

    /**
     * Checks if Quark's More Banners are enabled.
     */
    fun hasMoreBanners(): Boolean {
        return ModuleLoader.isFeatureEnabled(MoreBanners::class.java)
    }

    fun isBarkBlock(state: IBlockState): Boolean {
        return state.block is BlockBark
    }

    /**
     * Gets the stripped variant of the Bark block.
     *
     * Used in wood stripping in `Events`.
     *
     * Because Quark bark blocks don't have rotation,
     * we return the default state of the bark block.
     *
     * Future MC's wood blocks have direction, so I
     * recommend using those over Quark's bark blocks.
     */
    fun getStrippedBark(state: IBlockState): IBlockState {
        return BARK_2_STRIPPED_WOOD.computeIfAbsent(state) { key ->
            when (key.getValue((BarkBlocks.bark as BlockBark).variantProp)) {
                BlockBark.Variants.BARK_OAK -> FBlocks.STRIPPED_OAK_WOOD.defaultState
                BlockBark.Variants.BARK_SPRUCE -> FBlocks.STRIPPED_SPRUCE_WOOD.defaultState
                BlockBark.Variants.BARK_BIRCH -> FBlocks.STRIPPED_BIRCH_WOOD.defaultState
                BlockBark.Variants.BARK_JUNGLE -> FBlocks.STRIPPED_JUNGLE_WOOD.defaultState
                BlockBark.Variants.BARK_ACACIA -> FBlocks.STRIPPED_ACACIA_WOOD.defaultState
                BlockBark.Variants.BARK_DARK_OAK -> FBlocks.STRIPPED_DARK_OAK_WOOD.defaultState
                else -> null!!
            }
        }
    }

    /**
     * Fixes a bug with Quark's Show Invalid Slots feature that caused items to disappear
     */
    fun isDrawingInvalidSlotsOverlay(): Boolean {
        return Reflection.getCallerClass(4) == ShowInvalidSlots::class.java
    }
}