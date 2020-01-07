@file:Suppress("UNUSED_PARAMETER")

package thedarkcolour.futuremc

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.monster.EntityIronGolem
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTBase
import net.minecraft.tileentity.BannerPattern
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.config.Config
import net.minecraftforge.common.config.ConfigManager
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.core.block.Generator
import thedarkcolour.core.block.IProjectileDispenserBehaviour
import thedarkcolour.core.command.CommandGenerate
import thedarkcolour.core.command.CommandHeal
import thedarkcolour.core.command.CommandModeToggle
import thedarkcolour.core.gui.Gui
import thedarkcolour.core.util.addListener
import thedarkcolour.core.util.registerDispenserBehaviour
import thedarkcolour.core.util.runOnClient
import thedarkcolour.core.util.subscribe
import thedarkcolour.futuremc.block.BlockFlower
import thedarkcolour.futuremc.capability.CapabilityHandler
import thedarkcolour.futuremc.capability.ISwimmingCapability
import thedarkcolour.futuremc.capability.SwimmingCapability
import thedarkcolour.futuremc.client.particle.ParticleCampfire
import thedarkcolour.futuremc.client.particle.Particles
import thedarkcolour.futuremc.client.tesr.bell.TESRBell
import thedarkcolour.futuremc.client.tesr.campfire.TESRCampfire
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.config._Internal
import thedarkcolour.futuremc.entity.trident.EntityTrident
import thedarkcolour.futuremc.init.FBlocks
import thedarkcolour.futuremc.init.FItems
import thedarkcolour.futuremc.init.RegistryEventHandler
import thedarkcolour.futuremc.init.Sounds
import thedarkcolour.futuremc.item.ItemBannerPattern
import thedarkcolour.futuremc.recipe.campfire.CampfireRecipes
import thedarkcolour.futuremc.recipe.furnace.BlastFurnaceRecipes
import thedarkcolour.futuremc.recipe.furnace.SmokerRecipes
import thedarkcolour.futuremc.recipe.stonecutter.StonecutterRecipes
import thedarkcolour.futuremc.tile.TileBell
import thedarkcolour.futuremc.tile.TileCampfire
import thedarkcolour.futuremc.world.OldWorldHandler
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
        useMetadata = true
)
object FutureMC {
    init {
        registerEventListeners()

        CapabilityManager.INSTANCE.register(
                ISwimmingCapability::class.java,
                object : Capability.IStorage<ISwimmingCapability> {
                    override fun readNBT(capability: Capability<ISwimmingCapability>, instance: ISwimmingCapability, side: EnumFacing, nbt: NBTBase) {}
                    override fun writeNBT(capability: Capability<ISwimmingCapability>, instance: ISwimmingCapability, side: EnumFacing): NBTBase? = null
                },
                ::SwimmingCapability
        )
    }

    fun registerEventListeners() {
        subscribe(this)
        subscribe(RegistryEventHandler)
        subscribe(OldWorldHandler::class.java)
        subscribe(_Internal::class.java)
        addListener { event: ConfigChangedEvent ->
            if (event.modID == ID) {
                ConfigManager.sync(ID, Config.Type.INSTANCE)
            }
        }
        addListener { event: AttachCapabilitiesEvent<Entity> ->
            CapabilityHandler.attachCapability(event)
        }
        addListener { event: PlayerInteractEvent.EntityInteract ->
            if (FConfig.buzzyBees.ironGolems.ironBarHealing) {
                val entity = event.target
                if (entity is EntityIronGolem && event.itemStack.item == Items.IRON_INGOT) {
                    val hp = entity.health
                    entity.heal(25f)
                    // only heal if the entity HP was increased
                    if (hp != entity.health) {
                        val rand = entity.rng
                        val pitch = 1.0f + (rand.nextFloat() - rand.nextFloat()) * 0.2f
                        entity.playSound(Sounds.IRON_GOLEM_REPAIR, 1.0f, pitch)
                        if (!event.entityPlayer.isCreative) {
                            event.itemStack.shrink(1)
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        runOnClient {
            ClientRegistry.bindTileEntitySpecialRenderer(TileBell::class.java, TESRBell())
            ClientRegistry.bindTileEntitySpecialRenderer(TileCampfire::class.java, TESRCampfire())
        }
    }

    @EventHandler
    fun init(event: FMLInitializationEvent) {
        Generator.setup()
        Gui.setup()
        GameRegistry.addSmelting(ItemStack(Blocks.STONE), ItemStack(FBlocks.SMOOTH_STONE), 0.1f)
        GameRegistry.addSmelting(ItemStack(Blocks.QUARTZ_BLOCK), ItemStack(FBlocks.SMOOTH_QUARTZ), 0.1f)
        CampfireRecipes.addDefaults()
        StonecutterRecipes.addDefaults()
        BlastFurnaceRecipes.addDefaults()
        SmokerRecipes.addDefaults()

        if (FConfig.updateAquatic.trident) {
            registerDispenserBehaviour(FItems.TRIDENT, IProjectileDispenserBehaviour { world, pos, stack -> EntityTrident(world, pos, stack) })
        }

        if (FConfig.villageAndPillage.loom.enabled) {
            val params = arrayOf(String::class.java, String::class.java)
            ItemBannerPattern.GLOBE = EnumHelper.addEnum(BannerPattern::class.java, "GLOBE", params, "globe", "glo")!!
        }

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

        if (FConfig.buzzyBees.bee) {
            BeeNestGenerator.init()
        }

        Particles.CAMPFIRE_COSY_SMOKE = Particles.registerParticle(
                "CAMPFIRE_COSY_SMOKE",
                "campfireCosySmoke",
                true,
                ParticleCampfire.CosyFactory()
        )
        Particles.CAMPFIRE_SIGNAL_SMOKE = Particles.registerParticle(
                "CAMPFIRE_SIGNAL_SMOKE",
                "campfireSignalSmoke",
                true,
                ParticleCampfire.SignalFactory()
        )
    }

    @EventHandler
    fun onServerStart(event: FMLServerStartingEvent) {
        if (DEBUG) {
            event.registerServerCommand(CommandHeal())
            event.registerServerCommand(CommandModeToggle())
            event.registerServerCommand(CommandGenerate())
        }

        //if (FConfig.updateAquatic.dataCommand) {
        //    event.registerServerCommand(CommandData)
        //}
    }

    const val ID = "futuremc"
    const val NAME = "Future MC"
    const val VERSION = "0.2.0"
    const val DEPENDENCIES = "required-after:forge@[14.23.5.2847,);required-after:forgelin@[1.8.4);"

    // Initialize after config
    lateinit var TAB: CreativeTabs

    val LOGGER: Logger = LogManager.getLogger()

    val CLIENT = FMLCommonHandler.instance().side == Side.CLIENT

    val DEBUG = FMLCommonHandler.instance().side == Side.CLIENT
}