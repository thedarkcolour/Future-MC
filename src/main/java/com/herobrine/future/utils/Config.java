package com.herobrine.future.utils;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

public class Config {

    private static final String CATEGORY_GENERAL = "general";
    public static boolean lily = true;   //lily enabled?
    public static boolean bluef = true; //cornflower enabled?
    public static boolean wrose = true; //wither rose enabled?
    public static boolean drose = true;     //wrose does damage?
    public static boolean trident = true;    //Trident enabled?
    public static boolean dyes = true;   //are any dyes enabled?
    public static boolean dyeb = true;    //blue dye enabled?
    public static boolean dyew = true;   //white dye enabled?
    public static boolean dyebr = true;   //enabled?
    public static boolean dyebk = true;   //black dye enabled?
    public static boolean lant = true; //Lantern enabled?
    public static boolean stonec = true; //Stonecutter enabled?
    public static boolean barl = true;  //enabled?
    public static boolean susstew = true; //suspicious soup enabled?
    public static boolean lilyg = true;   //enabled?
    public static boolean bluefg = true;   //enabled?
    public static boolean loom = true;  //enabled?
    public static boolean berrybush = true;
    public static boolean campfire = true;
    public static boolean campfiredmg = true;

    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
        try {
            cfg.load();
            init(cfg);
        } catch (Exception e1) {
            FutureJava.logger.log(Level.ERROR, "Problem loading Config file!", e1);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    public static void init(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
        lily = cfg.getBoolean("lilyenabled", CATEGORY_GENERAL, lily, "whether Lily of the Valley is enabled");
        bluef = cfg.getBoolean("cornflowerenabled", CATEGORY_GENERAL, bluef, "whether Cornflower is enabled");
        wrose = cfg.getBoolean("witherroseenabled", CATEGORY_GENERAL, wrose, "whether Wither Rose is enabled");
        drose = cfg.getBoolean("witherrosedoesdamage", CATEGORY_GENERAL, wrose, "whether Wither Rose does damage");
        trident = cfg.getBoolean("tridentenabled", CATEGORY_GENERAL, trident, "whether Trident is enabled");
        dyes = cfg.getBoolean("dyesenabled", CATEGORY_GENERAL, dyes, "whether dyes are enabled");
        dyeb = cfg.getBoolean("dyeblueenabled", CATEGORY_GENERAL, dyeb, "whether blue dye is enabled");
        dyew = cfg.getBoolean("dyesenabled", CATEGORY_GENERAL, dyew, "whether white dye is enabled");
        dyebr = cfg.getBoolean("dyebrownenabled", CATEGORY_GENERAL, dyebr, "whether brown dye is enabled");
        dyebk = cfg.getBoolean("dyesenabled", CATEGORY_GENERAL, dyebk, "whether black dye is enabled");
        lant = cfg.getBoolean("lanternenabled", CATEGORY_GENERAL, lant, "whether Lantern is enabled");
        stonec = cfg.getBoolean("stonecutterenabled", CATEGORY_GENERAL, stonec, "whether Stonecutter is enabled");
        barl = cfg.getBoolean("barrelenabled", CATEGORY_GENERAL, barl, "whether Barrel is enabled");
        susstew = cfg.getBoolean("suspiciousstewenabled", CATEGORY_GENERAL, susstew, "whether suspicious stew is enabled");
        lilyg = cfg.getBoolean("lilygeneration", CATEGORY_GENERAL, lilyg, "whether the Lily of the Valley generates in the world (forests)");
        bluefg = cfg.getBoolean("cornflowergeneration", CATEGORY_GENERAL, bluefg, "whether the Cornflower generates in the world (plains)");
        loom = cfg.getBoolean("loomenabled", CATEGORY_GENERAL, loom, "whether the loom is enabled");
        berrybush = cfg.getBoolean("berrybushenabled", CATEGORY_GENERAL, berrybush, "whether Berry Bushes and Berries are enabled");
        campfire = cfg.getBoolean("campfireenabled", CATEGORY_GENERAL, campfire, "whether the campfire is enabled");
        campfiredmg = cfg.getBoolean("campfiredamagesplayer", CATEGORY_GENERAL, campfiredmg, "Whether the campfire does damage to players when walked on");
    }
}