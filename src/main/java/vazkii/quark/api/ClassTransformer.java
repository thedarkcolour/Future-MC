/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [26/03/2016, 21:31:04 (GMT)]
 */
package vazkii.quark.api;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.objectweb.asm.Opcodes.*;

// Note from TheDarkColour
// I use this file because I don't want to "reinvent the wheel"
// Obviously I did change things so that it only has what I need.
// Thanks to the Vazkii dev team for creating this!
public final class ClassTransformer {
    public static final String ASM_HOOKS = "vazkii/quark/api/ASMHooks";

    public static byte[] transformBlockPistonBase(byte[] basicClass) {
        MethodSignature sig1 = new MethodSignature("canPush", "func_185646_a", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;ZLnet/minecraft/util/EnumFacing;)Z");
        MethodSignature sig2 = new MethodSignature("doMove", "func_176319_a", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Z)Z");
        MethodSignature sig3 = new MethodSignature("checkForMove", "func_176316_e", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)V");

        String targetClazz = "net/minecraft/block/state/BlockPistonStructureHelper";

        MethodSignature target = new MethodSignature("hasTileEntity", "", "(Lnet/minecraft/block/state/IBlockState;)Z");
        MethodSignature target2 = new MethodSignature("getBlocksToMove", "func_177254_c", "()Ljava/util/List;");
        MethodSignature target3 = new MethodSignature("<init>", "", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Z)V");

        return transform(basicClass,
                /*forMethod(sig1, combine(
                        (AbstractInsnNode node) -> { // Filter
                            return node.getOpcode() == INVOKEVIRTUAL && target.matches((MethodInsnNode) node);
                        },
                        (MethodNode method, AbstractInsnNode node) -> { // Action
                            InsnList newInstructions = new InsnList();

                            newInstructions.add(new VarInsnNode(ALOAD, 0));
                            newInstructions.add(new MethodInsnNode(INVOKESTATIC, ASM_HOOKS, "shouldPistonMoveTE", "(ZLnet/minecraft/block/state/IBlockState;)Z", false));

                            method.instructions.insert(node, newInstructions);
                            return true;
                        })),
                forMethod(sig2, combine(
                        (AbstractInsnNode node) -> { // Filter
                            return node.getOpcode() == INVOKEVIRTUAL && ((MethodInsnNode) node).owner.equals(targetClazz) && target2.matches((MethodInsnNode) node);
                        },
                        (MethodNode method, AbstractInsnNode node) -> { // Action
                            InsnList newInstructions = new InsnList();

                            newInstructions.add(new VarInsnNode(ALOAD, 5));
                            newInstructions.add(new VarInsnNode(ALOAD, 1));
                            newInstructions.add(new VarInsnNode(ALOAD, 3));
                            newInstructions.add(new VarInsnNode(ILOAD, 4));
                            newInstructions.add(new MethodInsnNode(INVOKESTATIC, ASM_HOOKS, "postPistonPush", "(Lnet/minecraft/block/state/BlockPistonStructureHelper;Lnet/minecraft/world/World;Lnet/minecraft/util/EnumFacing;Z)V", false));

                            method.instructions.insertBefore(node, newInstructions);
                            return true;
                        })),*/
                forMethod(sig2, combine(
                        (AbstractInsnNode node) -> { // Filter
                            return node.getOpcode() == INVOKESPECIAL && ((MethodInsnNode) node).owner.equals(targetClazz) && target3.matches((MethodInsnNode) node);
                        },
                        (MethodNode method, AbstractInsnNode node) -> { // Action
                            InsnList newInstructions = new InsnList();

                            newInstructions.add(new VarInsnNode(ALOAD, 1));
                            newInstructions.add(new VarInsnNode(ALOAD, 2));
                            newInstructions.add(new VarInsnNode(ALOAD, 3));
                            newInstructions.add(new VarInsnNode(ILOAD, 4));
                            newInstructions.add(new MethodInsnNode(INVOKESTATIC, ASM_HOOKS, "transformStructureHelper", "(Lnet/minecraft/block/state/BlockPistonStructureHelper;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Z)Lnet/minecraft/block/state/BlockPistonStructureHelper;", false));

                            method.instructions.insert(node, newInstructions);
                            return true;
                        })),
                forMethod(sig3, combine(
                        (AbstractInsnNode node) -> { // Filter
                            return node.getOpcode() == INVOKESPECIAL && ((MethodInsnNode) node).owner.equals(targetClazz) && target3.matches((MethodInsnNode) node);
                        },
                        (MethodNode method, AbstractInsnNode node) -> { // Action
                            InsnList newInstructions = new InsnList();

                            newInstructions.add(new VarInsnNode(ALOAD, 1));
                            newInstructions.add(new VarInsnNode(ALOAD, 2));
                            newInstructions.add(new VarInsnNode(ALOAD, 4));
                            newInstructions.add(new InsnNode(ICONST_1));
                            newInstructions.add(new MethodInsnNode(INVOKESTATIC, ASM_HOOKS, "transformStructureHelper", "(Lnet/minecraft/block/state/BlockPistonStructureHelper;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Z)Lnet/minecraft/block/state/BlockPistonStructureHelper;", false));

                            method.instructions.insert(node, newInstructions);
                            return true;
                        })));
    }

    private static TransformerAction forMethod(MethodSignature sig, MethodAction... actions) {
        return new MethodTransformerAction(sig, actions);
    }

    private static final class MethodTransformerAction implements TransformerAction {
        private final MethodSignature sig;
        private final MethodAction[] actions;

        private MethodTransformerAction(MethodSignature sig, MethodAction[] actions) {
            this.sig = sig;
            this.actions = actions;
        }

        @Override
        public boolean test(ClassNode classNode) {
            boolean didAnything = false;
            for (MethodAction action : actions)
                didAnything |= findMethodAndTransform(classNode, sig, action);
            return didAnything;
        }
    }

    public static boolean findMethodAndTransform(ClassNode node, MethodSignature sig, MethodAction predicate) {
        for (MethodNode method : node.methods) {
            if (sig.matches(method)) {
                return predicate.test(method);
            }
        }

        return false;
    }

    public static MethodAction combine(NodeFilter filter, NodeAction action) {
        return (MethodNode node) -> applyOnNode(node, filter, action);
    }
    
    public static boolean applyOnNode(MethodNode method, NodeFilter filter, NodeAction action) {
        Iterator<AbstractInsnNode> iterator = method.instructions.iterator();

        boolean didAny = false;
        while (iterator.hasNext()) {
            AbstractInsnNode anode = iterator.next();
            if (filter.test(anode)) {
                didAny = true;
                if (action.test(method, anode))
                    break;
            }
        }

        return didAny;
    }
    
    private interface Transformer extends Function<byte[], byte[]> {
        // NO-OP
    }

    private interface MethodAction extends Predicate<MethodNode> {
        // NO-OP
    }

    private interface NodeFilter extends Predicate<AbstractInsnNode> {
        // NO-OP
    }

    private interface NodeAction extends BiPredicate<MethodNode, AbstractInsnNode> {
        // NO-OP
    }

    private interface TransformerAction extends Predicate<ClassNode> {
        // NO-OP
    }

    private interface NewMethodAction extends BiPredicate<ClassNode, MethodVisitor> {
        // NO-OP
    }

    public static class MethodSignature {
        private final String funcName, srgName, funcDesc;

        public MethodSignature(String funcName, String srgName, String funcDesc) {
            this.funcName = funcName;
            this.srgName = srgName;
            this.funcDesc = funcDesc;
        }

        @Override
        public String toString() {
            return "Names [" + funcName + ", " + srgName + "] Descriptor " + funcDesc;
        }

        public boolean matches(String methodName, String methodDesc) {
            return (methodName.equals(funcName) || methodName.equals(srgName))
                    && (methodDesc.equals(funcDesc));
        }

        public boolean matches(MethodNode method) {
            return matches(method.name, method.desc);
        }

        public boolean matches(MethodInsnNode method) {
            return matches(method.name, method.desc);
        }

        public String mappedName(String owner) {
            return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, srgName, funcDesc);
        }
    }

    private static byte[] transform(byte[] basicClass, TransformerAction... methods) {
        ClassReader reader;
        try {
            reader = new ClassReader(basicClass);
        } catch (NullPointerException ex) {
            return basicClass;
        }

        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        boolean didAnything = false;

        for (TransformerAction pair : methods)
            didAnything |= pair.test(node);

        if (didAnything) {
            ClassWriter writer = new SafeClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            node.accept(writer);
            return writer.toByteArray();
        }

        return basicClass;
    }

    public static class SafeClassWriter extends ClassWriter {
        public SafeClassWriter(int flags) {
            super(flags);
        }

        @Override
        protected String getCommonSuperClass(String type1, String type2) {
            Class<?> c, d;
            ClassLoader classLoader = Launch.classLoader;
            try {
                c = Class.forName(type1.replace('/', '.'), false, classLoader);
                d = Class.forName(type2.replace('/', '.'), false, classLoader);
            } catch (Exception e) {
                throw new RuntimeException(e.toString());
            }
            if (c.isAssignableFrom(d)) {
                return type1;
            }
            if (d.isAssignableFrom(c)) {
                return type2;
            }
            if (c.isInterface() || d.isInterface()) {
                return "java/lang/Object";
            } else {
                do {
                    c = c.getSuperclass();
                } while (!c.isAssignableFrom(d));
                return c.getName().replace('.', '/');
            }
        }
    }
}