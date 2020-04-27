package thedarkcolour.futuremc.recipe

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.crafting.Ingredient
import net.minecraft.item.crafting.ShapedRecipe
import net.minecraft.network.PacketBuffer
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.ForgeRegistryEntry
import thedarkcolour.futuremc.registry.FRecipes
import thedarkcolour.futuremc.util.DarkInventory

class SmithingRecipe(
        private val id: ResourceLocation,
        private val ingredient: Ingredient,
        private val material: Ingredient,
        val materialCost: Int,
        private val result: ItemStack
) : DarkRecipe {
    override fun canFit(width: Int, height: Int): Boolean {
        return true
    }

    override fun getId(): ResourceLocation {
        return id
    }

    override fun getType(): DarkRecipeType<*> {
        return FRecipes.SMITHING
    }

    override fun getRecipeOutput(): ItemStack {
        return result
    }

    override fun getSerializer(): IRecipeSerializer<*> {
        return FRecipes.SMITHING_SERIALIZER
    }

    override fun getCraftingResult(inv: IInventory): ItemStack {
        return result.copy()
    }

    override fun matches(inv: IInventory, worldIn: World): Boolean {
        val a = ingredient.test(inv.getStackFrom(0))
        val b = material.test(inv.getStackFrom(1))
        val c = inv.getStackFrom(1).count >= materialCost

        return a && b && c
    }

    override fun matches(inv: DarkInventory, worldIn: World): Boolean {
        val a = ingredient.test(inv[0])
        val b = material.test(inv[1])
        val c = inv[1].count >= materialCost

        return a && b && c
    }

    class Serializer : ForgeRegistryEntry<IRecipeSerializer<*>>(), IRecipeSerializer<SmithingRecipe> {
        override fun read(recipeId: ResourceLocation, json: JsonObject): SmithingRecipe {
            val element = (if (JSONUtils.isJsonArray(json, "input")) {
                JSONUtils.getJsonArray(json, "input")
            } else {
                JSONUtils.getJsonObject(json, "input")
            }) as JsonElement
            val input = Ingredient.deserialize(element)
            val element1 = (if (JSONUtils.isJsonArray(json, "material")) {
                JSONUtils.getJsonArray(json, "material")
            } else {
                JSONUtils.getJsonObject(json, "material")
            }) as JsonElement
            val material = Ingredient.deserialize(element1)
            val materialCost = JSONUtils.getInt(json, "material_cost", 1)
            if (!json.has("result")) throw JsonSyntaxException("Missing result, expected to find a string or object")
            val result = if (json.get("result").isJsonObject) {
                ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"))
            } else {
                val s1 = JSONUtils.getString(json, "result")
                val location = ResourceLocation(s1)
                ItemStack(ForgeRegistries.ITEMS.getValue(location)!!)
            }
            return SmithingRecipe(recipeId, input, material, materialCost, result)
        }

        override fun read(recipeId: ResourceLocation, buffer: PacketBuffer): SmithingRecipe? {
            val input = Ingredient.read(buffer)
            val material = Ingredient.read(buffer)
            val materialCost = buffer.readInt()
            val result = buffer.readItemStack()
            return SmithingRecipe(recipeId, input, material, materialCost, result)
        }

        override fun write(buffer: PacketBuffer, recipe: SmithingRecipe) {
            recipe.ingredient.write(buffer)
            recipe.material.write(buffer)
            buffer.writeInt(recipe.materialCost)
            buffer.writeItemStack(recipe.result)
        }
    }
}