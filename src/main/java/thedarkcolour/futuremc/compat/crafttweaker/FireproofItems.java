package thedarkcolour.futuremc.compat.crafttweaker;

import net.minecraft.item.Item;
import thedarkcolour.futuremc.asm.ClassBuilder;

/**
 * Fire proof items compat class for mod pack makers.
 * TODO dynamically generate new items
 */
public final class FireproofItems extends Item {

    public static final int PUBLIC = 1;
    public static final int CONSTRUCTOR = 524288;

    /**
     * Generates bytecode for the given Item or
     * prints an error to the log if the class cannot be generated.
     */
    private static Class<?> generateNewItemClass(Item item) {
        ClassBuilder b = new ClassBuilder(item.getClass());

        b.constructor(PUBLIC | CONSTRUCTOR, constructor -> {
            constructor.invokeSuperConstructor();
        });

        return b.build();
    }
}
