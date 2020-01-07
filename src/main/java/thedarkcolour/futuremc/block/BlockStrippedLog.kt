package thedarkcolour.futuremc.block

import net.minecraft.block.BlockLog
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraftforge.client.model.ModelLoader
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.FutureMC.TAB
import thedarkcolour.futuremc.config.FConfig
import java.util.*

class BlockStrippedLog(variant: String) : BlockLog() {
    fun model() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, ModelResourceLocation(Item.getItemFromBlock(this).registryName, "inventory"))
    }

    override fun getSubBlocks(itemIn: CreativeTabs, items: NonNullList<ItemStack>) {
        items.add(ItemStack(this))
    }

    override fun isFlammable(world: IBlockAccess, pos: BlockPos, face: EnumFacing): Boolean {
        return true
    }

    override fun getFlammability(world: IBlockAccess, pos: BlockPos, face: EnumFacing): Int {
        return 5
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        var state = this.defaultState
        state = when (meta and 12) {
            0 -> state.withProperty(LOG_AXIS, EnumAxis.Y)
            4 -> state.withProperty(LOG_AXIS, EnumAxis.X)
            8 -> state.withProperty(LOG_AXIS, EnumAxis.Z)
            else -> state.withProperty(LOG_AXIS, EnumAxis.NONE)
        }
        return state
    }

    override fun getMetaFromState(state: IBlockState): Int {
        var meta = 0
        when (state.getValue(LOG_AXIS)) {
            EnumAxis.X -> meta = meta or 4
            EnumAxis.Z -> meta = meta or 8
            EnumAxis.NONE -> meta = meta or 12
        }
        return meta
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, LOG_AXIS)
    }

    companion object {
        @kotlin.jvm.JvmField
        var variants = Arrays.asList("acacia", "jungle", "birch", "oak", "spruce", "dark_oak")
    }

    init {
        translationKey = FutureMC.ID + ".stripped_" + variant + "_log"
        setRegistryName("stripped_" + variant + "_log")
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.BUILDING_BLOCKS else TAB
        this.defaultState = getBlockState().baseState.withProperty(LOG_AXIS, EnumAxis.Y)
    }
}