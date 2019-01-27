package com.herobrine.future.items;

import com.herobrine.future.utils.Init;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Trident extends ItemSword {
    @SuppressWarnings("unused")
    private final float attackDamage;
    public Trident(String name, ToolMaterial material, CreativeTabs tab) {
        super(material);
        setRegistryName(name);
        setUnlocalizedName(Init.MODID + ".Trident");
        setCreativeTab(tab);
        this.attackDamage = material.getAttackDamage();
        this.addPropertyOverride(new ResourceLocation("throwing"), (stack, worldIn, entityIn) -> entityIn != null &&  entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0F);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    /**@Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackHeld = playerIn.getHeldItem(hand);
        playerIn.setActiveHand(hand);
        return new ActionResult(EnumActionResult.PASS, itemStackHeld);
    }*/
}