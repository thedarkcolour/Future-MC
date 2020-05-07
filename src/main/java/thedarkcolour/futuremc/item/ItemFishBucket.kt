package thedarkcolour.futuremc.item

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import net.minecraftforge.event.ForgeEventFactory
import thedarkcolour.core.item.ItemModeled
import thedarkcolour.core.util.getOrCreateTag
import thedarkcolour.core.util.stack
import thedarkcolour.futuremc.FutureMC
import thedarkcolour.futuremc.config.FConfig
import thedarkcolour.futuremc.entity.fish.EntityFish
import thedarkcolour.futuremc.entity.fish.tropical.EntityTropicalFish
import thedarkcolour.futuremc.entity.fish.tropical.EntityTropicalFish.Companion.getBodyDyeColor
import thedarkcolour.futuremc.entity.fish.tropical.EntityTropicalFish.Companion.getPatternDyeColor
import thedarkcolour.futuremc.entity.fish.tropical.EntityTropicalFish.Companion.getTranslationPrefix
import thedarkcolour.futuremc.entity.fish.tropical.EntityTropicalFish.Companion.getTranslationPrefixSpecial
import thedarkcolour.futuremc.registry.FSounds

class ItemFishBucket<E : EntityFish>(private val regName: String, private val fishType: (World) -> E) : ItemModeled(regName) {
    init {
        creativeTab = if (FConfig.useVanillaCreativeTabs) CreativeTabs.MISC else FutureMC.GROUP
    }

    @Suppress("NAME_SHADOWING")
    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
        val stack = playerIn.getHeldItem(handIn)
        val result: RayTraceResult? = rayTrace(worldIn, playerIn, false)
        val ret = ForgeEventFactory.onBucketUse(playerIn, worldIn, stack, result)
        if (ret != null) return ret

        when {
            result == null -> {
                return ActionResult(EnumActionResult.PASS, stack)
            }
            result.typeOfHit != RayTraceResult.Type.BLOCK -> {
                return ActionResult(EnumActionResult.PASS, stack)
            }
            else -> {
                val pos = result.blockPos

                if (!worldIn.isBlockModifiable(playerIn, pos)) {
                    return ActionResult(EnumActionResult.FAIL, stack)
                } else {
                    val flag = worldIn.getBlockState(pos).block.isReplaceable(worldIn, pos)
                    val pos = if (flag && result.sideHit == EnumFacing.UP) pos else pos.offset(result.sideHit)

                    if (!playerIn.canPlayerEdit(pos, result.sideHit, stack)) {
                        return ActionResult(EnumActionResult.FAIL, stack)
                    } else if (tryPlaceContainedLiquid(playerIn, worldIn, pos)) {
                        if (!worldIn.isRemote) {
                            val fish = fishType(worldIn)
                            fish.fromBucket = true
                            fish.setPosition(pos.x.toDouble() + 0.5, pos.y.toDouble(), pos.z.toDouble() + 0.5)
                            if (fish is EntityTropicalFish) {
                                if (stack.getOrCreateTag().hasKey("BucketVariantTag", 3)) {
                                    fish.variant = stack.tagCompound!!.getInteger("BucketVariantTag")
                                } else {
                                    fish.generateVariant()
                                }
                            }
                            worldIn.spawnEntity(fish)
                        }
                        if (playerIn is EntityPlayerMP) {
                            CriteriaTriggers.PLACED_BLOCK.trigger(playerIn, pos, stack)
                        }
                        return if (!playerIn.isCreative) {
                            ActionResult(EnumActionResult.SUCCESS, Items.BUCKET.stack)
                        } else {
                            ActionResult(EnumActionResult.SUCCESS, stack)
                        }
                    } else {
                        return ActionResult(EnumActionResult.FAIL, stack)
                    }
                }
            }
        }
    }

    private fun tryPlaceContainedLiquid(player: EntityPlayer, worldIn: World, pos: BlockPos): Boolean {
        val state = worldIn.getBlockState(pos)
        val material = state.material
        val flag = !material.isSolid
        val flag1 = state.block.isReplaceable(worldIn, pos)

        if (!worldIn.isAirBlock(pos) && !flag && !flag1) {
            return false
        } else {
            if (worldIn.provider.doesWaterVaporize()) {
                val x = pos.x
                val y = pos.y
                val z = pos.z

                worldIn.playSound(
                    player,
                    pos,
                    SoundEvents.BLOCK_FIRE_EXTINGUISH,
                    SoundCategory.BLOCKS,
                    0.5F,
                    2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F
                )

                for (k in 0..7) {
                    worldIn.spawnParticle(
                        EnumParticleTypes.SMOKE_LARGE,
                        x.toDouble() + Math.random(),
                        y.toDouble() + Math.random(),
                        z.toDouble() + Math.random(),
                        0.0,
                        0.0,
                        0.0
                    )
                }
            } else {
                if (!worldIn.isRemote && (flag || flag1) && !material.isLiquid) {
                    worldIn.destroyBlock(pos, true)
                }

                worldIn.playSound(player, pos, FSounds.BUCKET_EMPTY_FISH, SoundCategory.BLOCKS, 1F, 1F)
                worldIn.setBlockState(pos, Blocks.FLOWING_WATER.defaultState, 11)
            }

            return true
        }
    }

    override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag) {
        if (regName == "tropical_fish_bucket") {

            stack.tagCompound?.let { tag ->
                if (tag.hasKey("BucketVariantTag", 3)) {
                    val i = tag.getInteger("BucketVariantTag")
                    val formatting = arrayOf(TextFormatting.ITALIC, TextFormatting.GRAY)
                    val s = "color.minecraft.${getBodyDyeColor(i)}"
                    val s1 = "color.minecraft.${getPatternDyeColor(i)}"

                    for (variant in EntityTropicalFish.SPECIAL_VARIANTS) {
                        if (i == variant) {
                            tooltip.add(
                                TextComponentTranslation(getTranslationPrefixSpecial(variant)).applyTextFormatting(
                                    *formatting
                                ).formattedText
                            )
                            return
                        }
                    }

                    tooltip.add(TextComponentTranslation(getTranslationPrefix(i)).applyTextFormatting(*formatting).formattedText)
                    val component = TextComponentTranslation(s)
                    if (s != s1) {
                        component.appendText(", ").appendSibling(TextComponentTranslation(s1))
                    }

                    component.applyTextFormatting(*formatting)
                    tooltip.add(component.formattedText)
                }
            }
        }
    }
}