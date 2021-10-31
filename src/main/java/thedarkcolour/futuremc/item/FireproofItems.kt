package thedarkcolour.futuremc.item

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import net.minecraft.block.Block
import net.minecraft.entity.FireproofItemLogic
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.item.EntityItem
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.*
import thedarkcolour.core.item.ModeledItem
import thedarkcolour.core.item.ModeledItemBlock
import thedarkcolour.core.util.setItemModel
import thedarkcolour.core.util.setItemName

class FireproofItemBlock(block: Block) : ModeledItemBlock(block) {
    override fun onEntityItemUpdate(entity: EntityItem): Boolean {
        FireproofItemLogic.update(entity)
        return true
    }
}

class FireproofItem(regName: String) : ModeledItem(regName) {
    override fun onEntityItemUpdate(entity: EntityItem): Boolean {
        FireproofItemLogic.update(entity)
        return true
    }
}

class FireproofAxeItem(
    regName: String,
    material: ToolMaterial,
    damage: Float,
    speed: Float
) : ItemAxe(material, damage, speed) {
    init {
        setItemName(this, regName)
        setItemModel(this, 0)
    }

    override fun onEntityItemUpdate(entity: EntityItem): Boolean {
        FireproofItemLogic.update(entity)
        return true
    }
}

class FireproofHoeItem(regName: String, material: ToolMaterial) : ItemHoe(material) {
    init {
        setItemName(this, regName)
        setItemModel(this, 0)
    }

    override fun onEntityItemUpdate(entity: EntityItem): Boolean {
        FireproofItemLogic.update(entity)
        return true
    }
}

class FireproofPickaxeItem(regName: String, material: ToolMaterial) : ItemPickaxe(material) {
    init {
        setItemName(this, regName)
        setItemModel(this, 0)
    }

    override fun onEntityItemUpdate(entity: EntityItem): Boolean {
        FireproofItemLogic.update(entity)
        return true
    }
}

class FireproofShovelItem(regName: String, material: ToolMaterial) : ItemSpade(material) {
    init {
        setItemName(this, regName)
        setItemModel(this, 0)
    }

    override fun onEntityItemUpdate(entity: EntityItem): Boolean {
        FireproofItemLogic.update(entity)
        return true
    }
}

class FireproofSwordItem(regName: String, material: ToolMaterial) : ItemSword(material) {
    init {
        setItemName(this, regName)
        setItemModel(this, 0)
    }

    override fun onEntityItemUpdate(entity: EntityItem): Boolean {
        FireproofItemLogic.update(entity)
        return true
    }
}

/**
 *
 */
@Deprecated(level = DeprecationLevel.WARNING, message = "This is only here for MIA compat")
class FireproofArmorItem(regName: String, materialIn: ArmorMaterial, equipmentSlotIn: EntityEquipmentSlot) : NetheriteArmorItem(regName, materialIn, equipmentSlotIn) {
    override fun getItemAttributeModifiers(slot: EntityEquipmentSlot): Multimap<String, AttributeModifier> {
        val map = HashMultimap.create<String, AttributeModifier>()

        if (equipmentSlot == armorType) {
            map.put(SharedMonsterAttributes.ARMOR.name, AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.index], "Armor modifier", damageReduceAmount.toDouble(), 0))
            map.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.name, AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.index], "Armor toughness", toughness.toDouble(), 0))
        }

        return map
    }
}

open class NetheriteArmorItem(
    regName: String,
    materialIn: ArmorMaterial,
    equipmentSlotIn: EntityEquipmentSlot
) : ItemArmor(materialIn, 0, equipmentSlotIn) {
    init {
        setItemName(this, regName)
        setItemModel(this, 0)
    }

    override fun getItemAttributeModifiers(slot: EntityEquipmentSlot): Multimap<String, AttributeModifier> {
        val map = super.getItemAttributeModifiers(slot)

        if (slot == armorType) {
            map.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.name, AttributeModifier(ARMOR_MODIFIERS[slot.index], "Armor knockback resistance", 0.1, 0))
        }

        return map
    }

    override fun onEntityItemUpdate(entity: EntityItem): Boolean {
        FireproofItemLogic.update(entity)
        return true
    }
}