package thedarkcolour.futuremc.client.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.resources.I18n
import net.minecraft.init.SoundEvents
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.compat.JEI
import thedarkcolour.futuremc.compat.isModLoaded
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.container.StonecutterContainer
import kotlin.math.sign

class StonecutterScreen(container: StonecutterContainer) : FGui<StonecutterContainer>(container) {
    private var sliderProgress = 0.0f
    private var clickedOnScroll = false
    private var recipeIndexOffset = 0
    private var hasInput = false

    init {
        container.inventoryUpdateListener = ::onInventoryUpdate
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        fontRenderer.drawString(I18n.format("container.stonecutter"), 8, 6, 4210752)
        fontRenderer.drawString(getContainer().playerInv.displayName.unformattedText, 8, ySize - 94, 4210752)
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        mc.textureManager.bindTexture(BACKGROUND)
        val i = (width - xSize) / 2
        val j = (height - ySize) / 2
        val scrollY = (41.0f * sliderProgress).toInt()

        drawTexturedModalRect(i, j, 0, 0, xSize, ySize)
        drawTexturedModalRect(i + 119, j + 15 + scrollY, 176 + if (canScroll()) 0 else 12, 0, 12, 15)
        drawRecipeButton(mouseX, mouseY, i, j)

        if (getContainer().recipeList.isNotEmpty()) {
            val x = guiLeft + 52
            val y = guiTop + 14
            val k = recipeIndexOffset + 12
            drawResultBackgrounds(mouseX, mouseY, x, y, k)
            drawResultItems(x, y, k)
        }
    }

    private fun canScroll(): Boolean {
        return hasInput && getContainer().recipeList.size > 12
    }

    /**
     * Draws the recipe button for JEI.
     */
    private fun drawRecipeButton(mouseX: Int, mouseY: Int, i: Int, j: Int) {
        if (FConfig.villageAndPillage.stonecutter.recipeButton && isModLoaded(JEI)) {
            val x = guiLeft + 143
            val y = guiTop + 8
            var textureY = 166
            if (mouseX >= x && mouseY >= y && mouseX < x + 16 && mouseY < y + 16) {
                textureY += 16
            }
            drawTexturedModalRect(i + 143, j + 8, 16, textureY, 16, 16)
        }
    }

    /**
     * Renders the button backgrounds for each choice.
     */
    private fun drawResultBackgrounds(mouseX: Int, mouseY: Int, left: Int, top: Int, maxOffset: Int) {
        var i = recipeIndexOffset
        while (i < maxOffset && i < getContainer().recipeList.size) {
            val j = i - recipeIndexOffset
            val k = left + j % 4 * 16
            val l = j / 4
            val i1 = top + l * 18 + 2
            var j1 = ySize
            if (i == getContainer().selectedIndex) {
                j1 += 18
            } else if (mouseX >= k && mouseY >= i1 && mouseX < k + 16 && mouseY < i1 + 18) {
                j1 += 36
            }
            drawTexturedModalRect(k, i1 - 1, 0, j1, 16, 18)
            ++i
        }
    }

    /**
     * Renders the choices for stonecutting into the gui.
     */
    private fun drawResultItems(left: Int, top: Int, maxOffset: Int) {
        RenderHelper.enableGUIStandardItemLighting()
        val recipes = getContainer().recipeList
        var i = recipeIndexOffset

        while (i < maxOffset && i < getContainer().recipeList.size) {
            val j = i - recipeIndexOffset
            val k = left + j % 4 * 16
            val l = j / 4
            val i1 = top + l * 18 + 2
            itemRender.renderItemAndEffectIntoGUI(recipes[i].output, k, i1)
            ++i
        }
        RenderHelper.disableStandardItemLighting()
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        clickedOnScroll = false
        if (hasInput) {
            var i = guiLeft + 52
            var j = guiTop + 14
            val k = recipeIndexOffset + 12

            for (l in recipeIndexOffset until k) {
                val i1 = l - recipeIndexOffset
                val x = mouseX - (i + i1 % 4 * 16).toDouble()
                val y = mouseY - (j + i1 / 4 * 18).toDouble()

                if (x >= 0.0 && y >= 0.0 && x < 16.0 && y < 18.0 && getContainer().enchantItem(mc.player, l)) {
                    Minecraft.getMinecraft().soundHandler.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f))
                    mc.playerController.sendEnchantPacket(getContainer().windowId, l)
                    return
                }
            }
            i = guiLeft + 119
            j = guiTop + 9

            if (mouseX >= i.toDouble() && mouseX < (i + 12).toDouble() && mouseY >= j.toDouble() && mouseY < (j + 54).toDouble()) {
                clickedOnScroll = true
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun mouseClickMove(
        mouseX: Int,
        mouseY: Int,
        clickedMouseButton: Int,
        timeSinceLastClick: Long
    ) {
        if (clickedOnScroll && canScroll()) {
            val i = guiTop + 14
            val j = i + 54
            sliderProgress = (mouseY - i - 7.5f) / (j - i - 15.0f)
            sliderProgress = MathHelper.clamp(sliderProgress, 0.0f, 1.0f)
            recipeIndexOffset = (sliderProgress * getHiddenRows() + 0.5).toInt() shl 2
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)
    }

    override fun handleMouseInput() {
        super.handleMouseInput()

        var delta = Mouse.getEventDWheel()
        if (delta != 0 && canScroll()) {
            delta = sign(delta.toFloat()).toInt()
            val h = getHiddenRows()
            sliderProgress = (sliderProgress.toDouble() - delta / h.toDouble()).toFloat()
            sliderProgress = MathHelper.clamp(sliderProgress, 0.0f, 1.0f)
            recipeIndexOffset = ((sliderProgress * h.toFloat()).toDouble() + 0.5).toInt() shl 2
        }
    }

    private fun getHiddenRows(): Int {
        return (getContainer().recipeList.size + 4 - 1) / 4 - 3
    }

    private fun onInventoryUpdate() {
        hasInput = getContainer().recipeList.isNotEmpty()

        if (!hasInput) {
            sliderProgress = 0.0f
            recipeIndexOffset = 0
        }
    }

    companion object {
        private val BACKGROUND = ResourceLocation(FutureMC.ID, "textures/gui/stonecutter.png")
    }
}