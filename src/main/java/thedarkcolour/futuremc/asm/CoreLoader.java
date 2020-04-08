package thedarkcolour.futuremc.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.shadowfacts.forgelin.KotlinAdapter;

import java.util.Map;

import static net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.*;

@Name("FutureMC")
@MCVersion("1.12.2")
@SortingIndex(1004)
@TransformerExclusions("thedarkcolour.futuremc.asm")
public class CoreLoader implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        //noinspection CatchMayIgnoreException
        try {
            KotlinAdapter.class.getName();
        } catch (Throwable any) {
            if (any instanceof ClassNotFoundException) {
                System.out.println("***********************");
                System.out.println(" Shadowfact's Forgelin ");
                System.out.println("      is missing!      ");
                System.out.println("***********************");
                throw any;
            }
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