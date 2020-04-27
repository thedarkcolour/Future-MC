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
import java.util.function.Function
import kotlin.math.abs
import kotlin.math.sqrt

object LocateBiomeCommand {
    private val ALL_BIOMES = SuggestionProviders.register<CommandSource>(ResourceLocation("all_biomes")) { _, builder ->
        ISuggestionProvider.suggestIterable(ForgeRegistries.BIOMES.keys, builder)
    }

    private val INVALID_EXCEPTION =
        DynamicCommandExceptionType(Function { TranslationTextComponent("commands.futuremc.locatebiome.invalid", it) })
    private val NOT_FOUND_EXCEPTION =
        DynamicCommandExceptionType(Function { TranslationTextComponent("commands.futuremc.locatebiome.notFound", it) })

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
        val pos2 = world.chunkProvider.chunkGenerator.biomeProvider.findBiome(pos.x, pos.y, pos.z, 6400, 8, listOf(biome), world.rand)
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

    private fun BiomeProvider.findBiome(i: Int, j: Int, k: Int, l: Int, m: Int, list: List<Biome>, random: Random, bl: Boolean = true): BlockPos? {
        val n = i shr 2
        val o = k shr 2
        val p = l shr 2
        val q = j shr 2
        var pos: BlockPos? = null
        val r = 0
        var t = 0

        while (t <= p) {
            var u = -1

            while (u <= t) {
                val bl2 = abs(u) == t
                var v = -t

                while (v <= t) {
                    if (bl2) {
                        val bl3 = abs(v) == t

                        if (!bl3 && !bl2) {
                            continue
                        }
                    }
                    val w = n + v
                    val x = o + u

                    if (list.contains(getBiomeForNoiseGen(w, q, x))) {
                        if (pos == null || random.nextInt(r + 1) == 0) {
                            pos = BlockPos(w shl 2, j, x shl 2)

                            if (bl) {
                                return pos
                            }
                        }
                    }
                    v += m
                }

                u += m
            }
            t += m
        }

        return pos
    }

    private fun suggestBiome(ctx: CommandContext<CommandSource>): Biome {
        val location = ctx.getArgument("biome", ResourceLocation::class.java)
        return ForgeRegistries.BIOMES.getValue(location) ?: throw INVALID_EXCEPTION.create(location)
    }
}