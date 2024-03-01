package thedarkcolour.futuremc.client

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiMerchant
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.world.GameType
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import org.lwjgl.input.Keyboard
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
    var selected = GameType.SURVIVAL
    val models = ArrayList<Triple<Item, Int, String>>()
    var showGameModeSwitcher = false

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
        }
        // todo escape will cancel the gamemode switch
        if (Keyboard.isKeyDown(Keyboard.KEY_F3)) {
            if (Keyboard.getEventKey() == Keyboard.KEY_F4 && Keyboard.getEventKeyState()) {
                // just selected
                if (!showGameModeSwitcher) {
                    // todo grab mouse
                    selected = prevGameMode
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
            }
        } else if (Keyboard.getEventKey() == Keyboard.KEY_F3) {
            // release from menu
            // todo release mouse
            showGameModeSwitcher = false

            val player = Minecraft.getMinecraft().player

            if (player != null && player.permissionLevel >= 2) {
                if (Minecraft.getMinecraft().connection?.getPlayerInfo(Minecraft.getMinecraft().player.gameProfile.id)?.gameType != selected) {
                    NetworkHandler.sendGameModeSwitch(selected)
                }
            }
        }
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