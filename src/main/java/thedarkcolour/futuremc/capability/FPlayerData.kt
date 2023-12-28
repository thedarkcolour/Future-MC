package thedarkcolour.futuremc.capability

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import thedarkcolour.futuremc.FutureMC

class FPlayerData {
    var inPowderSnow = false
    var wasInPowderSnow = false
    var frozenTicks = 0

    companion object {
        @JvmStatic
        val ID = ResourceLocation(FutureMC.ID, "swimming")

        fun register() {
            CapabilityManager.INSTANCE.register(
                FPlayerData::class.java,
                object : IStorage<FPlayerData> {
                    override fun writeNBT(capability: Capability<FPlayerData>, instance: FPlayerData, side: EnumFacing): NBTBase {
                        val nbt = NBTTagCompound()
                        nbt.setInteger("frozenTicks", instance.frozenTicks)
                        return nbt
                    }

                    override fun readNBT(capability: Capability<FPlayerData>, instance: FPlayerData, side: EnumFacing, nbt: NBTBase) {
                        if (nbt is NBTTagCompound) {
                            instance.frozenTicks = nbt.getInteger("frozenTicks")
                        }
                    }
                }
            ) { FPlayerData() }
            MinecraftForge.EVENT_BUS.register(this)
        }

        @SubscribeEvent
        fun attachCapability(event: AttachCapabilitiesEvent<*>) {
            if (event.getObject() is EntityPlayer) {
                event.addCapability(ID, FPlayerDataProvider())
            }
        }
    }
}