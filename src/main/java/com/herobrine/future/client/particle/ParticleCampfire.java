package com.herobrine.future.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleCampfire extends Particle {
    public ParticleCampfire(World worldIn, double x, double y, double z, double speedX, double speedY, double speedZ, boolean isSignal) {
        super(worldIn, x, y, z);
        this.multipleParticleScaleBy(3.0F);
        this.setSize(0.25F, 0.25F);
        if (isSignal) {
            this.particleMaxAge = this.rand.nextInt(50) + 280;
        } else {
            this.particleMaxAge = this.rand.nextInt(50) + 80;
        }

        this.particleGravity = 3.0E-6F;
        this.motionX = speedX;
        this.motionY = speedY + (double)(this.rand.nextFloat() / 500.0F);
        this.motionZ = speedZ;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ < this.particleMaxAge && !(this.particleAlpha <= 0.0F)) {
            this.motionX += (double)(this.rand.nextFloat() / 5000.0F * (float)(this.rand.nextBoolean() ? 1 : -1));
            this.motionZ += (double)(this.rand.nextFloat() / 5000.0F * (float)(this.rand.nextBoolean() ? 1 : -1));
            this.motionY -= (double)this.particleGravity;
            this.move(this.motionX, this.motionY, this.motionZ);
            if (this.particleAge >= this.particleMaxAge - 60 && this.particleAlpha > 0.01F) {
                this.particleAlpha -= 0.015F;
            }

        } else {
            this.setExpired();
        }
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    public static class CozySmokeFactory implements IParticleFactory {
        @Override
        public Particle createParticle(int particleID, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... p_178902_15_) {
            ParticleCampfire particleCampfire = new ParticleCampfire(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, false);
            particleCampfire.setAlphaF(0.9F);
            return particleCampfire;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class SignalSmokeFactory implements IParticleFactory {
        @Override
        public Particle createParticle(int particleID, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... p_178902_15_) {
            ParticleCampfire particleCampfire = new ParticleCampfire(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, true);
            particleCampfire.setAlphaF(0.95F);
            return particleCampfire;
        }
    }
}