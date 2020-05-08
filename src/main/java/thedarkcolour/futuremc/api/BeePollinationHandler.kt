package thedarkcolour.futuremc.api

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.futuremc.entity.bee.BeeEntity
import thedarkcolour.futuremc.entity.bee.ai.GrowCropsAI

interface BeePollinationHandler {
    /**
     * This method is called in [GrowCropsAI.updateTask].
     *
     * Nothing is checked at the call site, so be sure to do age checks.
     *
     * You must add the bone meal particles yourself.
     * @see net.minecraftforge.common.util.Constants.WorldEvents.BONEMEAL_PARTICLES
     *
     * If the bee cannot pollinate this crop (say the crop is maximum age),
     * then return false.
     *
     * @return whether the bee has successfully pollinated the crop
     */
    fun pollinateCrop(worldIn: World, pos: BlockPos, state: IBlockState, beeEntity: BeeEntity): Boolean

    companion object {
        private val handlers = hashMapOf<Block, BeePollinationHandler>()

        /**
         * Add a custom pollination behaviour to this block,
         * or override the pollination behaviour of a block.
         */
        @JvmStatic
        fun addBeePollinationHandler(block: Block, handler: BeePollinationHandler) {
            handlers[block] = handler
        }

        /**
         * @param block block to check for bee pollination handling
         * @return the bee pollination handler or null of one doesn't exist
         */
        @JvmStatic
        fun get(block: Block): BeePollinationHandler? {
            return handlers[block]
        }
    }
}