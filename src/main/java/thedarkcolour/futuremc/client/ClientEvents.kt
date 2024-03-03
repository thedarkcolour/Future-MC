package thedarkcolour.futuremc.client

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiMerchant
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraft.world.GameType
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.client.gui.GuiVillager
import thedarkcolour.futuremc.client.render.TridentBakedModel
import thedarkcolour.futuremc.compat.OE
import thedarkcolour.futuremc.compat.isModLoaded
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.container.ContainerVillager
import thedarkcolour.futuremc.item.TridentItem
import thedarkcolour.futuremc.network.NetworkHandler

object ClientEvents {
    var prevGameMode = GameType.CREATIVE
    var selected = GameType.NOT_SET
    val models = ArrayList<Triple<Item, Int, String>>()
    private var showGameModeSwitcher = false
    private var oldDebugSetting = false
    @JvmStatic
    private val gameModeSwitcherTexture = ResourceLocation(FutureMC.ID, "textures/gui/gamemode_switcher.png")
    @JvmStatic
    private val gameTypeItems = arrayOf(ItemStack(Blocks.GRASS), ItemStack(Items.IRON_SWORD), ItemStack(Items.MAP), ItemStack(Items.ENDER_EYE))
    private val gameTypes = arrayOf(GameType.CREATIVE, GameType.SURVIVAL, GameType.ADVENTURE, GameType.SPECTATOR)

    @SubscribeEvent
    fun onGuiOpen(event: GuiOpenEvent) {
        if (FConfig.villageAndPillage.newVillagerGui) {
            val gui = event.gui

            if (gui is GuiMerchant && gui !is GuiVillager) {
                event.gui = GuiVillager(ContainerVillager(Minecraft.getMinecraft().player.inventory, gui.merchant, null))
            }
        }
    }

    @SubscribeEvent
    fun onModelRegistry(event: ModelRegistryEvent) {
        for (item in models) {
            ModelLoader.setCustomModelResourceLocation(item.first, item.second, ModelResourceLocation(item.third, "inventory"))
        }
    }

    @SubscribeEvent
    fun onModelBake(event: ModelBakeEvent) {
        if (!isModLoaded(OE)) {
            if (FConfig.updateAquatic.trident) {
                val registry = event.modelRegistry
                val trident = ModelResourceLocation("futuremc:trident", "inventory")
                val simpleModel = registry.getObject(trident)!!
                TridentItem.simpleModel = simpleModel
                val hand = registry.getObject(ModelResourceLocation("futuremc:trident_in_hand", "inventory"))!!

                registry.putObject(trident, TridentBakedModel(hand, simpleModel))
            }
        }
    }

    @SubscribeEvent
    fun onKeyInput(event: KeyInputEvent) {
        if (!FConfig.netherUpdate.gameModeSwitcher) {
            showGameModeSwitcher = false
            return
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_F3)) {
            if (Keyboard.getEventKey() == Keyboard.KEY_F4 && Keyboard.getEventKeyState()) {
                // just selected
                if (!showGameModeSwitcher) {
                    val currentGameMode = getGameMode()
                    if (selected == GameType.NOT_SET) {
                        if (currentGameMode == GameType.CREATIVE) {
                            selected = GameType.SURVIVAL
                        } else {
                            selected = GameType.CREATIVE
                        }
                    }
                    if (currentGameMode != prevGameMode) {
                        selected = prevGameMode
                    }
                    // Release cursor
                    Minecraft.getMinecraft().inGameHasFocus = false
                    Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor()
                } else {
                    // cycle game mode
                    selected = when (selected) {
                        GameType.CREATIVE -> GameType.SURVIVAL
                        GameType.SURVIVAL -> GameType.ADVENTURE
                        GameType.ADVENTURE -> GameType.SPECTATOR
                        else -> GameType.CREATIVE
                    }
                }
                showGameModeSwitcher = true
                // Save setting to avoid opening F3 menu when selected
                oldDebugSetting = Minecraft.getMinecraft().gameSettings.showDebugInfo
            }
        } else if (Keyboard.getEventKey() == Keyboard.KEY_F3) {
            if (showGameModeSwitcher) {
                Minecraft.getMinecraft().gameSettings.showDebugInfo = oldDebugSetting
                Minecraft.getMinecraft().inGameHasFocus = true
                Minecraft.getMinecraft().mouseHelper.grabMouseCursor()

                val player = Minecraft.getMinecraft().player

                if (player != null && player.permissionLevel >= 2) {
                    if (getGameMode() != selected) {
                        NetworkHandler.sendGameModeSwitch(selected)
                    }
                }
            }
            // release from menu
            showGameModeSwitcher = false
        }
    }

    @SubscribeEvent
    fun renderOverlay(event: RenderGameOverlayEvent.Post) {
        if (showGameModeSwitcher && event.type == RenderGameOverlayEvent.ElementType.ALL) {
            GlStateManager.enableBlend()
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)
            RenderHelper.enableGUIStandardItemLighting()
            val mc = Minecraft.getMinecraft()
            mc.textureManager.bindTexture(gameModeSwitcherTexture)

            val resolutionWidth = event.resolution.scaledWidth
            val resolutionHeight = event.resolution.scaledHeight
            val mouseX = Mouse.getX() * resolutionWidth / mc.displayWidth
            val mouseY = resolutionHeight - Mouse.getY() * resolutionHeight / mc.displayHeight - 1
            val x = resolutionWidth / 2
            val y = resolutionHeight / 2 - 31
            mc.ingameGUI.drawTexturedModalRect(x - 62, y - 27, 0, 0, 125, 75)

            for (i in 0..3) {
                // render
                val x1 = x - 119 / 2 + i * 31

                // hover select
                if (x1 <= mouseX && mouseX <= x1 + 26 && y <= mouseY && mouseY <= y + 26) {
                    selected = gameTypes[i]
                }

                mc.ingameGUI.drawTexturedModalRect(x1, y, 0, 75, 26, 26)
                if (selected == gameTypes[i]) {
                    // render select
                    mc.ingameGUI.drawTexturedModalRect(x1, y, 26, 75, 26, 26)
                }
            }
            for (i in 0..3) {
                val x1 = x - 119 / 2 + i * 31
                mc.renderItem.renderItemAndEffectIntoGUI(gameTypeItems[i], x1 + 5, y + 5)
            }

            mc.ingameGUI.drawCenteredString(mc.fontRenderer, I18n.format("gameMode." + selected.getName()), x, y - 20, 0xffffff)
            mc.ingameGUI.drawCenteredString(mc.fontRenderer, "[ F4 ]", x, y + 5 + 31, 5636095)
            GlStateManager.disableBlend()
            RenderHelper.disableStandardItemLighting()
        }
    }

    private fun getGameMode(): GameType {
        return Minecraft.getMinecraft().connection?.getPlayerInfo(Minecraft.getMinecraft().player.gameProfile.id)?.gameType ?: GameType.NOT_SET
    }

    /**
     * If the player has a Trident (or eventually a Crossbow)
     * this sets the correct pose for the player's arms.
     */
    fun getCustomArmPose(
        playerIn: EntityPlayer,
        mainItem: ItemStack,
        offItem: ItemStack,
        hand: EnumHand
    ): ModelBiped.ArmPose? {
        val stack = if (hand == EnumHand.MAIN_HAND) mainItem else offItem

        if (!stack.isEmpty) {
            if (playerIn.itemInUseCount > 0) {
                val action = stack.itemUseAction

                if (action == TridentBakedModel.TRIDENT_USE_ACTION) {
                    return TridentBakedModel.TRIDENT_ARM_POSE
                }/* else if (action == CrossbowItem.CROSSBOW_USE_ACTION) {
                    pose = CrossbowItem.CROSSBOW_ARM_POSE
                }
            } else {
                val flag3 = mainItem.item == FItems.CROSSBOW
                val flag = CrossbowItem.isCharged(mainItem)
                val flag1 = offItem.item == FItems.CROSSBOW
                val flag2 = CrossbowItem.isCharged(offItem)

                if (flag3 && flag) {
                    pose = CrossbowItem.CROSSBOW_ARM_POSE
                }

                if (flag1 && flag2) {
                    pose = CrossbowItem.CROSSBOW_ARM_POSE
                }*/
            }
        }

        return null
    }
}