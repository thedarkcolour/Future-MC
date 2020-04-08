package thedarkcolour.futuremc.block

import net.minecraft.block.BlockPurpurSlab
import net.minecraft.block.BlockSlab
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import thedarkcolour.futuremc.FutureMC
import java.util.*

abstract class BlockNewSlab : BlockSlab(Material.ROCK) {
    protected abstract val slab: Item

    init {
        blockHardness = 2.0F
    }

    override fun createBlockState(): BlockStateContainer {
        return if (isDouble) {
            BlockStateContainer(this, BlockPurpurSlab.VARIANT)
        } else {
            BlockStateContainer(this, HALF, BlockPurpurSlab.VARIANT)
        }
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return if (isDouble) {
            defaultState
        } else {
            getBlockState().baseState.withProperty(HALF, if (meta == 1) EnumBlockHalf.TOP else EnumBlockHalf.BOTTOM)
        }
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return if (isDouble) 0 else if (state.getValue(HALF) == EnumBlockHalf.TOP) 1 else 0
    }

    override fun getPickBlock(
        state: IBlockState,
        target: RayTraceResult,
        world: World,
        pos: BlockPos,
        player: EntityPlayer
    ): ItemStack {
        return ItemStack(slab)
    }

    override fun getVariantProperty(): IProperty<*> = BlockPurpurSlab.VARIANT

    override fun getTypeForItem(stack: ItemStack): Comparable<*> = BlockPurpurSlab.Variant.DEFAULT

    override fun isDouble(): Boolean = this is BlockDoubleSlab

    override fun getTranslationKey(meta: Int): String {
        return super.getTranslationKey()
    }

    class Half(variant: String) : BlockNewSlab() {
        override val slab: Item
            get() = Item.getItemFromBlock(this)

        init {
            translationKey = FutureMC.ID + "." + variant + "_slab"
            setRegistryName(variant + "_slab")
            setLightOpacity(0)
        }

        override fun isOpaqueCube(state: IBlockState): Boolean = false
    }

    class BlockDoubleSlab(val variant: String) : BlockNewSlab() {
        override val slab: Item
            get() = Item.getItemFromBlock(this)

        init {
            translationKey = "${FutureMC.ID}.${variant}_double_slab"
            setRegistryName("${variant}_double_slab")
        }

        override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item = slab

        override fun quantityDropped(random: Random): Int = 2
    }
}