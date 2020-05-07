package thedarkcolour.futuremc.item

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.item.crafting.ShapelessRecipes
import net.minecraft.potion.PotionEffect
import net.minecraft.potion.PotionUtils
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.fml.common.registry.ForgeRegistries
import thedarkcolour.core.util.setItemModel
import thedarkcolour.core.util.stack
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.registry.FItems

class ItemSuspiciousStew : ItemFood(6, 0.6f, false) {
    init {
        setMaxStackSize(1)
        setRegistryName("suspicious_stew")
        translationKey = "${FutureMC.ID}.suspicious_stew"
        if (!FConfig.useVanillaCreativeTabs) creativeTab = FutureMC.GROUP
        setItemModel(this, 0)
    }

    override fun onItemUseFinish(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase): ItemStack {
        super.onItemUseFinish(stack, worldIn, entityLiving)
        return ItemStack(Items.BOWL)
    }

    override fun onFoodEaten(stack: ItemStack, worldIn: World, player: EntityPlayer) {
        if (!worldIn.isRemote) {
            for (effect in PotionUtils.getFullEffectsFromItem(stack)) {
                player.addPotionEffect(effect)
            }
            /*if (stack.hasTagCompound()) {
                when (stack.tagCompound!!.getString("effect")) {
                    "REGEN" -> {
                        player.addPotionEffect(PotionEffect(MobEffects.REGENERATION, 140, 1))
                    }
                    "JUMP" -> {
                        player.addPotionEffect(PotionEffect(MobEffects.JUMP_BOOST, 100, 1))
                    }
                    "POISON" -> {
                        player.addPotionEffect(PotionEffect(MobEffects.POISON, 220, 1))
                    }
                    "WITHER" -> {
                        player.addPotionEffect(PotionEffect(MobEffects.WITHER, 140, 1))
                    }
                    "WEAKNESS" -> {
                        player.addPotionEffect(PotionEffect(MobEffects.WEAKNESS, 160, 1))
                    }
                    "BLINDNESS" -> {
                        player.addPotionEffect(PotionEffect(MobEffects.BLINDNESS, 140, 1))
                    }
                    "FIRE_RESISTANCE" -> {
                        player.addPotionEffect(PotionEffect(MobEffects.FIRE_RESISTANCE, 60, 1))
                    }
                    "SATURATION" -> {
                        player.addPotionEffect(PotionEffect(MobEffects.SATURATION, 100, 1))
                    }
                    "SPEED" -> {
                        player.addPotionEffect(PotionEffect(MobEffects.NIGHT_VISION, 100, 1))
                    }
                    "NULL" -> {
                    }
                }
            }*/
        }
    }

    companion object {
        fun addRecipe(flower: ItemStack, effect: PotionEffect) {
            val r = ShapelessRecipes(
                "",
                PotionUtils.appendEffects(FItems.SUSPICIOUS_STEW.stack, listOf(effect)),
                NonNullList.create<Ingredient>().also { ingredients ->
                    ingredients.addAll(
                        arrayOf(
                            Ingredient.fromStacks(ItemStack(Blocks.BROWN_MUSHROOM)),
                            Ingredient.fromStacks(ItemStack(Blocks.RED_MUSHROOM)),
                            Ingredient.fromStacks(ItemStack(Items.BOWL)), Ingredient.fromStacks(flower)
                        )
                    )
                })
            ForgeRegistries.RECIPES.register(r.setRegistryName("futuremc:suspicious_stew_${flower.item.registryName!!.path}"))
        }
    }
}