function initializeCoreMod() {
    return {
        'BeeNestsCoremod': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.world.gen.feature.TreeFeature'
            },
            'transformer': function (classNode) {
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var arrayLength = classNode.methods.size();
                for (var i = 0; i < arrayLength; ++i) {
                    var method = classNode.methods.get(i);
                    if (method.access === Opcodes.ACC_PUBLIC) {
                        var arrLength = method.instructions.size();
                        for (var j = 0; j < arrLength; ++j) {
                            var insn = method.instructions.get(j);
                            if (insn.getOpcode() == Opcodes.IRETURN && insn.getPrevious().getOpcode() == Opcodes.ICONST_1) {
                                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
                                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                                var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
                                var toInsert = new InsnList();
                                toInsert.add(new VarInsnNode(Opcodes.ALOAD, 2));
                                toInsert.add(new VarInsnNode(Opcodes.ALOAD, 3));
                                toInsert.add(new VarInsnNode(Opcodes.ALOAD, 4));
                                toInsert.add(new VarInsnNode(Opcodes.ILOAD, 6));
                                toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "thedarkcolour/futuremc/world/gen/BeeNestGenerator", "generateBeeNestForSmallTrees", "(Lnet/minecraft/world/gen/IWorldGenerationReader;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;I)V", false));
                                method.instructions.insertBefore(insn, toInsert);
                            }
                        }
                    }
                }

                return classNode
            }
        }
    }
}