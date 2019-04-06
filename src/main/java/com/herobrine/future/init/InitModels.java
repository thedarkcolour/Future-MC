package com.herobrine.future.init;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.herobrine.future.config.FutureConfig.general;
import static com.herobrine.future.config.FutureConfig.modFlowers;
import static com.herobrine.future.init.Init.*;

public class InitModels {
    @SubscribeEvent
    public static void initModel() {
        if(general.lantern) LANTERN.model(); // Blocks
        if(general.barrel) BARREL.model();
        if(general.stonecutter) STONECUTTER.model();
        if(general.blastFurnace) BLAST_FURNACE.model();
        if(general.smoker) SMOKER.model();
        if(general.loom) LOOM.model();
        if(general.fletchingTable) FLETCHING_TABLE.model();
        if(general.smithingTable) SMITHING_TABLE.model();

        if(modFlowers.lily) LILY_OF_VALLEY.model();
        if(modFlowers.cornflower) CORNFLOWER.model();
        if(modFlowers.witherRose) WITHER_ROSE.model();
        if(general.berryBush) SWEET_BERRY.model();
        if(general.campfire) CAMPFIRE.model();

        if(general.strippedLogs) {
            STRIPPED_ACACIA_LOG.model();
            STRIPPED_JUNGLE_LOG.model();
            STRIPPED_BIRCH_LOG.model();
            STRIPPED_OAK_LOG.model();
            STRIPPED_SPRUCE_LOG.model();
            STRIPPED_DARK_OAK_LOG.model();
        }
        if(general.newWallVariants) {
            BRICK_WALL.model();
            GRANITE_WALL.model();
            ANDESITE_WALL.model();
            DIORITE_WALL.model();
            SANDSTONE_WALL.model();
            RED_SANDSTONE_WALL.model();
            STONE_BRICK_WALL.model();
            MOSSY_STONE_WALL.model();
            NETHER_BRICK_WALL.model();
            RED_NETHER_BRICK_WALL.model();
            END_STONE_WALL.model();
            PRISMARINE_WALL.model();
        }
        if(general.smoothStone) SMOOTH_STONE.model();

        if(modFlowers.dyes) DYES.model();    // Items
        if(general.trident) TRIDENT.model();
        if(modFlowers.suspiciousStew) SUSPICIOUS_STEW.models();
    }
}