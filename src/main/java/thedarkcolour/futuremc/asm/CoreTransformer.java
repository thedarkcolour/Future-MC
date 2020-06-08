package thedarkcolour.futuremc.asm;

import kotlin.collections.CollectionsKt;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import thedarkcolour.core.util.UtilKt;
import vazkii.quark.api.ClassTransformer;

import javax.annotation.Nullable;
import java.util.function.Predicate;

import static org.objectweb.asm.Opcodes.*;

/**
 * Core mods for Future MC.
 *
 * @author TheDarkColour
 */
public final class CoreTransformer implements IClassTransformer {
    static boolean isObfuscated;

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        try {
            switch (transformedName) {
                case "net.minecraft.client.renderer.EntityRenderer":
                    return patchEntityRenderer(basicClass);

                case "net.minecraft.world.gen.feature.WorldGenTrees":
                    return patchWorldGenTrees(basicClass);

                case "net.minecraft.world.gen.feature.WorldGenBigTree":
                    return patchWorldGenBigTree(basicClass);

                //case "net.minecraft.client.model.ModelBiped":
                //    return patchModelBiped(basicClass);

                //case "net.minecraft.client.renderer.RenderItem":
                //    return patchRenderItem(basicClass);

                case "net.minecraft.block.BlockPistonBase":
                    try {
                        Class.forName("vazkii.quark.base.asm.LoadingPlugin");
                    } catch (ClassNotFoundException e) {
                        // If Quark isn't here just use the bundled coremod
                        return ClassTransformer.transformBlockPistonBase(basicClass);
                    }
                    // use Quark's coremod
                    return basicClass;
            }
        } catch (NoClassDefFoundError e) {
            return basicClass;
        }

        return basicClass;
    }

    private static byte[] patchEntityRenderer(byte[] basicClass) {
        ClassNode classNode = createClassNode(basicClass);
        MethodNode method = findMethod(classNode, "func_175068_a", "renderWorldPass", null);
        MethodInsnNode target = findMethodInsn(method, "func_70055_a", "isInsideOfMaterial", null);

        method.instructions.remove(target.getPrevious().getPrevious());
        method.instructions.remove(target.getPrevious());
        method.instructions.remove(method.instructions.get(method.instructions.indexOf(target) + 1));
        method.instructions.remove(target);

        return compile(classNode);
    }

    private static byte[] patchWorldGenTrees(byte[] basicClass) {
        ClassNode classNode = createClassNode(basicClass);

        MethodNode method = findMethod(
                classNode,
                "func_180709_b",
                "generate",
                "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Z"
        );
        InsnList toAdd = UtilKt.make(new InsnList(), list -> {
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new VarInsnNode(ALOAD, 2));
            list.add(new VarInsnNode(ALOAD, 3));
            list.add(new VarInsnNode(ILOAD, 4));
            list.add(new MethodInsnNode(INVOKESTATIC, "thedarkcolour/futuremc/world/gen/feature/BeeNestGenerator", "generateBeeNestsForSmallTrees", "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;I)V", false));
        });
        return patchBeforeReturnTrue(classNode, method, toAdd);
    }

    private static byte[] patchWorldGenBigTree(byte[] basicClass) {
        ClassNode classNode = createClassNode(basicClass);
        MethodNode method = findMethod(
                classNode,
                "func_180709_b",
                "generate",
                "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Z"
        );

        InsnList toAdd = UtilKt.make(new InsnList(), list -> {
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new VarInsnNode(ALOAD, 2));
            list.add(new VarInsnNode(ALOAD, 3));
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/gen/feature/WorldGenBigTree", isObfuscated ? "field_76501_f" : "height", "I"));
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new MethodInsnNode(INVOKESTATIC, "thedarkcolour/futuremc/world/gen/feature/BeeNestGenerator", "generateBeeNestsForBigTrees", "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;ILnet/minecraft/world/gen/feature/WorldGenAbstractTree;)V", false));
        });
        return patchBeforeReturnTrue(classNode, method, toAdd);
    }

    private static byte[] patchModelBiped(byte[] basicClass) {
        ClassNode classNode = createClassNode(basicClass);
        MethodNode method = findMethod(
                classNode,
                "func_78087_a",
                "setRotationAngles",
                "(FFFFFFLnet/minecraft/entity/Entity;)V"
        );

        InsnList toAdd = UtilKt.make(new InsnList(), list -> {
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new MethodInsnNode(INVOKESTATIC, "thedarkcolour/futuremc/event/Events", "setPlayerRotations", "(Lnet/minecraft/client/model/ModelBiped;)V", false));
        });
        return patchBeforeMcMethod(classNode, method, toAdd, "func_178685_a", "copyModelAngles", 1);
    }

    private static byte[] patchRenderItem(byte[] basicClass) {
        ClassNode classNode = createClassNode(basicClass);
        MethodNode metohd = findMethod(
                classNode,
                "func_181564_a",
                "renderItem",
                "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
        );

        /*InsnList toAdd = UtilKt.make(new InsnList(), list -> {
            list.add(new VarInsnNode(ALOAD, 0));
            list.add;
        });*/
        return basicClass;
    }

    private static ClassNode createClassNode(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        return classNode;
    }

    /**
     * Finds the corresponding method node with the
     * specified srgName/mcpName and desc if it is not empty.
     *
     * @param classNode the class to search
     * @param srgName the srg name of the method
     * @param mcpName the human readable name of the method
     * @param desc the method signature (empty if method signature should be ignored)
     * @return the method that matches the name and description if it is provided
     *
     * @throws NoSuchMethodError if no matching method was found
     */
    private static MethodNode findMethod(ClassNode classNode, String srgName, String mcpName, @Nullable String desc) {
        String actualName = isObfuscated ? srgName : mcpName;

        MethodNode method = CollectionsKt.firstOrNull(
            CollectionsKt.filter(classNode.methods, (node) -> {
                return node.name.equals(actualName) && (desc == null || node.desc.equals(desc));
            })
        );

        if (method != null) {
            return method;
        } else {
            System.err.println("******************************");
            System.err.println("REPORT THIS TO FUTUREMC GITHUB");
            System.err.println("******************************");

            throw new NoSuchMethodError(
                    "Class bytecode did not contain expected method " +
                            srgName + " or " + mcpName + " (expected: " + actualName + ")"
            );
        }
    }

    /**
     * Patches before a line that returns true.
     *
     * @param classNode the class to patch
     * @param method the method to patch
     * @param toAdd the patch to apply
     * @return the patched bytecode of the class
     */
    private static byte[] patchBeforeReturnTrue(ClassNode classNode, MethodNode method, InsnList toAdd) {
        return patchBeforeInsn(classNode, method, toAdd, 1, (node) -> {
            return node.getOpcode() == IRETURN && node.getPrevious().getOpcode() == ICONST_1;
        });
    }

    /**
     * Patches
     * @param classNode
     * @param method
     * @param toAdd
     * @param srgName
     * @param mcpName
     * @param occurrence
     * @return
     */
    private static byte[] patchBeforeMcMethod(
            ClassNode classNode,
            MethodNode method,
            InsnList toAdd,
            String srgName,
            String mcpName,
            int occurrence
    ) {
        return patchBeforeInsn(classNode, method, toAdd, occurrence, (node) -> {
            String actualName = isObfuscated ? srgName : mcpName;

            if (node instanceof MethodInsnNode) {
                MethodInsnNode methodNode = ((MethodInsnNode) node);

                return methodNode.name.equals(actualName);
            } else {
                return false;
            }
        });
    }

    /**
     * Applies the patch BEFORE the (occurrence)th node that matches the condition.
     *
     * @param classNode the class to patch
     * @param method the method to patch
     * @param toAdd the patch to apply
     * @param occurrence if there are multiple nodes, which of them to patch before
     * @param condition the condition to check when finding a patch location
     * @return the patched bytecode
     */
    private static byte[] patchBeforeInsn(ClassNode classNode, MethodNode method, InsnList toAdd, int occurrence, Predicate<AbstractInsnNode> condition) {
        int[] occurrences = { 0 };

        AbstractInsnNode insn = findInsn(method, condition.and(node -> {
            occurrences[0] = ++occurrences[0];
            return occurrences[0] == occurrence;
        }));

        method.instructions.insertBefore(insn, toAdd);

        return compile(classNode);
    }

    /**
     * Returns a method invocation instruction matching
     * the given name and description if it is not null.
     *
     * @param methodNode the method to search
     * @param srgName the srg name of the desired method invocation
     * @param mcpName the human readable name of desired method invocation
     * @param desc the method description (can be null)
     * @return The first MethodInsnNode to match the given name and description
     */
    private static MethodInsnNode findMethodInsn(
            MethodNode methodNode,
            String srgName,
            String mcpName,
            @Nullable String desc
    ) {
        String actualName = isObfuscated ? srgName : mcpName;

        AbstractInsnNode insn = findInsn(methodNode, node -> {
            if (node instanceof MethodInsnNode) {
                MethodInsnNode method = (MethodInsnNode) node;
                return method.name.equals(actualName) && (desc == null || desc.equals(method.name));
            } else return false;
        });

        return (MethodInsnNode) insn;
    }

    private static AbstractInsnNode findInsn(MethodNode methodNode, Predicate<AbstractInsnNode> condition) {
        for (AbstractInsnNode node : methodNode.instructions.toArray()) {
            if (condition.test(node)) {
                return node;
            }
        }

        throw new RuntimeException("Could not find matching instruction in bytecode");
    }

    private static byte[] compile(ClassNode classNode) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);
        return cw.toByteArray();
    }
}