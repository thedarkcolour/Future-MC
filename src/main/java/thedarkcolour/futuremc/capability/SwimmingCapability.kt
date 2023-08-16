package thedarkcolour.futuremc.capability

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTPrimitive
import net.minecraft.nbt.NBTTagByte
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import thedarkcolour.futuremc.FutureMC

interface SwimmingCapability {
    var swimAnimation: Float
    var lastSwimAnimation: Float
    var isSwimming: Boolean

    companion object {
        @JvmStatic
        val ID = ResourceLocation(FutureMC.ID, "swimming")

        fun register() {
            CapabilityManager.INSTANCE.register(
                SwimmingCapability::class.java,
                object : IStorage<SwimmingCapability> {
                    override fun writeNBT(
                        capability: Capability<SwimmingCapability>,
                        instance: SwimmingCapability,
                        side: EnumFacing
                    ): NBTBase {
                        return NBTTagByte((if (instance.isSwimming) 1 else 0).toByte())
                    }

                    override fun readNBT(
                        capability: Capability<SwimmingCapability>,
                        instance: SwimmingCapability,
                        side: EnumFacing,
                        nbt: NBTBase
                    ) {
                        instance.isSwimming = (nbt as NBTPrimitive).int == 1
                    }
                },
                ::Impl
            )
            MinecraftForge.EVENT_BUS.register(this)
        }

        @SubscribeEvent
        fun attachCapability(event: AttachCapabilitiesEvent<*>) {
            if (event.getObject() is EntityPlayer) {
                event.addCapability(ID, SwimmingProvider())
            }
        }
    }

    private class Impl : SwimmingCapability {
        override var swimAnimation = 0f
        override var lastSwimAnimation = 0f
        override var isSwimming = false
    }
}