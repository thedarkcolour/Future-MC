package thedarkcolour.futuremc.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

public class CoreTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (name.equals("net.minecraft.world.gen.feature.WorldGenTrees")) {
            return patchWorldGenTrees(basicClass);
        }
        if (name.equals("net.minecraft.world.gen.feature.WorldGenBigTree")) {
            return patchWorldGenBigTree(basicClass);
        }

        return basicClass;
    }

    private byte[] patchWorldGenTrees(byte[] basicClass) {
        System.out.println("Found WorldGenTrees");
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        for (MethodNode method : classNode.methods) {
            if (method.name.equals(CoreLoader.isObfuscated ? "func_180709_b" : "generate") && method.desc.equals("(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Z")) {
                System.out.println("Found generate()");
                for (AbstractInsnNode node : method.instructions.toArray()) {
                    if (node instanceof LineNumberNode) {
                        LineNumberNode line = (LineNumberNode) node;
                        if (line.line == 225) {
                            InsnList toAdd = new InsnList();
                            toAdd.add(new VarInsnNode(ALOAD, 1));
                            toAdd.add(new VarInsnNode(ALOAD, 2));
                            toAdd.add(new VarInsnNode(ALOAD, 3));
                            toAdd.add(new VarInsnNode(ILOAD, 4));
                            toAdd.add(new VarInsnNode(ALOAD, 0));
                            toAdd.add(new MethodInsnNode(INVOKESTATIC, "thedarkcolour/futuremc/world/gen/feature/BeeNestGenerator", "generateBeeNestsForSmallTrees", "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;ILnet/minecraft/world/gen/feature/WorldGenAbstractTree;)V", false));
                            method.instructions.insertBefore(node, toAdd);
                            System.out.println("Patched generate()");
                            break;
                        }
                    }
                }
                break;
            }
        }
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);
        return cw.toByteArray();
    }

    private byte[] patchWorldGenBigTree(byte[] basicClass) {
        System.out.println("Found WorldGenBigTree");
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        for (MethodNode method : classNode.methods) {
            if (method.name.equals(CoreLoader.isObfuscated ? "func_180709_b" : "generate") && method.desc.equals("(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Z")) {
                System.out.println("Found generate()");
                for (AbstractInsnNode node : method.instructions.toArray()) {
                    if (node instanceof LineNumberNode) {
                        LineNumberNode line = (LineNumberNode) node;
                        if (line.line == 340) {
                            InsnList toAdd = new InsnList();
                            toAdd.add(new VarInsnNode(ALOAD, 1));
                            toAdd.add(new VarInsnNode(ALOAD, 2));
                            toAdd.add(new VarInsnNode(ALOAD, 3));
                            toAdd.add(new VarInsnNode(ALOAD, 0));
                            toAdd.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/gen/feature/WorldGenBigTree", "height", "I"));
                            toAdd.add(new VarInsnNode(ALOAD, 0));
                            toAdd.add(new MethodInsnNode(INVOKESTATIC, "thedarkcolour/futuremc/world/gen/feature/BeeNestGenerator", "generateBeeNestsForBigTrees", "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;ILnet/minecraft/world/gen/feature/WorldGenAbstractTree;)V", false));
                            method.instructions.insertBefore(node, toAdd);
                            System.out.println("Patched generate()");
                            break;
                        }
                    }
                }
                break;
            }
        }
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);
        return cw.toByteArray();
    }
}