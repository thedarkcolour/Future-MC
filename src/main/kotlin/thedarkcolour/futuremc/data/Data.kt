package thedarkcolour.futuremc.data

import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
object Data {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val gen = event.generator
        val helper = event.existingFileHelper

        if (event.includeClient()) {
            gen.addProvider(Lang(gen))
            gen.addProvider(BlockModelGenerator(gen, helper))
            //gen.addProvider(ItemModelGenerator(gen, helper))
        }
        if (event.includeServer()) {
            //gen.addProvider(RecipeGenerator(gen))
            gen.addProvider(LootGenerator(gen))
            gen.addProvider(BlockTagGenerator(gen))
        }
    }
}