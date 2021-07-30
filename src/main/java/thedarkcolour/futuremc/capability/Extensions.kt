package thedarkcolour.futuremc.capability

import net.minecraft.entity.player.EntityPlayer
import thedarkcolour.core.util.lerp

fun EntityPlayer.hasSwimmingCap(): Boolean {
    return getCapability(SwimmingProvider.SWIMMING_CAP, null) != null
}

fun EntityPlayer.getSwimmingCap(): SwimmingCapability {
    return getCapability(SwimmingProvider.SWIMMING_CAP, null)!!
}

// check for capability before getting
var EntityPlayer.swimAnimation
    get() = getCapability(SwimmingProvider.SWIMMING_CAP, null)!!.swimAnimation
    set(value) {
        getSwimmingCap().swimAnimation = value
    }

// lerp frame smoothing
var EntityPlayer.lastSwimAnimation
    get() = getCapability(SwimmingProvider.SWIMMING_CAP, null)!!.lastSwimAnimation
    set(value) {
        getSwimmingCap().lastSwimAnimation = value
    }

// Whether the player has swimming movement speed + animations
var EntityPlayer.isSwimming
    get() = getCapability(SwimmingProvider.SWIMMING_CAP, null)?.isSwimming ?: false
    set(value) {
        getSwimmingCap().isSwimming = value
    }

fun EntityPlayer.getSwimmingAnimation(partialTicks: Float): Float {
    return lerp(partialTicks, lastSwimAnimation, swimAnimation)
}