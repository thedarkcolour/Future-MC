package thedarkcolour.futuremc.capability

interface ISwimmingCapability {
    fun getSwimmingAnimation(): Float
    fun setSwimmingAnimation(value: Float)
    fun getLastSwimmingAnimation(): Float
    fun setLastSwimmingAnimation(value: Float)
}