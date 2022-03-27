package thedarkcolour.futuremc.block.villagepillage;

import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import thedarkcolour.futuremc.FutureMC;

public final class FSignBlock {
    public interface FSignType {
        String getType();
    }

    public static final class Standing extends BlockStandingSign implements FSignType {
        private final String type;

        public Standing(String type) {
            this.type = type;
            type += "_standing_sign";

            setTranslationKey(FutureMC.ID + "." + type);
            setRegistryName(new ResourceLocation(FutureMC.ID, type));

            setHardness(1.0F);
            setSoundType(SoundType.WOOD);
        }

        @Override
        public String getType() {
            return type;
        }
    }

    public static final class Wall extends BlockWallSign implements FSignType {
        private final String type;

        public Wall(String type) {
            this.type = type;
            type += "_wall_sign";

            setTranslationKey(FutureMC.ID + "." + type);
            setRegistryName(new ResourceLocation(FutureMC.ID, type));

            setHardness(1.0F);
            setSoundType(SoundType.WOOD);
        }

        @Override
        public String getType() {
            return type;
        }
    }
}
