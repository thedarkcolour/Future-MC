package thedarkcolour.futuremc.feature

import com.mojang.datafixers.Dynamic
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.material.Material
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockPos.Mutable
import net.minecraft.util.math.MathHelper
import net.minecraft.world.IWorld
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.GenerationSettings
import net.minecraft.world.gen.feature.Feature
import java.util.*
import kotlin.math.min

class HugeFungusFeature(configFactory: (Dynamic<*>) -> HugeFungusFeatureConfig) : Feature<HugeFungusFeatureConfig>(configFactory) {
    override fun place(worldIn: IWorld, generator: ChunkGenerator<out GenerationSettings>?, rand: Random, pos: BlockPos, config: HugeFungusFeatureConfig): Boolean {
        val block = config.base.block
        var pos2: BlockPos? = null
        if (config.planted) {
            val block2: Block = worldIn.getBlockState(pos.down()).block
            if (block2 == block) {
                pos2 = pos
            }
        } else {
            pos2 = getStartPos(worldIn, pos, block)
        }

        return if (pos2 == null) {
            false
        } else {
            var i = MathHelper.nextInt(rand, 4, 13)

            if (rand.nextInt(12) == 0) {
                i *= 2
            }

            if (!config.planted) {
                val j = if (worldIn.dimension.isNether) 128 else 256

                if (pos2.y + i + 1 >= j) {
                    return false
                }
            }
            worldIn.setBlockState(pos, Blocks.AIR.defaultState, 4)

            val bl = !config.planted && rand.nextFloat() < 0.06f
            generateHat(worldIn, rand, config, pos2, i, bl)
            generateStem(worldIn, rand, config, pos2, i, bl)
            true
        }
    }

    private fun getStartPos(worldIn: IWorld, pos: BlockPos, block: Block): BlockPos? {
        val mutable = Mutable(pos)
        
        for (i in pos.y downTo 1) {
            mutable.y = i
            val block2 = worldIn.getBlockState(mutable.down()).block
            if (block2 == block) {
                return mutable
            }
        }
        return null
    }

    private fun generateHat(world: IWorld, random: Random, config: HugeFungusFeatureConfig, pos: BlockPos, hatHeight: Int, thickStem: Boolean) {
        val mutable = Mutable()
        val isCrimson = config.hat.block == Blocks.NETHER_WART_BLOCK
        val i = min(random.nextInt(1 + hatHeight / 3) + 5, hatHeight)
        val hatBottom = hatHeight - i

        for (y in hatBottom..hatHeight) {
            var l = if (y < hatHeight - random.nextInt(3)) 2 else 1
            if (i > 8 && y < hatBottom + 4) {
                l = 3
            }
            if (thickStem) {
                ++l
            }
            for (x in -l..l) {
                for (z in -l..l) {
                    val bl2 = x == -l || x == l
                    val bl3 = z == -l || z == l
                    val bl4 = !bl2 && !bl3 && y != hatHeight
                    val bl5 = bl2 && bl3
                    val bl6 = y < hatBottom + 3
                    mutable.setPos(pos.x + x, pos.y + y, pos.z + z)
                    if (canReplace(world, mutable)) {
                        if (config.planted && !world.getBlockState(mutable.down()).isAir) {
                            world.breakBlock(mutable, true, null)
                        }
                        if (bl6) {
                            if (!bl4) {
                                tryGenerateVines(world, random, mutable, config.hat, isCrimson)
                            }
                        } else if (bl4) {
                            generateHatBlock(world, random, config, mutable, 0.1f, 0.2f, if (isCrimson) 0.1f else 0.0f)
                        } else if (bl5) {
                            generateHatBlock(world, random, config, mutable, 0.01f, 0.7f, if (isCrimson) 0.083f else 0.0f)
                        } else {
                            generateHatBlock(world, random, config, mutable, 5.0E-4f, 0.98f, if (isCrimson) 0.07f else 0.0f)
                        }
                    }
                }
            }
        }
    }

    private fun canReplace(worldIn: IWorld, pos: BlockPos): Boolean {
        return worldIn.getBlockState(pos).isAir(worldIn, pos) || !worldIn.getFluidState(pos).isEmpty || isReplaceable(worldIn, pos)
    }

    private fun isReplaceable(worldIn: IWorld, pos: BlockPos): Boolean {
        return worldIn.hasBlockState(pos) { state ->
            state.material == Material.TALL_PLANTS
        }
    }

    private fun tryGenerateVines(worldIn: IWorld, rand: Random, pos: Mutable, state: BlockState, isCrimson: Boolean) {
        if (worldIn.getBlockState(pos.down()).block == state.block) {
            setBlockState(worldIn, pos, state)
        } else if (rand.nextFloat() < 0.15) {
            setBlockState(worldIn, pos, state)
            if (isCrimson && rand.nextInt(11) == 0) {
                generateVines(pos, worldIn, rand)
            }
        }
    }

    private fun generateVines(pos: Mutable, worldIn: IWorld, rand: Random) {
        pos.offset(Direction.DOWN)

        if (worldIn.isAirBlock(pos)) {
            var length = MathHelper.nextInt(rand, 1, 5)

            if (rand.nextInt(7) == 0) {
                length *= 2
            }

            WeepingVinesFeature.placeVine(worldIn, rand, pos, length, minAge = 23, maxAge = 25)
        }
    }

    private fun generateHatBlock(worldIn: IWorld, rand: Random, config: HugeFungusFeatureConfig, mutable: Mutable, decorationChance: Float, generationChance: Float, vineChance: Float) {
        if (rand.nextFloat() < decorationChance) {
            setBlockState(worldIn, mutable, config.decoration)
        } else if (rand.nextFloat() < generationChance) {
            setBlockState(worldIn, mutable, config.hat)
            if (rand.nextFloat() < vineChance) {
                generateVines(mutable, worldIn, rand)
            }
        }
    }

    private fun generateStem(world: IWorld, random: Random, config: HugeFungusFeatureConfig, pos: BlockPos, stemHeight: Int, thickStem: Boolean) {
        val mutable = Mutable()
        val stem = config.stem
        val i = java.lang.Boolean.compare(thickStem, false) // faster than boolean ? 1 : 0

        for (j in -i..i) {
            for (k in -i..i) {
                val bl = thickStem && MathHelper.abs(j) == i && MathHelper.abs(k) == i
                for (l in 0 until stemHeight) {
                    mutable.setPos(pos.x + j, pos.y + l, pos.z + k)
                    if (canReplace(world, mutable)) {
                        if (config.planted) {
                            if (!world.getBlockState(mutable.down()).isAir) {
                                world.breakBlock(mutable, true, null)
                            }
                            world.setBlockState(mutable, stem, 3)
                        } else if (bl) {
                            if (random.nextFloat() < 0.1f) {
                                setBlockState(world, mutable, stem)
                            }
                        } else {
                            setBlockState(world, mutable, stem)
                        }
                    }
                }
            }
        }
    }
}