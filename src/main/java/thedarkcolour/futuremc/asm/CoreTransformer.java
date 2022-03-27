package thedarkcolour.futuremc.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.*;
import thedarkcolour.futuremc.compat.Compat;
import vazkii.quark.api.ClassTransformer;

import static org.objectweb.asm.Opcodes.*;

/**
 * Core mods for Future MC.
 *
 * @author TheDarkColour
 */
public final class CoreTransformer implements IClassTransformer {
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

                //case "net.minecraft.entity.player.EntityPlayerMP":
                //    return patchEntityPlayerMP(basicClass);
                //case "net.minecraft.client.entity.EntityPlayerSP":
                //    return patchEntityPlayerSP(basicClass);
                //case "net.minecraft.client.network.NetHandlerPlayClient":
                //    return patchNetHandlerPlayClient(basicClass);

                //case "net.minecraft.client.model.ModelBiped":
                //    return ASMUtil.patchModelBiped(basicClass);

                //case "net.minecraft.client.renderer.RenderItem":
                //    return ASMUtil.patchRenderItem(basicClass);

                case "net.minecraft.block.BlockPistonBase":
                    try {
                        Class.forName("vazkii.quark.base.asm.LoadingPlugin");
                    } catch (ClassNotFoundException e) {
                        // If Quark isn't here just use the bundled coremod
                        return ClassTransformer.transformBlockPistonBase(basicClass);
                    }
                    // use Quark's coremod
                    return basicClass;

                case "com.pg85.otg.customobjects.bo3.BO3Loader":
                    return transformBO3Loader(basicClass);

                case "biomesoplenty.common.world.generator.tree.GeneratorTreeBase":
                    return transformBOPTree(basicClass);
            }
        } catch (NoClassDefFoundError e) {
            return basicClass;
        }

        return basicClass;
    }

    private static byte[] transformBOPTree(byte[] basicClass) {
        ClassNode classNode = ASMUtil.createClassNode(basicClass);
        String fmcFieldName = "_fmc_has_placed_beehive";
        String className = "biomesoplenty/common/world/generator/tree/GeneratorTreeBase";

        // PATCH 1
        // set to true during generate and then set to false after a tree is done generating
        classNode.visitField(ACC_PRIVATE, fmcFieldName, "Z", null, false); // value is "false" by default

        // PATCH 2
        InsnList toAddGetScatterY = ASMUtil.createInsnList(
                new VarInsnNode(ALOAD, 0), // load instance
                new InsnNode(ICONST_0), // FALSE
                new FieldInsnNode(PUTFIELD, className, fmcFieldName, "Z") // set instance field to FALSE
        );
        // patch at the beginning of the method
        ASMUtil.patchBeforeInsn(classNode, ASMUtil.findMethod(classNode, "getScatterY", "getScatterY", null), toAddGetScatterY, 1, node -> {
            return node.getOpcode() == ALOAD;
        });

        // PATCH 3
        LabelNode l7 = new LabelNode(new Label());
        InsnList toAddSetLog = ASMUtil.createInsnList(
                new VarInsnNode(ALOAD, 0),
                new FieldInsnNode(GETFIELD, className, fmcFieldName, "Z"),
                new JumpInsnNode(IFNE, l7),
                new VarInsnNode(ALOAD, 0), // load instance to set field
                new VarInsnNode(ALOAD, 1), // load world
                new VarInsnNode(ALOAD, 2), // load pos
                // method call + pop vars 1 and 2 off of the stack
                new MethodInsnNode(INVOKESTATIC, "thedarkcolour/futuremc/compat/biomesoplenty/BiomesOPlentyCompat", "placeBeehive", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z"),
                // set field of remaining instance var 0
                new FieldInsnNode(PUTFIELD, className, fmcFieldName, "Z"),
                l7,
                new FrameNode(F_APPEND, 1, new Object[]{"net/minecraft/block/state/IBlockState"}, 0, null)
        );
        ASMUtil.patchBeforeReturnTrue(classNode, ASMUtil.findMethod(classNode, "setLog", "setLog", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing$Axis;)Z"), toAddSetLog);

        return ASMUtil.compile(classNode);
    }

    private static byte[] transformBO3Loader(byte[] basicClass) {
        ClassNode classNode = ASMUtil.createClassNode(basicClass);
        MethodNode mv = ASMUtil.findMethod(classNode, "loadFromFile", "loadFromFile", null);

        mv.instructions = new InsnList();

        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(58, l0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(INVOKESTATIC, "thedarkcolour/futuremc/compat/otg/OTGCompat", "patchBO3", "(Ljava/lang/String;Ljava/io/File;)Lcom/pg85/otg/customobjects/bo3/BO3;", false);
        mv.visitInsn(ARETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLocalVariable("this", "Lcom/pg85/otg/customobjects/bo3/BO3Loader;", null, l0, l1, 0);
        mv.visitLocalVariable("objectName", "Ljava/lang/String;", null, l0, l1, 1);
        mv.visitLocalVariable("file", "Ljava/io/File;", null, l0, l1, 2);
        mv.visitMaxs(2, 3);

        return ASMUtil.compile(classNode);
    }

    private static byte[] patchEntityRenderer(byte[] basicClass) {
        // fix incompatibility with vivecraft?
        if (Compat.checkVivecraft()) return basicClass;

        ClassNode classNode = ASMUtil.createClassNode(basicClass);
        MethodNode method = ASMUtil.findMethod(classNode, "func_175068_a", "renderWorldPass", null);
        MethodInsnNode target = ASMUtil.findMethodInsn(method, "func_70055_a", "isInsideOfMaterial", null);

        method.instructions.remove(target.getPrevious().getPrevious());
        method.instructions.remove(target.getPrevious());
        method.instructions.remove(method.instructions.get(method.instructions.indexOf(target) + 1));
        method.instructions.remove(target);

        return ASMUtil.compile(classNode);
    }

    private static byte[] patchWorldGenTrees(byte[] basicClass) {
        ClassNode classNode = ASMUtil.createClassNode(basicClass);

        MethodNode method = ASMUtil.findMethod(
                classNode,
                "func_180709_b",
                "generate",
                "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Z"
        );
        InsnList toAdd = ASMUtil.createInsnList(
            new VarInsnNode(ALOAD, 1),
            new VarInsnNode(ALOAD, 2),
            new VarInsnNode(ALOAD, 3),
            new VarInsnNode(ILOAD, 4),
            new MethodInsnNode(INVOKESTATIC, "thedarkcolour/futuremc/world/gen/feature/BeeNestGenerator", "generateBeeNestsForSmallTrees", "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;I)V", false)
        );
        ASMUtil.patchBeforeReturnTrue(classNode, method, toAdd);

        return ASMUtil.compile(classNode);
    }

    private static byte[] patchWorldGenBigTree(byte[] basicClass) {
        ClassNode classNode = ASMUtil.createClassNode(basicClass);
        MethodNode method = ASMUtil.findMethod(
                classNode,
                "func_180709_b",
                "generate",
                "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Z"
        );

        InsnList toAdd = ASMUtil.createInsnList(
            new VarInsnNode(ALOAD, 1),
            new VarInsnNode(ALOAD, 2),
            new VarInsnNode(ALOAD, 3),
            new VarInsnNode(ALOAD, 0),
            new FieldInsnNode(GETFIELD, "net/minecraft/world/gen/feature/WorldGenBigTree", ASMUtil.isObfuscated ? "field_76501_f" : "height", "I"),
            new VarInsnNode(ALOAD, 0),
            new MethodInsnNode(INVOKESTATIC, "thedarkcolour/futuremc/world/gen/feature/BeeNestGenerator", "generateBeeNestsForBigTrees", "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;ILnet/minecraft/world/gen/feature/WorldGenAbstractTree;)V", false)
        );
        ASMUtil.patchBeforeReturnTrue(classNode, method, toAdd);

        return ASMUtil.compile(classNode);
    }

    private static byte[] patchModelBiped(byte[] basicClass) {
        ClassNode classNode = ASMUtil.createClassNode(basicClass);
        MethodNode method = ASMUtil.findMethod(
                classNode,
                "func_78087_a",
                "setRotationAngles",
                "(FFFFFFLnet/minecraft/entity/Entity;)V"
        );

        InsnList toAdd = ASMUtil.createInsnList(
            new VarInsnNode(ALOAD, 0),
            new MethodInsnNode(INVOKESTATIC, "thedarkcolour/futuremc/event/Events", "setPlayerRotations", "(Lnet/minecraft/client/model/ModelBiped;)V", false)
        );
        ASMUtil.patchBeforeMcMethod(classNode, method, toAdd, "func_178685_a", "copyModelAngles", 1);

        return ASMUtil.compile(classNode);
    }

    private static byte[] patchRenderItem(byte[] basicClass) {
        ClassNode classNode = ASMUtil.createClassNode(basicClass);
        MethodNode method = ASMUtil.findMethod(
                classNode,
                "func_181564_a",
                "renderItem",
                "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
        );

        /*InsnList toAdd = ASMUtil.createInsnList(list -> {
            list.add(new VarInsnNode(ALOAD, 0));
            list.add;
        });*/
        return basicClass;
    }
}