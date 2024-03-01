package thedarkcolour.futuremc.network

import io.netty.buffer.ByteBuf
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.GameType
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import org.apache.commons.lang3.mutable.MutableBoolean

class GameModeSwitchPacket() : IMessage {
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

    class Handler : IMessageHandler<GameModeSwitchPacket, IMessage?> {
        override fun onMessage(message: GameModeSwitchPacket, ctx: MessageContext): IMessage? {
            val playerIn = ctx.serverHandler.player

            if (message.gameType != GameType.NOT_SET && playerIn.canUseCommand(2, "gamemode")) {
                playerIn.setGameType(message.gameType)
                val text = TextComponentTranslation("gameMode." + message.gameType.getName(), *arrayOfNulls<Any>(0))

                if (playerIn.world.gameRules.getBoolean("sendCommandFeedback")) {
                    playerIn.sendMessage(TextComponentTranslation("gameMode.changed", text))
                }
            }

            return null
        }
    }

    companion object {
        val state = MutableBoolean(true)
    }
}