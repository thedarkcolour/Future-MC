package com.herobrine.future.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleSpawner {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static Particle spawnParticle(EnumParticleType type, World worldIn, BlockPos pos, Vec3d motion) {
        return spawnParticle(type, worldIn, pos.getX(), pos.getY(), pos.getZ(), motion.x, motion.y, motion.z);
    }

    public static Particle spawnParticle(EnumParticleType type, World worldIn, double x, double y, double z, double mX, double mY, double mZ) {
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
                return null;
            } else if (var14 > 1) {
                return null;
            } else {
                var21 = type.create(worldIn, x, y, z, mX, mY, mZ);

                mc.effectRenderer.addEffect(var21);
                return var21;
            }
        }
        return null;
    }

    interface ParticleSupplier {
        Particle get(World worldIn, double par2, double par4, double par6, double par8, double par10, double par12);
    }
}