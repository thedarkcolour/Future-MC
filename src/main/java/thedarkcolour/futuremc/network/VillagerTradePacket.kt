package thedarkcolour.futuremc.network

import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.simpleimpl.IMessage

class VillagerTradePacket() : IMessage {
    var tradeIndex = 0

    constructor(tradeIndex: Int) : this() {
        this.tradeIndex = tradeIndex
    }

    override fun fromBytes(buf: ByteBuf) {
        tradeIndex = buf.readInt()
    }

    override fun toBytes(buf: ByteBuf) {
        buf.writeInt(tradeIndex)
    }
}