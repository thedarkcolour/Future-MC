package thedarkcolour.futuremc.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;

public class ParticleCampfire extends Particle {
    public static TextureAtlasSprite[] textures = new TextureAtlasSprite[12];
    private final boolean isSignal;

    public ParticleCampfire(World worldIn, double x, double y, double z, double mX, double mY, double mZ, boolean isSignal) {
        super(worldIn, x, y, z);
        this.isSignal = isSignal;

        multipleParticleScaleBy(3.0F);
        setSize(0.25F, 0.25F);

        if (isSignal) {
            particleMaxAge = rand.nextInt(50) + 280;
        } else {
            particleMaxAge = rand.nextInt(50) + 80;
        }

        particleGravity = 3.0E-6F;
        motionX = mX;
        motionY = mY + rand.nextFloat() / 500.0f;
        motionZ = mZ;

        setParticleTexture(getSprite(rand.nextInt(12)));
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    private TextureAtlasSprite getSprite(int index) {
        return textures[index];
    }

    @Override
    public void setParticleTexture(TextureAtlasSprite texture) {
        if (texture == null) {
            return;
        }
        particleTexture = texture;
    }

    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        if (particleAge++ < particleMaxAge && !(particleAlpha <= 0.0F)) {
            motionX += rand.nextFloat() / 5000.0F * (float)(rand.nextBoolean() ? 1 : -1);
            motionZ += rand.nextFloat() / 5000.0F * (float)(rand.nextBoolean() ? 1 : -1);
            motionY -= particleGravity;
            move(motionX, motionY, motionZ);
            if (particleAge >= particleMaxAge - 60 && particleAlpha > 0.01F) {
                particleAlpha -= 0.015F;
            }
        } else {
            setExpired();
        }
    }

    public static class CosyFactory implements IParticleFactory {
        @Override
        public Particle createParticle(int particleID, World worldIn, double x, double y, double z, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... args) {
            return new ParticleCampfire(worldIn, x, y, z, xSpeedIn, ySpeedIn, zSpeedIn, false);
        }
    }

    public static class SignalFactory implements IParticleFactory {
        @Override
        public Particle createParticle(int particleID, World worldIn, double x, double y, double z, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... args) {
            return new ParticleCampfire(worldIn, x, y, z, xSpeedIn, ySpeedIn, zSpeedIn, true);
        }
    }
/*
    public static class Signal extends ParticleCampfire {
        public Signal(World worldIn, double x, double y, double z, double speedX, double speedY, double speedZ) {
            super(worldIn, x, y, z, speedX, speedY, speedZ);
            setAlphaF(0.95F);
        }

        @Override
        boolean isSignal() {
            return true;
        }
    }*/
}