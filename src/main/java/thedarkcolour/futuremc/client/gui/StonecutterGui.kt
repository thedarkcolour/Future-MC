package thedarkcolour.futuremc.client.gui
/*
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import thedarkcolour.core.gui.GuiContainer
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.compat.JEI
import thedarkcolour.futuremc.compat.isModLoaded
import thedarkcolour.futuremc.config.FConfig.villageAndPillage
import thedarkcolour.futuremc.container.ContainerStonecutter

class StonecutterGui(container: ContainerStonecutter) : GuiContainer<ContainerStonecutter>(container) {
    private var sliderProgress = 0f
    private val clickedOnScroll = false
    // used to start at a certain point to allow scrolling
    private var recipeIndexOffset = 0
    private var hasInput = false

    init {
        container.setInventoryUpdateListener(::onInventoryUpdate)
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        fontRenderer.drawString(I18n.format("container.stonecutter"), 8, 6, 4210752)
        fontRenderer.drawString(container.playerInv.displayName.unformattedText, 8, ySize - 94, 4210752)
    }

    override fun drawGuiContainerBackgroundLayer(
        partialTicks: Float,
        mouseX: Int,
        mouseY: Int
    ) {
        drawDefaultBackground()
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        mc.textureManager.bindTexture(BACKGROUND)
        val i = (width - xSize) / 2
        val j = (height - ySize) / 2
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize)
        val scrollY = (41.0f * sliderProgress).toInt()
        drawTexturedModalRect(i + 119, j + 15 + scrollY, 176 + if (canScroll()) 0 else 12, 0, 12, 15)
        if (isModLoaded(JEI)) {
            drawRecipeButton(mouseX, mouseY, i, j)
        }
        if (container.currentRecipes.size != 0) {
            val x = guiLeft + 52
            val y = guiTop + 14
            val k = recipeIndexOffset + 12
            drawRecipesBackground(mouseX, mouseY, x, y, k)
            drawRecipesItems(x, y, k)
        }
    }

    private fun canScroll(): Boolean {
        return hasInput && container.currentRecipes.size > 12
    }

    private fun drawRecipeButton(mouseX: Int, mouseY: Int, i: Int, j: Int) {
        if (villageAndPillage.stonecutter.recipeButton) {
            val x = guiLeft + 143
            val y = guiTop + 8
            var textureY = 166
            if (mouseX >= x && mouseY >= y && mouseX < x + 16 && mouseY < y + 16) {
                textureY += 16
            }
            drawTexturedModalRect(i + 143, j + 8, 16, textureY, 16, 16)
        }
    }

    private fun drawRecipesBackground(mouseX: Int, mouseY: Int, left: Int, top: Int, maxOffset: Int) {
        var i = recipeIndexOffset
        while (i < maxOffset && i < container.currentRecipes.size) {
            val j = i - recipeIndexOffset
            val k = left + j % 4 * 16
            val l = j / 4
            val i1 = top + l * 18 + 2
            var j1 = ySize
            if (i == container.selectedIndex) {
                j1 += 18
            } else if (mouseX >= k && mouseY >= i1 && mouseX < k + 16 && mouseY < i1 + 18) {
                j1 += 36
            }
            drawTexturedModalRect(k, i1 - 1, 0, j1, 16, 18)
            ++i
        }
    }

    private fun drawRecipesItems(left: Int, top: Int, maxOffset: Int) {
        RenderHelper.enableGUIStandardItemLighting()
        val recipes = container.currentRecipes
        var i = recipeIndexOffset

        while (i < maxOffset && i < container.currentRecipes.size) {
            val j = i - recipeIndexOffset
            val k = left + j % 4 * 16
            val l = j / 4
            val i1 = top + l * 18 + 2
            itemRender.renderItemAndEffectIntoGUI(recipes[i].output, k, i1)
            ++i
        }
        RenderHelper.disableStandardItemLighting()
    }

    private fun onInventoryUpdate() {
        hasInput = container.hasRecipe()

        if (!hasInput) {
            sliderProgress = 0.0f
            recipeIndexOffset = 0
        }
    }

    companion object {
        private val BACKGROUND = ResourceLocation(FutureMC.ID, "textures/gui/stonecutter.png")
    }
} */