// Package allows access to eventListeners
@file:Suppress("PackageDirectoryMismatch")

package net.minecraft.world

import net.minecraft.entity.Entity
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraftforge.event.ForgeEventFactory

@Suppress("NAME_SHADOWING")
fun World.playSound(pos: BlockPos, soundIn: SoundEvent, category: SoundCategory, volume: Float, pitch: Float) {
    val event = ForgeEventFactory.onPlaySoundAtEntity(null, soundIn, category, volume, pitch)
    if (event.isCanceled || event.sound == null) {
        return
    }
    val soundIn = event.sound
    val category = event.category
    val volume = event.volume
    val pitch = event.pitch

    for (i in eventListeners.indices) {
        (eventListeners[i] as IWorldEventListener).playSoundToAllNearExcept(null, soundIn, category, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), volume, pitch)
    }
}

fun World.playSound(x: Double, y: Double, z: Double, soundIn: SoundEvent, category: SoundCategory, volume: Float, pitch: Float) {
    playSound(null, x, y, z, soundIn, category, volume, pitch)
}

fun Entity.squaredDistanceTo(otherEntity: Entity): Double {
    val x = posX - otherEntity.posX
    val y = posY - otherEntity.posY
    val z = posZ - otherEntity.posZ
    return x * x + y * y + z * z
}