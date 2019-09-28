package thedarkcolour.futuremc.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public interface IWorldPosCallable {
    static IWorldPosCallable of(final World worldIn, final BlockPos pos) {
        return new IWorldPosCallable() {
            public <T> Optional<T> apply(BiFunction<World, BlockPos, T> function) {
                return Optional.of(function.apply(worldIn, pos));
            }
        };
    }

    <T> Optional<T> apply(BiFunction<World, BlockPos, T> function);

    default <T> T applyOrElse(BiFunction<World, BlockPos, T> function, T orElse) {
        return apply(function).orElse(orElse);
    }

    default void consume(BiConsumer<World, BlockPos> function) {
        apply((p_221487_1_, p_221487_2_) -> {
            function.accept(p_221487_1_, p_221487_2_);
            return Optional.empty();
        });
    }
}