package thedarkcolour.futuremc.asm;


import kotlin.jvm.functions.Function1;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

import java.util.Map;

@Name("FutureMC")
@MCVersion("1.12.2")
@SortingIndex(1004)
@TransformerExclusions("thedarkcolour.futuremc.asm")
public class CoreLoader implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        //noinspection CatchMayIgnoreException
        try {
            Function1.class.getName();
        } catch (Throwable any) {
            if (any instanceof ClassNotFoundException) {
                System.out.println("***********************");
                System.out.println(" Shadowfact's Forgelin ");
                System.out.println("      is missing!      ");
                System.out.println("***********************");
                throw any;
            }

            return new String[0];
        }
        return new String[]{
                "thedarkcolour.futuremc.asm.CoreTransformer"
        };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        CoreTransformer.isObfuscated = (boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}