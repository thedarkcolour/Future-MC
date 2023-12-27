package thedarkcolour.futuremc.client.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.client.renderer.BannerTextures
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.init.SoundEvents
import net.minecraft.item.EnumDyeColor.GRAY
import net.minecraft.item.EnumDyeColor.WHITE
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

class GuiLoom(container: ContainerLoom) : FGui<ContainerLoom>(container) {
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
        container.setInventoryUpdateListener { onInventoryUpdate() }
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        fontRenderer.drawString(I18n.format("container.loom"), 8, 4, 4210752)
        fontRenderer.drawString(getContainer().playerInv.displayName.unformattedText, 8, ySize - 94, 4210752)
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        mc.textureManager.bindTexture(BACKGROUND)
        val i = guiLeft
        val j = guiTop
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize)
        val bannerSlot = getContainer().getLoomSlot(0)
        val dyeSlot = getContainer().getLoomSlot(1)
        val patternSlot = getContainer().getLoomSlot(2)
        val outputSlot = getContainer().getLoomSlot(3)
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
            val listLeft = i + 60
            val listTop = j + 13

            if (isCrafting) {
                val lastVisibleItem = recipeIndexOffset + 16
                var index = recipeIndexOffset

                while (index < lastVisibleItem && index <= ContainerLoom.BASIC_PATTERNS.size) {
                    val listIndex = index - recipeIndexOffset
                    val slotLeft = listLeft + listIndex % 4 * 14
                    val slotTop = listTop + listIndex / 4 * 14

                    mc.textureManager.bindTexture(BACKGROUND)
                    var k2 = ySize
                    if (index == getContainer().getSelectedIndex()) {
                        k2 += 14
                    } else if (((mouseX - slotLeft) in 0..14) && ((mouseY - slotTop) in 0..14)) {
                        k2 += 28
                    }
                    drawTexturedModalRect(slotLeft, slotTop, 0, k2, 14, 14)

                    if (BASIC_TEXTURES[index - 1] != null) {
                        mc.textureManager.bindTexture(BASIC_TEXTURES[index - 1]!!)
                        drawScaledCustomSizeModalRect(slotLeft + 4, slotTop + 2, 1f, 1f, 20, 40, 5, 10, 64.0f, 64.0f)
                    }
                    ++index
                }
            } else if (hasBannerPattern) {
                val index = getContainer().getSelectedIndex()

                mc.textureManager.bindTexture(BACKGROUND)
                drawTexturedModalRect(listLeft, listTop, 0, ySize, 14, 14)

                if (PATTERN_TEXTURES[index] != null) {
                    mc.textureManager.bindTexture(PATTERN_TEXTURES[index]!!)
                    drawScaledCustomSizeModalRect(listLeft + 4, listTop + 2, 1f, 1f, 20, 40, 5, 10, 64f, 64f)
                }
            }
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        clickedOnScroll = false

        if (isCrafting) {
            var listLeft = guiLeft + 60
            var listTop = guiTop + 13
            val lastVisibleItem = recipeIndexOffset + 16

            for (i in recipeIndexOffset until lastVisibleItem) {
                val listIndex = i - recipeIndexOffset
                val mX = mouseX - (listLeft + listIndex % 4 * 14).toDouble()
                val mY = mouseY - (listTop + listIndex / 4 * 14).toDouble()

                if (mX in 0.0..14.0 && mY in 0.0..14.0 && getContainer().enchantItem(mc.player, i)) {
                    Minecraft.getMinecraft().soundHandler.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f))
                    mc.playerController.sendEnchantPacket(getContainer().windowId, i)
                    return
                }
            }
            listLeft = guiLeft + 119
            listTop = guiTop + 9
            if (mouseX >= listLeft.toDouble() && mouseX < (listLeft + 12) && mouseY >= listTop.toDouble() && mouseY < (listTop + 56).toDouble()) {
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
        val stack = getContainer().output

        preview = if (stack.isEmpty) {
            null
        } else {
            val tile = TileEntityBanner()
            tile.setItemValues(stack, false)
            BannerTextures.BANNER_DESIGNS.getResourceLocation(tile.patternResourceLocation, tile.patternList, tile.colorList)
        }

        val banner = getContainer().banner
        val color = getContainer().color
        val pattern = getContainer().pattern
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
        private val ROWS = ceil(ContainerLoom.BASIC_PATTERNS.size / 4.0f).toInt()
        // Used for previews in the list
        private val PALETTE = listOf(GRAY, WHITE)

        // Contains all pattern textures
        private val PATTERN_TEXTURES = arrayListOf<ResourceLocation?>()
        // Contains only regular textures
        private val BASIC_TEXTURES = arrayListOf<ResourceLocation?>()

        init {
            for (pattern in BannerPattern.values()) {
                val s = "b" + GRAY.metadata
                val s1 = pattern.hashname + WHITE.metadata

                val texture = BannerTextures.BANNER_DESIGNS.getResourceLocation(s + s1, listOf(BannerPattern.BASE, pattern), PALETTE)

                if (texture == null) {
                    FutureMC.LOGGER.error("Failed to load texture for banner pattern: $pattern")
                }

                PATTERN_TEXTURES.add(texture)
                if (ContainerLoom.BASIC_PATTERNS.contains(pattern)) {
                    BASIC_TEXTURES.add(texture)
                }
            }
        }
    }
}