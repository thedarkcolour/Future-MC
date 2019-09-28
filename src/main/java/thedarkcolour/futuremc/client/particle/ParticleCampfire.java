package thedarkcolour.futuremc.client.particle;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class ParticleCampfire {/*extends Particle {/*
    private static final ResourceLocation CAMPFIRE_PARTICLE_TEXTURE = new ResourceLocation(FutureMC.ID, "textures/particles/campfire.png");
    private final Vec3d[] vec3ds = new Vec3d[4]; // Cache the array

    public ParticleCampfire(World worldIn, double x, double y, double z, double speedX, double speedY, double speedZ) {
        super(worldIn, x, y, z);
        setScale(3.0F);
        this.multipleParticleScaleBy(3.0F);
        this.setSize(0.25F, 0.25F);
        if (isSignal()) {
            this.particleMaxAge = this.rand.nextInt(50) + 280;
        } else {
            this.particleMaxAge = this.rand.nextInt(50) + 80;
        }

        this.particleGravity = 3.0E-6F;
        this.motionX = speedX;
        this.motionY = speedY + (double)(this.rand.nextFloat() / 500.0F);
        this.motionZ = speedZ;

        setParticleTexture(RenderUtils.makeTextureAtlasSprite(CAMPFIRE_PARTICLE_TEXTURE));
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float f = (float)this.particleTextureIndexX / 16.0F;
        float f1 = f + 0.0624375F;
        float f2 = (float)this.particleTextureIndexY / 16.0F;
        float f3 = f2 + 0.0624375F;
        float f4 = 0.1F * this.particleScale;

        if (this.particleTexture != null)
        {
            f = this.particleTexture.getMinU();
            f1 = this.particleTexture.getMaxU();
            f2 = this.particleTexture.getMinV();
            f3 = this.particleTexture.getMaxV();
        }

        float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
        float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
        float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        vec3ds[0] = new Vec3d(-rotationX * f4 - rotationXY * f4, -rotationZ * f4, -rotationYZ * f4 - rotationXZ * f4);
        vec3ds[1] = new Vec3d(-rotationX * f4 + rotationXY * f4, rotationZ * f4, -rotationYZ * f4 + rotationXZ * f4);
        vec3ds[2] = new Vec3d(rotationX * f4 + rotationXY * f4, rotationZ * f4, rotationYZ * f4 + rotationXZ * f4);
        vec3ds[3] = new Vec3d(rotationX * f4 - rotationXY * f4, -rotationZ * f4, rotationYZ * f4 - rotationXZ * f4);
        if (this.particleAngle != 0.0F) {
            float f8 = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
            float f9 = MathHelper.cos(f8 * 0.5F);
            float f10 = MathHelper.sin(f8 * 0.5F) * (float)cameraViewDir.x;
            float f11 = MathHelper.sin(f8 * 0.5F) * (float)cameraViewDir.y;
            float f12 = MathHelper.sin(f8 * 0.5F) * (float)cameraViewDir.z;
            Vec3d vec3d = new Vec3d(f10, f11, f12);

            for (int l = 0; l < 4; ++l) {
                vec3ds[l] = vec3d.scale(2.0D * vec3ds[l].dotProduct(vec3d)).add(vec3ds[l].scale((double)(f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(vec3ds[l]).scale(2.0F * f9));
            }
        }

        buffer.pos((double)f5 + vec3ds[0].x, (double)f6 + vec3ds[0].y, (double)f7 + vec3ds[0].z).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos((double)f5 + vec3ds[1].x, (double)f6 + vec3ds[1].y, (double)f7 + vec3ds[1].z).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos((double)f5 + vec3ds[2].x, (double)f6 + vec3ds[2].y, (double)f7 + vec3ds[2].z).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos((double)f5 + vec3ds[3].x, (double)f6 + vec3ds[3].y, (double)f7 + vec3ds[3].z).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
    }

    // Take that vanilla!
    @Override
    public void setParticleTexture(TextureAtlasSprite texture) {
        if (getFXLayer() == 3) {
            this.particleTexture = texture;
        }
    }

    public void setScale(float scale) {
        this.particleScale *= scale;
        this.setBoundingBoxSpacing(0.2F * scale, 0.2F * scale);
    }

    private void setBoundingBoxSpacing(float v, float v1) {
        if (v != this.width || v1 != this.height) {
            this.width = v;
            this.height = v1;
            AxisAlignedBB box_1 = this.getBoundingBox();
            double double_1 = (box_1.minX + box_1.maxX - (double)v) / 2.0D;
            double double_2 = (box_1.minZ + box_1.maxZ - (double)v) / 2.0D;
            this.setBoundingBox(new AxisAlignedBB(double_1, box_1.minY, double_2, double_1 + (double)this.width, box_1.minY + (double)this.height, double_2 + (double)this.width));
        }
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ < this.particleMaxAge && !(this.particleAlpha <= 0.0F)) {
            this.motionX += this.rand.nextFloat() / 5000.0F * (float)(this.rand.nextBoolean() ? 1 : -1);
            this.motionZ += this.rand.nextFloat() / 5000.0F * (float)(this.rand.nextBoolean() ? 1 : -1);
            this.motionY -= this.particleGravity;
            this.move(this.motionX, this.motionY, this.motionZ);
            if (this.particleAge >= this.particleMaxAge - 60 && this.particleAlpha > 0.01F) {
                this.particleAlpha -= 0.015F;
            }
        } else {
            this.setExpired();
        }
    }
/*
    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        GL11.glPushMatrix();
        GL11.glDepthFunc(GL11.GL_ALWAYS);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, this.particleAlpha);
        GlStateManager.disableLighting();
        RenderHelper.disableStandardItemLighting();

        Minecraft.getMinecraft().getTextureManager().bindTexture(getParticleTexture());
    }*/
/*
    @Override
    protected ResourceLocation getParticleTexture() {
        return CAMPFIRE_PARTICLE_TEXTURE;
    }*//*

    abstract boolean isSignal();

    @Override
    public int getFXLayer() {
        return 3;
    }

    public static class Cozy extends ParticleCampfire {
        public Cozy(World worldIn, double x, double y, double z, double speedX, double speedY, double speedZ) {
            super(worldIn, x, y, z, speedX, speedY, speedZ);
            setAlphaF(0.9F);
        }

        @Override
        boolean isSignal() {
            return false;
        }
    }

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