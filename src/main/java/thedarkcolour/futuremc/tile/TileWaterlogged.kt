package thedarkcolour.futuremc.tile

import net.minecraft.block.Block
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraftforge.fluids.Fluid
import thedarkcolour.core.tile.InteractionTile
import thedarkcolour.futuremc.block.AlternateBlock

class TileWaterlogged(var fluid: Fluid, val block: Block) : InteractionTile() {
    init {
        // block must implement AlternateBlock
        assert(block is AlternateBlock) { "Waterlogged blocks must implement AlternateBlock" }
    }
    override fun getUpdatePacket(): SPacketUpdateTileEntity? {
        return SPacketUpdateTileEntity(pos, 13, writeToNBT(NBTTagCompound()))
    }

    override fun onDataPacket(net: NetworkManager, pkt: SPacketUpdateTileEntity) {
        readFromNBT(pkt.nbtCompound)
    }
}