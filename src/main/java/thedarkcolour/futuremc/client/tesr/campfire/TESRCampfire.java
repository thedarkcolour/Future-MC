package thedarkcolour.futuremc.client.tesr.campfire;

public class TESRCampfire {}/*extends TileEntitySpecialRenderer<TileCampfire> {
    @Override
    public void render(TileCampfire te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        EnumFacing facing = te.getWorld().getBlockState(te.getPos()).getValue(BlockCampfire.FACING);
        NonNullList<ItemStack> list = te.;

        for(int i = 0; i < list.size(); ++i) {
            ItemStack stack = list.get(i);
            if (!stack.isEmpty()) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)x + 0.5F, (float)y + 0.44921875F, (float)z + 0.5F);
                EnumFacing direction1 = EnumFacing.getHorizontal((i + facing.getHorizontalIndex()) % 4);
                GlStateManager.rotate(-direction1.getHorizontalAngle(), 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(-0.3125F, -0.3125F, 0.0F);
                GlStateManager.scale(0.375F, 0.375F, 0.375F);
                ItemRendererAccess.renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
                GlStateManager.popMatrix();
            }
        }
    }
}*/