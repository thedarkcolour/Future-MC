package com.herobrine.future.blocks;

import com.herobrine.future.blocks.tile.TileEntityBarrel;
import com.herobrine.future.utils.FutureJava;
import com.herobrine.future.utils.Init;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class Barrel extends Block implements ITileEntityProvider {
    public static final PropertyDirection FACING = PropertyDirection.func_177714_a("facing");
    public static final int GUI_ID = 1;
    public Barrel() {
        super(Material.field_151575_d);
        func_149663_c(Init.MODID + ".Barrel");
        setRegistryName("Barrel");
        func_149672_a(SoundType.field_185848_a);
        func_149647_a(Init.futuretab);
        func_149711_c(2.0F);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public TileEntity func_149915_a(World worldIn, int meta) {
        return new TileEntityBarrel();
    }

    @Override
    public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.field_72995_K) {
            return true;
        }
        TileEntity te = world.func_175625_s(pos);
        if (!(te instanceof TileEntityBarrel)) {
            return false;
        }
        player.openGui(FutureJava.instance, GUI_ID, world, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
        return true;
    }

    @Override
    public void func_180633_a(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        world.func_180501_a(pos, state.func_177226_a(FACING, getFacingFromEntity(pos, placer)), 2);
    }

    public static EnumFacing getFacingFromEntity(BlockPos clickedBlock, EntityLivingBase entity) {
        return EnumFacing.func_176737_a(
                (float) (entity.field_70165_t - clickedBlock.func_177958_n()),
                (float) (entity.field_70163_u - clickedBlock.func_177956_o()),
                (float) (entity.field_70161_v - clickedBlock.func_177952_p()));
    }

    @Override
    public IBlockState func_176203_a(int meta) {
        return func_176223_P().func_177226_a(FACING, EnumFacing.func_82600_a(meta & 7));
    }

    @Override
    public int func_176201_c(IBlockState state) {
        return state.func_177229_b(FACING).func_176745_a();
    }

    @Override
    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public void func_180663_b(World world, BlockPos pos, IBlockState state) {
        TileEntity tile = world.func_175625_s(pos);
        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        if (tile instanceof TileEntityBarrel)
        for(int slot=0; slot < 27; slot++) {
            ItemStack stack = itemHandler.getStackInSlot(slot);
            if (!stack.func_190926_b()) {
                EntityItem item = new EntityItem(world, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), stack);
                world.func_72838_d(item);
                if(world.func_72838_d(item) == true)
                    stack.func_190920_e(0);
            }

        }
        super.func_180663_b(world, pos, state);
    }
    public int success = 1;
}
