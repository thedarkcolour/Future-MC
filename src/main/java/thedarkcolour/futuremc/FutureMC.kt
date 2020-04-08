@file:Suppress("UNUSED_PARAMETER")

package thedarkcolour.futuremc

import net.minecraft.block.BlockDispenser
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BlockFluidRenderer
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.IReloadableResourceManager
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.dispenser.IBlockSource
import net.minecraft.init.Blocks
import net.minecraft.init.Bootstrap
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.BannerPattern
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.ObfuscationReflectionHelper
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.core.block.IProjectileDispenserBehaviour
import thedarkcolour.core.command.GenerateCommand
import thedarkcolour.core.command.HealCommand
import thedarkcolour.core.command.ModeToggleCommand
import thedarkcolour.core.gui.Gui
import thedarkcolour.core.util.registerDispenserBehaviour
import thedarkcolour.core.util.runOnClient
import thedarkcolour.futuremc.block.BlockFlower
import thedarkcolour.futuremc.capability.SwimmingCapability
import thedarkcolour.futuremc.client.tesr.bell.BellRenderer
import thedarkcolour.futuremc.client.tesr.campfire.CampfireRenderer
import thedarkcolour.futuremc.command.FastGiveCommand
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.entity.trident.EntityTrident
import thedarkcolour.futuremc.event.Events
import thedarkcolour.futuremc.item.ItemBannerPattern
import thedarkcolour.futuremc.recipe.campfire.CampfireRecipes
import thedarkcolour.futuremc.recipe.furnace.BlastFurnaceRecipes
import thedarkcolour.futuremc.recipe.furnace.SmokerRecipes
import thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipes
import thedarkcolour.futuremc.registry.FBlocks
import thedarkcolour.futuremc.registry.FItems
import thedarkcolour.futuremc.registry.FParticles
import thedarkcolour.futuremc.tile.BeeHiveTile
import thedarkcolour.futuremc.tile.TileBell
import thedarkcolour.futuremc.tile.TileCampfire
import thedarkcolour.futuremc.world.gen.feature.BeeNestGenerator
import thedarkcolour.futuremc.world.gen.feature.WorldGenBamboo
import thedarkcolour.futuremc.world.gen.feature.WorldGenFlower

@Suppress("MemberVisibilityCanBePrivate")
@Mod(
    modid = FutureMC.ID,
    name = FutureMC.NAME,
    version = FutureMC.VERSION,
    dependencies = FutureMC.DEPENDENCIES,
    modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter",
    guiFactory = "thedarkcolour.futuremc.config.GuiFactory",
    useMetadata = true
)
object FutureMC {
    const val ID = "futuremc"
    const val NAME = "Future MC"
    const val VERSION = "0.2.01"
    const val DEPENDENCIES = "after:forgelin"

    // Set this to false
    const val DEBUG = true

    init {
        Events.registerEvents()
    }

    @EventHandler
    fun init(event: FMLInitializationEvent) {
        if (DEBUG) {
            SwimmingCapability.register()
        }

        Gui.registerGuiHandler()

        if (FConfig.updateAquatic.trident) {
            registerDispenserBehaviour(FItems.TRIDENT, IProjectileDispenserBehaviour(::EntityTrident))
        }
        if (FConfig.buzzyBees.bee.enabled) {
            registerDispenserBehaviour(Items.SHEARS, object : Bootstrap.BehaviorDispenseOptional() {
                override fun dispenseStack(source: IBlockSource, stack: ItemStack): ItemStack {
                    val worldIn = source.world
                    if (!worldIn.isRemote) {
                        val pos = source.blockPos.offset(source.blockState.getValue(BlockDispenser.FACING))
                        val te = worldIn.getTileEntity(pos)

                        if (te is BeeHiveTile && te.honeyLevel >= 5) {
                            if (stack.attemptDamageItem(1, worldIn.rand, null)) {
                                stack.count = 0
                            }

                            te.dropHoneyCombs(worldIn, pos)
                        }
                    }

                    return stack
                }
            })
            registerDispenserBehaviour(Items.SHEARS, object : Bootstrap.BehaviorDispenseOptional() {
                override fun dispenseStack(source: IBlockSource, stack: ItemStack): ItemStack {
                    val worldIn = source.world
                    if (!worldIn.isRemote) {
                        val pos = source.blockPos.offset(source.blockState.getValue(BlockDispenser.FACING))
                        val te = worldIn.getTileEntity(pos)

                        if (te is BeeHiveTile && te.honeyLevel >= 5) {
                            if (stack.attemptDamageItem(1, worldIn.rand, null)) {
                                stack.count = 0
                            }

                            te.dropHoneyCombs(worldIn, pos)
                        }
                    }

                    return stack
                }
            })
        }

        if (FConfig.villageAndPillage.loom.enabled) {
            val params = arrayOf(String::class.java, String::class.java)
            ItemBannerPattern.GLOBE = EnumHelper.addEnum(BannerPattern::class.java, "GLOBE", params, "globe", "glo")!!
        }

        registerWorldGen()

        FParticles.registerParticles()

        // TESR registering
        runOnClient {
            ClientRegistry.bindTileEntitySpecialRenderer(TileBell::class.java, BellRenderer())
            ClientRegistry.bindTileEntitySpecialRenderer(TileCampfire::class.java, CampfireRenderer)
            //ClientRegistry.bindTileEntitySpecialRenderer(TileSeagrassRenderer::class.java, TESRSeagrassRenderer)

            (Minecraft.getMinecraft().resourceManager as IReloadableResourceManager).registerReloadListener {
                val field = Fluid::class.java.getDeclaredField("color")
                field.isAccessible = true
                field.set(FluidRegistry.WATER, 0x3f76e4)

                if (FConfig.updateAquatic.newWaterColor) {
                    val textures = ObfuscationReflectionHelper.getPrivateValue<Array<TextureAtlasSprite>, BlockFluidRenderer>(
                        BlockFluidRenderer::class.java, Minecraft.getMinecraft().blockRendererDispatcher.fluidRenderer, "field_178271_b")

                    val map = Minecraft.getMinecraft().textureMapBlocks

                    textures[0] = map.getAtlasSprite("futuremc:blocks/water_still")
                    textures[1] = map.getAtlasSprite("futuremc:blocks/water_flow")
                }
            }
        }
        //StonecutterRecipes.validate()
    }

    @EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        // Recipes
        GameRegistry.addSmelting(ItemStack(Blocks.STONE), ItemStack(FBlocks.SMOOTH_STONE), 0.1f)
        GameRegistry.addSmelting(ItemStack(Blocks.QUARTZ_BLOCK), ItemStack(FBlocks.SMOOTH_QUARTZ), 0.1f)
        CampfireRecipes.addDefaults()
        StonecutterRecipes.addDefaults()
        BlastFurnaceRecipes.addDefaults()
        SmokerRecipes.addDefaults()
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
            GameRegistry.registerWorldGenerator(WorldGenBamboo, 0)
        }
        if (FConfig.buzzyBees.bee.enabled) {
            BeeNestGenerator.refresh()
        }
    }

    @EventHandler
    fun onServerStart(event: FMLServerStartingEvent) {
        event.registerServerCommand(FastGiveCommand)
        if (DEBUG) {
            event.registerServerCommand(HealCommand())
            event.registerServerCommand(ModeToggleCommand())
            event.registerServerCommand(GenerateCommand())
        }
    }

    // Initialize after config
    lateinit var TAB: CreativeTabs

    @JvmField
    val LOGGER: Logger = LogManager.getLogger()

    val CLIENT = FMLCommonHandler.instance().side == Side.CLIENT
}