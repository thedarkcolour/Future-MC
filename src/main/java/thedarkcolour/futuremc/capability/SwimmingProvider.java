package thedarkcolour.futuremc.capability;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SwimmingProvider implements ICapabilityProvider {
    private SwimmingCapability instance = SWIMMING_CAP.getDefaultInstance();

    @CapabilityInject(SwimmingCapability.class)
    public static Capability<SwimmingCapability> SWIMMING_CAP = null;

    @CapabilityInject(SwimmingCapability.class)
    public static void method(Capability<SwimmingCapability> cap) {
        SWIMMING_CAP = cap;
        System.out.println("CAPABILITY ASSIGNED HOPEFULLY");
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == SWIMMING_CAP;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return hasCapability(capability, facing) ? SWIMMING_CAP.cast(instance) : null;
    }
}