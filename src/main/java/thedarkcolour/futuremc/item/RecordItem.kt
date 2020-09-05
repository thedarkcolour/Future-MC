package thedarkcolour.futuremc.item

import net.minecraft.item.ItemRecord
import net.minecraft.util.SoundEvent
import thedarkcolour.core.util.setItemModel
import thedarkcolour.core.util.setItemName

class RecordItem(recordName: String, soundIn: SoundEvent) : ItemRecord(recordName, soundIn) {
    init {
        setItemName(this, "record_pigstep")
        setItemModel(this, 0)
    }
}