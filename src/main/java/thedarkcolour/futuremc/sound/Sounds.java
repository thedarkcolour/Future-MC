package thedarkcolour.futuremc.sound;

import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistry;
import thedarkcolour.futuremc.FutureMC;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = FutureMC.ID)
public class Sounds extends SoundEvents {
    public static final SoundEvent HONEY_BLOCK_BREAK = soundEventOf("honey_block_break");
    public static final SoundEvent HONEY_BLOCK_STEP = soundEventOf("honey_block_step");
    public static final SoundEvent HONEY_BLOCK_SLIDE = soundEventOf("honey_block_slide");
    public static final SoundEvent HONEY_BOTTLE_DRINK = soundEventOf("honey_bottle_drink");
    public static final SoundEvent BEE_ENTER_HIVE = soundEventOf("bee_enter_hive");
    public static final SoundEvent BEE_EXIT_HIVE = soundEventOf("bee_exit_hive");
    public static final SoundEvent BEE_STING = soundEventOf("bee_sting");
    public static final SoundEvent BEE_DEATH = soundEventOf("bee_death");
    public static final SoundEvent BEE_HURT = soundEventOf("bee_hurt");
    public static final SoundEvent BEE_POLLINATE = soundEventOf("bee_pollinate");
    public static final SoundEvent BEE_WORK = soundEventOf("bee_work");
    public static final SoundEvent BEE_AGGRESSIVE = soundEventOf("bee_aggressive");
    public static final SoundEvent BEE_PASSIVE = soundEventOf("bee_passive");
    public static final SoundEvent BEEHIVE_SHEAR = soundEventOf("beehive_shear");

    public static final SoundType HONEY_BLOCK = new SoundType(1F, 1F, HONEY_BLOCK_BREAK, HONEY_BLOCK_STEP, HONEY_BLOCK_BREAK, HONEY_BLOCK_BREAK, HONEY_BLOCK_STEP);

    public static SoundEvent soundEventOf(String name) {
        ResourceLocation location = new ResourceLocation(FutureMC.ID, name);
        return new SoundEvent(location).setRegistryName(location);
    }

    @SubscribeEvent
    public static void onSoundRegistry(final RegistryEvent.Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> registry = event.getRegistry();

        registry.register(HONEY_BLOCK_BREAK);
        registry.register(HONEY_BLOCK_STEP);
        registry.register(HONEY_BLOCK_SLIDE);
        registry.register(HONEY_BOTTLE_DRINK);
        registry.register(BEE_ENTER_HIVE);
        registry.register(BEE_EXIT_HIVE);
        registry.register(BEE_STING);
        registry.register(BEE_DEATH);
        registry.register(BEE_HURT);
        registry.register(BEE_POLLINATE);
        registry.register(BEE_WORK);
        registry.register(BEE_AGGRESSIVE);
        registry.register(BEE_PASSIVE);
        registry.register(BEEHIVE_SHEAR);
    }
}