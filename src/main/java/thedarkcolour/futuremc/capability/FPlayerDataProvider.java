package thedarkcolour.futuremc.capability;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FPlayerDataProvider implements ICapabilityProvider {
    private FPlayerData instance = DATA_CAP.getDefaultInstance();

    @CapabilityInject(FPlayerData.class)
    public static Capability<FPlayerData> DATA_CAP;

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == DATA_CAP;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return hasCapability(capability, facing) ? DATA_CAP.cast(instance) : null;
    }
}