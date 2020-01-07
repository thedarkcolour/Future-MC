package thedarkcolour.futuremc.api

import net.minecraft.block.state.IBlockState

/**
 * Used to determine if your Block#isStickyBlock block should attach to another Block#isStickyBlock.
 */
interface IStickyBlock {
    /**
     * Return whether this block sticks to the other block.
     * @param state the state of this block
     * @param other the state of the other block
     */
    fun canStickTo(state: IBlockState, other: IBlockState): Boolean
}