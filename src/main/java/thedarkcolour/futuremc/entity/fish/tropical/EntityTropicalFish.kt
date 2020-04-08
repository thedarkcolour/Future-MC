package thedarkcolour.futuremc.entity.fish.tropical

import net.minecraft.entity.IEntityLivingData
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.DamageSource
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.World
import thedarkcolour.core.util.getOrCreateTag
import thedarkcolour.core.util.stack
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.entity.fish.EntityGroupFish
import thedarkcolour.futuremc.registry.FItems
import thedarkcolour.futuremc.registry.FSounds
import java.util.*

class EntityTropicalFish(worldIn: World) : EntityGroupFish(worldIn) {
    private var isSpecialVariant = true
    var variant: Int
        get() {
            return dataManager[VARIANT]
        }
        set(value) {
            dataManager[VARIANT] = value
        }

    override fun entityInit() {
        super.entityInit()
        dataManager.register(VARIANT, 0)
    }

    override fun writeEntityToNBT(compound: NBTTagCompound) {
        super.writeEntityToNBT(compound)
        compound.setInteger("Variant", variant)
    }

    override fun readEntityFromNBT(compound: NBTTagCompound) {
        super.readEntityFromNBT(compound)
        variant = compound.getInteger("Variant")
    }

    override fun setBucketData(stack: ItemStack) {
        super.setBucketData(stack)
        val nbt = stack.getOrCreateTag()
        nbt.setInteger("BucketVariant", variant)
    }

    override fun getFishBucket(): ItemStack = FItems.TROPICAL_FISH_BUCKET.stack

    override fun getAmbientSound(): SoundEvent = FSounds.TROPICAL_FISH_AMBIENT

    override fun getDeathSound(): SoundEvent = FSounds.TROPICAL_FISH_DEATH

    override fun getHurtSound(damageSourceIn: DamageSource): SoundEvent = FSounds.TROPICAL_FISH_HURT

    override val flopSound: SoundEvent
        get() = FSounds.TROPICAL_FISH_FLOP

    fun getSize(): Int {
        return getBody(variant)
    }

    fun getPatternTexture(): ResourceLocation {
        return if (getBody(variant) == 0) {
            PATTERN_TEXTURES_A[getPattern(variant)]
        } else {
            PATTERN_TEXTURES_B[getPattern(variant)]
        }
    }

    fun getPatternColorComponentValues(): FloatArray {
        return EnumDyeColor.byMetadata(getPatternColor(variant)).colorComponentValues
    }

    fun getBodyColorComponentValues(): FloatArray {
        return EnumDyeColor.byMetadata(getBodyColor(variant)).colorComponentValues
    }

    fun getBodyTexture(): ResourceLocation {
        return BODY_TEXTURES[getBody(variant)]
    }

    override fun onInitialSpawn(difficulty: DifficultyInstance, livingdata: IEntityLivingData?): IEntityLivingData {
        generateVariant()
        return super.onInitialSpawn(difficulty, livingdata)
    }

    fun generateVariant() {
        val i: Int
        val j: Int
        val k: Int
        val l: Int

        if (rand.nextFloat() < 0.9F) {
            val specialVariant = SPECIAL_VARIANTS[rand.nextInt(SPECIAL_VARIANTS.size)]
            i = specialVariant and 255
            j = (specialVariant and '\uff00'.toInt()) shr 8
            k = (specialVariant and 16711680) shr 16
            l = (specialVariant and -16777216) shr 24
        } else {
            isSpecialVariant = false
            i = rand.nextInt(2)
            j = rand.nextInt(6)
            k = rand.nextInt(15)
            l = rand.nextInt(15)
        }

        variant = i or (j shl 8) or (k shl 16) or (l shl 24)
    }

    enum class Type(val primary: Int, val secondary: Int) {
        KOB(0, 0),
        SUNSTREAK(0, 1),
        SNOOPER(0, 2),
        DASHER(0, 3),
        BRINELY(0, 4),
        SPOTTY(0, 5),
        FLOPPER(1, 0),
        STRIPEY(1, 1),
        GLITTER(1, 2),
        BLOCKFISH(1, 3),
        BETTY(1, 4),
        CLAYFISH(1, 5)
    }

    companion object {
        private val VARIANT = EntityDataManager.createKey(EntityTropicalFish::class.java, DataSerializers.VARINT)
        private val BODY_TEXTURES = arrayOf(
            ResourceLocation(FutureMC.ID, "textures/entity/tropical_fish/tropical_a.png"),
            ResourceLocation(FutureMC.ID, "textures/entity/tropical_fish/tropical_b.png")
        )
        private val PATTERN_TEXTURES_A = arrayOf(
            ResourceLocation(FutureMC.ID, "textures/entity/tropical_fish/tropical_a_pattern_1.png"),
            ResourceLocation(FutureMC.ID, "textures/entity/tropical_fish/tropical_a_pattern_2.png"),
            ResourceLocation(FutureMC.ID, "textures/entity/tropical_fish/tropical_a_pattern_3.png"),
            ResourceLocation(FutureMC.ID, "textures/entity/tropical_fish/tropical_a_pattern_4.png"),
            ResourceLocation(FutureMC.ID, "textures/entity/tropical_fish/tropical_a_pattern_5.png"),
            ResourceLocation(FutureMC.ID, "textures/entity/tropical_fish/tropical_a_pattern_6.png")
        )
        private val PATTERN_TEXTURES_B = arrayOf(
            ResourceLocation(FutureMC.ID, "textures/entity/tropical_fish/tropical_b_pattern_1.png"),
            ResourceLocation(FutureMC.ID, "textures/entity/tropical_fish/tropical_b_pattern_2.png"),
            ResourceLocation(FutureMC.ID, "textures/entity/tropical_fish/tropical_b_pattern_3.png"),
            ResourceLocation(FutureMC.ID, "textures/entity/tropical_fish/tropical_b_pattern_4.png"),
            ResourceLocation(FutureMC.ID, "textures/entity/tropical_fish/tropical_b_pattern_5.png"),
            ResourceLocation(FutureMC.ID, "textures/entity/tropical_fish/tropical_b_pattern_6.png")
        )
        val SPECIAL_VARIANTS = intArrayOf(
            makeVariant(Type.STRIPEY, EnumDyeColor.ORANGE, EnumDyeColor.GRAY),
            makeVariant(Type.FLOPPER, EnumDyeColor.GRAY, EnumDyeColor.GRAY),
            makeVariant(Type.FLOPPER, EnumDyeColor.GRAY, EnumDyeColor.BLUE),
            makeVariant(Type.CLAYFISH, EnumDyeColor.WHITE, EnumDyeColor.GRAY),
            makeVariant(Type.SUNSTREAK, EnumDyeColor.BLUE, EnumDyeColor.GRAY),
            makeVariant(Type.KOB, EnumDyeColor.ORANGE, EnumDyeColor.WHITE),
            makeVariant(Type.SPOTTY, EnumDyeColor.PINK, EnumDyeColor.LIGHT_BLUE),
            makeVariant(Type.BLOCKFISH, EnumDyeColor.PURPLE, EnumDyeColor.YELLOW),
            makeVariant(Type.CLAYFISH, EnumDyeColor.WHITE, EnumDyeColor.RED),
            makeVariant(Type.SPOTTY, EnumDyeColor.WHITE, EnumDyeColor.YELLOW),
            makeVariant(Type.GLITTER, EnumDyeColor.WHITE, EnumDyeColor.GRAY),
            makeVariant(Type.CLAYFISH, EnumDyeColor.WHITE, EnumDyeColor.ORANGE),
            makeVariant(Type.DASHER, EnumDyeColor.CYAN, EnumDyeColor.PINK),
            makeVariant(Type.BRINELY, EnumDyeColor.LIME, EnumDyeColor.LIGHT_BLUE),
            makeVariant(Type.BETTY, EnumDyeColor.RED, EnumDyeColor.WHITE),
            makeVariant(Type.SNOOPER, EnumDyeColor.GRAY, EnumDyeColor.RED),
            makeVariant(Type.BLOCKFISH, EnumDyeColor.RED, EnumDyeColor.WHITE),
            makeVariant(Type.FLOPPER, EnumDyeColor.WHITE, EnumDyeColor.YELLOW),
            makeVariant(Type.KOB, EnumDyeColor.RED, EnumDyeColor.WHITE),
            makeVariant(Type.SUNSTREAK, EnumDyeColor.GRAY, EnumDyeColor.WHITE),
            makeVariant(Type.DASHER, EnumDyeColor.CYAN, EnumDyeColor.YELLOW),
            makeVariant(Type.FLOPPER, EnumDyeColor.YELLOW, EnumDyeColor.YELLOW)
        )

        private fun makeVariant(size: Type, pattern: EnumDyeColor, bodyColor: EnumDyeColor): Int {
            return (size.primary and 255) or ((size.secondary and 255) shl 8) or ((pattern.metadata and 255) shl 16) or ((bodyColor.metadata and 255) shl 24)
        }

        // Gets the Dye color of the body color from the packed variant
        fun getBodyDyeColor(variant: Int): EnumDyeColor {
            return EnumDyeColor.byMetadata(getBodyColor(variant))
        }

        private fun getBodyColor(variant: Int): Int {
            return (variant and 16711680) shr 16
        }

        // Gets the Dye color of the pattern color from the packed variant
        fun getPatternDyeColor(variant: Int): EnumDyeColor {
            return EnumDyeColor.byMetadata(getPatternColor(variant))
        }

        private fun getPatternColor(variant: Int): Int {
            return (variant and -16777216) shr 24
        }

        // Gets the prefix for this unique variant
        fun getTranslationPrefixSpecial(variant: Int): String {
            return "entity.minecraft.tropical_fish.predefined.$variant"
        }

        fun getTranslationPrefix(variant: Int): String {
            val i = getBody(variant)
            val j = getPattern(variant)
            return "entity.futuremc.tropical_fish.type.${getTranslation(i, j)}"
        }

        private fun getBody(variant: Int): Int {
            return (variant and 255).coerceAtMost(1)
        }

        private fun getPattern(variant: Int): Int {
            return (variant and '\uff00'.toInt() shr 8).coerceAtMost(5)
        }

        private fun getTranslation(i: Int, j: Int) {
            Type.values()[i + 6 * j].name.toLowerCase(Locale.ROOT)
        }
    }
}