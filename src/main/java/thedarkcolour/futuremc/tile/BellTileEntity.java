package thedarkcolour.futuremc.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class BellTileEntity extends TileEntity implements ITickable {
    //private long ringTime;
    public int ringingTicks;
    public boolean isRinging;
    public EnumFacing ringFacing;
    //private List<EntityLivingBase> entitiesAtRing;
    //private boolean field_213948_i;
    //private int field_213949_j;

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            //this.ringEntities();
            //this.field_213949_j = 0;
            this.ringFacing = EnumFacing.byIndex(type);
            this.ringingTicks = 0;
            this.isRinging = true;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }

    @Override
    public void update() {
        if (this.isRinging) {
            ++this.ringingTicks;
        }

        if (this.ringingTicks >= 50) {
            this.isRinging = false;
            this.ringingTicks = 0;
        }

        //if (this.ringTime >= 5 && this.field_213949_j == 0 && this.hasRaidersNearby()) {
        //    this.field_213948_i = true;
        //    this.resonate();
        //}

        //if (this.field_213948_i) {
        //    if (this.field_213949_j < 40) {
        //        ++this.field_213949_j;
        //    } else {
        //        this.highlightRaiders(this.world);
        //        this.particleRaiders(this.world);
        //        this.field_213948_i = false;
        //    }
        //}
    }

    //
    //private void resonate() {
    //    this.world.playSound(null, this.getPos(), Sounds.BELL_RESONATE, SoundCategory.BLOCKS, 1.0F, 1.0F);
    //}

    public void ring(EnumFacing facing) {
        this.ringFacing = facing;
        if (this.isRinging) {
            this.ringingTicks = 0;
        } else {
            this.isRinging = true;
        }

        this.world.addBlockEvent(getPos(), this.getBlockType(), 1, facing.getIndex());
    }

    //private void ringEntities() {
    //BlockPos blockpos = this.getPos();
    //if (this.world.getWorldTime() > this.ringTime + 60L || this.entitiesAtRing == null) {
    //    this.ringTime = this.world.getWorldTime();
    //    AxisAlignedBB axisalignedbb = (new AxisAlignedBB(blockpos)).grow(48.0D);
    //    this.entitiesAtRing = this.world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
    //}
/*
        if (!this.world.isRemote) {
            for(EntityLivingBase livingentity : this.entitiesAtRing) {
                if (livingentity.isEntityAlive() && !livingentity.removed && blockpos.withinDistance(livingentity.getPositionVec(), 32.0D)) {
                    livingentity.getBrain().setMemory(MemoryModuleType.HEARD_BELL_TIME, this.world.getGameTime());
                }
            }
        }*/
    //}

    //private boolean hasRaidersNearby() {
    //BlockPos blockpos = this.getPos();
//
    //for(EntityLivingBase livingentity : this.entitiesAtRing) {
    //    if (livingentity.isEntityAlive() && !livingentity.isDead && blockpos.withinDistance(livingentity.getPositionVec(), 32.0D) && livingentity.getType().isContained(EntityTypeTags.RAIDERS)) {
    //        return true;
    //    }
    //}

    //return false;
    //}

    //private void highlightRaiders(World worldIn) {
    //if (!worldIn.isRemote) {
    //    this.entitiesAtRing.stream().filter(this::isNearbyRaider).forEach(this::glow);
    //}
    //}

    //private void particleRaiders(World worldIn) {
    //if (worldIn.isRemote) {
    //BlockPos blockpos = this.getPos();
    //AtomicInteger atomicinteger = new AtomicInteger(16700985);
    //int i = (int)this.entitiesAtRing.stream().filter((p_222829_1_) -> blockpos.withinDistance(p_222829_1_.getPositionVec(), 48.0D)).count();
    //this.entitiesAtRing.stream().filter(this::isNearbyRaider).forEach((p_222831_4_) -> {
    //    float f = 1.0F;
    //    float f1 = MathHelper.sqrt((p_222831_4_.posX - (double)blockpos.getX()) * (p_222831_4_.posX - (double)blockpos.getX()) + (p_222831_4_.posZ - (double)blockpos.getZ()) * (p_222831_4_.posZ - (double)blockpos.getZ()));
    //    double d0 = (double)((float)blockpos.getX() + 0.5F) + (double)(1.0F / f1) * (p_222831_4_.posX - (double)blockpos.getX());
    //    double d1 = (double)((float)blockpos.getZ() + 0.5F) + (double)(1.0F / f1) * (p_222831_4_.posZ - (double)blockpos.getZ());
    //    int j = MathHelper.clamp((i - 21) / -2, 3, 15);
//
    //    for(int k = 0; k < j; ++k) {
    //        atomicinteger.addAndGet(5);
    //        double d2 = (double)(atomicinteger.get() >> 16 & 255) / 255.0D;
    //        double d3 = (double)(atomicinteger.get() >> 8 & 255) / 255.0D;
    //        double d4 = (double)(atomicinteger.get() & 255) / 255.0D;
    //        worldIn.spawnParticle(EnumParticleTypes.SPELL_MOB, d0, (double)((float)blockpos.getY() + 0.5F), d1, d2, d3, d4);
    //    }
//
    //});
    //}
    //}

    //private boolean isNearbyRaider(EntityLivingBase entityIn) {
    //return false;
    //return entityIn.isEntityAlive() && !entityIn.isDead && this.getPos().withinDistance(entityIn.getPositionVec(), 48.0D) && entityIn.getType().isContained(EntityTypeTags.RAIDERS);
    //}
}