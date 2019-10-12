package thedarkcolour.futuremc.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

import static net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import static net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import static net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import static net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@Name("FutureMC")
@MCVersion("1.12.2")
@SortingIndex(1004)
@TransformerExclusions("thedarkcolour.futuremc.asm")
public class CoreLoader implements IFMLLoadingPlugin {
    protected static boolean isObfuscated;

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {
                "thedarkcolour.futuremc.asm.CoreTransformer"
        };
    }

    @Override
    public String getModContainerClass() {
        return "thedarkcolour.futuremc.asm.CoreContainer";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        isObfuscated = (boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}