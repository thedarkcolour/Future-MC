package com.herobrine.future.utils;

import com.herobrine.future.blocks.*;
import com.herobrine.future.blocks.tile.TileEntityBarrel;
import com.herobrine.future.blocks.tile.TileEntityStonecutter;
import com.herobrine.future.items.*;
import com.herobrine.future.utils.worldgen.WorldGenFlower;
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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;

@Mod.EventBusSubscriber
public class CommonProxy {
    public static Configuration config;
    public static Configuration developer_config;

    public static void preInit(FMLPreInitializationEvent e) {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "minecraftfuture.cfg"));
        developer_config = new Configuration(new File(directory.getPath(), "minecraftfuturedeveloper.cfg"));
        Config.readConfig();
        Init.init();
    }
    public static void init(FMLInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(FutureJava.instance, new GuiHandler());
        GameRegistry.registerWorldGenerator(new WorldGenFlower(), 0);
    }
    public static void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
                config.save();
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

            if (Config.striplog) {
                if (stack.func_77973_b() instanceof ItemTool) {
                    ItemTool tool = (ItemTool) stack.func_77973_b();
                    if (tool.getToolClasses(stack).contains("axe")) {
                        IBlockState state = world.func_180495_p(pos);
                        Block block = state.func_177230_c();

                        if (block == Blocks.field_150364_r || block == Blocks.field_150363_s) {
                            IProperty axis = null, variant = null;
                            for (IProperty<?> prop : state.func_177228_b().keySet()) {
                                if (prop.func_177701_a().equals("axis")) {
                                    axis = prop;
                                }
                                if (prop.func_177701_a().equals("variant")) {
                                    variant = prop;
                                }
                            }
                            if (axis != null && variant != null) {
                                if (Init.variantsLog.contains(state.func_177229_b(variant).toString())) {
                                    int i = Init.variantsLog.indexOf(state.func_177229_b(variant).toString());
                                    player.func_184609_a(event.getHand());
                                    world.func_184133_a(player, pos, SoundEvents.field_187881_gQ, SoundCategory.BLOCKS, 1.0F, 1.0F);
                                    world.func_180501_a(pos, Init.strippedLogs.get(i).func_176223_P().func_177226_a(axis, state.func_177229_b(axis)), 0b1011);
                                    stack.func_77972_a(1, player);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {    //registers tile
        if (Config.lant) event.getRegistry().register(new Lantern());
        if (Config.stonec) event.getRegistry().register(new Stonecutter());
        if (Config.barl) event.getRegistry().register(new Barrel());
        if (Config.bluef) event.getRegistry().register(new FlowerBlue());
        if (Config.lily) event.getRegistry().register(new FlowerWhite());
        if (Config.wrose) event.getRegistry().register(new FlowerBlack());
        if (Config.berrybush) event.getRegistry().register(new BerryBush());
        if (Config.loom) event.getRegistry().register(new Loom());
        if (Config.barl) GameRegistry.registerTileEntity(TileEntityBarrel.class, Init.MODID + ":containerbarrel");
        if (Config.stonec) GameRegistry.registerTileEntity(TileEntityStonecutter.class, Init.MODID + ":containerstonecutter");
        if (Config.campfire) event.getRegistry().register(new Campfire());

    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {  //registers items
        if (Config.trident) event.getRegistry().register(new Trident("Trident", Init.TRIDENT, Init.futuretab));
        if (Config.lant) event.getRegistry().register(new ItemBlock(Init.lantern).setRegistryName(Init.lantern.getRegistryName()));
        if (Config.bluef) event.getRegistry().register(new ItemBlock(Init.flowerblue).setRegistryName(Init.flowerblue.getRegistryName()));
        if (Config.lily) event.getRegistry().register(new ItemBlock(Init.flowerwhite).setRegistryName(Init.flowerwhite.getRegistryName()));
        if (Config.wrose) event.getRegistry().register(new ItemBlock(Init.flowerblack).setRegistryName(Init.flowerblack.getRegistryName()));
        if (Config.susstew) event.getRegistry().register(new SuspiciousStew("SuspiciousStew", 6, 0.6F, false));
        if (Config.berrybush) event.getRegistry().register(new ItemBerry(2, 0.2F, false));
        if (Config.dyes && Config.dyeb) event.getRegistry().register(new DyeBlue());
        if (Config.dyes && Config.dyew) event.getRegistry().register(new DyeWhite());
        if (Config.dyes && Config.dyebr) event.getRegistry().register(new DyeBrown());
        if (Config.dyes && Config.dyebk) event.getRegistry().register(new DyeBlack());
        if (Config.stonec) event.getRegistry().register(new ItemBlock(Init.stonecutter).setRegistryName(Init.stonecutter.getRegistryName()));
        if (Config.loom) event.getRegistry().register(new ItemBlock(Init.loom).setRegistryName(Init.loom.getRegistryName()));
        if (Config.barl) event.getRegistry().register(new ItemBlock(Init.barrel).setRegistryName(Init.barrel.getRegistryName()));
        if (Config.campfire) event.getRegistry().register(new ItemBlock(Init.campfire).setRegistryName(Init.campfire.getRegistryName()));

    }

    @SubscribeEvent
    public static void registerSpecialBlocks(RegistryEvent.Register<Block> event) {
        if (Config.striplog) {
            for (Block block : Init.strippedLogs) {
                event.getRegistry().register(block);
            }
        }
        if (Config.newwall) {
            for (Block block : Init.newwall) {
                event.getRegistry().register(block);
            }
        }
    }

    @SubscribeEvent
    public static void registerSpecialItems(RegistryEvent.Register<Item> event) {
        if (Config.striplog) {
            for (Block block : Init.strippedLogs) {
                event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
            }
        }
        if (Config.newwall) {
            for (Block block : Init.newwall) {
                event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
            }
        }
    }
}
