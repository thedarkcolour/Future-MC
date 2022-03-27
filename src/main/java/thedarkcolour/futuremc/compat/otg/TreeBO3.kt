package thedarkcolour.futuremc.compat.otg

import com.pg85.otg.common.LocalWorld
import com.pg85.otg.customobjects.bo3.BO3
import com.pg85.otg.customobjects.structures.CustomStructure
import com.pg85.otg.forge.materials.ForgeMaterialData
import com.pg85.otg.forge.world.ForgeWorld
import com.pg85.otg.util.ChunkCoordinate
import com.pg85.otg.util.bo3.Rotation
import net.minecraft.block.BlockLeaves
import net.minecraft.block.BlockLog
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.relauncher.ReflectionHelper
import thedarkcolour.core.util.isAir
import thedarkcolour.futuremc.world.gen.feature.BeeNestGenerator
import java.io.File
import java.util.*

class TreeBO3(obj: BO3) : BO3(nameField.get(obj) as String, fileField.get(obj) as File) {
    init {
        cfgField.set(this, cfgField.get(obj))
        invalidField.set(this, invalidField.get(obj))
    }

    companion object {
        val nameField = ReflectionHelper.findField(BO3::class.java, "name")
        val fileField = ReflectionHelper.findField(BO3::class.java, "file")
        val cfgField = ReflectionHelper.findField(BO3::class.java, "settings")
        val invalidField = ReflectionHelper.findField(BO3::class.java, "isInvalidConfig")
    }

    // This is the function for adding beehives
    @Suppress("NAME_SHADOWING")
    override fun handleBO3Functions(structure: CustomStructure?, world: LocalWorld?, random: Random, rotation: Rotation, x: Int, y: Int, z: Int, chunks: HashSet<ChunkCoordinate>?, chunkBeingPopulated: ChunkCoordinate?) {
        super.handleBO3Functions(structure, world, random, rotation, x, y, z, chunks, chunkBeingPopulated)
        postProcess(world, random, rotation, x, y, z)
    }

    fun postProcess(world: LocalWorld?, random: Random, rotation: Rotation, x: Int, y: Int, z: Int) {
        // obtain vanilla world
        val worldIn = (world as ForgeWorld).world

        // random chance to have a beehive based on biome
        if (BeeNestGenerator.fastCannotGenerate(worldIn, random, BlockPos(x, 0, z))) return

        for (block in settings.getBlocks(rotation.rotationId)) {
            // Skip all non-log blocks
            val material = block.material

            if (material is ForgeMaterialData) {
                if (material.internalBlock().block is BlockLog) {
                    val rotationDir = random.nextInt(3)
                    val dir = BeeNestGenerator.VALID_OFFSETS[rotationDir]
                    val sidePos = BlockPos(block.x + x, block.y + y, block.z + z).offset(dir)
                    // on the side of the log
                    val worldState = worldIn.getBlockState(sidePos)

                    // Extension function
                    // If the block below is air and pos is replaceable, and leaves are above
                    if (worldState.block.isReplaceable(worldIn, sidePos) && worldIn.isAir(sidePos.down()) && worldIn.getBlockState(sidePos.up()).block is BlockLeaves) {
                        BeeNestGenerator.placeBeeHive(worldIn, random, sidePos)
                        break
                    }
                }
            }
        }
    }
}