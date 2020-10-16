package thedarkcolour.futuremc.item

import net.minecraft.block.Block
import net.minecraft.entity.FireproofItemLogic
import net.minecraft.entity.item.ItemEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.*

class FireproofItem(properties: Properties) : Item(properties) {
    override fun onEntityItemUpdate(stack: ItemStack, entity: ItemEntity): Boolean {
        FireproofItemLogic.update(entity)
        return true
    }
}

class FireproofBlockItem(block: Block, properties: Properties) : BlockItem(block, properties) {
    override fun onEntityItemUpdate(stack: ItemStack, entity: ItemEntity): Boolean {
        FireproofItemLogic.update(entity)
        return true
    }
}

class FireproofArmorItem(material: IArmorMaterial, slotType: EquipmentSlotType, properties: Properties
) : ArmorItem(material, slotType, properties) {
    override fun onEntityItemUpdate(stack: ItemStack, entity: ItemEntity): Boolean {
        FireproofItemLogic.update(entity)
        return true
    }
}

class FireproofAxeItem(tier: IItemTier, attackDamage: Float, attackSpeed: Float, properties: Properties
) : AxeItem(tier, attackDamage, attackSpeed, properties) {
    override fun onEntityItemUpdate(stack: ItemStack, entity: ItemEntity): Boolean {
        FireproofItemLogic.update(entity)
        return true
    }
}

class FireproofHoeItem(tier: IItemTier, speed: Float, properties: Properties) : HoeItem(tier, speed, properties) {
    override fun onEntityItemUpdate(stack: ItemStack, entity: ItemEntity): Boolean {
        FireproofItemLogic.update(entity)
        return true
    }
}

class FireproofPickaxeItem(tier: IItemTier, attackDamage: Int, attackSpeed: Float, properties: Properties
) : PickaxeItem(tier, attackDamage, attackSpeed, properties) {
    override fun onEntityItemUpdate(stack: ItemStack, entity: ItemEntity): Boolean {
        FireproofItemLogic.update(entity)
        return true
    }
}

class FireproofShovelItem(tier: IItemTier, attackDamage: Float, attackSpeed: Float, properties: Properties
) : ShovelItem(tier, attackDamage, attackSpeed, properties) {
    override fun onEntityItemUpdate(stack: ItemStack, entity: ItemEntity): Boolean {
        FireproofItemLogic.update(entity)
        return true
    }
}

class FireproofSwordItem(tier: IItemTier, attackDamage: Int, attackSpeed: Float, properties: Properties
) : SwordItem(tier, attackDamage, attackSpeed, properties) {
    override fun onEntityItemUpdate(stack: ItemStack, entity: ItemEntity): Boolean {
        FireproofItemLogic.update(entity)
        return true
    }
}
