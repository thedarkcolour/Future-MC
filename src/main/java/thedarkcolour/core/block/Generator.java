package thedarkcolour.core.block;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import thedarkcolour.core.util.Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Generates needed blockStates for every block in my mod.
 * TODO stairs
 */
public final class Generator {
    public static void generate() {
        try {
            generateBlockStates();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "StringConcatenationInLoop"})
    private static void generateBlockStates() throws IOException {
        File folder = new File("config/futuremc/generated/blockstates/");
        folder.mkdirs();
        for (Block block : Util.make(Lists.newArrayList(ForgeRegistries.BLOCKS.getValues()),list -> list.removeIf(block -> !block.getRegistryName().getNamespace().equals("minecraftfuture")))) {
            File json = new File("config/futuremc/generated/blockstates/" + block.getRegistryName().getPath() + ".json");

            if (!json.exists()) {
                json.createNewFile();

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
            }
        }
    }
}