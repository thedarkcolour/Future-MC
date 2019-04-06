package com.herobrine.future;

import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.entity.Entities;
import com.herobrine.future.init.Init;
import com.herobrine.future.items.OreDict;
import com.herobrine.future.proxy.IProxy;
import com.herobrine.future.tile.GuiHandler;
import com.herobrine.future.worldgen.NewWorldGenFlower;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = Init.MODID,
        name = MainFuture.MODNAME,
        version = MainFuture.VERSION,
        dependencies = "required-after:forge@[14.23.5.2796,)", useMetadata = true
)
public class MainFuture {
    public static final String MODNAME = "Future MC";
    public static final String VERSION = "0.1.2";
    public static Logger logger;

    @SidedProxy(clientSide = "com.herobrine.future.proxy.ClientProxy", serverSide = "com.herobrine.future.proxy.ServerProxy")
    public static IProxy proxy;

    @Mod.Instance
    public static MainFuture instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(FutureConfig.class);
        Entities.init();
        proxy.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        GameRegistry.addSmelting(new ItemStack(Blocks.STONE), new ItemStack(Init.SMOOTH_STONE), 0.1F);
        OreDict.registerOres();
        registerGenerators();
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

    public void registerGenerators() {
        if(FutureConfig.modFlowers.lily && FutureConfig.modFlowers.lilyGen) {
            GameRegistry.registerWorldGenerator(new NewWorldGenFlower(Init.LILY_OF_VALLEY), 0);
        }

        if(FutureConfig.modFlowers.cornflower && FutureConfig.modFlowers.cornflowerGen) {
            GameRegistry.registerWorldGenerator(new NewWorldGenFlower(Init.CORNFLOWER), 0);
        }

        if(FutureConfig.general.berryBush && FutureConfig.general.berryBushGen) {
            GameRegistry.registerWorldGenerator(new NewWorldGenFlower(Init.BERRY_BUSH), 0);
        }
    }


}