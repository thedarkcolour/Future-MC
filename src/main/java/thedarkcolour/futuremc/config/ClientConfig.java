package thedarkcolour.futuremc.config;

import com.google.common.collect.Lists;
import net.minecraft.item.DyeColor;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ClientConfig {
    final ForgeConfigSpec.BooleanValue clientBool;
    final ForgeConfigSpec.ConfigValue<List<String>> clientStrings;
    final ForgeConfigSpec.ConfigValue<DyeColor> clientColor;

    private static final String PREFIX = "config.futuremc.";

    public ClientConfig(ForgeConfigSpec.Builder builder) {
        clientBool = builder
                .comment("Example client boolean")
                .translation(PREFIX + "clientBool")
                .define("clientBool", true);
        clientStrings = builder
                .comment("Example client List of Strings")
                .translation(PREFIX + "clientStrings")
                .define("clientStrings", Lists.newArrayList("Blue", "Green", "Red"));

        clientColor = builder
                .comment("Example client DyeColor")
                .translation(PREFIX + "clientColor")
                .define("clientColor", DyeColor.BROWN);
    }
}