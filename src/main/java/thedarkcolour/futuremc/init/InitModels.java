package thedarkcolour.futuremc.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thedarkcolour.core.item.Modeled;

@SideOnly(Side.CLIENT)
public final class InitModels {
    public static void initModel() {
        for(Modeled modeled : InitElements.MODELED) {
            if (modeled instanceof Item) {
                if (ForgeRegistries.ITEMS.getValue(((Item) modeled).getRegistryName()) != null) {
                    modeled.model();
                }
            } else if (ForgeRegistries.BLOCKS.getValue(((Block) modeled).getRegistryName()) != null) {
                modeled.model();
            }
        }
    }
}