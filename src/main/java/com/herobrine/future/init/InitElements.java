package com.herobrine.future.init;

import com.herobrine.future.tile.advancedfurnace.TileBlastFurnace;
import com.herobrine.future.tile.advancedfurnace.TileSmoker;
import com.herobrine.future.tile.barrel.TileBarrel;
import com.herobrine.future.tile.campfire.TileCampfire;
import com.herobrine.future.tile.stonecutter.TileStonecutter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import static com.herobrine.future.config.FutureConfig.general;
import static com.herobrine.future.config.FutureConfig.modFlowers;
import static com.herobrine.future.init.Init.*;

@SuppressWarnings("ConstantConditions")
@Mod.EventBusSubscriber
public class InitElements {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> r = event.getRegistry();
        if(general.lantern) r.register(LANTERN);
        if(general.stonecutter) r.register(STONECUTTER);
        if(general.barrel) r.register(BARREL);
        if(general.blastFurnace) r.register(BLAST_FURNACE);
        if(general.smoker) r.register(SMOKER);
        if(general.loom) r.register(LOOM);
        if(general.fletchingTable) r.register(FLETCHING_TABLE);
        if(general.smithingTable) r.register(SMITHING_TABLE);

        if(modFlowers.cornflower) r.register(CORNFLOWER);
        if(modFlowers.lily) r.register(LILY_OF_VALLEY);
        if(modFlowers.witherRose) r.register(WITHER_ROSE);
        if(general.berryBush) r.register(BERRY_BUSH);
        if(general.campfire) r.register(CAMPFIRE);

        if(general.strippedLogs) r.registerAll(STRIPPED_OAK_LOG, STRIPPED_SPRUCE_LOG, STRIPPED_BIRCH_LOG, STRIPPED_JUNGLE_LOG, STRIPPED_ACACIA_LOG, STRIPPED_DARK_OAK_LOG);
        if(general.newWallVariants) r.registerAll(BRICK_WALL, GRANITE_WALL, ANDESITE_WALL, DIORITE_WALL, SANDSTONE_WALL, RED_SANDSTONE_WALL, STONE_BRICK_WALL, MOSSY_STONE_WALL, NETHER_BRICK_WALL, RED_NETHER_BRICK_WALL, END_STONE_WALL, PRISMARINE_WALL);
        if(general.smoothStone) r.register(SMOOTH_STONE);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        if(general.lantern) r.register(new ItemBlock(LANTERN).setRegistryName(LANTERN.getRegistryName()));
        if(general.stonecutter) r.register(new ItemBlock(STONECUTTER).setRegistryName(STONECUTTER.getRegistryName()));
        if(general.barrel) r.register(new ItemBlock(BARREL).setRegistryName(BARREL.getRegistryName()));
        if(general.blastFurnace) r.register(new ItemBlock(BLAST_FURNACE).setRegistryName(BLAST_FURNACE.getRegistryName()));
        if(general.smoker) r.register(new ItemBlock(SMOKER).setRegistryName(SMOKER.getRegistryName()));
        if(general.loom) r.register(new ItemBlock(LOOM).setRegistryName(LOOM.getRegistryName()));
        if(general.fletchingTable) r.register(new ItemBlock(FLETCHING_TABLE).setRegistryName(FLETCHING_TABLE.getRegistryName()));
        if(general.smithingTable) r.register(new ItemBlock(SMITHING_TABLE).setRegistryName(SMITHING_TABLE.getRegistryName()));

        if(general.trident) r.register(TRIDENT);

        if(modFlowers.lily) r.register(new ItemBlock(LILY_OF_VALLEY).setRegistryName(LILY_OF_VALLEY.getRegistryName()));
        if(modFlowers.cornflower) r.register(new ItemBlock(CORNFLOWER).setRegistryName(CORNFLOWER.getRegistryName()));
        if(modFlowers.witherRose) r.register(new ItemBlock(WITHER_ROSE).setRegistryName(WITHER_ROSE.getRegistryName()));
        if(modFlowers.suspiciousStew) r.register(SUSPICIOUS_STEW);
        if(modFlowers.dyes) r.register(DYES);
        if(general.berryBush) r.register(SWEET_BERRY);
        if(general.campfire) r.register(new ItemBlock(CAMPFIRE).setRegistryName(CAMPFIRE.getRegistryName()));

        if(general.strippedLogs) registerLogs(event);
        if(general.newWallVariants) registerWalls(event);
        if(general.smoothStone) r.register(new ItemBlock(SMOOTH_STONE).setRegistryName(SMOOTH_STONE.getRegistryName()));

        registerTileEntities();
    }

    public static void registerLogs(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();
        r.registerAll(
                new ItemBlock(STRIPPED_ACACIA_LOG).setRegistryName(STRIPPED_ACACIA_LOG.getRegistryName()),
                new ItemBlock(STRIPPED_JUNGLE_LOG).setRegistryName(STRIPPED_JUNGLE_LOG.getRegistryName()),
                new ItemBlock(STRIPPED_BIRCH_LOG).setRegistryName(STRIPPED_BIRCH_LOG.getRegistryName()),
                new ItemBlock(STRIPPED_OAK_LOG).setRegistryName(STRIPPED_OAK_LOG.getRegistryName()),
                new ItemBlock(STRIPPED_SPRUCE_LOG).setRegistryName(STRIPPED_SPRUCE_LOG.getRegistryName()),
                new ItemBlock(STRIPPED_DARK_OAK_LOG).setRegistryName(STRIPPED_DARK_OAK_LOG.getRegistryName()));
    }

    public static void registerWalls(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();
        r.registerAll(
                new ItemBlock(BRICK_WALL).setRegistryName(BRICK_WALL.getRegistryName()),
                new ItemBlock(GRANITE_WALL).setRegistryName(GRANITE_WALL.getRegistryName()),
                new ItemBlock(ANDESITE_WALL).setRegistryName(ANDESITE_WALL.getRegistryName()),
                new ItemBlock(DIORITE_WALL).setRegistryName(DIORITE_WALL.getRegistryName()),
                new ItemBlock(SANDSTONE_WALL).setRegistryName(SANDSTONE_WALL.getRegistryName()),
                new ItemBlock(RED_SANDSTONE_WALL).setRegistryName(RED_SANDSTONE_WALL.getRegistryName()),
                new ItemBlock(STONE_BRICK_WALL).setRegistryName(STONE_BRICK_WALL.getRegistryName()),
                new ItemBlock(MOSSY_STONE_WALL).setRegistryName(MOSSY_STONE_WALL.getRegistryName()),
                new ItemBlock(NETHER_BRICK_WALL).setRegistryName(NETHER_BRICK_WALL.getRegistryName()),
                new ItemBlock(RED_NETHER_BRICK_WALL).setRegistryName(RED_NETHER_BRICK_WALL.getRegistryName()),
                new ItemBlock(END_STONE_WALL).setRegistryName(END_STONE_WALL.getRegistryName()),
                new ItemBlock(PRISMARINE_WALL).setRegistryName(PRISMARINE_WALL.getRegistryName())
        );
    }
    public static void registerTileEntities() {
        if(general.stonecutter) GameRegistry.registerTileEntity(TileStonecutter.class, new ResourceLocation(MODID + ":containerStonecutter"));
        if(general.barrel) GameRegistry.registerTileEntity(TileBarrel.class, new ResourceLocation(MODID + ":containerBarrel"));
        if(general.blastFurnace) GameRegistry.registerTileEntity(TileBlastFurnace.class, new ResourceLocation(MODID + ":containerBlastFurnace"));
        if(general.smoker) GameRegistry.registerTileEntity(TileSmoker.class, new ResourceLocation(MODID + ":containerSmoker"));
        if(general.campfire) GameRegistry.registerTileEntity(TileCampfire.class, new ResourceLocation(MODID + ":containerCampfire"));
    }
}