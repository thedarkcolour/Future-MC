package thedarkcolour.futuremc;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;
import thedarkcolour.core.command.CommandHeal;
import thedarkcolour.core.command.CommandModeToggle;
import thedarkcolour.core.gui.Gui;
import thedarkcolour.core.proxy.CommonProxy;
import thedarkcolour.core.util.RegistryHelper;
import thedarkcolour.futuremc.block.BlockFurnaceAdvanced;
import thedarkcolour.futuremc.entity.Entities;
import thedarkcolour.futuremc.entity.trident.EntityTrident;
import thedarkcolour.futuremc.init.FutureConfig;
import thedarkcolour.futuremc.init.Init;
import thedarkcolour.futuremc.init.InitElements;
import thedarkcolour.futuremc.item.ItemGroup;
import thedarkcolour.futuremc.recipe.StonecutterRecipes;
import thedarkcolour.futuremc.tile.TileCampfire;

import java.io.FileNotFoundException;
import java.io.FileReader;

@Mod(
        modid = FutureMC.ID,
        name = "Future MC",
        version = "0.1.13",
        dependencies = "required-after:forge@[14.23.5.2776,)", useMetadata = true
)
public class FutureMC {
    public static final String ID = "minecraftfuture";
    public static Logger logger;

    @SidedProxy(clientSide = "thedarkcolour.core.proxy.ClientProxy", serverSide = "thedarkcolour.core.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static FutureMC instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(FutureConfig.class);
        MinecraftForge.EVENT_BUS.register(InitElements.class);
        Entities.init();
        proxy.preInit(e);
        logger = e.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        Gui.setup();
        GameRegistry.addSmelting(new ItemStack(Blocks.STONE), new ItemStack(Init.SMOOTH_STONE), 0.1F);
        GameRegistry.addSmelting(new ItemStack(Blocks.QUARTZ_BLOCK), new ItemStack(Init.SMOOTH_QUARTZ), 0.1F);
        BlockFurnaceAdvanced.Recipes.init();
        TileCampfire.Recipes.init();
        StonecutterRecipes.addDefaults();
        if (FutureConfig.general.trident) {
            RegistryHelper.registerDispenserBehaviour(Init.TRIDENT, EntityTrident::new);
        }

        InitElements.registerGenerators();
        proxy.init(e);
    }

    @Mod.EventHandler
    public void onServerStart(final FMLServerStartingEvent event) {
        if (DEBUG) {
            event.registerServerCommand(new CommandHeal());
            event.registerServerCommand(new CommandModeToggle());
        }
    }

    public static final boolean DEBUG;
    public static final boolean CLIENT;
    public static ItemGroup TAB;

    static {
        boolean temp = false;

        try {
            new FileReader("debug_future_mc.txt");
        } catch (FileNotFoundException e) {
            temp = true;
        }
        DEBUG = !temp;

        try {
            Class.forName("net.minecraft.client.Minecraft");
            temp = true;
        } catch (ClassNotFoundException ignored) {
            temp = false;
        }
        CLIENT = temp;
    }
}