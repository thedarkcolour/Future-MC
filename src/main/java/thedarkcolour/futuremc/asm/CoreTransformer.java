package thedarkcolour.futuremc.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassWriter;
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
                    return ASMUtil.patch(basicClass, CoreTransformer::patchEntityRenderer);

                case "net.minecraft.world.gen.feature.WorldGenTrees":
                    return ASMUtil.patch(basicClass, CoreTransformer::patchWorldGenTrees);
                case "net.minecraft.world.gen.feature.WorldGenBigTree":
                    return ASMUtil.patch(basicClass, CoreTransformer::patchWorldGenBigTree);

                case "net.minecraft.entity.EntityLivingBase":
                    return ASMUtil.patch(basicClass, CoreTransformer::patchEntityLivingBase, ClassWriter.COMPUTE_MAXS); // my frames are correct thanks

                //case "net.minecraft.entity.player.EntityPlayer":
                //    return ASMUtil.patch(basicClass, CoreTransformer::patchEntityPlayer, ClassWriter.COMPUTE_MAXS);

                case "net.minecraft.item.Item":
                    return ASMUtil.patch(basicClass, CoreTransformer::patchItem);

                case "net.minecraft.entity.monster.EntitySnowman":
                    return ASMUtil.patch(basicClass, CoreTransformer::patchEntitySnowman);

                //case "net.minecraft.entity.player.EntityPlayerMP":
                //    return patchEntityPlayerMP(basicClass);
                //case "net.minecraft.client.entity.EntityPlayerSP":
                //    return patchEntityPlayerSP(basicClass);
                //case "net.minecraft.client.network.NetHandlerPlayClient":
                //    return patchNetHandlerPlayClient(basicClass);

                case "net.minecraft.client.model.ModelBiped":
                    return ASMUtil.patch(basicClass, CoreTransformer::patchModelBiped);

                //case "net.minecraft.client.renderer.RenderItem":
                //    return ASMUtil.patchRenderItem(basicClass);

                case "com.fuzs.gamblingstyle.handler.OpenContainerHandler":
                    return ASMUtil.patch(basicClass, CoreTransformer::transformOpenContainerHandler);

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
                    return ASMUtil.patch(basicClass, CoreTransformer::transformBO3Loader);

                case "biomesoplenty.common.world.generator.tree.GeneratorTreeBase":
                    return ASMUtil.patch(basicClass, CoreTransformer::transformBOPTree);
            }
        } catch (NoClassDefFoundError e) {
            return basicClass;
        }

        return basicClass;
    }

    private static void patchEntityPlayer(ClassNode classNode) {
        MethodNode canEat = ASMUtil.findMethod(classNode, "func_71043_e", "canEat", null);

        InsnList instructions = canEat.instructions;
        int labels = 0;
        LabelNode l1 = null;
        VarInsnNode iload0 = null;

        // get labels from existing bytecode
        for (int i = 0; i < instructions.size(); i++) {
            AbstractInsnNode node = instructions.get(i);

            if (l1 == null && node.getClass() == LabelNode.class) {
                l1 = (LabelNode)node;
            } else if (iload0 == null && node.getClass() == VarInsnNode.class) {
                VarInsnNode n = (VarInsnNode) node;
                if (n.var == 1) {
                    iload0 = n;
                }
            }
        }

        InsnList insertion = ASMUtil.createInsnList(
                new VarInsnNode(ALOAD, 0),
                new MethodInsnNode(INVOKESTATIC, "thedarkcolour/futuremc/asm/ASMHooks", "creativeEat", "(Lnet/minecraft/entity/player/EntityPlayer;Z)Z"),
                new JumpInsnNode(IFEQ, l1),
                new LabelNode(new Label()),
                new InsnNode(ICONST_1),
                new InsnNode(IRETURN)
        );
        instructions.insertBefore(instructions.getFirst(), insertion);
        instructions.insertBefore(iload0, new FrameNode(F_SAME, 0, null, 0, null));
    }

    private static void patchItem(ClassNode classNode) {
        MethodNode method = ASMUtil.findMethod(classNode, "func_77613_e", "getRarity", null);
        method.instructions = ASMUtil.createInsnList(
                new VarInsnNode(ALOAD, 0),
                new VarInsnNode(ALOAD, 1),
                new MethodInsnNode(INVOKESTATIC, "thedarkcolour/futuremc/asm/ASMHooks", "getEnchantmentRarity", "(Lnet/minecraft/item/Item;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/EnumRarity;"),
                new InsnNode(ARETURN)
        );
    }

    private static void patchEntitySnowman(ClassNode classNode) {
        MethodNode method = ASMUtil.findMethod(classNode, "onSheared", "onSheared", null);
        method.instructions.insert(ASMUtil.createInsnList(
                new VarInsnNode(ALOAD, 0),
                new MethodInsnNode(INVOKESTATIC, "thedarkcolour/futuremc/asm/ASMHooks", "onSnowmanSheared", "(Lnet/minecraft/entity/monster/EntitySnowman;)V")
        ));
    }

    private static void patchEntityLivingBase(ClassNode classNode) {
        MethodNode travelNode = ASMUtil.findMethod(classNode, "func_191986_a", "travel", null);

        String collidedHorizontallyFieldName = ASMUtil.isObfuscated ? "field_70123_F" : "collidedHorizontally";
        String isJumpingFieldName = ASMUtil.isObfuscated ? "field_70703_bu" : "isJumping";

        for (AbstractInsnNode node : travelNode.instructions.toArray()) {
            int flagLoc = ASMUtil.isObfuscated ? 21 : 9; // I have no idea why this is different

            if (node.getOpcode() == ISTORE && ((VarInsnNode) node).var == flagLoc) {
                travelNode.instructions.insert(node, ASMUtil.createInsnList(
                        new VarInsnNode(ILOAD, flagLoc),
                        new VarInsnNode(ALOAD, 0),
                        new MethodInsnNode(INVOKESTATIC, "thedarkcolour/futuremc/asm/ASMHooks", "scaffoldFallThrough", "(ZLnet/minecraft/entity/EntityLivingBase;)Z"),
                        new VarInsnNode(ISTORE, flagLoc)
                ));
                break;
            }
        }

        int occurrence = 0;

        // easier to iterate array than to use the built-in iterator
        for (AbstractInsnNode node : travelNode.instructions.toArray()) {
            if (node.getOpcode() == GETFIELD && ((FieldInsnNode) node).name.equals(collidedHorizontallyFieldName)) {
                // want the second occurrence
                if (occurrence++ == 1) {
                    LabelNode l74 = new LabelNode(new Label());

                    travelNode.instructions.insert(node.getNext(), ASMUtil.createInsnList(
                            l74,
                            new FrameNode(F_SAME, 0, null, 0, null)
                    ));
                    travelNode.instructions.insert(node, ASMUtil.createInsnList(
                            new JumpInsnNode(IFNE, l74),
                            new VarInsnNode(ALOAD, 0),
                            new FieldInsnNode(GETFIELD, "net/minecraft/entity/EntityLivingBase", isJumpingFieldName, "Z")
                    ));

                    break;
                }
            }
        }
    }

    private static void transformBOPTree(ClassNode classNode) {
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
    }

    private static void transformBO3Loader(ClassNode classNode) {
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
    }

    private static void transformOpenContainerHandler(ClassNode classNode) {
        MethodNode mv = ASMUtil.findMethod(classNode, "onContainerOpen", "onContainerOpen", null);

        LabelNode label = null;

        try {
            label = (LabelNode) mv.instructions.getFirst();
        } catch (ClassCastException e) {
            for (int i = 0; i < 10; i++) {
                AbstractInsnNode insn = mv.instructions.get(i);
                System.out.println(insn.getClass() + ": " + insn.getOpcode());
            }
        }

        InsnList list = new InsnList();
        list.add(new LabelNode(new Label()));
        list.add(new FieldInsnNode(GETSTATIC, "thedarkcolour/futuremc/config/FConfig", "INSTANCE", "Lthedarkcolour/futuremc/config/FConfig;"));
        list.add(new MethodInsnNode(INVOKEVIRTUAL, "thedarkcolour/futuremc/config/FConfig", "getVillageAndPillage", "()Lthedarkcolour/futuremc/config/FConfig$VillageAndPillage;", false));
        list.add(new FieldInsnNode(GETFIELD, "thedarkcolour/futuremc/config/FConfig$VillageAndPillage", "newVillagerGui", "Z"));
        list.add(new JumpInsnNode(IFEQ, label));
        list.add(new InsnNode(RETURN));

        mv.instructions.insertBefore(label, list);
    }

    private static void patchEntityRenderer(ClassNode classNode) {
        // fix incompatibility with vivecraft?
        if (Compat.checkVivecraft()) {
            return;
        }

        MethodNode method = ASMUtil.findMethod(classNode, "func_175068_a", "renderWorldPass", null);
        MethodInsnNode target = ASMUtil.findMethodInsn(method, "func_70055_a", "isInsideOfMaterial", null);

        method.instructions.remove(target.getPrevious().getPrevious());
        method.instructions.remove(target.getPrevious());
        method.instructions.remove(method.instructions.get(method.instructions.indexOf(target) + 1));
        method.instructions.remove(target);
    }

    private static void patchWorldGenTrees(ClassNode classNode) {
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
    }

    private static void patchWorldGenBigTree(ClassNode classNode) {
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
    }

    private static void patchModelBiped(ClassNode classNode) {
        MethodNode method = ASMUtil.findMethod(
                classNode,
                "func_78087_a",
                "setRotationAngles",
                "(FFFFFFLnet/minecraft/entity/Entity;)V"
        );
        InsnList toAdd = ASMUtil.createInsnList(
            new VarInsnNode(ALOAD, 0),
            new MethodInsnNode(INVOKESTATIC, "thedarkcolour/futuremc/asm/ASMHooks", "rotateByPose", "(Lnet/minecraft/client/model/ModelBiped;)V", false)
        );
        ASMUtil.patchBeforeMcMethod(classNode, method, toAdd, "func_178685_a", "copyModelAngles", 1);
    }
}