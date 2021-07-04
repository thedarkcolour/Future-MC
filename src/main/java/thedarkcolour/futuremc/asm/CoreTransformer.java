package thedarkcolour.futuremc.asm;

import net.minecraft.launchwrapper.IClassTransformer;
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
            }
        } catch (NoClassDefFoundError e) {
            return basicClass;
        }

        return basicClass;
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
        InsnList toAdd = ASMUtil.createInsnList(list -> {
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new VarInsnNode(ALOAD, 2));
            list.add(new VarInsnNode(ALOAD, 3));
            list.add(new VarInsnNode(ILOAD, 4));
            list.add(new MethodInsnNode(INVOKESTATIC, "thedarkcolour/futuremc/world/gen/feature/BeeNestGenerator", "generateBeeNestsForSmallTrees", "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;I)V", false));
        });
        return ASMUtil.patchBeforeReturnTrue(classNode, method, toAdd);
    }

    private static byte[] patchWorldGenBigTree(byte[] basicClass) {
        ClassNode classNode = ASMUtil.createClassNode(basicClass);
        MethodNode method = ASMUtil.findMethod(
                classNode,
                "func_180709_b",
                "generate",
                "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Z"
        );

        InsnList toAdd = ASMUtil.createInsnList(list -> {
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new VarInsnNode(ALOAD, 2));
            list.add(new VarInsnNode(ALOAD, 3));
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/gen/feature/WorldGenBigTree", ASMUtil.isObfuscated ? "field_76501_f" : "height", "I"));
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new MethodInsnNode(INVOKESTATIC, "thedarkcolour/futuremc/world/gen/feature/BeeNestGenerator", "generateBeeNestsForBigTrees", "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;ILnet/minecraft/world/gen/feature/WorldGenAbstractTree;)V", false));
        });
        return ASMUtil.patchBeforeReturnTrue(classNode, method, toAdd);
    }/*

    private static byte[] patchEntityPlayerMP(byte[] basicClass) {
        ClassNode classNode = ASMUtil.createClassNode(basicClass);
        MethodNode method = ASMUtil.findMethod(
                classNode,
                "func_180472_a",
                "displayVillagerTradeGui",
                "(Lnet/minecraft/entity/IMerchant;)V"
        );
        // redirect to our constructor
        TypeInsnNode newNode = (TypeInsnNode) ASMUtil.findInsn(method, insn -> {
            return insn.getOpcode() == NEW && ((TypeInsnNode) insn).desc.equals("net/minecraft/inventory/ContainerMerchant");
        });
        newNode.desc = "thedarkcolour/futuremc/container/ContainerVillager";
        MethodInsnNode constructorCall = (MethodInsnNode) ASMUtil.findInsn(method, insn -> {
           return insn.getOpcode() == INVOKESPECIAL && ((MethodInsnNode) insn).owner.equals("net/minecraft/inventory/ContainerMerchant");
        });
        constructorCall.owner = "thedarkcolour/futuremc/container/ContainerVillager";
        constructorCall.desc = "(Lnet/minecraft/entity/player/InventoryPlayer;Lnet/minecraft/entity/IMerchant;Lnet/minecraft/world/World;)V";

        // Remove extra world parameter
        //AbstractInsnNode a = constructorCall.getPrevious();
        //AbstractInsnNode b = a.getPrevious();
        //method.instructions.remove(a);
        //method.instructions.remove(b);

        // Replace cast
        TypeInsnNode castNode = (TypeInsnNode) ASMUtil.findInsn(method, insn -> {
            return insn.getOpcode() == CHECKCAST && ((TypeInsnNode) insn).desc.equals("net/minecraft/inventory/ContainerMerchant");
        });
        castNode.desc = "thedarkcolour/futuremc/container/ContainerVillager";

        // Redirect method call
        MethodInsnNode getMerchantInventoryNode = (MethodInsnNode) castNode.getNext();
        getMerchantInventoryNode.owner = "thedarkcolour/futuremc/container/ContainerVillager";
        // Rename because this becomes the SRG name outside of dev environment
        getMerchantInventoryNode.name = "getMerchantInventory";

        return ASMUtil.compile(classNode, 0);
    }

    private static byte[] patchEntityPlayerSP(byte[] basicClass) {
        ClassNode classNode = ASMUtil.createClassNode(basicClass);
        MethodNode method = ASMUtil.findMethod(
                classNode,
                "func_180472_a",
                "displayVillagerTradeGui",
                "(Lnet/minecraft/entity/IMerchant;)V"
        );

        TypeInsnNode newNode = (TypeInsnNode) ASMUtil.findInsn(method, node -> {
            return node.getOpcode() == NEW;
        });
        newNode.desc = "thedarkcolour/futuremc/client/gui/GuiVillager";
        MethodInsnNode constructorCall = ASMUtil.findMethodInsn(method, "<init>", "<init>", null);
        constructorCall.owner = "thedarkcolour/futuremc/client/gui/GuiVillager";

        return ASMUtil.compile(classNode);
    }

    private static byte[] patchNetHandlerPlayClient(byte[] basicClass) {
        ClassNode classNode = ASMUtil.createClassNode(basicClass);
        MethodNode method = ASMUtil.findMethod(
                classNode,
                "func_147240_a",
                "handleCustomPayload",
                "(Lnet/minecraft/network/play/server/SPacketCustomPayload;)V"
        );
        // Patch instanceof
        TypeInsnNode instanceOf = (TypeInsnNode) ASMUtil.findInsn(method, node -> {
            return node.getOpcode() == INSTANCEOF && ((TypeInsnNode) node).desc.equals("net/minecraft/client/gui/GuiMerchant");
        });
        instanceOf.desc = "thedarkcolour/futuremc/client/gui/GuiVillager";

        // Patch cast
        TypeInsnNode cast = (TypeInsnNode) ASMUtil.findInsn(method, node -> {
            return node.getOpcode() == CHECKCAST && ((TypeInsnNode) node).desc.equals("net/minecraft/client/gui/GuiMerchant");
        });
        cast.desc = "thedarkcolour/futuremc/client/gui/GuiVillager";

        // Patch getMerchant call
        MethodInsnNode getMerchant = (MethodInsnNode) cast.getNext();
        getMerchant.owner = "thedarkcolour/futuremc/client/gui/GuiVillager";

        return ASMUtil.compile(classNode);
    }*/

    private static byte[] patchModelBiped(byte[] basicClass) {
        ClassNode classNode = ASMUtil.createClassNode(basicClass);
        MethodNode method = ASMUtil.findMethod(
                classNode,
                "func_78087_a",
                "setRotationAngles",
                "(FFFFFFLnet/minecraft/entity/Entity;)V"
        );

        InsnList toAdd = ASMUtil.createInsnList(list -> {
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new MethodInsnNode(INVOKESTATIC, "thedarkcolour/futuremc/event/Events", "setPlayerRotations", "(Lnet/minecraft/client/model/ModelBiped;)V", false));
        });
        return ASMUtil.patchBeforeMcMethod(classNode, method, toAdd, "func_178685_a", "copyModelAngles", 1);
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