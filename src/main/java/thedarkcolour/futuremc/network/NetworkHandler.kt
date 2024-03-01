package thedarkcolour.futuremc.network

import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.world.GameType
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.relauncher.Side
import thedarkcolour.futuremc.FutureMC

object NetworkHandler {
    val INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(FutureMC.ID)

    fun registerPackets() {
        INSTANCE.registerMessage(VillagerPacketHandler::class.java, VillagerTradePacket::class.java, 0, Side.SERVER)
        INSTANCE.registerMessage(GameModeSwitchPacket.Handler::class.java, GameModeSwitchPacket::class.java, 1, Side.SERVER)
        INSTANCE.registerMessage(SetPrevGameModePacket.Handler::class.java, SetPrevGameModePacket::class.java, 2, Side.CLIENT)
    }

    fun sendVillagerPacket(tradeIndex: Int) {
        INSTANCE.sendToServer(VillagerTradePacket(tradeIndex))
    }

    fun sendGameModeSwitch(prevGameMode: GameType) {
        INSTANCE.sendToServer(GameModeSwitchPacket(prevGameMode))
    }

    fun sendSetPrevGameMode(player: EntityPlayerMP, gameType: GameType) {
        if (player.connection != null) {
            INSTANCE.sendTo(SetPrevGameModePacket(gameType), player)
        }
    }
}