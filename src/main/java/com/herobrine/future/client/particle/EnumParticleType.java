package com.herobrine.future.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

public enum EnumParticleType {
    CAMPFIRE_COZY_SMOKE("campfire_cosy_smoke", true, (id, worldIn, x, y, z, mX, mY, mZ, params) -> new ParticleCampfire.Cozy(worldIn, x, y, z, mX, mY, mZ)),
    CAMPFIRE_SIGNAL_SMOKE("campfire_signal_smoke", true, (id, worldIn, x, y, z, mX, mY, mZ, params) -> new ParticleCampfire.Signal(worldIn, x, y, z, mX, mY, mZ));
    //DRIPPING_HONEY("dripping_honey", false, (id, worldIn, x, y, z, mX, mY, mZ, params) -> ),
    //FALLING_HONEY("falling_honey", false, (id, worldIn, x, y, z, mX, mY, mZ, params) -> ),
    //LANDING_HONEY("landing_honey", false, (id, worldIn, x, y, z, mX, mY, mZ, params) -> ),
    //FALLING_NECTAR("falling_nectar", false, ParticleFallingNectar::new);

    private final String name;
    private final boolean alwaysShow;
    private final int count;
    private final IParticleFactory factory;

    EnumParticleType(String name, boolean alwaysShow, int count, IParticleFactory factory) {
        this.name = name;
        this.alwaysShow = alwaysShow;
        this.count = count;
        this.factory = factory;
    }

    EnumParticleType(String name, boolean alwaysShow, IModParticleFactory factory) {
        this(name, alwaysShow, 0, factory);
    }

    EnumParticleType(String name, boolean alwaysShow, IParticleFactory factory) {
        this(name, alwaysShow, 0, factory);
    }

    public IParticleFactory getFactory() {
        return factory;
    }
    
    public Particle create(World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return getFactory().createParticle(0, worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    public String getName() {
        return name;
    }

    public boolean alwaysShow() {
        return alwaysShow;
    }

    public int getCount() {
        return count;
    }

    public interface IModParticleFactory extends IParticleFactory {
        @Override
        default Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
            return createModParticle(worldIn, xCoordIn, yCoordIn, zCoordIn);
        }

        Particle createModParticle(World worldIn, double x, double y, double z);
    }
}