package thedarkcolour.futuremc.util;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModAPIManager;
import net.minecraftforge.fml.relauncher.Side;
import thedarkcolour.futuremc.FutureMC;

/**
 *
 * Copy from AEAdditions Mod. <br>
 * Identify whether the specified Mod is installed
 * @author Beachman4
 */
public class Integration {
    public enum Mods {
        OE("oe","Oceanic Expanse");

        private final String modID;

        private boolean shouldLoad = true;

        private final String name;

        private final Side side;

        Mods(String modid) {
            this(modid, modid);
        }

        Mods(String modid, String modName, Side side) {
            this.modID = modid;
            this.name = modName;
            this.side = side;
        }

        Mods(String modid, String modName) {
            this(modid, modName, null);
        }

        Mods(String modid, Side side) {
            this(modid, modid, side);
        }

        public String getModID() {
            return modID;
        }

        public String getModName() {
            return name;
        }

        public boolean isOnClient() {
            return side != Side.SERVER;
        }

        public boolean isOnServer() {
            return side != Side.CLIENT;
        }

        public void loadConfig(Configuration config) {
            shouldLoad = config.get("Integration", "enable" + getModID(), true, "Enable " + getModName() + " Integration.").getBoolean(true);
        }

        public boolean isEnabled() {
            return (Loader.isModLoaded(getModID()) && shouldLoad) || (ModAPIManager.INSTANCE.hasAPI(getModID()) && shouldLoad);
        }
    }

    public void loadConfig(Configuration config) {
        for (Mods mod : Mods.values()) {
            mod.loadConfig(config);
        }
    }


    public void preInit() {
        for(Mods mod : Mods.values()){
            if(mod.isEnabled())
                FutureMC.LOGGER.info("Enable integration for '" + mod.name + " (" + mod.modID + ")'");
        }


    }

    public void init() {

    }

    public void postInit() {
    }
}
