package thedarkcolour.futuremc.compat.jei.blastfurnace;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thedarkcolour.futuremc.FutureMC;
import thedarkcolour.futuremc.registry.FBlocks;

public class BlastFurnaceRecipeCategory implements IRecipeCategory<BlastFurnaceRecipeWrapper> {
    public static final String NAME = "container.jei.futuremc.blast_furnace.name";
    private final IDrawable background;
    private final IDrawableAnimated animatedFlame;
    private final IDrawableAnimated arrow;
    private final IDrawable icon;
    private final String localizedName;

    public BlastFurnaceRecipeCategory(IGuiHelper helper) {
        background = helper.createDrawable(new ResourceLocation("jei", "textures/gui/gui_vanilla.png"), 0, 114, 82, 54);
        IDrawableStatic staticFlame = helper.createDrawable(new ResourceLocation("jei", "textures/gui/gui_vanilla.png"), 82, 114, 14, 14);
        animatedFlame = helper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);

        arrow = helper.drawableBuilder(new ResourceLocation("jei", "textures/gui/gui_vanilla.png"), 82, 128, 24, 17).buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        icon = helper.createDrawableIngredient(new ItemStack(FBlocks.INSTANCE.getBLAST_FURNACE()));
        localizedName = I18n.format("gui.jei.category.smelting");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        animatedFlame.draw(minecraft, 1, 20);
        arrow.draw(minecraft, 24, 18);
    }

    @Override
    public String getUid() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public String getModName() {
        return FutureMC.ID;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, BlastFurnaceRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 0, 0);
        guiItemStacks.init(1, false, 60, 18);

        guiItemStacks.set(ingredients);
    }
}