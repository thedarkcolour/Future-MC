package thedarkcolour.core.block;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import thedarkcolour.core.util.Util;
import thedarkcolour.futuremc.FutureMC;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Generates needed blockStates for every block in my mod.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public final class Generator {
    public static void setup() {
        if (FutureMC.INSTANCE.getDEBUG()) {
            generateFolders();
        }
    }

    public static void generateFolders() {
        File blockStates = new File("config/futuremc/generated/blockstates/");
        File registry = new File("config/futuremc/generated/registry/");
        blockStates.mkdirs();
        registry.mkdirs();
    }

    public static void generateRegistries() {
        generateRegistry(ForgeRegistries.BIOMES);
        generateRegistry(ForgeRegistries.BLOCKS);
        generateRegistry(ForgeRegistries.ENCHANTMENTS);
        generateRegistry(ForgeRegistries.ENTITIES);
        generateRegistry(ForgeRegistries.ITEMS);
        generateRegistry(ForgeRegistries.SOUND_EVENTS);
    }

    private static <V extends IForgeRegistryEntry<V>> void generateRegistry(IForgeRegistry<V> registry) {
        File file = new File("config/future-mc/generated/registry/" + registry.getRegistrySuperType().getName().toLowerCase() + ".txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            if (!file.canWrite()) {
                file.setWritable(true);
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (ResourceLocation key : Util.jmake(new HashSet<>(registry.getKeys()), keys -> keys.removeIf(key -> !(key.getNamespace().equals("minecraftfuture") || key.getNamespace().equals("minecraftfuture"))))) {
                writer.write(key.toString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateMappedRegistries() {
        generateMappedRegistry(ForgeRegistries.BIOMES);
        generateMappedRegistry(ForgeRegistries.BLOCKS);
        generateMappedRegistry(ForgeRegistries.ENCHANTMENTS);
        generateMappedRegistry(ForgeRegistries.ENTITIES);
        generateMappedRegistry(ForgeRegistries.ITEMS);
        generateMappedRegistry(ForgeRegistries.SOUND_EVENTS);
    }

    private static <V extends IForgeRegistryEntry<V>> void generateMappedRegistry(IForgeRegistry<V> registry) {
        File file = new File("config/future-mc/generated/registry/" + registry.getRegistrySuperType().getName().toLowerCase() + ".txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            if (!file.canWrite()) {
                file.setWritable(true);
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (ResourceLocation key : Util.jmake(new HashSet<>(registry.getKeys()), keys -> keys.removeIf(key -> !(key.getNamespace().equals("minecraftfuture") || key.getNamespace().equals("minecraftfuture"))))) {
                writer.write(key.toString() + ", " + key.toString().replaceFirst("minecraftfuture", "futuremc"));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("StringConcatenationInLoop")
    public static void generateBlockStates() {
        for (Block block : Util.jmake(Lists.newArrayList(ForgeRegistries.BLOCKS.getValues()), list -> list.removeIf(block -> !block.getRegistryName().getNamespace().equals("minecraftfuture")))) {
            File json = new File("config/future-mc/generated/blockstates/" + block.getRegistryName().getPath() + ".json");

            try {
                if (!json.exists()) {
                    json.createNewFile();
                }

                if (!json.canWrite()) {
                    json.setWritable(true);
                }

                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                JsonObject obj = new JsonObject();
                JsonObject element = new JsonObject();
                List<IBlockState> states = block.getBlockState().getValidStates();

                for (IBlockState state : states) {
                    Collection<IProperty<?>> properties = state.getPropertyKeys();
                    String[] names = new String[properties.size()], values = new String[properties.size()];

                    for (int i = 0; i < names.length; ++i) {
                        IProperty<?> prop = (IProperty<?>) properties.toArray()[i];
                        names[i] = prop.getName();
                        values[i] = state.getProperties().get(prop).toString();
                    }
                    StringBuilder tagBuilder = new StringBuilder();
                    for (int i = 0; i < properties.size(); ++i) {
                        if (i == properties.size() - 1) {
                            tagBuilder.append(names[i]).append("=").append(values[i]);
                        } else {
                            tagBuilder.append(names[i]).append("=").append(values[i]).append(",");
                        }
                    }

                    element.add(tagBuilder.toString(), new JsonObject());
                }
                obj.add("variants", element);
                String[] jsonText = gson.toJson(obj).split("[{]");
                for (int i = 0; i < jsonText.length; i++) {
                    if (!jsonText[i].endsWith("}")) jsonText[i] = jsonText[i] += "{";
                }
                List<String[]> linesList = new ArrayList<>();
                for (String line : jsonText) {
                    String[] array = line.split("},");
                    for (int i = 0; i < array.length; i++) {
                        if (i != array.length - 1) array[i] = array[i] += "},";
                    }
                    linesList.add(array);
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(json));
                int tabs = 0;
                for (String[] lines : linesList) {
                    for (String line : lines) {
                        if (line.startsWith("}")) {
                            writer.newLine();
                            --tabs;
                        } else if (line.endsWith("{")) {
                            ++tabs;
                        }
                        for (int i = 0; i < tabs; ++i) {
                            line = "    " + line;
                        }
                        writer.write(line);
                        writer.newLine();
                    }
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}