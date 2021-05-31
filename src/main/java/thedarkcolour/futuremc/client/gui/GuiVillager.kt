package thedarkcolour.futuremc.client.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.IMerchant
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import net.minecraft.village.MerchantRecipe
import net.minecraft.village.MerchantRecipeList
import net.minecraft.world.World
import org.lwjgl.input.Mouse
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.container.ContainerVillager
import thedarkcolour.futuremc.network.NetworkHandler
import kotlin.math.min
import kotlin.math.sign

class GuiVillager(container: ContainerVillager) : FGui<ContainerVillager>(container) {
    val merchant = container.merchant
    private val careerLevel = if (merchant is EntityVillager) merchant.careerLevel else 0
    private var selectedTradeIndex = 0
    private var scrollOffset = 0
    private var clickedOnScroll = false
    val tradeButtons = arrayOfNulls<TradeButton>(7)

    constructor(playerInv: InventoryPlayer, merchant: IMerchant, world: World) : this(ContainerVillager(playerInv, merchant))

    init {
        xSize = 276
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        val level = careerLevel
        val displayName = merchant.displayName.unformattedText

        if (level in 1..5) {
            val text = displayName + LEVELS[level - 1]
            val j = fontRenderer.getStringWidth(text)
            val k = 49 + (xSize / 2) - (j / 2)

            fontRenderer.drawString(text, k, 6, 4210752)
        } else {
            fontRenderer.drawString(displayName, (49 + xSize / 2 - fontRenderer.getStringWidth(displayName) / 2), 6, 4210752)
        }

        fontRenderer.drawString(container.playerInv.displayName.unformattedText, 107, ySize - 94, 4210752)
        val l = fontRenderer.getStringWidth("Trades") // todo other languages
        fontRenderer.drawString("Trades", (5 - l / 2 + 48), 6, 4210752)
    }

    private fun postButtonClick() {
        container.setTradeIndex(selectedTradeIndex)
        container.moveAroundItems(selectedTradeIndex)
        NetworkHandler.sendVillagerPacket(selectedTradeIndex)
    }

    override fun actionPerformed(button: GuiButton) {
        if (button.enabled) {
            selectedTradeIndex = button.id + scrollOffset
            postButtonClick()
        }
    }

    override fun initGui() {
        super.initGui()

        val i = (width - xSize) / 2
        val j = (height - ySize) / 2
        var k = j + 16 + 2

        for (id in 0 until 7) {
            tradeButtons[id] = addButton(TradeButton(id, i + 5, k))

            k += 20
        }
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        mc.textureManager.bindTexture(TEXTURE)
        val i = (width - xSize) / 2
        val j = (height - ySize) / 2
        blit(i, j, zLevel, 0.0f, 0.0f, this.xSize, this.ySize, 256, 512)
        val trades = merchant.getRecipes(mc.player) ?: return

        if (trades.isNotEmpty()) {
            val i = selectedTradeIndex

            if (i < 0 || i >= trades.size) {
                return
            }

            val trade = trades[i]

            if (trade.isRecipeDisabled) {
                mc.textureManager.bindTexture(TEXTURE)
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
                blit(guiLeft + 83 + 99, guiTop + 35, zLevel, 311.0f, 0.0f, 28, 21, 256, 512)
            }
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        clickedOnScroll = false
        val i = (width - xSize) / 2
        val j = (height - ySize) / 2

        if (canScroll(merchant.getRecipes(mc.player)?.size ?: return) && mouseX > (i + 94) && mouseX < (i + 94 + 6) && mouseY > j + 18 && mouseY <= (j + 18 + 139 + 1)) {
            clickedOnScroll = true
        }

        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun handleMouseInput() {
        super.handleMouseInput()

        var delta = Mouse.getEventDWheel()
        val i = merchant.getRecipes(mc.player)?.size ?: return
        if (delta != 0 && canScroll(i)) {
            delta = sign(delta.toFloat()).toInt()
            val j = i - 7
            scrollOffset -= delta
            scrollOffset = MathHelper.clamp(scrollOffset, 0, j)
        }
    }

    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
        val i = merchant.getRecipes(mc.player)?.size ?: return

        if (clickedOnScroll) {
            val j = guiTop + 18
            val k = j + 139
            val l = i - 7
            var f = (mouseY - j - 13.5f) / ((k - j) - 27.0f)
            f = f * l + 0.5f
            scrollOffset = MathHelper.clamp(f.toInt(), 0 , l)
        }

        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)
    }

    override fun render(mouseX: Int, mouseY: Int, partialTicks: Float) {
        val trades = merchant.getRecipes(mc.player) ?: return

        if (trades.isNotEmpty()) {
            val i = (width - xSize) / 2
            val j = (height - ySize) / 2

            var k = j + 16 + 1
            val l = i + 5 + 5

            GlStateManager.pushMatrix()
            GlStateManager.enableRescaleNormal()
            RenderHelper.enableGUIStandardItemLighting()
            GlStateManager.enableColorMaterial()
            GlStateManager.disableLighting()

            mc.textureManager.bindTexture(TEXTURE)

            renderScrollBar(i, j, trades)
            var i1 = 0

            for (trade in trades) {
                if (canScroll(trades.size) && (i1 < scrollOffset || i1 >= 7 + scrollOffset)) {
                    ++i1
                } else {
                    val cost1 = trade.itemToBuy
                    val cost2 = trade.secondItemToBuy
                    val result = trade.itemToSell

                    itemRender.zLevel = 100.0f
                    val j1 = k + 2

                    renderTradeItem(cost1, l, j1)

                    if (!cost2.isEmpty) {
                        renderTradeItem(cost2, i + 5 + 35, j1)
                    }

                    renderArrow(trade, i, j1)
                    renderTradeItem(result, i + 5 + 68, j1)
                    itemRender.zLevel = 0.0f
                    k += 20
                    ++i1
                }
            }

            var trade = trades[selectedTradeIndex]

            // Exp bar
            //if (showProgressBar()) {
            //    renderProgressBar()
            //}

            // Restock tooltip (todo when we can differentiate restock-able trades)
            //if (trade.isRecipeDisabled && isPointInRegion(186, 35, 22, 21, mouseX, mouseY) && trade.canRestock()) {
            //    renderToolTip()
            //}

            // Some trade related tooltip ??
            for (button in tradeButtons) {
                if (button!!.isMouseOver) {
                    button.renderToolTip(mouseX, mouseY)
                }

                button.visible = button.id < trades.size
            }

            GlStateManager.popMatrix()
            GlStateManager.enableDepth()
            GlStateManager.enableLighting()
            RenderHelper.enableStandardItemLighting()
        }
    }

    private fun renderScrollBar(x: Int, y: Int, trades: MerchantRecipeList) {
        val i = trades.size + 1 - 7

        if (i > 1) {
            val j = 139 - (27 + (i - 1) * 139 / i)
            val k = 1 + j / i + 139 / i
            var i1 = min(113, scrollOffset * k)
            if (scrollOffset == i - 1) {
                i1 = 113
            }

            blit(x + 94, y + 18 + i1, zLevel, 0.0F, 199.0F, 6, 27, 256, 512)
        } else {
            blit(x + 94, y + 18, zLevel, 6.0F, 199.0F, 6, 27, 256, 512)
        }
    }

    private fun renderTradeItem(toBuy: ItemStack, x: Int, y: Int) {
        itemRender.renderItemAndEffectIntoGUI(null, toBuy, x, y)
        itemRender.renderItemOverlays(fontRenderer, toBuy, x, y)
    }

    private fun renderArrow(trade: MerchantRecipe, x: Int, y: Int) {
        GlStateManager.enableBlend()
        mc.textureManager.bindTexture(TEXTURE)

        if (trade.isRecipeDisabled) {
            blit(x + 5 + 35 + 20, y + 3, zLevel, 25.0F, 171.0F, 10, 9, 256, 512)
        } else {
            blit(x + 5 + 35 + 20, y + 3, zLevel, 15.0F, 171.0F, 10, 9, 256, 512)
        }
    }

    private fun canScroll(size: Int): Boolean {
        return size > 7
    }

    companion object {
        private val TEXTURE = ResourceLocation(FutureMC.ID, "textures/gui/villager.png")
        private val LEVELS = arrayOf("Novice", "Apprentice", "Journeyman", "Expert", "Master") // todo other languages
    }

    inner class TradeButton(buttonId: Int, x: Int, y: Int) : GuiButton(buttonId, x, y, 89, 20, "") {
        init {
            visible = false
        }

        override fun drawButton(mc: Minecraft, mouseX: Int, mouseY: Int, partialTicks: Float) {
            super.drawButton(mc, mouseX, mouseY, partialTicks)

            if (hovered) {
                renderToolTip(mouseX, mouseY)
            }
        }

        fun renderToolTip(mouseX: Int, mouseY: Int) {
            val trades = merchant.getRecipes(mc.player) ?: return
            val tradeIndex = id + scrollOffset

            if (hovered && trades.size > tradeIndex) {
                if (mouseX < x + 20) {
                    val stack = trades[tradeIndex].itemToBuy
                    renderToolTip(stack, mouseX, mouseY)
                } else if (mouseX < x + 50 && mouseX > x + 30) {
                    val stack = trades[tradeIndex].secondItemToBuy

                    if (!stack.isEmpty) {
                        renderToolTip(stack, mouseX, mouseY)
                    }
                } else if (mouseX > x + 65) {
                    val stack = trades[tradeIndex].itemToSell
                    renderToolTip(stack, mouseX, mouseY)
                }
            }
        }
    }
}