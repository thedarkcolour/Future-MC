package thedarkcolour.futuremc.asm;

import kotlin.collections.CollectionsKt;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import thedarkcolour.core.util.Util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class CoreTransformer implements IClassTransformer {
    protected static boolean isObfuscated;

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        try {
            if (transformedName.equals("net.minecraft.world.gen.feature.WorldGenTrees")) {
                return patchWorldGenTrees(basicClass);
            }
            if (transformedName.equals("net.minecraft.world.gen.feature.WorldGenBigTree")) {
                return patchWorldGenBigTree(basicClass);
            }
            if (transformedName.equals("net.minecraft.block.BlockPistonBase")) {
                if (!Util.isQuarkLoaded()) {
                    return patchBlockPistonBase(basicClass);
                } else {
                    // Use Quark API instead
                    return basicClass;
                }
            }
            if (transformedName.equals("net.minecraft.client.renderer.EntityRenderer")) {
                return patchEntityRenderer(basicClass);
            }
        } catch (NoClassDefFoundError e) {
            return basicClass;
        }

        return basicClass;
    }

    private static byte[] patchEntityRenderer(byte[] basicClass) {
        ClassNode classNode = createClassNode(basicClass);

        List<String> names = CollectionsKt.listOf("renderWorldPass", "func_175068_a", "isInsideOfMaterial", "func_70055_a");

        for (MethodNode method : classNode.methods) {
            if (names.contains(method.name)) {
                for (AbstractInsnNode instruction : method.instructions.toArray()) {
                    if (instruction.getOpcode() == IFNE) {
                        if (instruction.getPrevious() instanceof MethodInsnNode) {
                            if (names.contains(((MethodInsnNode) instruction.getPrevious()).name)) {
                                method.instructions.remove(instruction.getPrevious().getPrevious().getPrevious());
                                method.instructions.remove(instruction.getPrevious().getPrevious());
                                method.instructions.remove(instruction.getPrevious());
                                method.instructions.remove(instruction);
                                System.out.println("Patched EntityRenderer");
                                break;
                            }
                        }
                    }
                }
                break;
            }
        }

        ClassWriter cw = new ClassWriter(0);
        classNode.accept(cw);
        return cw.toByteArray();
    }

    private static byte[] patchBlockPistonBase(byte[] basicClass) {
        ClassNode classNode = createClassNode(basicClass);

        List<String> names = CollectionsKt.listOf("checkForMove", "func_176316_e", "doMove", "func_176319_a");

        for (MethodNode method : classNode.methods) {
            if (names.contains(method.name)) {
                String helperName = "net/minecraft/block/state/BlockPistonStructureHelper";
                String tdcHelperName = "thedarkcolour/futuremc/asm/BlockPistonStructureHelper";
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();

                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();

                    if (node.getOpcode() == NEW && ((TypeInsnNode) node).desc.equals(helperName)) {
                        ((TypeInsnNode) node).desc = tdcHelperName;
                    } else if (node instanceof MethodInsnNode && ((MethodInsnNode) node).owner.equals(helperName)) {
                        ((MethodInsnNode) node).owner = tdcHelperName;
                    } else if (node instanceof FrameNode) {
                        FrameNode frame = (FrameNode) node;
                        List<Object> local = frame.local;
                        if (local == null) continue;

                        for (Object o : local) {
                            if (helperName.equals(o)) {
                                local.set(local.indexOf(o), tdcHelperName);
                            }
                        }
                    }
                }

                for (LocalVariableNode localVariable : method.localVariables) {
                    if (localVariable.desc.equals("Lnet/minecraft/block/state/BlockPistonStructureHelper;")) {
                        localVariable.desc = "Lthedarkcolour/futuremc/asm/BlockPistonStructureHelper;";
                    }
                }
            }
        }

        return compile(classNode);
    }

    private static byte[] patchWorldGenTrees(byte[] basicClass) {
        ClassNode classNode = createClassNode(basicClass);

        MethodNode method = classNode.methods.stream()
                .filter(node -> node.name.equals(isObfuscated ? "func_180709_b" : "generate") && node.desc.equals("(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Z"))
                .findFirst()
                .orElseThrow(NoSuchMethodError::new);
        InsnList toAdd = Util.make(new InsnList(), list -> {
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new VarInsnNode(ALOAD, 2));
            list.add(new VarInsnNode(ALOAD, 3));
            list.add(new VarInsnNode(ILOAD, 4));
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new MethodInsnNode(INVOKESTATIC, "thedarkcolour/futuremc/world/gen/feature/BeeNestGenerator", "generateBeeNestsForSmallTrees", "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;ILnet/minecraft/world/gen/feature/WorldGenAbstractTree;)V", false));
        });
        return patchBeforeReturnTrue(classNode, method, toAdd);
    }

    private static byte[] patchWorldGenBigTree(byte[] basicClass) {
        ClassNode classNode = createClassNode(basicClass);

        MethodNode method = classNode.methods.stream()
                //.peek(node -> System.out.println(node.name + "   " + node.desc))
                .filter(node -> node.name.equals(isObfuscated ? "func_180709_b" : "generate") && node.desc.equals("(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Z"))
                .findFirst()
                .orElseThrow(NoSuchMethodError::new);
        InsnList toAdd = Util.make(new InsnList(), list -> {
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

    private static ClassNode createClassNode(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        return classNode;
    }

    private static byte[] patchBeforeReturnTrue(ClassNode classNode, MethodNode method, InsnList toAdd) {
        method.instructions.insertBefore(Arrays.stream(method.instructions.toArray())
                .filter(node -> node.getOpcode() == IRETURN && node.getPrevious().getOpcode() == ICONST_1)
                .findFirst()
                .orElseThrow(RuntimeException::new), toAdd);
        return compile(classNode);
    }

    private static byte[] compile(ClassNode classNode) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);
        return cw.toByteArray();
    }
}