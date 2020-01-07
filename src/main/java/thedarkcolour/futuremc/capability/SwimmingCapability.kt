package thedarkcolour.futuremc.capability

class SwimmingCapability : ISwimmingCapability {
    private var lastSwimmingAnimation: Float = 0.0f
    private var swimmingAnimation: Float = 0.0f

    override fun getSwimmingAnimation() = swimmingAnimation

    override fun setSwimmingAnimation(value: Float) {
        swimmingAnimation = value
    }

    override fun getLastSwimmingAnimation() = lastSwimmingAnimation

    override fun setLastSwimmingAnimation(value: Float) {
        lastSwimmingAnimation = value
    }
}