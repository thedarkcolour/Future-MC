package thedarkcolour.futuremc.capability

import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.AttachCapabilitiesEvent
import thedarkcolour.futuremc.FutureMC

object CapabilityHandler {
    val SWIMMING_CAP = ResourceLocation(FutureMC.ID, "swimming")

    fun attachCapability(event: AttachCapabilitiesEvent<Entity>) {
        if (event.`object` is EntityPlayer) {
            event.addCapability(SWIMMING_CAP, SwimmingProvider())
        }
    }
}