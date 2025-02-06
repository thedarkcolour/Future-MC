package thedarkcolour.futuremc.config;

import net.minecraftforge.common.config.Config;
import thedarkcolour.futuremc.FutureMC;

import static net.minecraftforge.common.config.Config.*;

/**
 * Exposes Kotlin config to the ConfigManager.
 * Required because the config manager also picks up the INSTANCE field
 * that Kotlin object classes generate.
 */
@Config(modid = FutureMC.ID, name = FutureMC.ID + "/" + FutureMC.ID)
public final class _Internal {
    @Name("Update Aquatic")
    public static final FConfig.UpdateAquatic updateAquatic = new FConfig.UpdateAquatic();
    @Name("Village & Pillage")
    public static final FConfig.VillageAndPillage villageAndPillage = new FConfig.VillageAndPillage();
    @Name("Buzzy Bees")
    public static final FConfig.BuzzyBees buzzyBees = new FConfig.BuzzyBees();
    @Name("Nether Update")
    public static final FConfig.NetherUpdate netherUpdate = new FConfig.NetherUpdate();
    @Name("Caves & Cliffs")
    public static final FConfig.CavesNCliffs cavesNCliffs = new FConfig.CavesNCliffs();
    @Name("Use Vanilla Creative Tabs")
    @Comment("Whether this mod's item are displayed in a separate creative tab or sorted into their Vanilla tabs.")
    @RequiresMcRestart
    public static boolean useVanillaCreativeTabs = false;
}