package com.herobrine.future.utils;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

public class Config {

    private static final String CATEGORY_GENERAL = "general";
    public static boolean lilyenabled = true;   //lily enabled?
    public static boolean cornflowerenabled = true; //cornflower enabled?
    public static boolean witherroseenabled = true; //wither rose enabled?
    public static boolean tridentenabled = true;    //trident enabled?
    public static boolean dyesenabled = true;   //are any dyes enabled?
    public static boolean dyeblueenabled = true;    //blue dye enabled?
    public static boolean dyewhiteenabled = true;   //white dye enabled?
    public static boolean dyeblackenabled = true;   //black dye enabled?
    public static boolean lanternenabled = true; //lantern enabled?
    public static boolean stonecutterenabled = true; //stonecutter enabled?

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
        lilyenabled = cfg.getBoolean("lilyenabled", CATEGORY_GENERAL, lilyenabled, "whether Lily of the Valley is enabled");
        cornflowerenabled = cfg.getBoolean("cornflowerenabled", CATEGORY_GENERAL, cornflowerenabled, "whether Cornflower is enabled");
        witherroseenabled = cfg.getBoolean("witherroseenabled", CATEGORY_GENERAL, witherroseenabled, "whether Wither Rose is enabled");
        tridentenabled = cfg.getBoolean("tridentenabled", CATEGORY_GENERAL, tridentenabled, "whether Trident is enabled");
        dyesenabled = cfg.getBoolean("dyesenabled", CATEGORY_GENERAL, dyesenabled, "whether dyes are enabled");
        dyeblueenabled = cfg.getBoolean("dyeblueenabled", CATEGORY_GENERAL, dyeblueenabled, "whether blue dye is enabled");
        dyewhiteenabled = cfg.getBoolean("dyesenabled", CATEGORY_GENERAL, dyewhiteenabled, "whether white dye is enabled");
        dyeblackenabled = cfg.getBoolean("dyesenabled", CATEGORY_GENERAL, dyeblackenabled, "whether black dye is enabled");
        lanternenabled = cfg.getBoolean("lanternenabled", CATEGORY_GENERAL, lanternenabled, "whether lantern is enabled");
        stonecutterenabled = cfg.getBoolean("stonecutterenabled", CATEGORY_GENERAL, stonecutterenabled, "whether stonecutter is enabled");
    }
}
