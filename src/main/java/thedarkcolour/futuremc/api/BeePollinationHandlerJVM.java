package thedarkcolour.futuremc.api;

import net.minecraft.block.Block;
import thedarkcolour.futuremc.entity.bee.EntityBee;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * This class provides Java methods for {@link BeePollinationHandler}.
 * <p>
 * Keep in mind that the Kotlin functions this class points to
 * perform intrinsic nullability checks on ALL parameters and returns,
 * so pay attention to the {@link Nullable} annotations.
 * <p>
 * An implementation of {@link BeePollinationHandler} should
 * include maturity checks, crop growth, bone meal particles,
 * and return whether the bee should gain a crop counter.
 *
 * @author TheDarkColour
 * @see BeePollinationHandler read the KDoc for more info
 * @see EntityBee.AIGrowCrops#updateTask
 * <p>
 * You may include this class in your mod JAR.
 */
@ParametersAreNonnullByDefault
public final class BeePollinationHandlerJVM {
    private BeePollinationHandlerJVM() {
    }

    /**
     * Gets the pollination handler for this block, or {@code null}
     * if no handler is registered for the block.
     *
     * @param block the block whose handler is being retrieved
     * @return the handler for this block or {@code null} if one does not exist.
     */
    @Nullable
    public static BeePollinationHandler getHandler(Block block) {
        return BeePollinationHandler.getHandler(block);
    }

    /**
     * Register (or override) custom bee pollination behaviour for this block.
     * You can also remove custom bee pollination behaviour
     * by passing {@code null} as the handler parameter.
     *
     * @param block   the block to register/override bee pollination behaviour for
     * @param handler the custom bee pollination behaviour
     */
    public static void registerHandler(Block block, @Nullable BeePollinationHandler handler) {
        BeePollinationHandler.registerHandler(block, handler);
    }
}
