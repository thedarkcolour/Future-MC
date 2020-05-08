@file:Suppress("DEPRECATION")

package thedarkcolour.futuremc.entity.bee.ai

import net.minecraft.block.Block
import net.minecraft.block.BlockBeetroot
import net.minecraft.block.BlockCrops
import net.minecraft.block.BlockStem
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.init.Blocks
import thedarkcolour.futuremc.api.BeePollinationHandler
import thedarkcolour.futuremc.block.SweetBerryBushBlock
import thedarkcolour.futuremc.entity.bee.BeeEntity
import thedarkcolour.futuremc.registry.FBlocks

class GrowCropsAI(bee: BeeEntity) : PassiveAI(bee) {
    override fun canBeeStart(): Boolean {
        return when {
            bee.getCropsGrownSincePollination() >= 10 -> false
            bee.rng.nextFloat() < 0.3f -> false
            else -> bee.hasNectar() && bee.isHiveValid()
        }
    }

    override fun canBeeContinue(): Boolean {
        return canBeeStart()
    }

    override fun updateTask() {
        if (bee.rng.nextInt(30) == 0) {
            for (i in 1..2) {
                val pos = bee.position.down(i)
                val state = bee.world.getBlockState(pos)
                val block = state.block
                var canGrow = false
                var ageProperty: PropertyInteger? = null

                val pollinationHandler = BeePollinationHandler.get(block)

                if (pollinationHandler != null) {
                    if (pollinationHandler.pollinateCrop(bee.world, pos, state, bee)) {
                        bee.addCropCounter()
                    }
                } else if (canGrowBlock(block)) {
                    if (block is BlockCrops) {
                        if (!block.isMaxAge(state)) {
                            canGrow = true

                            ageProperty = if (block is BlockBeetroot) {
                                BlockBeetroot.BEETROOT_AGE
                            } else {
                                block.ageProperty
                            }
                        }
                    } else {
                        val age: Int
                        if (block is BlockStem) {
                            age = state.getValue(BlockStem.AGE)
                            if (age < 7) {
                                canGrow = true
                                ageProperty = BlockStem.AGE
                            }
                        } else if (block == FBlocks.SWEET_BERRY_BUSH) {
                            age = state.getValue(SweetBerryBushBlock.AGE)
                            if (age < 3) {
                                canGrow = true
                                ageProperty = SweetBerryBushBlock.AGE
                            }
                        }
                    }

                    if (ageProperty != null && canGrow) {
                        bee.world.playEvent(2005, pos, 0)
                        bee.world.setBlockState(
                            pos,
                            state.withProperty(ageProperty, state.getValue(ageProperty) + 1)
                        )
                        bee.addCropCounter()
                    }
                }
            }

        }
    }

    private fun canGrowBlock(block: Block): Boolean {
        return block == Blocks.WHEAT || block == Blocks.CARROTS || block == Blocks.POTATOES || block == Blocks.BEETROOTS
                || block == Blocks.MELON_STEM || block == Blocks.PUMPKIN_STEM || block == FBlocks.SWEET_BERRY_BUSH
    }
}