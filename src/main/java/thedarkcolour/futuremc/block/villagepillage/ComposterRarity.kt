package thedarkcolour.futuremc.block.villagepillage

// inlined because there's no reason for an actual enum aha
inline class ComposterRarity(val chance: Byte) {
    companion object {
        val COMMON = ComposterRarity(30)
        val UNCOMMON = ComposterRarity(50)
        val RARE = ComposterRarity(65)
        val EPIC = ComposterRarity(85)
        val LEGENDARY = ComposterRarity(100)
    }
}