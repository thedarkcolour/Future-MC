package thedarkcolour.futuremc.api;

import net.minecraft.block.state.IBlockState;
import thedarkcolour.futuremc.entity.bee.EntityBee;

/**
 * Class to add/remove pollination targets for the bee.
 *
 * Although the {@link EntityBee#FLOWERS} field is static,
 * it is probably better to have Java methods here
 * to have everything in one place.
 *
 * @author TheDarkColour
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class BeePollinationTargetsJVM {
    /**
     * Add a pollination target (poppy, cornflower, etc.) for the bee.
     *
     * @param state the state of the pollination target to be added
     */
    public static void addPollinationTarget(IBlockState state) {
        EntityBee.FLOWERS.add(state);
    }

    /**
     * Add multiple pollination targets (poppy, cornflower, etc.) for the bee.
     *
     * @param states the pollination targets to be added
     */
    public static void addPollinationTargets(IBlockState... states) {
        for (IBlockState state : states) {
            addPollinationTarget(state);
        }
    }

    /**
     * Remove a pollination target from the bee.
     *
     * @param state the state of the pollination target to be removed
     * @return if the list was changed
     */
    public static boolean removePollinationTarget(IBlockState state) {
        return EntityBee.FLOWERS.remove(state);
    }

    /**
     * Remove multiple pollination targets (poppy, cornflower, etc.) from the bee.
     *
     * @param states the pollination targets to be removed
     */
    public static void removePollinationTargets(IBlockState... states) {
        for (IBlockState state : states) {
            removePollinationTarget(state);
        }
    }
}
