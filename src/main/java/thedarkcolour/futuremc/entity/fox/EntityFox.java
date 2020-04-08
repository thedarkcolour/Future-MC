package thedarkcolour.futuremc.entity.fox;

public class EntityFox {
}/*extends EntityAnimal {
    private static final DataParameter<Integer> FOX_TYPE = EntityDataManager.createKey(EntityFox.class, DataSerializers.VARINT);
    private static final DataParameter<Byte> FOX_FLAGS = EntityDataManager.createKey(EntityFox.class, DataSerializers.BYTE);
    private static final DataParameter<Optional<UUID>> TRUSTED_UUID_SECONDARY = EntityDataManager.createKey(EntityFox.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Optional<UUID>> TRUSTED_UUID_MAIN = EntityDataManager.createKey(EntityFox.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final Predicate<EntityItem> CAN_PICKUP_ITEM = ((Predicate<EntityItem>)(EntityItem item) -> !item.cannotPickup()).and(Entity::isEntityAlive);
    private static final Predicate<Entity> field_213512_bF = entity -> {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) entity;
            return living.getLastAttackedEntity() != null && living.getLastAttackedEntityTime() < living.ticksExisted + 600;
        } else {
            return false;
        }
    };
    private static final Predicate<Entity> IS_PREY = entity -> entity instanceof EntityChicken || entity instanceof EntityRabbit;
    private static final Predicate<Entity> IS_SPOOKY = ((Predicate<Entity>)entity -> !entity.isSneaking()).and(entity -> !(entity instanceof EntityPlayer) || ((EntityPlayer) entity).isSpectator() && !((EntityPlayer) entity).isCreative());
}*/