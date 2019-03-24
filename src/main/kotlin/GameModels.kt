data class BowlingGameScoreModel(val state: BowlingGameState, val score: IntArray)

data class BowlingFrame(val frameType: FrameType, val first: Int = 0, val second: Int = 0) {
    val sum: Int
        get() = first + second
}
