package thedarkcolour.futuremc.registry

import net.minecraft.init.Blocks
import net.minecraft.init.MobEffects
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.potion.PotionEffect
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.registries.IForgeRegistryModifiable
import thedarkcolour.futuremc.item.SuspiciousStewItem

// todo register all recipes here
//      including fmc modded recipes
object FRecipes {
    fun registerRecipes(recipes: IForgeRegistryModifiable<IRecipe>) {
        SuspiciousStewItem.addRecipe(recipes, ItemStack(Blocks.RED_FLOWER, 1, 8), PotionEffect(MobEffects.REGENERATION, 140, 1))
        SuspiciousStewItem.addRecipe(recipes, ItemStack(FBlocks.CORNFLOWER), PotionEffect(MobEffects.JUMP_BOOST, 100, 1))
        SuspiciousStewItem.addRecipe(recipes, ItemStack(FBlocks.LILY_OF_THE_VALLEY), PotionEffect(MobEffects.POISON, 220, 1))
        SuspiciousStewItem.addRecipe(recipes, ItemStack(FBlocks.WITHER_ROSE), PotionEffect(MobEffects.WITHER, 140, 1))
        SuspiciousStewItem.addRecipe(recipes, ItemStack(Blocks.RED_FLOWER, 1, 4), PotionEffect(MobEffects.WEAKNESS, 160, 1))
        SuspiciousStewItem.addRecipe(recipes, ItemStack(Blocks.RED_FLOWER, 1, 5), PotionEffect(MobEffects.WEAKNESS, 160, 1))
        SuspiciousStewItem.addRecipe(recipes, ItemStack(Blocks.RED_FLOWER, 1, 6), PotionEffect(MobEffects.WEAKNESS, 160, 1))
        SuspiciousStewItem.addRecipe(recipes, ItemStack(Blocks.RED_FLOWER, 1, 7), PotionEffect(MobEffects.WEAKNESS, 160, 1))
        SuspiciousStewItem.addRecipe(recipes, ItemStack(Blocks.RED_FLOWER, 1, 3), PotionEffect(MobEffects.BLINDNESS, 140, 1))
        SuspiciousStewItem.addRecipe(recipes, ItemStack(Blocks.RED_FLOWER, 1, 2), PotionEffect(MobEffects.FIRE_RESISTANCE, 60, 1))
        SuspiciousStewItem.addRecipe(recipes, ItemStack(Blocks.RED_FLOWER), PotionEffect(MobEffects.NIGHT_VISION, 100, 1))
        SuspiciousStewItem.addRecipe(recipes, ItemStack(Blocks.RED_FLOWER, 1, 1), PotionEffect(MobEffects.SATURATION, 100, 1))
        SuspiciousStewItem.addRecipe(recipes, ItemStack(Blocks.YELLOW_FLOWER), PotionEffect(MobEffects.SATURATION, 100, 1))

        GameRegistry.addSmelting(ItemStack(Blocks.STONE), ItemStack(FBlocks.SMOOTH_STONE), 0.1f)
        GameRegistry.addSmelting(ItemStack(Blocks.SANDSTONE), ItemStack(FBlocks.SMOOTH_SANDSTONE), 0.1f)
        GameRegistry.addSmelting(ItemStack(Blocks.QUARTZ_BLOCK), ItemStack(FBlocks.SMOOTH_QUARTZ), 0.1f)
        GameRegistry.addSmelting(ItemStack(Blocks.RED_SANDSTONE), ItemStack(FBlocks.SMOOTH_RED_SANDSTONE), 0.1f)

        recipes.remove(ResourceLocation("minecraft:nether_brick_fence"))
    }
}