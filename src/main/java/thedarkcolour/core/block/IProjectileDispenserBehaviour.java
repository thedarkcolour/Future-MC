package thedarkcolour.core.block;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IProjectileDispenserBehaviour extends IBehaviorDispenseItem {
    @Override
    default ItemStack dispense(IBlockSource source, ItemStack stack) {
        ItemStack itemStack = dispenseStack(source, stack);
        playDispenseSound(source);
        spawnDispenseParticles(source, source.getBlockState().getValue(BlockDispenser.FACING));
        return itemStack;
    }

    default void playDispenseSound(IBlockSource source) {
        source.getWorld().playEvent(1000, source.getBlockPos(), 0);
    }

    default void spawnDispenseParticles(IBlockSource source, EnumFacing facingIn) {
        source.getWorld().playEvent(2000, source.getBlockPos(), this.getWorldEventDataFrom(facingIn));
    }

    default int getWorldEventDataFrom(EnumFacing facingIn) {
        return facingIn.getFrontOffsetX() + 1 + (facingIn.getFrontOffsetZ() + 1) * 3;
    }

    default ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        World worldIn = source.getWorld();
        IPosition iposition = BlockDispenser.getDispensePosition(source);
        EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
        IProjectile iprojectile = this.getProjectileEntity(worldIn, iposition, stack);
        iprojectile.shoot(enumfacing.getFrontOffsetX(), (float)enumfacing.getFrontOffsetY() + 0.1F, enumfacing.getFrontOffsetZ(), this.getProjectileVelocity(), this.getProjectileInaccuracy());
        worldIn.spawnEntity((Entity)iprojectile);
        stack.shrink(1);
        return stack;
    }

    IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stack);

    default float getProjectileInaccuracy()
    {
        return 6.0F;
    }

    default float getProjectileVelocity()
    {
        return 1.1F;
    }
}