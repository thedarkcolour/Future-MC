package thedarkcolour.futuremc;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;
import thedarkcolour.core.gui.Gui;
import thedarkcolour.core.proxy.CommonProxy;
import thedarkcolour.core.util.RegistryHelper;
import thedarkcolour.futuremc.compat.oredict.OreDict;
import thedarkcolour.futuremc.entity.Entities;
import thedarkcolour.futuremc.entity.trident.EntityTrident;
import thedarkcolour.futuremc.init.FutureConfig;
import thedarkcolour.futuremc.init.Init;
import thedarkcolour.futuremc.init.InitElements;
import thedarkcolour.futuremc.item.ItemGroup;
import thedarkcolour.futuremc.tile.TileCampfire;

@Mod(
        modid = FutureMC.ID,
        name = "Future MC",
        version = "0.1.11",
        dependencies = "required-after:forge@[14.23.5.2776,)", useMetadata = true
)
public class FutureMC {
    public static final String ID = "minecraftfuture";
    public static Logger LOGGER;

    @SidedProxy(clientSide = "thedarkcolour.core.proxy.ClientProxy", serverSide = "thedarkcolour.core.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static FutureMC instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(FutureConfig.class);
        Entities.init();
        proxy.preInit(e);
        LOGGER = e.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        Gui.setup();
        GameRegistry.addSmelting(new ItemStack(Blocks.STONE), new ItemStack(Init.SMOOTH_STONE), 0.1F);
        GameRegistry.addSmelting(new ItemStack(Blocks.QUARTZ_BLOCK), new ItemStack(Init.SMOOTH_QUARTZ), 0.1F);
        TileCampfire.Recipes.registerDefaults();

        if (FutureConfig.general.trident) {
            RegistryHelper.registerDispenserBehaviour(Init.TRIDENT, EntityTrident::new);
        }

        OreDict.registerOres();
        InitElements.registerGenerators();
        proxy.init(e);
    }

    public static ItemGroup TAB;
}