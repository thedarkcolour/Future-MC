package com.herobrine.future;

import com.herobrine.future.blocks.futureBlocks;
import com.herobrine.future.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = com.herobrine.future.futurejava.MODID,
        name = com.herobrine.future.futurejava.MODNAME,
        version = com.herobrine.future.futurejava.VERSION,
        dependencies = "required-after:forge@[14.23.5.2768,)", useMetadata = true
)

public class futurejava {

    public static final String MODID = "minecraftfuture";
    public static final String MODNAME = "Minecraft Future";
    public static final String VERSION = "0.0.1";

    @SidedProxy(clientSide = "com.herobrine.future.proxy.ClientProxy",
                serverSide = "com.herobrine.future.proxy.ServerProxy")
    public static CommonProxy proxy;

    public static CreativeTabs futuretab = new CreativeTabs("Future") {
        @Override    //New creative tab
        public ItemStack getTabIconItem() {
            return new ItemStack(futureBlocks.lantern);
        }
    };

    @Mod.Instance
    public static futurejava instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}
