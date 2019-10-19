package thedarkcolour.futuremc.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import thedarkcolour.core.util.Util;

import java.util.Arrays;

import static org.objectweb.asm.Opcodes.*;

public class
CoreTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("net.minecraft.world.gen.feature.WorldGenTrees")) {
            return patchWorldGenTrees(basicClass);
        }
        if (transformedName.equals("net.minecraft.world.gen.feature.WorldGenBigTree")) {
            return patchWorldGenBigTree(basicClass);
        }

        return basicClass;
    }

    private byte[] patchWorldGenTrees(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        MethodNode method = classNode.methods.stream()
                .filter(node -> node.name.equals(CoreLoader.isObfuscated ? "func_180709_b" : "generate") && node.desc.equals("(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Z"))
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
        method.instructions.insertBefore(Arrays.stream(method.instructions.toArray())
                .filter(node -> node.getOpcode() == IRETURN && node.getPrevious().getOpcode() == ICONST_1)
                .findFirst()
                .orElseThrow(RuntimeException::new), toAdd);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);
        return cw.toByteArray();
    }

    private byte[] patchWorldGenBigTree(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        MethodNode method = classNode.methods.stream()
                .filter(node -> node.name.equals(CoreLoader.isObfuscated ? "func_180709_b" : "generate") && node.desc.equals("(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Z"))
                .findFirst()
                .orElseThrow(NoSuchMethodError::new);
        InsnList toAdd = Util.make(new InsnList(), list -> {
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new VarInsnNode(ALOAD, 2));
            list.add(new VarInsnNode(ALOAD, 3));
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/gen/feature/WorldGenBigTree", CoreLoader.isObfuscated ? "field_76501_f" : "height", "I"));
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new MethodInsnNode(INVOKESTATIC, "thedarkcolour/futuremc/world/gen/feature/BeeNestGenerator", "generateBeeNestsForBigTrees", "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;ILnet/minecraft/world/gen/feature/WorldGenAbstractTree;)V", false));
        });
        method.instructions.insertBefore(Arrays.stream(method.instructions.toArray())
                .filter(node -> node.getOpcode() == IRETURN && node.getPrevious().getOpcode() == ICONST_1)
                .findFirst()
                .orElseThrow(RuntimeException::new), toAdd);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);
        return cw.toByteArray();
    }
}