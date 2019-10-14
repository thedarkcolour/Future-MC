package thedarkcolour.futuremc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TargetBlock extends Block {
    public TargetBlock() {
        super(Properties.create(Material.WOOL, DyeColor.WHITE).hardnessAndResistance(0.8F).sound(SoundType.CLOTH));
        setRegistryName("target");
    }

    @Override
    public void onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit, Entity projectile) {
        Direction direction = hit.getFace();
        BlockPos pos = hit.getPos(); // Can be mutable if needed
        Vec3d vec3d = hit.getHitVec();
        System.out.println(vec3d);
        //switch (direction) {
        //    case UP: {
        double x = hit.getHitVec().x - Math.floor(hit.getHitVec().x);
        double z = hit.getHitVec().z - Math.floor(hit.getHitVec().z);
        System.out.println(x + ", " + z); // 350 4 94
        //    }
        //}
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        System.out.println(hit.getHitVec());
        double x = hit.getHitVec().x - Math.floor(hit.getHitVec().x);
        double z = hit.getHitVec().z - Math.floor(hit.getHitVec().z);

        System.out.println(x + ", " + z);
        return false;
    }
}