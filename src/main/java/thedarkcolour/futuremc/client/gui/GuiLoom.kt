package thedarkcolour.futuremc.client.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.BannerTextures
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.init.SoundEvents
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.BannerPattern
import net.minecraft.tileentity.TileEntityBanner
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import org.lwjgl.input.Mouse
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.container.ContainerLoom
import kotlin.math.ceil
import kotlin.math.sign

class GuiLoom(private val container: ContainerLoom) : GuiContainer(container) {
    private var preview: ResourceLocation? = null
    private var banner = ItemStack.EMPTY
    private var color = ItemStack.EMPTY
    private var pattern = ItemStack.EMPTY
    private var isCrafting = false
    private var hasBannerPattern = false
    private var cannotFitPatterns = false
    private var sliderProgress = 0f
    private var clickedOnScroll = false
    private var recipeIndexOffset = 1

    init {
        container.setInventoryUpdateListener(::onInventoryUpdate)
        //BASIC_PATTERNS.forEach { println(it.name) }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        drawDefaultBackground()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        mc.textureManager.bindTexture(BACKGROUND)
        val i = guiLeft
        val j = guiTop
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize)
        val bannerSlot = container.getLoomSlot(0)
        val dyeSlot = container.getLoomSlot(1)
        val patternSlot = container.getLoomSlot(2)
        val outputSlot = container.getLoomSlot(3)
        if (!bannerSlot.hasStack) {
            drawTexturedModalRect(i + bannerSlot.xPos, j + bannerSlot.yPos, xSize, 0, 16, 16)
        }

        if (!dyeSlot.hasStack) {
            drawTexturedModalRect(i + dyeSlot.xPos, j + dyeSlot.yPos, xSize + 16, 0, 16, 16)
        }

        if (!patternSlot.hasStack) {
            drawTexturedModalRect(i + patternSlot.xPos, j + patternSlot.yPos, xSize + 32, 0, 16, 16)
        }

        val k = (41.0f * sliderProgress).toInt()
        drawTexturedModalRect(i + 119, j + 13 + k, 232 + if (isCrafting) 0 else 12, 0, 12, 15)
        if (preview != null && !cannotFitPatterns) {
            mc.textureManager.bindTexture(preview!!)
            drawScaledCustomSizeModalRect(i + 141, j + 8, 1f, 1f, 20, 40, 20, 40, 64f, 64f)
        } else if (cannotFitPatterns) {
            drawTexturedModalRect(i + outputSlot.xPos - 2, j + outputSlot.yPos - 2, xSize, 17, 17, 16)
        }

        if (bannerSlot.hasStack) {
            if (isCrafting) {
                val l = i + 60
                val i1 = j + 13
                val j1 = recipeIndexOffset + 16
                var k1 = recipeIndexOffset
                while (k1 < j1 && k1 <= BASIC_PATTERNS.size) {
                    val l1 = k1 - recipeIndexOffset
                    val i2 = l + l1 % 4 * 14
                    val j2 = i1 + l1 / 4 * 14
                    mc.textureManager.bindTexture(BACKGROUND)
                    var k2 = ySize
                    if (k1 == container.getSelectedIndex()) {
                        k2 += 14
                    } else if (mouseX >= i2 && mouseY >= j2 && mouseX < i2 + 14 && mouseY < j2 + 14) {
                        k2 += 28
                    }
                    drawTexturedModalRect(i2, j2, 0, k2, 14, 14)

                    if (BASIC_TEXTURES[k1 - 1] != null) {
                        mc.textureManager.bindTexture(BASIC_TEXTURES[k1 - 1]!!)
                        drawScaledCustomSizeModalRect(i2 + 4, j2 + 2, 1f, 1f, 20, 40, 5, 10, 64.0f, 64.0f)
                    }
                    ++k1
                }
            } else if (hasBannerPattern) {
                val l2 = i + 60
                val i3 = j + 13
                mc.textureManager.bindTexture(BACKGROUND)
                drawTexturedModalRect(l2, i3, 0, ySize, 14, 14)
                val j3 = container.getSelectedIndex()
                if (PATTERN_TEXTURES[j3] != null) {
                    mc.textureManager.bindTexture(PATTERN_TEXTURES[j3]!!)
                    drawScaledCustomSizeModalRect(l2 + 4, i3 + 2, 1f, 1f, 20, 40, 5, 10, 64f, 64f)
                }
            }
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        clickedOnScroll = false
        if (isCrafting) {
            var i = guiLeft + 60
            var j = guiTop + 13
            val k = recipeIndexOffset + 16
            for (l in recipeIndexOffset until k) {
                val i1 = l - recipeIndexOffset
                val d0 = mouseX - (i + i1 % 4 * 14).toDouble()
                val d1 = mouseY - (j + i1 / 4 * 14).toDouble()
                if (d0 >= 0.0 && d1 >= 0.0 && d0 < 14.0 && d1 < 14.0 && container.enchantItem(mc.player, l)) {
                    Minecraft.getMinecraft().soundHandler.playSound(
                        PositionedSoundRecord.getMasterRecord(
                            SoundEvents.UI_BUTTON_CLICK,
                            1.0f
                        )
                    )
                    mc.playerController.sendEnchantPacket(container.windowId, l)
                    return
                }
            }
            i = guiLeft + 119
            j = guiTop + 9
            if (mouseX >= i.toDouble() && mouseX < (i + 12) && mouseY >= j.toDouble() && mouseY < (j + 56).toDouble()) {
                clickedOnScroll = true
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
        if (clickedOnScroll && isCrafting) {
            val i = guiTop + 13
            val j = i + 56
            sliderProgress = (mouseY - i - 7.5f) / (j - i - 15.0f)
            sliderProgress = MathHelper.clamp(sliderProgress, 0.0f, 1.0f)
            val k = ROWS - 4
            val l = ((sliderProgress * k.toFloat()).toDouble() + 0.5).toInt().coerceAtLeast(0)

            recipeIndexOffset = 1 + (l * 4)
        } else {
            super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)
        }
    }

    override fun handleMouseInput() {
        super.handleMouseInput()
        if (isCrafting) {
            val i = sign(Mouse.getEventDWheel().toDouble())
            val j = ROWS - 4
            sliderProgress = (sliderProgress - i / j).toFloat()
            sliderProgress = MathHelper.clamp(sliderProgress, 0f, 1f)
            recipeIndexOffset = 1 + (((sliderProgress * j) + 0.5).toInt() shl 2)
        }
    }

    override fun hasClickedOutside(mouseX: Int, mouseY: Int, guiLeft: Int, guiTop: Int): Boolean {
        return mouseX < guiLeft || mouseY < guiTop || mouseX >= (guiLeft + xSize) || mouseY >= (guiTop + ySize)
    }

    private fun onInventoryUpdate() {
        val stack = container.output
        preview = if (stack.isEmpty) {
            null
        } else {
            val tile = TileEntityBanner()
            tile.setItemValues(stack, false)
            BannerTextures.BANNER_DESIGNS.getResourceLocation(tile.patternResourceLocation, tile.patternList, tile.colorList)
        }

        val banner = container.banner
        val color = container.color
        val pattern = container.pattern
        val nbt = banner.getOrCreateSubCompound("BlockEntityTag")
        cannotFitPatterns = nbt.hasKey("Pattern", 9) && !banner.isEmpty && nbt.getTagList("Patterns", 10).tagCount() >= 6
        if (cannotFitPatterns) {
            preview = null
        }

        if (!ItemStack.areItemStacksEqual(banner, this.banner) || !ItemStack.areItemStacksEqual(color, this.color) || !ItemStack.areItemStacksEqual(pattern, this.pattern)) {
            isCrafting = !banner.isEmpty && !color.isEmpty && pattern.isEmpty && !cannotFitPatterns
            hasBannerPattern = !banner.isEmpty && !color.isEmpty && !pattern.isEmpty && !cannotFitPatterns
        }

        this.banner = banner.copy()
        this.color = color.copy()
        this.pattern = pattern.copy()
    }

    companion object {
        private val BACKGROUND = ResourceLocation(FutureMC.ID, "textures/gui/loom.png")
        val BASIC_PATTERNS = arrayOf(
            BannerPattern.SQUARE_BOTTOM_LEFT,
            BannerPattern.SQUARE_BOTTOM_RIGHT,
            BannerPattern.SQUARE_TOP_LEFT,
            BannerPattern.SQUARE_TOP_RIGHT,
            BannerPattern.STRIPE_BOTTOM,
            BannerPattern.STRIPE_TOP,
            BannerPattern.STRIPE_LEFT,
            BannerPattern.STRIPE_RIGHT,
            BannerPattern.STRIPE_CENTER,
            BannerPattern.STRIPE_MIDDLE,
            BannerPattern.STRIPE_DOWNRIGHT,
            BannerPattern.STRIPE_DOWNLEFT,
            BannerPattern.STRIPE_SMALL,
            BannerPattern.CROSS,
            BannerPattern.STRAIGHT_CROSS,
            BannerPattern.TRIANGLE_BOTTOM,
            BannerPattern.TRIANGLE_TOP,
            BannerPattern.TRIANGLES_BOTTOM,
            BannerPattern.TRIANGLES_TOP,
            BannerPattern.DIAGONAL_LEFT,
            BannerPattern.DIAGONAL_RIGHT,
            BannerPattern.DIAGONAL_LEFT_MIRROR,
            BannerPattern.DIAGONAL_RIGHT_MIRROR,
            BannerPattern.CIRCLE_MIDDLE,
            BannerPattern.RHOMBUS_MIDDLE,
            BannerPattern.HALF_VERTICAL,
            BannerPattern.HALF_HORIZONTAL,
            BannerPattern.HALF_VERTICAL_MIRROR,
            BannerPattern.HALF_HORIZONTAL_MIRROR,
            BannerPattern.BORDER,
            BannerPattern.CURLY_BORDER,
            BannerPattern.GRADIENT,
            BannerPattern.GRADIENT_UP,
            BannerPattern.BRICKS
        )
        private val ROWS = ceil(BASIC_PATTERNS.size / 4f).toInt()
        private val GREY = EnumDyeColor.GRAY
        private val WHITE = EnumDyeColor.WHITE
        private val PALETTE = arrayListOf(GREY, WHITE)
        private val PATTERN_TEXTURES: Array<ResourceLocation?> = BannerPattern.values().map {
            val s = "b" + GREY.metadata
            val s1 = it.hashname + WHITE.metadata
            BannerTextures.BANNER_DESIGNS.getResourceLocation(
                s + s1,
                arrayListOf(BannerPattern.BASE, it),
                PALETTE
            )
        }.toTypedArray()
        private val BASIC_TEXTURES: Array<ResourceLocation?> = BASIC_PATTERNS.map {
            val s = "b" + GREY.metadata
            val s1 = it.hashname + WHITE.metadata
            BannerTextures.BANNER_DESIGNS.getResourceLocation(
                s + s1,
                arrayListOf(BannerPattern.BASE, it),
                PALETTE
            )
        }.toTypedArray()
    }
}