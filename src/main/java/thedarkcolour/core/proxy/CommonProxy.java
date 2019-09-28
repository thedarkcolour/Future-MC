package thedarkcolour.core.proxy;

import net.minecraft.tileentity.BannerPattern;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import thedarkcolour.futuremc.init.FutureConfig;
import thedarkcolour.futuremc.init.Init;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {}

    public void init(FMLInitializationEvent e) {
        if (FutureConfig.general.loom && Init.isDebug) {
            Class<?>[] params = {String.class, String.class};
            EnumHelper.addEnum(BannerPattern.class, "GLOBE", params, "globe", "glo");
        }
    }
}