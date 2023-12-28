package thedarkcolour.core.item

import net.minecraft.block.BlockHorizontal
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.World
import thedarkcolour.core.block.RotatableBlock
import thedarkcolour.futuremc.block.villagepillage.BlockBamboo
import thedarkcolour.futuremc.block.villagepillage.BlockBamboo.EnumLeaves
import thedarkcolour.futuremc.block.villagepillage.CampfireBlock
import thedarkcolour.futuremc.block.villagepillage.ComposterBlock
import thedarkcolour.futuremc.block.villagepillage.SweetBerryBushBlock
import thedarkcolour.futuremc.entity.bee.EntityBee
import thedarkcolour.futuremc.recipe.campfire.CampfireRecipes
import thedarkcolour.futuremc.registry.FBlocks.BAMBOO
import thedarkcolour.futuremc.registry.FBlocks.CAMPFIRE
import thedarkcolour.futuremc.registry.FBlocks.COMPOSTER
import thedarkcolour.futuremc.registry.FBlocks.SWEET_BERRY_BUSH
import thedarkcolour.futuremc.tile.BeeHiveTile
import thedarkcolour.futuremc.tile.CampfireTile
import thedarkcolour.futuremc.tile.TileComposter

class DebuggerItem : ModeledItem("debugger") {
    init {
        setMaxStackSize(1)
    }

    override fun onItemUse(
        player: EntityPlayer,
        worldIn: World,
        pos: BlockPos,
        hand: EnumHand,
        facing: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float
    ): EnumActionResult {
        val state = worldIn.getBlockState(pos)
        val block = state.block
        if (worldIn.getTileEntity(pos) != null) {
            player.sendMessage(TextComponentString("Tile entity exists: ${worldIn.getTileEntity(pos)!!.serializeNBT()}"))
        }
        if (worldIn.getTileEntity(pos) is BeeHiveTile) {
            val hive = worldIn.getTileEntity(pos) as BeeHiveTile
            if (!worldIn.isRemote) {
                if (player.isSneaking) {
                    hive.setHoneyLevel(5)
                } else {
                    player.sendMessage(TextComponentString("Bees: ${hive.getBeeCount()}, HoneyLevel: ${hive.honeyLevel}, " + pos))
                }
            }
        } else if (worldIn.getTileEntity(pos) is CampfireTile) {
            val campfire = worldIn.getTileEntity(pos) as CampfireTile?
            if (!worldIn.isRemote) {
                if (player.isSneaking) {
                    for (i in 0..3) {
                        player.sendMessage(
                            TextComponentString(
                                "Current: " + campfire!!.inventory.getStackInSlot(i).toString() + ", Result: " + CampfireRecipes.getRecipe(
                                    campfire.inventory.getStackInSlot(i)
                                )!!
                            )
                        )
                    }
                } else {
                    for (i in 0..3) {
                        player.sendMessage(TextComponentString("Progress: " + campfire!!.cookingTimes[i] + ", Total: " + campfire.cookingTotalTimes[i]))
                    }
                }
            }
        } else if (block == COMPOSTER) {
            if (player.isSneaking) {
                if (!ComposterBlock.isFull(state)) {
                    (worldIn.getTileEntity(pos) as TileComposter?)!!.addLayer()
                }
            } else {
                if (!worldIn.isRemote) {
                    player.sendStatusMessage(
                        TextComponentString(
                            "Layers: " + worldIn.getBlockState(pos).getValue(
                                ComposterBlock.LEVEL
                            )
                        ), true
                    )
                }
            }
        } else if (block is RotatableBlock || state.propertyKeys.contains(BlockHorizontal.FACING)) {
            var i = worldIn.getBlockState(pos).getValue(BlockHorizontal.FACING).horizontalIndex
            if (i == 3) {
                i = 0
            } else {
                ++i
            }
            worldIn.setBlockState(
                pos,
                worldIn.getBlockState(pos).withProperty(BlockHorizontal.FACING, EnumFacing.byHorizontalIndex(i))
            )
        } else if (block == CAMPFIRE) {
            worldIn.setBlockState(pos, state.withProperty(CampfireBlock.LIT, !state.getValue(CampfireBlock.LIT)))
        } else if (block == BAMBOO) {
            if (player.isSneaking) {
                worldIn.setBlockState(pos, state.withProperty(BlockBamboo.THICK, !state.getValue(BlockBamboo.THICK)))
            } else {
                var i = state.getValue(BlockBamboo.LEAVES).ordinal
                if (i > 2) {
                    i = 0
                } else {
                    ++i
                }
                worldIn.setBlockState(pos, state.withProperty(BlockBamboo.LEAVES, EnumLeaves.values()[i]))
            }
        } else if (block == SWEET_BERRY_BUSH) {
            var i = state.getValue(SweetBerryBushBlock.AGE)
            if (i > 3) {
                i = 0
            } else {
                ++i
            }
            worldIn.setBlockState(pos, state.withProperty(SweetBerryBushBlock.AGE, i))
        } else {
            return EnumActionResult.FAIL
        }
        return EnumActionResult.SUCCESS
    }

    override fun itemInteractionForEntity(
        stack: ItemStack,
        playerIn: EntityPlayer,
        target: EntityLivingBase,
        hand: EnumHand
    ): Boolean {
        if (target is EntityBee && !playerIn.world.isRemote) {
            playerIn.sendMessage(TextComponentString("Hive: " + target.hivePos + ", Flower: " + target.flowerPos))
        }
        return false
    }
}