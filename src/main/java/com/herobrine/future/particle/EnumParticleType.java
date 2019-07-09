package com.herobrine.future.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

public enum EnumParticleType {
    CAMPFIRE_COZY_SMOKE("campfire_cosy_smoke", true, new ParticleCampfire.CozySmokeFactory()),
    CAMPFIRE_SIGNAL_SMOKE("campfire_signal_smoke", true, new ParticleCampfire.SignalSmokeFactory());

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
}