package thedarkcolour.futuremc.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraft.command.ISuggestionProvider
import net.minecraft.command.arguments.ResourceLocationArgument
import net.minecraft.command.arguments.SuggestionProviders
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.text.TextComponentUtils
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.util.text.event.ClickEvent
import net.minecraft.util.text.event.HoverEvent
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraftforge.registries.ForgeRegistries
import java.util.*
import kotlin.math.sqrt

object LocateBiomeCommand {
    private val ALL_BIOMES = SuggestionProviders.register<CommandSource>(ResourceLocation("all_biomes")) { _, builder ->
        ISuggestionProvider.suggestIterable(ForgeRegistries.BIOMES.keys, builder)
    }

    private val INVALID_EXCEPTION =
        DynamicCommandExceptionType { TranslationTextComponent("commands.futuremc.locatebiome.invalid", it) }
    private val NOT_FOUND_EXCEPTION =
        DynamicCommandExceptionType { TranslationTextComponent("commands.futuremc.locatebiome.notFound", it) }

    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        dispatcher.register(Commands.literal("locatebiome").requires {
            it.hasPermissionLevel(2)
        }.then(Commands.argument("biome", ResourceLocationArgument.resourceLocation()).suggests(ALL_BIOMES).executes { ctx ->
            execute(ctx.source, suggestBiome(ctx))
        }))
    }

    @Suppress("UsePropertyAccessSyntax")
    fun execute(source: CommandSource, biome: Biome): Int {
        val pos = BlockPos(source.pos)
        val world = source.world
        val pos2 = world.chunkProvider.chunkGenerator.biomeProvider.locateBiome(pos.x, pos.y, pos.z, 6400, 8, setOf(biome), world.rand)
            ?: throw NOT_FOUND_EXCEPTION.create(TranslationTextComponent(biome.translationKey).formattedText)

        val posX = pos.x - pos2.x
        val posZ = pos.z - pos2.z
        val i = MathHelper.floor(sqrt((posX * posX + posZ * posZ).toFloat()))
        val text = TextComponentUtils.wrapInSquareBrackets(TranslationTextComponent("chat.coordinates", pos2.x, "~", pos2.z)).applyTextStyle {

            it.setColor(TextFormatting.GREEN)
                .setClickEvent(ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + pos2.x + " ~ " + pos2.z))
                .setHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, TranslationTextComponent("chat.coordinates.tooltip")))
        }
        source.sendFeedback(TranslationTextComponent("commands.locate.success", TranslationTextComponent(biome.translationKey).formattedText, text, i), false)
        return i
    }

    private fun BiomeProvider.locateBiome(x: Int, y: Int, z: Int, radius: Int, grid: Int, biomes: Set<Biome>, rand: Random, findFirst: Boolean = true): BlockPos? {
        return BiomeLocator.locateBiome(this, x, y, z, radius, grid, biomes, rand, findFirst)
    }

    private fun suggestBiome(ctx: CommandContext<CommandSource>): Biome {
        val location = ctx.getArgument("biome", ResourceLocation::class.java)
        return ForgeRegistries.BIOMES.getValue(location) ?: throw INVALID_EXCEPTION.create(location)
    }
}