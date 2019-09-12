package com.herobrine.future.init.proxy;

import com.herobrine.future.init.FutureConfig;
import com.herobrine.future.init.Init;
import net.minecraft.tileentity.BannerPattern;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {}

    public void init(FMLInitializationEvent e) {
        if(FutureConfig.general.loom && Init.isDebug) {
            Class<?>[] params = {String.class, String.class};
            EnumHelper.addEnum(BannerPattern.class, "GLOBE", params, "globe", "glo");
        }
    }

    public void postInit(FMLPostInitializationEvent e) {}
}