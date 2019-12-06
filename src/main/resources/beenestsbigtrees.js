function initializeCoreMod() {
    return {
        'BeeNestsCoremod': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.world.gen.feature.BigTreeFeature'
            },
            'transformer': function (classNode) {
                var Patcher = Java.type('thedarkcolour.futuremc.asm.Patcher');

                return Patcher.INSTANCE.patch(classNode)
            }
        }
    }
}