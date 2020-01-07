package thedarkcolour.futuremc.capability

import net.minecraft.nbt.NBTBase
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.common.capabilities.ICapabilitySerializable

class SwimmingProvider : ICapabilitySerializable<NBTBase> {
    private val instance = SWIMMING_CAP.defaultInstance!!

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        return capability == SWIMMING_CAP
    }

    override fun <T : Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        return if (capability == SWIMMING_CAP) {
            SWIMMING_CAP.cast(instance)
        } else {
            null
        }
    }

    override fun serializeNBT(): NBTBase {
        return SWIMMING_CAP.storage.writeNBT(SWIMMING_CAP, instance, null)!!
    }

    override fun deserializeNBT(nbt: NBTBase) {
        SWIMMING_CAP.storage.readNBT(SWIMMING_CAP, instance, null, nbt)
    }

    companion object {
        @CapabilityInject(ISwimmingCapability::class)
        @JvmStatic lateinit var SWIMMING_CAP: Capability<ISwimmingCapability>
    }
}