package com.herobrine.future.network;

import com.herobrine.future.tile.stonecutter.ContainerStonecutter;
import com.herobrine.future.tile.stonecutter.TileEntityStonecutter;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ButtonMessage implements IMessage {
    public ButtonMessage() {
    }

    private int selectedButton;
    //private BlockPos pos;

    @Override
    public void fromBytes(ByteBuf buf) {
        selectedButton = buf.readInt();
        //pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(selectedButton);
        //buf.writeLong(pos.toLong());
    }

    public ButtonMessage(int buttonID) {
        selectedButton = buttonID;
    }

    public static class Handler implements IMessageHandler<ButtonMessage, IMessage> {
        @Override
        public IMessage onMessage(ButtonMessage message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(ButtonMessage message, MessageContext ctx) {
            EntityPlayerMP playerEntity = ctx.getServerHandler().player;
            World world = playerEntity.getEntityWorld();
            Container container = playerEntity.openContainer;
            BlockPos pos = ((ContainerStonecutter) container).te.getPos();

            if(world.isBlockLoaded(pos)) {
                if(world.getTileEntity(pos) instanceof TileEntityStonecutter) {
                    TileEntityStonecutter te = (TileEntityStonecutter) world.getTileEntity(pos);

                    te.setSelectedID(message.selectedButton);
                }
            }
        }
    }
}
