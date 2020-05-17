@file:Suppress("UNUSED_PARAMETER")

package thedarkcolour.futuremc

import net.minecraft.block.BlockDispenser
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.BannerPattern
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.core.block.IProjectileDispenserBehaviour
import thedarkcolour.core.command.GenerateCommand
import thedarkcolour.core.command.HealCommand
import thedarkcolour.core.command.ModeToggleCommand
import thedarkcolour.core.util.TODO
import thedarkcolour.core.util.registerOrDispenserBehaviour
import thedarkcolour.core.util.registerServerDispenserBehaviour
import thedarkcolour.core.util.runOnClient
import thedarkcolour.futuremc.block.BlockFlower
import thedarkcolour.futuremc.capability.SwimmingCapability
import thedarkcolour.futuremc.client.gui.GuiType
import thedarkcolour.futuremc.client.tesr.bell.BellTileEntityRenderer
import thedarkcolour.futuremc.client.tesr.campfire.CampfireRenderer
import thedarkcolour.futuremc.command.FastGiveCommand
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.entity.trident.EntityTrident
import thedarkcolour.futuremc.event.Events
import thedarkcolour.futuremc.item.ItemBannerPattern
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.registry.FItems
import thedarkcolour.futuremc.registry.FParticles
import thedarkcolour.futuremc.tile.BeeHiveTile
import thedarkcolour.futuremc.tile.BellTileEntity
import thedarkcolour.futuremc.tile.CampfireTile
import thedarkcolour.futuremc.world.gen.feature.BambooFeature
import thedarkcolour.futuremc.world.gen.feature.BeeNestGenerator
import thedarkcolour.futuremc.world.gen.feature.WorldGenFlower

@Suppress("MemberVisibilityCanBePrivate")
@Mod(
    modid = FutureMC.ID,
    name = FutureMC.NAME,
    version = FutureMC.VERSION,
    dependencies = FutureMC.DEPENDENCIES,
    modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter",
    useMetadata = true
)
object FutureMC {
    const val ID = "futuremc"
    const val NAME = "Future MC"
    const val VERSION = "0.2.2"
    const val DEPENDENCIES = "required-after:forgelin;required-after:forge@[14.23.5.2847,)"

    const val DEBUG = false

    init {
        Events.registerEvents()
    }

    @EventHandler
    fun init(event: FMLInitializationEvent) {
        if (DEBUG) {
            SwimmingCapability.register()
        }

        GuiType.registerGuiHandler()

        if (FConfig.updateAquatic.trident) {
            registerOrDispenserBehaviour(FItems.TRIDENT, IProjectileDispenserBehaviour(::EntityTrident))
        }
        if (FConfig.buzzyBees.bee.enabled) {
            registerServerDispenserBehaviour(Items.SHEARS) { worldIn, source, stack ->
                val pos = source.blockPos.offset(source.blockState.getValue(BlockDispenser.FACING))
                val te = worldIn.getTileEntity(pos)

                if (te is BeeHiveTile && te.honeyLevel >= 5) {
                    if (stack.attemptDamageItem(1, worldIn.rand, null)) {
                        stack.count = 0
                    }

                    te.dropHoneyCombs(worldIn, pos)
                    te.emptyHoney(worldIn, pos, null)
                }

                stack
            }
            registerServerDispenserBehaviour(Items.GLASS_BOTTLE) { worldIn, source, stack ->
                val pos = source.blockPos.offset(source.blockState.getValue(BlockDispenser.FACING))
                val te = worldIn.getTileEntity(pos)

                if (te is BeeHiveTile && te.honeyLevel >= 5) {
                    te.emptyHoney(worldIn, pos, null)
                    ItemStack(FItems.HONEY_BOTTLE)
                } else stack
            }
        }

        if (FConfig.villageAndPillage.loom.enabled) {
            val params = arrayOf(String::class.java, String::class.java)
            ItemBannerPattern.GLOBE = EnumHelper.addEnum(BannerPattern::class.java, "GLOBE", params, "globe", "glo")!!
        }

        registerWorldGen()

        FParticles.registerParticles()

        // TESR registering
        runOnClient {
            ClientRegistry.bindTileEntitySpecialRenderer(BellTileEntity::class.java, BellTileEntityRenderer())
            ClientRegistry.bindTileEntitySpecialRenderer(CampfireTile::class.java, CampfireRenderer)
            //ClientRegistry.bindTileEntitySpecialRenderer(TileSeagrassRenderer::class.java, TESRSeagrassRenderer)

            /*(Minecraft.getMinecraft().resourceManager as IReloadableResourceManager).registerReloadListener(
                ISelectiveResourceReloadListener { _, predicate ->
                    if (predicate.test(VanillaResourceType.TEXTURES)) {
                        val field = Fluid::class.java.getDeclaredField("color")
                        field.isAccessible = true
                        field.set(FluidRegistry.WATER, 0x3f76e4)

                        // fix crash before getting to load screen

                        kotlin.runCatching {
                            if (FConfig.updateAquatic.newWaterColor) {
                                val textures =
                                    ObfuscationReflectionHelper.getPrivateValue<Array<TextureAtlasSprite>, BlockFluidRenderer>(
                                        BlockFluidRenderer::class.java,
                                        Minecraft.getMinecraft().blockRendererDispatcher.fluidRenderer,
                                        "field_178271_b"
                                    )

                                val map = Minecraft.getMinecraft().textureMapBlocks

                                textures[0] = map.getAtlasSprite("futuremc:blocks/water_still")
                                textures[1] = map.getAtlasSprite("futuremc:blocks/water_flow")
                            }
                        }
                    }
                }
            )*/
        }
    }

    private fun registerWorldGen() {
        if (FConfig.villageAndPillage.lilyOfTheValley.enabled) {
            GameRegistry.registerWorldGenerator(WorldGenFlower(FBlocks.LILY_OF_THE_VALLEY as BlockFlower), 0)
        }
        if (FConfig.villageAndPillage.cornflower.enabled) {
            GameRegistry.registerWorldGenerator(WorldGenFlower(FBlocks.CORNFLOWER as BlockFlower), 0)
        }
        if (FConfig.villageAndPillage.sweetBerryBush.enabled) {
            GameRegistry.registerWorldGenerator(WorldGenFlower(FBlocks.SWEET_BERRY_BUSH as BlockFlower), 0)
        }
        if (FConfig.villageAndPillage.bamboo.enabled) {
            GameRegistry.registerWorldGenerator(BambooFeature, 0)
        }
        if (FConfig.buzzyBees.bee.enabled) {
            BeeNestGenerator.refresh()
        }
    }

    @EventHandler
    fun onServerStart(event: FMLServerStartingEvent) {
        event.registerServerCommand(FastGiveCommand)
        if (TODO()) {
            event.registerServerCommand(HealCommand())
            event.registerServerCommand(ModeToggleCommand())
            event.registerServerCommand(GenerateCommand())
        }
    }

    // Initialize after config
    // todo split into object to match hardcore dungeons and fmc 1.14/15
    lateinit var GROUP: CreativeTabs

    @JvmField
    val LOGGER: Logger = LogManager.getLogger()

    val CLIENT = FMLCommonHandler.instance().side == Side.CLIENT
}