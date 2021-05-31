package thedarkcolour.futuremc.network

import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.relauncher.Side
import thedarkcolour.futuremc.FutureMC

object NetworkHandler {
    val INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(FutureMC.ID)

    fun registerPackets() {
        INSTANCE.registerMessage(VillagerPacketHandler::class.java, VillagerTradePacket::class.java, 0, Side.SERVER)
    }

    fun sendVillagerPacket(tradeIndex: Int) {
        INSTANCE.sendToServer(VillagerTradePacket(tradeIndex))
    }
}