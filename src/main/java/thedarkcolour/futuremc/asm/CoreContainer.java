package thedarkcolour.futuremc.asm;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import thedarkcolour.core.util.Util;

import java.util.List;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public class CoreContainer extends DummyModContainer {
    private static final ModMetadata meta = Util.jmake(new ModMetadata(), md -> {
        md.modId = "futuremccore";
        md.name = "FutureMCCore";
        md.description = "Coremod for Future MC. Patches trees and pistons.";
        md.authorList = ImmutableList.of("TheDarkColour");
        md.version = "0.1.12";
        md.credits = "Herobrine sees all.";
    });

    public CoreContainer() {
        super(meta);
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        return true;
    }

    @Override
    public List<String> getOwnedPackages() {
        return ImmutableList.of("thedarkcolour.futuremc.asm");
    }
}