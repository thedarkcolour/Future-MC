package thedarkcolour.futuremc.config;

import net.minecraftforge.common.config.Config;

import static net.minecraftforge.common.config.Config.*;

@Config(modid = "futuremc", name = "futuremc/futuremc")
public final class _Internal {
    @Name("Update Aquatic")
    public static final FConfig.UpdateAquatic updateAquatic = new FConfig.UpdateAquatic();
    @Name("Village & Pillage")
    public static final FConfig.VillageAndPillage villageAndPillage = new FConfig.VillageAndPillage();
    @Name("Buzzy Bees")
    public static final FConfig.BuzzyBees buzzyBees = new FConfig.BuzzyBees();
    @Name("Nether Update")
    public static final FConfig.NetherUpdate netherUpdate = new FConfig.NetherUpdate();
    @Name("Use Vanilla Creative Tabs")
    @Comment("Whether this mod's items are displayed in a separate creative tab or sorted into their Vanilla tabs.")
    @RequiresMcRestart
    public static boolean useVanillaCreativeTabs = true;
}