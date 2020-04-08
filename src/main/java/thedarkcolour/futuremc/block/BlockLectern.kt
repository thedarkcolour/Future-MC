package thedarkcolour.futuremc.block

import io.netty.buffer.Unpooled
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.server.SPacketCustomPayload
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import thedarkcolour.core.block.RotatableBlock
import thedarkcolour.futuremc.FutureMC.TAB
import thedarkcolour.futuremc.client.gui.GuiLectern
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.tile.TileLectern

class BlockLectern : RotatableBlock("Lectern", Material.WOOD), ITileEntityProvider {
    init {
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.REDSTONE else TAB
    }

    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity? {
        return TileLectern()
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
        val tile = worldIn.getTileEntity(pos)
        if (tile is TileLectern) {
            if (!tile.book.isEmpty) {
                openForPlayer(playerIn, hand, tile.book)
                return true
            }
        }
        val item = playerIn.getHeldItem(hand).item
        if (item == Items.WRITABLE_BOOK || item == Items.WRITTEN_BOOK) {
            playerIn.getHeldItem(hand).shrink(1)
        }
        return false
    }

    companion object {
        fun openForPlayer(player: EntityPlayer, hand: EnumHand, stack: ItemStack?) {
            if (player is EntityPlayerMP) {
                val buffer = PacketBuffer(Unpooled.buffer())
                buffer.writeEnumValue(hand)
                player.connection.sendPacket(SPacketCustomPayload("MC|BOpen", buffer))
            } else if (player is EntityPlayerSP) {
                Minecraft.getMinecraft().displayGuiScreen(GuiLectern(player, stack))
            }
        }
    }
}