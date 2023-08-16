package thedarkcolour.futuremc.client

import net.minecraft.block.BlockStairs
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiMerchant
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.ObfuscationReflectionHelper
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import thedarkcolour.futuremc.client.gui.GuiVillager
import thedarkcolour.futuremc.client.render.TridentBakedModel
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.container.ContainerVillager
import thedarkcolour.futuremc.item.TridentItem
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object ClientEvents {
    val models = ArrayList<Triple<Item, Int, String>>()

    init {

    }

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
        if (FConfig.updateAquatic.trident) {
            val registry = event.modelRegistry
            val trident = ModelResourceLocation("futuremc:trident", "inventory")
            val simpleModel = registry.getObject(trident)!!
            TridentItem.simpleModel = simpleModel
            val hand = registry.getObject(ModelResourceLocation("futuremc:trident_in_hand", "inventory"))!!

            registry.putObject(trident, TridentBakedModel(hand, simpleModel))
        }
    }

    // todo fix
    @SubscribeEvent
    fun onDrawBlockHighlight(event: DrawBlockHighlightEvent) {
        if (event.target.typeOfHit == RayTraceResult.Type.BLOCK) {
            val world = Minecraft.getMinecraft().world
            val pos = event.target.blockPos
            var state = world.getBlockState(pos)

            if (state.block is BlockStairs) {
                state = state.getActualState(world, pos)

                if (state.properties.containsKey(BlockStairs.SHAPE) && state.properties.containsKey(BlockStairs.HALF) && state.properties.containsKey(BlockStairs.FACING)) {
                    event.isCanceled = true

                    val list = arrayListOf<AxisAlignedBB>()
                    state.addCollisionBoxToList(world, pos, state.getSelectedBoundingBox(world, pos), list, null, true)

                    val partialTicks = event.partialTicks
                    val player = Minecraft.getMinecraft().player
                    val d3 = pos.x - (player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks.toDouble())
                    val d4 = pos.y - (player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks.toDouble())
                    val d5 = pos.z - (player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks.toDouble())

                    //val tessellator = Tessellator.getInstance()
                    //val builder = tessellator.buffer
                    //builder.begin(GL11.GL_LINE, DefaultVertexFormats.POSITION_COLOR)

                    for (box in list) {
                        val minX = box.minX
                        val minY = box.minY
                        val minZ = box.minZ
                        val maxX = box.maxX
                        val maxY = box.maxY
                        val maxZ = box.maxZ
                    }
                    //FancyBlockOutlines.drawStairsOutline(pos, event.partialTicks, state.getValue(BlockStairs.SHAPE), state.getValue(BlockStairs.HALF), state.getValue(BlockStairs.FACING))
                }
            }
        }
    }

    fun getFaces(box: AxisAlignedBB): ArrayList<Face> {
        return arrayListOf(
            // x axis plane
            Face(EnumFacing.Axis.X, box.minX, box.minY, box.minZ, box.minX, box.maxY, box.maxZ),
            Face(EnumFacing.Axis.X, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ),
            // y axis plane
            Face(EnumFacing.Axis.Y, box.minX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ),
            Face(EnumFacing.Axis.Y, box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ),
            // z axis plane
            Face(EnumFacing.Axis.Z, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ),
            Face(EnumFacing.Axis.Z, box.minX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ)
        )
    }

    init {
        val facings = EnumFacing.HORIZONTALS
        val halves = BlockStairs.EnumHalf.values()
        val shapes = BlockStairs.EnumShape.values()
        val getCollisionBoxListMethod = ObfuscationReflectionHelper.findMethod(BlockStairs::class.java, "func_185708_x", List::class.java, IBlockState::class.java)

        val statesToBounds = Array(40) { i ->
            val facing = facings[i / 10]
            val half = halves[(i % 10) / 5]
            val shape = shapes[i % 5]
            val state = Blocks.OAK_STAIRS.defaultState.withProperty(BlockStairs.FACING, facing).withProperty(BlockStairs.HALF, half).withProperty(BlockStairs.SHAPE, shape)
            getCollisionBoxListMethod.invoke(null, state) as List<AxisAlignedBB>
        }

        for (bounds in statesToBounds) {
            val faces = bounds.flatMapTo(ArrayList(), ::getFaces)
            var i = 0

            while (i < faces.size) {
                val face = faces[i++]

                var j = 0
                while (j < faces.size) {
                    val other = faces[j++]

                    if (face.overlaps(other)) {
                        faces.remove(face)
                        faces.remove(other)

                        faces.addAll(splitOverlappingFaces(face, other))
                    }
                }
            }
        }
    }

    // This method assumes the two faces are overlapping
    private fun splitOverlappingFaces(first: Face, second: Face): ArrayList<Face> {
        // intersection is the smallest face made from corners of the overlapping faces
        var intersection: Face? = null
        var minArea = Double.MAX_VALUE

        for (min in first.getCorners()) {
            for (max in second.getCorners()) {
                if (min != max) {
                    var xSpan = abs(min.x - max.x)
                    var ySpan = abs(min.y - max.y)
                    var zSpan = abs(min.z - max.z)
                    var zeroSpans = 0

                    // Don't know which axis is coplanar with the overlap, so ignore ONE span.
                    // If a span other than the coplanar one is 0, the face is 1D, so skip.
                    if (xSpan == 0.0) {
                        xSpan = 1.0
                        zeroSpans++
                    }
                    if (ySpan == 0.0) {
                        ySpan = 1.0
                        zeroSpans++
                    }
                    if (zSpan == 0.0) {
                        zSpan = 1.0
                        zeroSpans++
                    }
                    if (zeroSpans > 1) continue

                    // since one span is 1.0 (because the faces are assumed to overlap),
                    // we can treat the volume as area
                    val area = xSpan * ySpan * zSpan
                    if (area < minArea) {
                        minArea = area
                        intersection = Face(first.normal, min(min.x, max.x), min(min.y, max.y), min(min.z, max.z), max(min.x, max.x), max(min.y, max.y), max(min.z, max.z))
                    }
                }
            }
        }
        if (intersection == null) {
            throw IllegalArgumentException("Faces are not overlapping!")
        }
        if (first.normal == EnumFacing.Axis.X) {

        }
    }

    data class Face(
        val normal: EnumFacing.Axis,
        val minX: Double,
        val minY: Double,
        val minZ: Double,
        val maxX: Double,
        val maxY: Double,
        val maxZ: Double
    ) {
        fun overlaps(other: Face): Boolean {
            if (normal == other.normal) {
                return when (normal) {
                    EnumFacing.Axis.X -> minX == other.minX && ((minY < other.maxY && maxY > other.minY) || (minZ < other.maxZ && maxZ > other.minZ))
                    EnumFacing.Axis.Y -> minY == other.minY && ((minX < other.maxX && maxX > other.minX) || (minZ < other.maxZ && maxZ > other.minZ))
                    EnumFacing.Axis.Z -> minZ == other.minZ && ((minX < other.maxX && maxX > other.minX) || (minY < other.maxY && maxY > other.minY))
                }
            }
            return false
        }

        fun getLines(): ArrayList<Line> {
            return arrayListOf(
                Line(minX, minY, minZ, maxX, maxY, minZ),
                Line(minX, minY, minZ, minX, minY, maxZ),
                Line(minX, minY, maxZ, maxX, maxY, maxZ),
                Line(maxX, maxY, minZ, maxX, maxY, maxZ)
            )
        }

        fun getCorners(): Array<Vec3d> {
            return arrayOf(
                Vec3d(minX, minY, minZ),
                Vec3d(maxX, maxY, minZ),
                Vec3d(minX, minY, maxZ),
                Vec3d(maxX, maxY, maxZ)
            )
        }
    }

    data class Line(
        val minX: Double,
        val minY: Double,
        val minZ: Double,
        val maxX: Double,
        val maxY: Double,
        val maxZ: Double
    )

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