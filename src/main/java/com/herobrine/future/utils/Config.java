package com.herobrine.future.utils;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import scala.reflect.internal.Importers;

public class Config {

    private static final String CATEGORY_GENERAL = "general";
    public static boolean lily = true;   //lily enabled?
    public static boolean bluef = true; //cornflower enabled?
    public static boolean wrose = true; //wither rose enabled?
    public static boolean drose = true;     //wrose does damage?
    public static boolean trident = true;    //trident enabled?
    public static boolean dyes = true;   //are any dyes enabled?
    public static boolean dyeb = true;    //blue dye enabled?
    public static boolean dyew = true;   //white dye enabled?
    public static boolean dyebk = true;   //black dye enabled?
    public static boolean lant = true; //lantern enabled?
    public static boolean stonec = true; //stonecutter enabled?
    public static boolean barl = true;  //barrel enabled?
    public static boolean susstew = true; //suspicious soup enabled?

    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
        try {
            cfg.load();
            init(cfg);
        } catch (Exception e1) {
            futurejava.logger.log(Level.ERROR, "Problem loading Config file!", e1);
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
        dyebk = cfg.getBoolean("dyesenabled", CATEGORY_GENERAL, dyebk, "whether black dye is enabled");
        lant = cfg.getBoolean("lanternenabled", CATEGORY_GENERAL, lant, "whether lantern is enabled");
        stonec = cfg.getBoolean("stonecutterenabled", CATEGORY_GENERAL, stonec, "whether stonecutter is enabled");
        barl = cfg.getBoolean("barrelenabled", CATEGORY_GENERAL, barl, "whether Barrel is enabled");
        susstew = cfg.getBoolean("suspiciousstewenabled", CATEGORY_GENERAL, susstew, "whether suspicious stew is enabled");
    }
}
