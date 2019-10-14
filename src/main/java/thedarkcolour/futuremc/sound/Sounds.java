package thedarkcolour.futuremc.sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import thedarkcolour.futuremc.FutureMC;

@ObjectHolder(FutureMC.ID)
public class Sounds extends SoundEvents {
    public static final SoundEvent HONEY_BLOCK_BREAK = null;
    public static final SoundEvent HONEY_BLOCK_STEP = null;
    public static final SoundEvent HONEY_BLOCK_SLIDE = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("HONEY_BLOCK_SLIDE".toLowerCase()));
    public static final SoundEvent BEE_ENTER_HIVE = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("BEE_ENTER_HIVE".toLowerCase()));
    public static final SoundEvent BEE_EXIT_HIVE = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("BEE_EXIT_HIVE".toLowerCase()));
    public static final SoundEvent BEE_STING = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("BEE_STING".toLowerCase()));
    public static final SoundEvent BEE_DEATH = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("BEE_DEATH".toLowerCase()));
    public static final SoundEvent BEE_HURT = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("BEE_HURT".toLowerCase()));
    public static final SoundEvent BEE_POLLINATE = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("BEE_POLLINATE".toLowerCase()));
    public static final SoundEvent BEE_WORK = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("BEE_WORK".toLowerCase()));
    public static final SoundEvent BEE_AGGRESSIVE = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("BEE_AGGRESSIVE".toLowerCase()));
    public static final SoundEvent BEE_PASSIVE = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("BEE_PASSIVE".toLowerCase()));
    public static final SoundEvent HONEY_BOTTLE_DRINK = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("HONEY_BOTTLE_DRINK".toLowerCase()));
    public static final SoundEvent BEEHIVE_SHEAR = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("BEEHIVE_SHEAR".toLowerCase()));
}