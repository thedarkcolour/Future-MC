package thedarkcolour.futuremc.registry

import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.init.MobEffects
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.Ingredient
import net.minecraft.item.crafting.ShapedRecipes
import net.minecraft.potion.PotionEffect
import net.minecraft.util.NonNullList
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.OreIngredient
import net.minecraftforge.oredict.ShapelessOreRecipe
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.IForgeRegistryModifiable
import thedarkcolour.futuremc.compat.checkActuallyAdditions
import thedarkcolour.futuremc.compat.checkHarvestCraft
import thedarkcolour.futuremc.compat.checkPlants
import thedarkcolour.futuremc.compat.checkTConstruct
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.item.SuspiciousStewItem
import thedarkcolour.futuremc.recipe.SimpleRecipe
import thedarkcolour.futuremc.recipe.crafting.TrapdoorRecipe
import thedarkcolour.futuremc.recipe.furnace.BlastFurnaceRecipes

/**
 * Object declaration that handles custom recipes and
 * for some reason also mod compatibility for Future MC.
 *
 * TODO move mod compatibility to an object in thedarkcolour.futuremc.compat package
 *
 * @author TheDarkColour
 */
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
        GameRegistry.addSmelting(ItemStack(FBlocks.ANCIENT_DEBRIS), ItemStack(FItems.NETHERITE_SCRAP), 2.0f)

        recipes.addShapedRecipe(
            id = "minecraft:nether_brick_fence",
            result = ItemStack(Blocks.NETHER_BRICK_FENCE),
            pattern = arrayOf("#X#", "#X#"),
            key = mapOf(
                '#' to Ingredient.fromItem(Items.NETHERBRICK),
                'X' to OreIngredient("ingotBrickNether")
            )
        )

        recipes.register(TrapdoorRecipe.create().setRegistryName("futuremc:wooden_trapdoor"))

        // actually additions compat with bees
        checkActuallyAdditions()?.registerPollinationTargets()
        checkActuallyAdditions()?.registerPollinationHandlers()
        // harvestcraft compat with bees
        checkHarvestCraft()?.registerPollinationHandlers()
        // plants compat with bees
        checkPlants()?.registerPollinationTargets()

        if (FConfig.netherUpdate.netherite) {
            recipes.addShapelessRecipe(
                "futuremc:netherite_ingot",
                ItemStack(FItems.NETHERITE_INGOT),
                charArrayOf('A','A','A','A','B','B','B','B'),
                mapOf('A' to OreIngredient("ingotAncientDebris"), 'B' to OreIngredient("ingotGold"))
            )
        }
    }

    private fun IForgeRegistry<IRecipe>.addShapedRecipe(id: String, result: ItemStack, pattern: Array<String>, key: Map<Char, Ingredient>) {
        val height = pattern.size

        if (height > 3) {
            throw IllegalArgumentException("Pattern must have =< 3 rows")
        }

        var width = -1

        for (row in pattern) {
            if (width == -1) {
                width = row.length
            } else {
                if (row.length != width) {
                    throw IllegalArgumentException("Pattern must be a rectangle")
                }
            }
        }

        val ingredients = NonNullList.create<Ingredient>()

        for (row in pattern) {
            for (character in row) {
                ingredients.add(key[character])
            }
        }

        val recipe = ShapedRecipes("", width, height, ingredients, result)
        recipe.setRegistryName(id)

        register(recipe)
    }

    private fun IForgeRegistry<IRecipe>.addShapelessRecipe(
        id: String,
        result: ItemStack,
        pattern: CharArray,
        key: Map<Char, Ingredient>
    ) {
        if (pattern.size > 9) {
            throw IllegalArgumentException("Recipe must have 9 or less items")
        }

        val ingredients = Array(pattern.size) { i ->
            key[pattern[i]] ?: error("No value for char '${pattern[i]}'")
        }

        val recipe = ShapelessOreRecipe(null, result, *ingredients)
        recipe.setRegistryName(id)

        register(recipe)
    }

    fun registerFMCRecipes() {
        for (string in OreDictionary.getOreNames()) {
            if (string.startsWith("ore") || string.startsWith("dust")) {
                val ores = OreDictionary.getOres(string)

                ores.forEach { stack ->
                    val result = FurnaceRecipes.instance().getSmeltingResult(stack)
                    if (!result.isEmpty) {
                        BlastFurnaceRecipes.recipes.add(SimpleRecipe(stack, result))
                    }
                }
            }
        }

        checkTConstruct()?.registerStonecutterRecipes()
    }
}