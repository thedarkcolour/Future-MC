package thedarkcolour.futuremc.network

import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.world.GameType
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import thedarkcolour.futuremc.client.ClientEvents

class SetPrevGameModePacket() : IMessage {
    var gameType: GameType = GameType.NOT_SET

    constructor(gameType: GameType) : this() {
        this.gameType = gameType
    }

    override fun fromBytes(buf: ByteBuf) {
        val b = buf.readByte().toInt()
        if (b in -1..3) {
            gameType = GameType.getByID(b)
        }
    }

    override fun toBytes(buf: ByteBuf) {
        buf.writeByte(gameType.id)
    }

    class Handler : IMessageHandler<SetPrevGameModePacket, IMessage?> {
        override fun onMessage(message: SetPrevGameModePacket, ctx: MessageContext): IMessage? {
            ClientEvents.prevGameMode = message.gameType

            return null
        }
    }
}