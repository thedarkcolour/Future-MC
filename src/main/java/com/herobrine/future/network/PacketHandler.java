package com.herobrine.future.network;

import com.herobrine.future.FutureJava;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public PacketHandler() {
    }

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(FutureJava.MODID);

    public static void registerMessages() {
        INSTANCE.registerMessage(ButtonMessage.Handler.class, ButtonMessage.class, 0, Side.SERVER);
    }
}
