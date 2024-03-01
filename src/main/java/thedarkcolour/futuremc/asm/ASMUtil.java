package thedarkcolour.futuremc.asm;

import com.google.common.base.Preconditions;
import kotlin.collections.CollectionsKt;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import javax.annotation.Nullable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IRETURN;

public final class ASMUtil {
    private static final boolean DUMP_PATCHED_CLASSES = false;
    static boolean isObfuscated;

    public static ClassNode createClassNode(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        return classNode;
    }

    public static InsnList createInsnList(AbstractInsnNode... fill) {
        InsnList list = new InsnList();

        for (AbstractInsnNode abstractInsnNode : fill) {
            list.add(abstractInsnNode);
        }

        return list;
    }

    /**
     * Finds the corresponding method node with the
     * specified srgName/mcpName and desc if it is not empty.
     *
     * @param classNode the class to search
     * @param srgName the srg name of the method
     * @param mcpName the human readable name of the method
     * @param desc the method signature (empty if method signature can be ignored)
     * @return the method that matches the name and description if it is provided
     *
     * @throws NoSuchMethodError if no matching method was found
     */
    public static MethodNode findMethod(ClassNode classNode, String srgName, String mcpName, @Nullable String desc) {
        String actualName = isObfuscated ? srgName : mcpName;

        MethodNode method = CollectionsKt.firstOrNull(
                CollectionsKt.filter(classNode.methods, node -> {
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
     * Patches before the first occurrence of [ICONST_1, IRETURN].
     * Modifies the classNode, so using this return value is optional.
     *
     * @param method the method to patch
     * @param toAdd the patch to apply
     */
    public static void patchBeforeReturnTrue(MethodNode method, InsnList toAdd) {
        patchBeforeInsn(method, toAdd, 1, node -> {
            return node.getOpcode() == IRETURN && node.getPrevious().getOpcode() == ICONST_1;
        });
    }

    /**
     * Patches before a minecraft method
     * @param method
     * @param toAdd
     * @param srgName
     * @param mcpName
     * @param occurrence
     */
    public static void patchBeforeMcMethod(
            MethodNode method,
            InsnList toAdd,
            String srgName,
            String mcpName,
            int occurrence
    ) {
        patchBeforeInsn(method, toAdd, occurrence, node -> {
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
     * @param method the method to patch
     * @param toAdd the patch to apply
     * @param occurrence if there are multiple nodes, which of them to patch before (1 is the first occurrence)
     * @param condition the condition to check when finding a patch location
     */
    public static void patchBeforeInsn(MethodNode method, InsnList toAdd, int occurrence, Predicate<AbstractInsnNode> condition) {
        int[] occurrences = { 0 };
        Preconditions.checkArgument(occurrence > 0, "Occurrences start at 1");

        // And condition: only runs when condition is true, increments and checks if occurrence matches
        AbstractInsnNode insn = findInsn(method, condition.and(node -> {
            occurrences[0] = ++occurrences[0];
            return occurrences[0] == occurrence;
        }));

        method.instructions.insertBefore(insn, toAdd);
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
    public static MethodInsnNode findMethodInsn(
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

    public static AbstractInsnNode findInsn(MethodNode methodNode, Predicate<AbstractInsnNode> condition) {
        for (AbstractInsnNode node : methodNode.instructions.toArray()) {
            if (condition.test(node)) {
                return node;
            }
        }

        throw new RuntimeException("Could not find matching instruction in bytecode");
    }
    public static byte[] patch(byte[] basicClass, Consumer<ClassNode> patch) {
        return patch(basicClass, patch, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    }

    public static byte[] patch(byte[] basicClass, Consumer<ClassNode> patch, int flags) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        patch.accept(classNode);

        ClassWriter cw = new ClassWriter(classReader, flags);
        classNode.accept(cw);

        byte[] bytes = cw.toByteArray();

        // change when needed
        if (DUMP_PATCHED_CLASSES) {
            try {
                String replace = classNode.name.substring(classNode.name.lastIndexOf('/') + 1) + ".class";
                FileOutputStream output = new FileOutputStream("asm_dump\\" + replace);
                output.write(bytes);
                output.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }

    /**
     * Compiles the class node into JVM bytecode. Terminal operation
     */
    public static byte[] compile(ClassNode classNode) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);

        byte[] bytes = cw.toByteArray();

        // change when needed
        if (DUMP_PATCHED_CLASSES) {
            try {
                String replace = classNode.name.substring(classNode.name.lastIndexOf('/') + 1) + ".class";
                FileOutputStream output = new FileOutputStream("C:\\Things\\mods\\future-mc\\asm_dump\\" + replace);
                output.write(bytes);
                output.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return bytes;
    }
}
