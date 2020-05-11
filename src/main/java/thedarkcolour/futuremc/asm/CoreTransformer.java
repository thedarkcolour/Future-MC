package thedarkcolour.futuremc.asm;

import kotlin.collections.CollectionsKt;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import thedarkcolour.core.util.UtilKt;
import vazkii.quark.api.ClassTransformer;

import java.util.Arrays;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class CoreTransformer implements IClassTransformer {
    protected static boolean isObfuscated;
    protected static boolean isQuarkLoaded;

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        try {
            if (transformedName.equals("net.minecraft.world.gen.feature.WorldGenTrees")) {
                return patchWorldGenTrees(basicClass);
            }
            if (transformedName.equals("net.minecraft.world.gen.feature.WorldGenBigTree")) {
                return patchWorldGenBigTree(basicClass);
            }
            if (!isQuarkLoaded) {
                if (transformedName.equals("net.minecraft.block.BlockPistonBase")) {
                    // Use Bundled Quark transformer
                    return ClassTransformer.transformBlockPistonBase(basicClass);
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

    private static byte[] patchWorldGenTrees(byte[] basicClass) {
        ClassNode classNode = createClassNode(basicClass);

        MethodNode method = classNode.methods.stream()
                .filter(node -> node.name.equals(isObfuscated ? "func_180709_b" : "generate") && node.desc.equals("(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Z"))
                .findFirst()
                .orElseThrow(NoSuchMethodError::new);
        InsnList toAdd = UtilKt.make(new InsnList(), list -> {
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

    static {
        boolean b = false;

        try {
            Class.forName("vazkii.quark.base.Quark");
            b = true;
        } catch (Throwable ignored) {}

        isQuarkLoaded = b;
    }
}