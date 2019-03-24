import ScoreCalculator.Companion.EXTRA_SPARE_SHOTS
import ScoreCalculator.Companion.EXTRA_STRIKE_SHOTS

class ObjectCalculator : ScoreCalculator {

    override fun calculateBowlingGameScore(pointsByShot: Array<Int?>): BowlingGameScoreModel {
        val gameMap = buildMap(pointsByShot)
        var state = getGameStateFromMap(gameMap)
        val frameScores = IntArray(BowlingGame.BOWLING_GAME_MAX_FRAME_SCORE)

        //SUCCESS means we have proper array of shots
        if (state == BowlingGameState.SUCCESS) {
            var prevScore = 0
            for (i in 0 until BowlingGame.BOWLING_GAME_MAX_FRAME_SCORE) {
                val currentFrame = gameMap[i]
                prevScore += when (gameMap[i].frameType) {
                    FrameType.BASIC -> currentFrame.sum
                    FrameType.SPARE -> currentFrame.sum + gameMap[i + 1].first
                    FrameType.STRIKE -> {
                        val nextFrame = gameMap[i + 1]
                        if (nextFrame.frameType == FrameType.STRIKE) {
                            currentFrame.sum + nextFrame.sum + gameMap[i + 2].first
                        } else {
                            currentFrame.sum + nextFrame.sum
                        }
                    }
                    else -> 0
                }
                frameScores[i] = prevScore
            }
        }

        return BowlingGameScoreModel(state, frameScores)
    }

    private fun buildMap(pointsByShot: Array<Int?>): Array<BowlingFrame> {
        val gameMap = mutableListOf<BowlingFrame>()
        var pointsIndex = 0
        while (pointsIndex < pointsByShot.size) {
            //no more shots to analyze
            if (pointsByShot[pointsIndex] == null) {
                break
            }

            when {
                //is strike
                pointsByShot[pointsIndex] == BowlingGame.BOWLING_GAME_MAX_FRAME_SCORE -> {
                    gameMap.add(BowlingFrame(FrameType.STRIKE, BowlingGame.BOWLING_GAME_MAX_FRAME_SCORE))
                    pointsIndex++
                }
                else -> {
                    when {
                        //last non-strike item with no moro items left
                        pointsIndex + 1 >= pointsByShot.size -> gameMap.add(BowlingFrame(FrameType.EXCESS_DATA))
                        //no second shot data for a frame
                        pointsByShot[pointsIndex + 1] == null -> gameMap.add(BowlingFrame(FrameType.EXTRA, pointsByShot[pointsIndex]!!))
                        //spare or basic
                        else -> {
                            val first = pointsByShot[pointsIndex]!!
                            val second = pointsByShot[pointsIndex + 1]!!
                            val type = if (isBasicShot(first, second)) FrameType.BASIC else FrameType.SPARE
                            gameMap.add(BowlingFrame(type, first, second))
                        }
                    }
                    pointsIndex += 2
                }
            }
        }

        return gameMap.toTypedArray()
    }

    private fun getGameStateFromMap(gameMap: Array<BowlingFrame>): BowlingGameState {
        var state = BowlingGameState.SUCCESS
        //enough frames
        if (gameMap.size < BowlingGame.BOWLING_GAME_FRAMES) {
            return BowlingGameState.NOT_ENOUGH_SHOTS
        }

        //check map items for error
        for (i in 0 until gameMap.size) {
            gameMap[i].let { entry ->
                when {
                    entry.sum > BowlingGame.BOWLING_GAME_MAX_FRAME_SCORE -> return BowlingGameState.BAD_INPUT_DATA
                    entry.frameType == FrameType.EXCESS_DATA -> return BowlingGameState.TOO_MUCH_SHOTS
                    entry.frameType == FrameType.NOT_ENOUGH_DATA -> return BowlingGameState.NOT_ENOUGH_SHOTS
                    else -> {
                        //no-op
                    }
                }
            }
        }

        //check last strike or spare
        val lastFrame = gameMap.last()
        when (lastFrame.frameType) {
            FrameType.SPARE,
            FrameType.STRIKE -> {
                val extraShots = if (lastFrame.frameType == FrameType.SPARE) EXTRA_SPARE_SHOTS else EXTRA_STRIKE_SHOTS
                val successShotsAmount = BowlingGame.BOWLING_GAME_FRAMES + extraShots
                when {
                    gameMap.size == successShotsAmount -> return BowlingGameState.SUCCESS
                    gameMap.size > successShotsAmount -> return BowlingGameState.TOO_MUCH_SHOTS
                    gameMap.size < successShotsAmount -> return BowlingGameState.NOT_ENOUGH_SHOTS
                }

            }
            else -> {
                //no-op
            }
        }

        return BowlingGameState.SUCCESS
    }

    private fun isBasicShot(first: Int, second: Int) = first + second < BowlingGame.BOWLING_GAME_MAX_FRAME_SCORE
}
