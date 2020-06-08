package thedarkcolour.futuremc.api

import net.minecraft.block.Block
import net.minecraft.block.BlockBeetroot
import net.minecraft.block.BlockCrops
import net.minecraft.block.BlockStem
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.util.Constants.WorldEvents
import thedarkcolour.futuremc.block.SweetBerryBushBlock
import thedarkcolour.futuremc.entity.bee.EntityBee
import thedarkcolour.futuremc.registry.FBlocks

/**
 * This class is used to handle crop pollination for [EntityBee].
 * If you would like to add custom crop pollination behaviour,
 * use [BeePollinationHandler.registerHandler].
 *
 * @author TheDarkColour
 *
 * You may include this class in your mod JAR.
 */
interface BeePollinationHandler {
    /**
     * This method is called in [EntityBee.AIGrowCrops.updateTask].
     *
     * If the bee cannot pollinate this crop (example: the crop is fully mature),
     * then return false.
     *
     * @return if the bee should gain a crop counter (increment the total number of crops grown)
     *
     * @see handlers for future mc defaults
     * @see WorldEvents.BONEMEAL_PARTICLES world event that adds bonemeal particles
     */
    fun pollinateCrop(worldIn: World, pos: BlockPos, state: IBlockState, beeEntity: EntityBee): Boolean

    companion object {
        val blockCropsHandler = create { worldIn, pos, state, bee ->
            if (worldIn.getBlockState(pos).getValue(BlockCrops.AGE) < 7) {
                bee.world.playEvent(WorldEvents.BONEMEAL_PARTICLES, pos, 0)
                bee.world.setBlockState(pos, state.withProperty(BlockCrops.AGE, state.getValue(BlockCrops.AGE) + 1))
                true
            } else {
                false
            }
        }

        private val handlers = hashMapOf<Block, BeePollinationHandler?>().also { map ->
            val beetRootsHandler = create { worldIn, pos, state, bee ->
                if (worldIn.getBlockState(pos).getValue(BlockBeetroot.BEETROOT_AGE) < 3) {
                    bee.world.playEvent(WorldEvents.BONEMEAL_PARTICLES, pos, 0)
                    bee.world.setBlockState(pos, state.withProperty(BlockBeetroot.BEETROOT_AGE, state.getValue(BlockBeetroot.BEETROOT_AGE) + 1))
                    true
                } else {
                    false
                }
            }
            val stemBlockHandler = create { worldIn, pos, state, bee ->
                if (worldIn.getBlockState(pos).getValue(BlockStem.AGE) < 7) {
                    bee.world.playEvent(WorldEvents.BONEMEAL_PARTICLES, pos, 0)
                    bee.world.setBlockState(pos, state.withProperty(BlockStem.FACING, state.getValue(BlockStem.FACING)).withProperty(BlockStem.AGE, state.getValue(BlockStem.AGE) + 1))
                    true
                } else {
                    false
                }
            }
            val sweetBerryBushHandler = create { worldIn, pos, state, bee ->
                if (worldIn.getBlockState(pos).getValue(SweetBerryBushBlock.AGE) < 3) {
                    bee.world.playEvent(WorldEvents.BONEMEAL_PARTICLES, pos, 0)
                    bee.world.setBlockState(pos, state.withProperty(SweetBerryBushBlock.AGE, state.getValue(SweetBerryBushBlock.AGE) + 1))
                    true
                } else {
                    false
                }
            }

            map[Blocks.WHEAT] = blockCropsHandler
            map[Blocks.CARROTS] = blockCropsHandler
            map[Blocks.POTATOES] = blockCropsHandler
            map[Blocks.BEETROOTS] = beetRootsHandler
            map[Blocks.MELON_STEM] = stemBlockHandler
            map[Blocks.PUMPKIN_STEM] = stemBlockHandler
            map[FBlocks.SWEET_BERRY_BUSH] = sweetBerryBushHandler
        }

        /**
         * Add a custom pollination behaviour to this block,
         * or override the pollination behaviour of a block.
         */
        @JvmStatic
        fun registerHandler(block: Block, handler: BeePollinationHandler?) {
            handlers[block] = handler
        }

        /**
         * Get the custom pollination handler for the [block]
         * or `null` if the block has no custom pollination handler
         */
        @JvmStatic
        fun getHandler(block: Block): BeePollinationHandler? {
            return handlers[block]
        }

        /**
         * Factory function
         * TODO make Kotlin for Forge in 1.12 and use `fun interface`
         */
        inline fun create(crossinline handler: (World, BlockPos, IBlockState, EntityBee) -> Boolean): BeePollinationHandler {
            return object : BeePollinationHandler {
                override fun pollinateCrop(
                    worldIn: World,
                    pos: BlockPos,
                    state: IBlockState,
                    beeEntity: EntityBee
                ): Boolean {
                    return handler(worldIn, pos, state, beeEntity)
                }
            }
        }
    }
}