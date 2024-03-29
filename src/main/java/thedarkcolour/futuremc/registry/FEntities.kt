package thedarkcolour.futuremc.registry

import net.minecraft.client.renderer.entity.RenderIronGolem
import net.minecraft.entity.Entity
import net.minecraft.entity.EnumCreatureType
import net.minecraft.init.Biomes
import net.minecraft.util.ResourceLocation
import net.minecraft.world.storage.loot.LootTableList
import net.minecraftforge.fml.common.registry.EntityRegistry
import thedarkcolour.core.util.registerEntity
import thedarkcolour.core.util.registerEntityModel
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.compat.OE
import thedarkcolour.futuremc.compat.isModLoaded
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.entity.bee.BeeRenderer
import thedarkcolour.futuremc.entity.bee.EntityBee
import thedarkcolour.futuremc.entity.fish.cod.EntityCod
import thedarkcolour.futuremc.entity.fish.cod.RenderCod
import thedarkcolour.futuremc.entity.fish.pufferfish.EntityPufferfish
import thedarkcolour.futuremc.entity.fish.pufferfish.RenderPufferfish
import thedarkcolour.futuremc.entity.fish.salmon.EntitySalmon
import thedarkcolour.futuremc.entity.fish.salmon.RenderSalmon
import thedarkcolour.futuremc.entity.fish.tropical.EntityTropicalFish
import thedarkcolour.futuremc.entity.fish.tropical.RenderTropicalFish
import thedarkcolour.futuremc.entity.irongolem.LayerIronGolemCrack
import thedarkcolour.futuremc.entity.panda.EntityPanda
import thedarkcolour.futuremc.entity.panda.RenderPanda
import thedarkcolour.futuremc.entity.trident.RenderTrident
import thedarkcolour.futuremc.entity.trident.Trident

object FEntities {
    fun registerEntities() {
        if (!isModLoaded(OE) || !FConfig.updateAquatic.oceanicExpanse) {
            if (FConfig.updateAquatic.trident) {
                registerEntity("trident", Trident::class.java, 32, 1)
            }
        }

        if (FConfig.villageAndPillage.panda && FConfig.villageAndPillage.bamboo.enabled) {
            registerEntity("panda", EntityPanda::class.java, 36, 3, 15198183, 1776418)
            EntityRegistry.addSpawn(EntityPanda::class.java, 14, 1, 2, EnumCreatureType.CREATURE, Biomes.JUNGLE, Biomes.JUNGLE_HILLS, Biomes.MUTATED_JUNGLE)
        }
        if (FConfig.buzzyBees.bee.enabled) {
            registerEntity("bee", EntityBee::class.java, 32, 4, 16770398, 2500144)
        }

        if (!isModLoaded(OE) || !FConfig.updateAquatic.oceanicExpanse) {
            if (FConfig.updateAquatic.fish.cod.enabled) {
                registerEntity("cod", EntityCod::class.java, 32, 5, 12691306, 15058059)
                LootTableList.register(EntityCod.LOOT_TABLE)
            }
            if (FConfig.updateAquatic.fish.pufferfish.enabled) {
                registerEntity("pufferfish", EntityPufferfish::class.java, 32, 6, 16167425, 3654642)
                LootTableList.register(EntityPufferfish.LOOT_TABLE)
            }
            if (FConfig.updateAquatic.fish.salmon.enabled) {
                registerEntity("salmon", EntitySalmon::class.java, 32, 7, 10489616, 951412)
                LootTableList.register(EntitySalmon.LOOT_TABLE)
            }
            if (FConfig.updateAquatic.fish.tropicalFish.enabled) {
                registerEntity("tropical_fish", EntityTropicalFish::class.java, 32, 8, 15690005, 16775663)
                LootTableList.register(EntityTropicalFish.LOOT_TABLE)
            }
        }
    }

    fun registerEntityRenderers() {
        if (!isModLoaded(OE) || !FConfig.updateAquatic.oceanicExpanse) {
            if (FConfig.updateAquatic.trident) {
                registerEntityModel { RenderTrident(it) }
            }
            if (FConfig.updateAquatic.fish.cod.enabled) {
                registerEntityModel { RenderCod(it) }
            }
            if (FConfig.updateAquatic.fish.pufferfish.enabled) {
                registerEntityModel { RenderPufferfish(it) }
            }
            if (FConfig.updateAquatic.fish.salmon.enabled) {
                registerEntityModel { RenderSalmon(it) }
            }
            if (FConfig.updateAquatic.fish.tropicalFish.enabled) {
                registerEntityModel { RenderTropicalFish(it) }
            }
        }
        if (FConfig.villageAndPillage.panda && FConfig.villageAndPillage.bamboo.enabled) {
            registerEntityModel { RenderPanda(it) }
        }
        if (FConfig.buzzyBees.bee.enabled) {
            registerEntityModel { BeeRenderer(it) }
        }

        // of course they do :)
        if (FConfig.buzzyBees.ironGolem.doCrack) {
            registerEntityModel { manager ->
                val renderer = RenderIronGolem(manager)
                renderer.addLayer(LayerIronGolemCrack(renderer))
                renderer
            }
        }
    }



    /**
     * Helper method to reduce verbosity when registering entities.
     */
    fun registerEntity(name: String, entity: Class<out Entity>, trackingRange: Int, id: Int) {
        EntityRegistry.registerModEntity(ResourceLocation(FutureMC.ID, name), entity, name, id, FutureMC, trackingRange, 1, true)
    }
}