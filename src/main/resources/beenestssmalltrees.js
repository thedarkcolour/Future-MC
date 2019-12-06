function initializeCoreMod() {
    return {
        'SmallTrees': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.gen.feature.TreeFeature',
                'methodName': 'func_208519_a',
                'methodDesc': '(Ljava/util/Set;Lnet/minecraft/world/gen/IWorldGenerationReader;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/MutableBoundingBox;)Z'
            },
            'transformer': function (method) {
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var arrLength = method.instructions.size();
                for (var j = 0; j < arrLength; ++j) {
                    var insn = method.instructions.get(j);
                    if (insn.getOpcode() == Opcodes.ICONST_1 && insn.getNext().getOpcode() == Opcodes.IRETURN) {
                        var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
                        var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                        var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');

                        var toInsert = new InsnList();
                        toInsert.add(new VarInsnNode(Opcodes.ALOAD, 2)); //worldGenerationReader
                        toInsert.add(new VarInsnNode(Opcodes.ALOAD, 3)); //rand
                        toInsert.add(new VarInsnNode(Opcodes.ALOAD, 4)); //position
                        toInsert.add(new VarInsnNode(Opcodes.ILOAD, 6)); //height
                        toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, 'thedarkcolour/futuremc/world/gen/BeeNestGenerator', 'generateBeeNestForSmallTrees', '(Lnet/minecraft/world/gen/IWorldGenerationReader;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;I)V', false));

                        method.instructions.insertBefore(insn, toInsert);

                        method.maxStack += 5;
                    }
                }
                return method
            }
        }
    }
}