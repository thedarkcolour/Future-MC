package thedarkcolour.futuremc.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.state.IntegerProperty
import net.minecraft.tags.FluidTags
import net.minecraft.util.ActionResultType
import net.minecraft.util.Direction
import net.minecraft.util.Hand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import thedarkcolour.futuremc.registry.FSounds
import kotlin.math.floor

class RespawnAnchorBlock(properties: Properties) : Block(properties) {
    override fun onUse(
        state: BlockState,
        worldIn: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockRayTraceResult
    ): ActionResultType {
        val stack = player.getHeldItem(hand)

        if (hand == Hand.MAIN_HAND && !isChargeItem(stack) && isChargeItem(player.getHeldItem(Hand.OFF_HAND))) {
            return ActionResultType.PASS
        } else if (isChargeItem(stack)) {
            charge(worldIn, pos, state)
            if (!player.abilities.isCreativeMode) {
                stack.shrink(1)
            }

            return ActionResultType.SUCCESS
        } else if (state.get(CHARGES) == 0) {
            return ActionResultType.PASS
        } else if (!isNether(worldIn)) {
            if (!worldIn.isRemote) {
                explode(state, worldIn, pos)
            }

            return ActionResultType.SUCCESS
        } else {
            if (player is ServerPlayerEntity) {
                val type = worldIn.dimension.type

                if (player.spawnDimension != type || player.getBedLocation(type) != pos) {
                    player.setSpawnPoint(pos, false, true, type)
                    worldIn.playSound(null, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, FSounds.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.BLOCKS, 1.0f, 1.0f)
                    return ActionResultType.SUCCESS
                }
            }

            return if (canCharge(state)) ActionResultType.PASS else ActionResultType.CONSUME
        }
    }

    /**
     * Get a light value for this block, taking into account the given state and coordinates, normal ranges are between 0 and 15
     *
     * @param state
     * @param world
     * @param pos
     * @return The light value
     */
    override fun getLightValue(state: BlockState, world: IBlockReader, pos: BlockPos): Int {
        return floor((state.get(CHARGES).toFloat() / 4.0f) * 15.0f).toInt()
    }

    override fun hasComparatorInputOverride(state: BlockState) = true

    override fun getComparatorInputOverride(state: BlockState, worldIn: World, pos: BlockPos): Int {
        return state.getLightValue(worldIn, pos)
    }

    companion object {
        val CHARGES = IntegerProperty.create("charges", 0, 4)

        // todo add a tag for this
        fun isChargeItem(stack: ItemStack): Boolean {
            return stack.item == Items.GLOWSTONE
        }

        fun canCharge(state: BlockState): Boolean {
            return state.get(CHARGES) < 4
        }

        fun explode(state: BlockState, worldIn: World, pos: BlockPos) {
            worldIn.removeBlock(pos, false)
            val bool = Direction.Plane.HORIZONTAL.map(pos::offset).any {
                hasWaterSource(it, worldIn)
            }
        }

        private fun hasWaterSource(pos: BlockPos, worldIn: World): Boolean {
            val state = worldIn.getFluidState(pos)
            return if (!state.isTagged(FluidTags.WATER)) {
                false
            } else if (state.isSource) {
                true
            } else {
                val f = state.level

                if (f < 2.0f) {
                    false
                } else {
                    val state2 = worldIn.getFluidState(pos.down())
                    !state2.isTagged(FluidTags.WATER)
                }
            }
        }

        private fun isNether(worldIn: World): Boolean {
            return worldIn.dimension.isNether
        }

        private fun charge(worldIn: World, pos: BlockPos, state: BlockState) {
            worldIn.setBlockState(pos, state.with(CHARGES, state.get(CHARGES) + 1))
            worldIn.playSound(null, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, FSounds.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.BLOCKS, 1.0f, 1.0f)
        }
    }
}
