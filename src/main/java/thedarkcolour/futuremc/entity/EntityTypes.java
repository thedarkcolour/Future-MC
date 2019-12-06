package thedarkcolour.futuremc.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

@SuppressWarnings("unchecked")
public class EntityTypes<T extends Entity> extends EntityType<T> {
    public static final EntityType<BeeEntity> BEE = (EntityType<BeeEntity>) Builder.create(BeeEntity::new, EntityClassification.CREATURE)
            .size(0.7F, 0.7F)
            .build("bee")
            .setRegistryName("bee");

    public EntityTypes(IFactory<T> p_i51559_1_, EntityClassification p_i51559_2_, boolean p_i51559_3_, boolean p_i51559_4_, boolean p_i51559_5_, boolean p_i51559_6_, EntitySize p_i51559_7_, Predicate<EntityType<?>> velocityUpdateSupplier, ToIntFunction<EntityType<?>> trackingRangeSupplier, ToIntFunction<EntityType<?>> updateIntervalSupplier, BiFunction<FMLPlayMessages.SpawnEntity, World, T> customClientFactory) {
        super(p_i51559_1_, p_i51559_2_, p_i51559_3_, p_i51559_4_, p_i51559_5_, p_i51559_6_, p_i51559_7_, velocityUpdateSupplier, trackingRangeSupplier, updateIntervalSupplier, customClientFactory);
    }
}