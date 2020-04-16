package thedarkcolour.futuremc.biome.provider

/**
 * Data class implements equals() and hashCode()
 */
data class NoisePoint(
        private val heat: Float, private val humidity: Float, private val hilliness: Float,
        private val style: Float, private val rarityPotential: Float
) {
    fun getDistance(other: NoisePoint): Float {
        return (heat - other.heat) * (heat - other.heat) +
                (humidity - other.humidity) * (humidity - other.humidity) +
                (hilliness - other.hilliness) * (hilliness - other.hilliness) +
                (style - other.style) * (style - other.style) +
                (rarityPotential - other.rarityPotential) * (rarityPotential - other.rarityPotential)

    }
}