package thedarkcolour.futuremc.network

import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import thedarkcolour.futuremc.container.ContainerVillager

class VillagerPacketHandler : IMessageHandler<VillagerTradePacket, IMessage?> {
    override fun onMessage(message: VillagerTradePacket, ctx: MessageContext): IMessage? {
        val playerIn = ctx.serverHandler.player
        val tradeIndex = message.tradeIndex

        playerIn.serverWorld.addScheduledTask {
            val container = playerIn.openContainer

            if (container is ContainerVillager) {
                container.setTradeIndex(tradeIndex)
                container.moveAroundItems(tradeIndex)
            }
        }

        return null
    }
}