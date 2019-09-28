package thedarkcolour.futuremc.sound;

import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thedarkcolour.futuremc.FutureMC;

@Mod.EventBusSubscriber
public final class Sounds {
    public static final SoundEvent TRIDENT_THROW = makeSoundEvent("throw_trident");
    public static final SoundEvent TRIDENT_PIERCE = makeSoundEvent("pierce_trident");
    public static final SoundEvent TRIDENT_IMPACT = makeSoundEvent("impact_trident");
    public static final SoundEvent TRIDENT_CONDUCTIVIDAD = makeSoundEvent("channeling_trident");
    public static final SoundEvent TRIDENT_LOYALTY = makeSoundEvent("loyalty");
    public static final SoundEvent TRIDENT_RIPTIDE_0 = makeSoundEvent("riptide_0");
    public static final SoundEvent TRIDENT_RIPTIDE_1 = makeSoundEvent("riptide_1");
    public static final SoundEvent TRIDENT_RIPTIDE_2 = makeSoundEvent("riptide_2");
    public static final SoundEvent CROSSBOW_CHARGE = makeSoundEvent("charge");
    public static final SoundEvent CROSSBOW_FIRE = makeSoundEvent("crossbow_fire");
    public static final SoundEvent CROSSBOW_QUICK_CHARGE = makeSoundEvent("quick_charge");
    public static final SoundEvent CROSSBOW_LOAD = makeSoundEvent("crossbow_load");
    public static final SoundEvent CAMPFIRE_CRACKLE = makeSoundEvent("campfire_crackle");
    public static final SoundEvent GRINDSTONE_USE = makeSoundEvent("grindstone_use");
    public static final SoundEvent COMPOSTER_EMPTY = makeSoundEvent("composter_empty");
    public static final SoundEvent COMPOSTER_FILL = makeSoundEvent("composter_fill");
    public static final SoundEvent COMPOSTER_FILL_SUCCESS = makeSoundEvent("composter_fill_success");
    public static final SoundEvent COMPOSTER_READY = makeSoundEvent("composter_ready");
    public static final SoundEvent PANDA_PRE_SNEEZE = makeSoundEvent("panda_pre_sneeze");
    public static final SoundEvent PANDA_SNEEZE = makeSoundEvent("panda_sneeze");
    public static final SoundEvent PANDA_AMBIENT = makeSoundEvent("panda_ambient");
    public static final SoundEvent PANDA_DEATH = makeSoundEvent("panda_death");
    public static final SoundEvent PANDA_EAT = makeSoundEvent("panda_eat");
    public static final SoundEvent PANDA_STEP = makeSoundEvent("panda_step");
    public static final SoundEvent PANDA_CANNOT_BREED = makeSoundEvent("panda_cannot_breed");
    public static final SoundEvent PANDA_AGGRESSIVE_AMBIENT = makeSoundEvent("panda_aggressive_ambient");
    public static final SoundEvent PANDA_WORRIED_AMBIENT = makeSoundEvent("panda_worried_ambient");
    public static final SoundEvent PANDA_HURT = makeSoundEvent("panda_hurt");
    public static final SoundEvent PANDA_BITE = makeSoundEvent("panda_bite");
    public static final SoundEvent BAMBOO_STEP = makeSoundEvent("bamboo_step");
    public static final SoundEvent BAMBOO_PLACE = makeSoundEvent("bamboo_place");
    public static final SoundEvent SCAFFOLD_BREAK = makeSoundEvent("scaffold_break");
    public static final SoundEvent SCAFFOLD_STEP = makeSoundEvent("scaffold_step");
    public static final SoundEvent SCAFFOLD_PLACE = makeSoundEvent("scaffold_place");
    public static final SoundEvent SCAFFOLD_FALL = makeSoundEvent("scaffold_fall");
    public static final SoundEvent SCAFFOLD_HIT = makeSoundEvent("scaffold_hit");
    public static final SoundEvent BELL_RING = makeSoundEvent("bell_ring");
    public static final SoundEvent BEE_ENTER_HIVE = makeSoundEvent("bee_enter_hive");
    public static final SoundEvent BEE_EXIT_HIVE = makeSoundEvent("bee_exit_hive");
    public static final SoundEvent BEE_STING = makeSoundEvent("bee_sting");
    public static final SoundEvent BEE_DEATH = makeSoundEvent("bee_death");
    public static final SoundEvent BEE_HURT = makeSoundEvent("bee_hurt");
    public static final SoundEvent BEE_POLLINATE = makeSoundEvent("bee_pollinate");
    public static final SoundEvent BEE_WORK = makeSoundEvent("bee_work");
    public static final SoundEvent BEE_AGGRESSIVE = makeSoundEvent("bee_aggressive");
    public static final SoundEvent BEE_PASSIVE = makeSoundEvent("bee_passive");
    public static final SoundEvent HONEY_BOTTLE_DRINK = makeSoundEvent("honey_bottle_drink");
    public static final SoundEvent BEEHIVE_SHEAR = makeSoundEvent("shear_hive");

    public static final SoundType BAMBOO = new SoundType(1.0F, 1.0F, BAMBOO_PLACE, BAMBOO_STEP, BAMBOO_PLACE, BAMBOO_PLACE, BAMBOO_STEP);
    public static final SoundType SCAFFOLDING = new SoundType(1.0F, 1.0F, SCAFFOLD_BREAK, SCAFFOLD_STEP, SCAFFOLD_PLACE, SCAFFOLD_HIT, SCAFFOLD_FALL);


    private static SoundEvent makeSoundEvent(String name) {
        ResourceLocation loc = new ResourceLocation(FutureMC.ID, name);
        return new SoundEvent(loc).setRegistryName(loc);
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                TRIDENT_THROW, TRIDENT_PIERCE, TRIDENT_IMPACT, TRIDENT_CONDUCTIVIDAD, TRIDENT_LOYALTY, TRIDENT_RIPTIDE_0, TRIDENT_RIPTIDE_1, TRIDENT_RIPTIDE_2,
                CROSSBOW_CHARGE, CROSSBOW_FIRE, CROSSBOW_QUICK_CHARGE, CROSSBOW_LOAD,
                COMPOSTER_EMPTY, COMPOSTER_FILL, COMPOSTER_FILL_SUCCESS, COMPOSTER_READY,
                PANDA_AGGRESSIVE_AMBIENT, PANDA_AMBIENT, PANDA_BITE, PANDA_CANNOT_BREED, PANDA_EAT, PANDA_HURT, PANDA_PRE_SNEEZE, PANDA_SNEEZE, PANDA_STEP, PANDA_WORRIED_AMBIENT,
                CAMPFIRE_CRACKLE,
                BELL_RING,
                BAMBOO_PLACE, BAMBOO_STEP,
                BEE_ENTER_HIVE, BEE_EXIT_HIVE, BEE_STING, BEE_DEATH, BEE_HURT, BEE_POLLINATE, BEE_WORK, BEE_AGGRESSIVE, BEE_PASSIVE, HONEY_BOTTLE_DRINK, BEEHIVE_SHEAR);
    }
}