class BowlingGame constructor(
    private val scoreCalculator: ScoreCalculator
) {

    companion object {
        const val BOWLING_GAME_FRAMES = 10
        const val BOWLING_GAMES_MAX_SHOTS = 21
        const val BOWLING_GAME_MAX_FRAME_SCORE = 10
    }

    private val pointsByShot = arrayOfNulls<Int>(BOWLING_GAMES_MAX_SHOTS)

    var scoreByFrame: IntArray = intArrayOf(0)
        private set
    var gameState: BowlingGameState = BowlingGameState.READY_TO_START
        private set

    fun setShot(points: Int) {
        val freeIndex = pointsByShot.indexOfFirst { it == null }
        when (freeIndex) {
            -1 -> gameState = BowlingGameState.TOO_MUCH_SHOTS
            else -> if (isValidScore(points).not()) {
                gameState = BowlingGameState.BAD_INPUT_DATA
            } else if (gameState != BowlingGameState.BAD_INPUT_DATA) {
                pointsByShot[freeIndex] = points
            }
        }
    }

    fun setShotsSequence(points: Int, amount: Int) {
        val freeIndex = pointsByShot.indexOfFirst { it == null }
        when {
            freeIndex == -1 || freeIndex + amount > BOWLING_GAMES_MAX_SHOTS -> {
                gameState = BowlingGameState.TOO_MUCH_SHOTS
            }
            else -> {
                if (isValidScore(points).not()) {
                    gameState = BowlingGameState.BAD_INPUT_DATA
                } else if (gameState != BowlingGameState.BAD_INPUT_DATA) {
                    for (i in 0 until amount) {
                        pointsByShot[freeIndex + i] = points
                    }
                }
            }
        }
    }

    fun setShotsFromArray(shots: IntArray) = shots.forEach { setShot(it) }

    fun startGame() {
        if (gameState == BowlingGameState.READY_TO_START) {
            val result = scoreCalculator.calculateBowlingGameScore(pointsByShot)
            gameState = result.state
            scoreByFrame = result.score
        }
    }

    private fun isValidScore(score: Int) = score in 0..BOWLING_GAME_MAX_FRAME_SCORE
}