package com.herobrine.future;

import com.herobrine.future.blocks.*;
import com.herobrine.future.config.FutureConfig;
import com.herobrine.future.entity.Entities;
import com.herobrine.future.items.*;
import com.herobrine.future.tile.GuiHandler;
import com.herobrine.future.tile.advancedfurnace.TileEntityBlastFurnace;
import com.herobrine.future.tile.advancedfurnace.TileEntitySmoker;
import com.herobrine.future.tile.barrel.TileEntityBarrel;
import com.herobrine.future.utils.proxy.IProxy;
import com.herobrine.future.utils.proxy.Init;
import com.herobrine.future.worldgen.NewWorldGenFlower;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = FutureJava.MODID,
        name = FutureJava.MODNAME,
        version = FutureJava.VERSION,
        dependencies = "required-after:forge@[14.23.5.2768,)", useMetadata = true
)

public class FutureJava {
    public static final String MODID = "minecraftfuture";
    static final String MODNAME = "Future MC";
    static final String VERSION = "0.1.1";

    @SidedProxy(clientSide = "com.herobrine.future.utils.proxy.ClientProxy",
                serverSide = "com.herobrine.future.utils.proxy.ServerProxy")
    public static IProxy proxy;

    public static FutureConfig config;

    @Mod.Instance
    public static FutureJava instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        logger = e.getModLog();
        MinecraftForge.EVENT_BUS.register(FutureConfig.class);
        if (FutureConfig.a.flintandsteelpatch) ForgeRegistries.ITEMS.register(new FlintAndSteelPatch()); // patches flint and steel
        Entities.init(); // Registers entity for itemTrident
        Init.init(); // Adds log variants

        //PacketHandler.registerMessages(); // Adds packets

        proxy.preInit(e); // Calls ClientProxy
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(FutureJava.instance, new GuiHandler());
        OreDict.registerOres();
        registerGenerators();
        ConfigManager.sync(MODID, Config.Type.INSTANCE);
        GameRegistry.addSmelting(new ItemStack(Blocks.STONE), new ItemStack(Init.smoothstone), 0.1F);
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

    public void registerGenerators() {
        if(FutureConfig.b.lilygen) {
            GameRegistry.registerWorldGenerator(new NewWorldGenFlower(Init.flowerwhite), 0);
        }

        if(FutureConfig.b.cornflowergen) {
            GameRegistry.registerWorldGenerator(new NewWorldGenFlower(Init.flowerblue), 0);
        }

        if(FutureConfig.a.berrybush && FutureConfig.a.berrybushgen) {
            GameRegistry.registerWorldGenerator(new NewWorldGenFlower(Init.berrybush), 0);
        }
    }

    @Mod.EventBusSubscriber
    public static class StripLogEvent {
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void stripLogEvent(PlayerInteractEvent.RightClickBlock event) {
            World world = event.getWorld();
            BlockPos pos = event.getPos();
            EntityPlayer player = event.getEntityPlayer();
            ItemStack stack = event.getItemStack();

            if (FutureConfig.a.strippedlogs) {
                if (stack.getItem() instanceof ItemTool) {
                    ItemTool tool = (ItemTool) stack.getItem();
                    if (tool.getToolClasses(stack).contains("axe")) {
                        IBlockState state = world.getBlockState(pos);
                        Block block = state.getBlock();

                        if (block == Blocks.LOG || block == Blocks.LOG2) {
                            IProperty axis = null, variant = null;
                            for (IProperty<?> prop : state.getProperties().keySet()) {
                                if (prop.getName().equals("axis")) {
                                    axis = prop;
                                }
                                if (prop.getName().equals("variant")) {
                                    variant = prop;
                                }
                            }
                            if (axis != null && variant != null) {
                                if (Init.variantsLog.contains(state.getValue(variant).toString())) {
                                    int i = Init.variantsLog.indexOf(state.getValue(variant).toString());
                                    player.swingArm(event.getHand());
                                    world.playSound(player, pos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
                                    world.setBlockState(pos, Init.strippedLogs.get(i).getDefaultState().withProperty(axis, state.getValue(axis)), 0b1011);
                                    stack.damageItem(1, player);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Mod.EventBusSubscriber
    public static class InitElements {
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {    //registers blocks
            if (FutureConfig.a.lantern) event.getRegistry().register(new Lantern());
            if (FutureConfig.a.barrel) event.getRegistry().register(new Barrel());
            if (FutureConfig.b.cornflower) event.getRegistry().register(new FlowerBlue());
            if (FutureConfig.b.lily) event.getRegistry().register(new FlowerWhite());
            if (FutureConfig.b.witherrose) event.getRegistry().register(new FlowerBlack());
            if (FutureConfig.a.berrybush) event.getRegistry().register(new BerryBush());
            if (FutureConfig.a.loom) event.getRegistry().register(new Loom());
            if (FutureConfig.a.stonecutter) event.getRegistry().register(new StonecutterBase());
            if (FutureConfig.a.barrel) GameRegistry.registerTileEntity(TileEntityBarrel.class,  new ResourceLocation(MODID + ":containerbarrel"));
            if (FutureConfig.a.smoker) GameRegistry.registerTileEntity(TileEntitySmoker.class,  new ResourceLocation(MODID + ":containersmoker"));
            if (FutureConfig.a.blastfurnace) GameRegistry.registerTileEntity(TileEntityBlastFurnace.class, new ResourceLocation(MODID + ":containerblastfurnace"));
            if (FutureConfig.a.campfire) event.getRegistry().register(new Campfire());
            if (FutureConfig.a.smoothstone) event.getRegistry().register(new SmoothStone());
        }

        @SuppressWarnings("ConstantConditions")
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {  //registers items
            if (FutureConfig.a.trident) event.getRegistry().register(new ItemTrident());
            if (FutureConfig.a.lantern) event.getRegistry().register(new ItemBlock(Init.lantern).setRegistryName(Init.lantern.getRegistryName()));
            if (FutureConfig.b.cornflower) event.getRegistry().register(new ItemBlock(Init.flowerblue).setRegistryName(Init.flowerblue.getRegistryName()));
            if (FutureConfig.b.lily) event.getRegistry().register(new ItemBlock(Init.flowerwhite).setRegistryName(Init.flowerwhite.getRegistryName()));
            if (FutureConfig.b.witherrose) event.getRegistry().register(new ItemBlock(Init.flowerblack).setRegistryName(Init.flowerblack.getRegistryName()));
            if (FutureConfig.b.suspiciousstew) event.getRegistry().register(new ItemSuspiciousStew("SuspiciousStew", 6, 0.6F, false));
            if (FutureConfig.a.sweetberry && FutureConfig.a.berrybush) event.getRegistry().register(new ItemBerry(2, 0.2F, false));
            if (FutureConfig.b.dyes) event.getRegistry().register(new ItemDye());
            if (FutureConfig.a.stonecutter) event.getRegistry().register(new ItemBlock(Init.stonecutter).setRegistryName(Init.stonecutter.getRegistryName()));
            if (FutureConfig.a.loom) event.getRegistry().register(new ItemBlock(Init.loom).setRegistryName(Init.loom.getRegistryName()));
            if (FutureConfig.a.barrel) event.getRegistry().register(new ItemBlock(Init.barrel).setRegistryName(Init.barrel.getRegistryName()));
            if (FutureConfig.a.campfire) event.getRegistry().register(new ItemBlock(Init.campfire).setRegistryName(Init.campfire.getRegistryName()));
            if (FutureConfig.a.smoothstone) event.getRegistry().register(new ItemBlock(Init.smoothstone).setRegistryName(Init.smoothstone.getRegistryName()));
        }

        @SubscribeEvent
        public static void registerSpecialBlocks(RegistryEvent.Register<Block> event) {
            if (FutureConfig.a.strippedlogs) {
                for (Block block : Init.strippedLogs) {
                    event.getRegistry().register(block);
                }
            }

            if (FutureConfig.a.newwallvariants) {
                for (Block block : Init.newwall) {
                    event.getRegistry().register(block);
                }
            }

            if (FutureConfig.a.smoker) {
                event.getRegistry().register(Init.advancedFurnace.get(0));
            }
            if (FutureConfig.a.blastfurnace) {
                event.getRegistry().register(Init.advancedFurnace.get(1));
            }
        }

        @SubscribeEvent
        public static void registerSpecialItems(RegistryEvent.Register<Item> event) {
            if (FutureConfig.a.strippedlogs) {
                for (Block block : Init.strippedLogs) {
                    event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
                }
            }

            if (FutureConfig.a.newwallvariants) {
                for (Block block : Init.newwall) {
                    event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
                }
            }

            if (FutureConfig.a.smoker) {
                event.getRegistry().register(new ItemBlock(Init.advancedFurnace.get(0)).setRegistryName(Init.advancedFurnace.get(0).getRegistryName()));
            }

            if (FutureConfig.a.blastfurnace) {
                event.getRegistry().register(new ItemBlock(Init.advancedFurnace.get(1)).setRegistryName(Init.advancedFurnace.get(1).getRegistryName()));
            }
        }
    }
}