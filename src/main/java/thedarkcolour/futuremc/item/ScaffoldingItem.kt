package thedarkcolour.futuremc.item

import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.util.BlockSnapshot
import net.minecraftforge.event.ForgeEventFactory
import thedarkcolour.core.item.ModeledItemBlock
import thedarkcolour.futuremc.block.villagepillage.ScaffoldingBlock
import thedarkcolour.futuremc.registry.FBlocks

class ScaffoldingItem : ModeledItemBlock(FBlocks.SCAFFOLDING) {
    override fun onItemUse(player: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        var placementPos = pos

        // place on the side of a non-replaceable block
        val clickedState = worldIn.getBlockState(placementPos)
        if (!clickedState.block.isReplaceable(worldIn, placementPos) && clickedState.block != this.block) {
            placementPos = placementPos.offset(facing)
        } else {
            if (clickedState.block != this.block && ScaffoldingBlock.getHorizontalDistance(worldIn, placementPos) == 7) {
                return EnumActionResult.FAIL
            } else {
                val direction = if (player.isSneaking) {
                    facing // todo look at the "isInside" thing to see what it does
                } else {
                    if (facing == EnumFacing.UP) {
                        player.horizontalFacing
                    } else EnumFacing.UP
                }

                var i = 0
                val cursor = BlockPos.MutableBlockPos(placementPos).move(direction)
                var found = false

                while (i < 7) {
                    if (!worldIn.isRemote && !worldIn.worldBorder.contains(cursor)) {
                        if (player is EntityPlayerMP && cursor.y >= 255) {
                            // todo send world border message
                        }
                        break
                    }

                    val state = worldIn.getBlockState(cursor)
                    if (state.block != this.block) {
                        if (state.block.isReplaceable(worldIn, cursor)) {
                            placementPos = cursor
                            found = true
                        }
                        break
                    }

                    cursor.move(direction)
                    if (direction.axis.isHorizontal) {
                        i++
                    }
                }

                if (!found) {
                    return EnumActionResult.FAIL
                }
            }
        }

        val stack = player.getHeldItem(hand)

        if (!stack.isEmpty && player.canPlayerEdit(placementPos, facing, stack) && canPlaceIgnoreBlockCheck(worldIn, this.block, placementPos, false, facing, player)) {
            val meta = this.getMetadata(stack.metadata)
            var state = this.block.getStateForPlacement(worldIn, placementPos, facing, hitX, hitY, hitZ, meta, player, hand)

            // play placement sound and shrink stack
            if (placeBlockAt(stack, player, worldIn, placementPos, facing, hitX, hitY, hitZ, state)) {
                state = worldIn.getBlockState(placementPos)
                val soundType = state.block.getSoundType(state, worldIn, placementPos, player)
                worldIn.playSound(player, placementPos, soundType.placeSound, SoundCategory.BLOCKS, (soundType.getVolume() + 1.0f) / 2.0f, soundType.getPitch() * 0.8f)
                stack.shrink(1)
            }
            return EnumActionResult.SUCCESS
        } else {
            return EnumActionResult.FAIL
        }
    }

    override fun canPlaceBlockOnSide(
        worldIn: World,
        pos: BlockPos,
        side: EnumFacing,
        player: EntityPlayer,
        stack: ItemStack
    ): Boolean {
        return true // does this work?
    }

    // World.mayPlace but ignores Block.canPlaceBlockAt + ignores placer collision check
    private fun canPlaceIgnoreBlockCheck(level: World, blockIn: Block, pos: BlockPos, skipCollisionCheck: Boolean, sidePlacedOn: EnumFacing, placer: Entity?): Boolean {
        val state = level.getBlockState(pos)
        val bounds: AxisAlignedBB? = if (skipCollisionCheck) null else this.block.defaultState.getCollisionBoundingBox(level, pos)

        // forge hook
        if (!(placer is EntityPlayer || !ForgeEventFactory.onBlockPlace(placer, BlockSnapshot(level, pos, blockIn.defaultState), sidePlacedOn).isCanceled)) {
            return false
        }

        if (bounds != null && !level.checkNoEntityCollision(bounds.offset(pos), placer)) {
            return false
        }

        return state.block.isReplaceable(level, pos)
    }
}