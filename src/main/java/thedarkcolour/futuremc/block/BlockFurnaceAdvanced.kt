package thedarkcolour.futuremc.block

import net.minecraft.block.BlockHorizontal.FACING
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.inventory.InventoryHelper
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.block.FBlock
import thedarkcolour.core.gui.Gui
import thedarkcolour.futuremc.recipe.furnace.BlastFurnaceRecipes
import thedarkcolour.futuremc.recipe.furnace.SmokerRecipes
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.tile.TileFurnaceAdvanced
import thedarkcolour.futuremc.tile.TileFurnaceAdvanced.TileBlastFurnace
import thedarkcolour.futuremc.tile.TileFurnaceAdvanced.TileSmoker
import java.util.*

class BlockFurnaceAdvanced(private val type: FurnaceType, properties: Properties) : FBlock(properties), ITileEntityProvider {
    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity? {
        if (type == FurnaceType.SMOKER) {
            return TileSmoker()
        }
        return if (type == FurnaceType.BLAST_FURNACE) {
            TileBlastFurnace()
        } else null
    }

    override fun getLightValue(state: IBlockState): Int {
        return if (state.getValue(LIT)) 13 else 0
    }

    override fun onBlockActivated(
        worldIn: World,
        pos: BlockPos,
        state: IBlockState,
        playerIn: EntityPlayer,
        hand: EnumHand,
        facing: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float
    ): Boolean {
        if (worldIn.isRemote) {
            return true
        }
        val te = worldIn.getTileEntity(pos) as? TileFurnaceAdvanced ?: return false
        val block = worldIn.getBlockState(pos).block
        if (block == FBlocks.BLAST_FURNACE) {
            if (te !is TileBlastFurnace) {
                return false
            }
        }
        if (block == FBlocks.SMOKER) {
            if (te !is TileSmoker) {
                return false
            }
        }
        Gui.FURNACE.open(playerIn, worldIn, pos)
        return true
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, FACING, LIT)
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState
            .withProperty(LIT, meta and 4 != 0)
            .withProperty(FACING, EnumFacing.byHorizontalIndex(meta and -5))
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return (if (state.getValue(LIT)) 4 else 0) or state.getValue(FACING).horizontalIndex
    }

    override fun getStateForPlacement(
        worldIn: World,
        pos: BlockPos,
        facing: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float,
        meta: Int,
        placer: EntityLivingBase
    ): IBlockState {
        return defaultState.withProperty(FACING, placer.horizontalFacing.opposite).withProperty(LIT, false)
    }

    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
        val tile = worldIn.getTileEntity(pos)
        if (tile is TileFurnaceAdvanced) {
            InventoryHelper.dropInventoryItems(worldIn, pos, tile)
        }
        worldIn.removeTileEntity(pos)
    }

    override fun randomDisplayTick(stateIn: IBlockState, worldIn: World, pos: BlockPos, rand: Random) {
        if (stateIn.getValue(LIT)) {
            val facing = stateIn.getValue(FACING)
            val d0 = pos.x.toDouble() + 0.5
            val d1 = pos.y.toDouble() + rand.nextDouble() * 6.0 / 16.0
            val d2 = pos.z.toDouble() + 0.5
            val d3 = rand.nextDouble() * 0.6 - 0.3
            if (rand.nextDouble() < 0.1) {
                worldIn.playSound(
                    pos.x.toDouble() + 0.5,
                    pos.y.toDouble(),
                    pos.z.toDouble() + 0.5,
                    SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE,
                    SoundCategory.BLOCKS,
                    1.0f,
                    1.0f,
                    false
                )
            }
            if (worldIn.getBlockState(pos).block == FBlocks.SMOKER) {
                when (facing) {
                    EnumFacing.WEST -> {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.52, d1, d2 + d3, 0.0, 0.0, 0.0)
                        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - 0.52, d1, d2 + d3, 0.0, 0.0, 0.0)
                    }
                    EnumFacing.EAST -> {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.52, d1, d2 + d3, 0.0, 0.0, 0.0)
                        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.52, d1, d2 + d3, 0.0, 0.0, 0.0)
                    }
                    EnumFacing.NORTH -> {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 - 0.52, 0.0, 0.0, 0.0)
                        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 - 0.52, 0.0, 0.0, 0.0)
                    }
                    EnumFacing.SOUTH -> {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + 0.52, 0.0, 0.0, 0.0)
                        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + 0.52, 0.0, 0.0, 0.0)
                    }
                    else -> {
                    }
                }
            }
        }
    }

    enum class FurnaceType {
        SMOKER, BLAST_FURNACE;

        val type = name.toLowerCase()

        fun canCraft(stack: ItemStack): Boolean {
            return if (this == BLAST_FURNACE) {
                BlastFurnaceRecipes.getRecipe(stack) != null
            } else {
                SmokerRecipes.getRecipe(stack) != null
            }
        }

        fun getOutput(stack: ItemStack): ItemStack? {
            return if (this == BLAST_FURNACE) {
                BlastFurnaceRecipes.getRecipe(stack)?.output
            } else {
                SmokerRecipes.getRecipe(stack)?.output
            }
        }

    }

    companion object {
        val LIT: PropertyBool = PropertyBool.create("lit")
    }
}