package thedarkcolour.futuremc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thedarkcolour.core.block.InteractionBlock;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.init.FutureConfig;
import thedarkcolour.futuremc.sound.Sounds;
import thedarkcolour.futuremc.tile.TileCampfire;

import java.util.Random;

public class BlockCampfire extends InteractionBlock {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private static final AxisAlignedBB BOUNDING_BOX = makeAABB(0,0,0, 16,7,16);//new AxisAlignedBB(0,0,0,1,0.375,1);
    public static final PropertyBool LIT = PropertyBool.create("lit");

    public BlockCampfire() {
        super("Campfire", Material.WOOD);
        setSoundType(SoundType.WOOD);
        setHardness(2.0F);
        setDefaultState(getBlockState().getBaseState().withProperty(LIT, true).withProperty(FACING, EnumFacing.NORTH));
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.DECORATIONS : FutureMC.TAB);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileCampfire();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, LIT);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(LIT, (meta & 4) != 0).withProperty(FACING, EnumFacing.byHorizontalIndex(meta & -5));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(LIT) ? 4 : 0) | state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(LIT, !(block instanceof BlockLiquid | block instanceof IFluidBlock));
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.getValue(LIT)) {
            if (rand.nextInt(10) == 0) {
                worldIn.playSound((float)pos.getX() + 0.5F, (float)pos.getY() + 0.5F, (float)pos.getZ() + 0.5F, Sounds.CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 0.5F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.6F, false);
            }

            if (rand.nextInt(5) == 0) {
                for(int i = 0; i < rand.nextInt(1) + 1; ++i) {
                    worldIn.spawnParticle(EnumParticleTypes.LAVA, (float)pos.getX() + 0.5F, (float)pos.getY() + 0.5F, (float)pos.getZ() + 0.5F, rand.nextFloat() / 2.0F, 5.0E-5D, rand.nextFloat() / 2.0F);
                }
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (worldIn.getTileEntity(pos) instanceof TileCampfire) {
            ((TileCampfire) worldIn.getTileEntity(pos)).dropAllItems();
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.005F);

        harvesters.set(player);
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);

        dropBlockAsItem(worldIn, pos, state, i);
        harvesters.set(null);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.COAL;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 1;
    }

    @Override
    public int quantityDropped(Random random) {
        return 2;
    }

    @Override
    public int getLightValue(IBlockState state) {
        return state.getValue(LIT) ? 15 : 0;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        if (block instanceof BlockFluidBase | block instanceof BlockLiquid) {
            setLit(worldIn, pos, false);
        }
    }

    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (FutureConfig.general.campfireDMG) {
            if (!entityIn.isImmuneToFire() && state.getValue(LIT)) {
                entityIn.attackEntityFrom(DamageSource.IN_FIRE, 1.0F);
            }
        }
    }

    public void setLit(World worldIn, BlockPos pos, boolean lit) {
        if (!lit) {
            if (worldIn.getTileEntity(pos) instanceof TileCampfire) {
                ((TileCampfire) worldIn.getTileEntity(pos)).dropAllItems();
            }
        }
        worldIn.setBlockState(pos, getBlockState().getBaseState().withProperty(LIT, lit));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOX;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.neighborChanged(state, worldIn, pos, worldIn.getBlockState(pos).getBlock(), pos);
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isTopSolid(IBlockState state) {
        return false;
    }
}