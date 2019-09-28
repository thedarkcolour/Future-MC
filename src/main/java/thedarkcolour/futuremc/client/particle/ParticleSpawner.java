package thedarkcolour.futuremc.client.particle;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ParticleSpawner {/*
    private static Minecraft mc = Minecraft.getMinecraft();

    public static void spawnParticle(EnumParticleType type, World worldIn, BlockPos pos, Vec3d motion) {
        spawnParticle(type, worldIn, pos.getX(), pos.getY(), pos.getZ(), motion.x, motion.y, motion.z);
    }

    public static void spawnParticle(EnumParticleType type, World worldIn, double x, double y, double z, double mX, double mY, double mZ) {
        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null) {
            int var14 = mc.gameSettings.particleSetting;

            if (var14 == 1 && worldIn.rand.nextInt(3) == 0) {
                var14 = 2;
            }

            double var15 = mc.getRenderViewEntity().posX - x;
            double var17 = mc.getRenderViewEntity().posY - y;
            double var19 = mc.getRenderViewEntity().posZ - z;
            Particle var21;
            double var22 = 16.0D;

            if (var15 * var15 + var17 * var17 + var19 * var19 > var22 * var22) {
            } else if (var14 > 1) {
            } else {
                var21 = type.create(worldIn, x, y, z, mX, mY, mZ);

                mc.effectRenderer.addEffect(var21);
            }
        }
    }
/*
    public static void spawnNectarParticle(World worldIn, double x, double y, double z, double mX, double mY, double mZ) {
        spawnParticle(EnumParticleType.FALLING_NECTAR, worldIn, x, y, z, mX, mY, mZ);
    }

    interface ParticleSupplier {
        Particle get(World worldIn, double par2, double par4, double par6, double par8, double par10, double par12);
    }*/
}