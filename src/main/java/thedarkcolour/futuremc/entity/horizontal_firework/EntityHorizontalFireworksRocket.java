package thedarkcolour.futuremc.entity.horizontal_firework;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import thedarkcolour.futuremc.FutureMC;

import java.io.IOException;

public class EntityHorizontalFireworksRocket extends Entity implements IProjectile {
    private static final DataParameter<ItemStack> FIREWORK_ITEM = EntityDataManager.createKey(EntityHorizontalFireworksRocket.class, new DataSerializer<ItemStack>() {
        @Override
        public DataParameter<ItemStack> createKey(int id) {
            return new DataParameter<>(id, this);
        }

        public void write(PacketBuffer buf, ItemStack value) {
            buf.writeItemStack(value);
        }

        public ItemStack read(PacketBuffer buf) {
            try {
                return buf.readItemStack();
            } catch (IOException e) {
                return ItemStack.EMPTY;
            }
        }

        public ItemStack copyValue(ItemStack value) {
            return value.copy();
        }
    });
    private int age, lifetime;

    public EntityHorizontalFireworksRocket(World worldIn, ItemStack ammo, double x, double y, double z) {
        super(worldIn);
        age = 0;
        setPosition(x, y, z);
        int i = 1;
        if (!ammo.isEmpty() && ammo.hasTagCompound()) {
            this.dataManager.set(FIREWORK_ITEM, ammo.copy());
            i += ammo.getTagCompound().getCompoundTag("Fireworks").getByte("Flight");
        }

        this.motionX = this.rand.nextGaussian() * 0.001D;
        this.motionY = 0.05D;
        this.motionZ = this.rand.nextGaussian() * 0.001D;
        this.lifetime = 10 * i + this.rand.nextInt(6) + this.rand.nextInt(7);
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(FIREWORK_ITEM, ItemStack.EMPTY);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.age = compound.getInteger("Life");
        this.lifetime = compound.getInteger("LifeTime");
        ItemStack itemstack = read(compound.getCompoundTag("FireworksItem"));
        if (!itemstack.isEmpty()) {
            this.dataManager.set(FIREWORK_ITEM, itemstack);
        }
    }

    private static ItemStack read(NBTTagCompound tag) {
        try {
            return new ItemStack(tag);
        } catch (RuntimeException runtimeexception) {
            FutureMC.logger.debug("Tried to load invalid item: {}", tag, runtimeexception);
            return ItemStack.EMPTY;
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {

    }
}
