import ScoreCalculator.Companion.BASIC_SPARE_STEP
import ScoreCalculator.Companion.EXTRA_SPARE_SHOTS
import ScoreCalculator.Companion.EXTRA_STRIKE_SHOTS
import ScoreCalculator.Companion.STRIKE_STEP

interface ScoreCalculator {

    companion object {
        const val STRIKE_STEP = 1
        const val BASIC_SPARE_STEP = 2
        const val EXTRA_SPARE_SHOTS = 1
        const val EXTRA_STRIKE_SHOTS = 2
    }

    fun calculateBowlingGameScore(pointsByShot: Array<Int?>): BowlingGameScoreModel
}

class FunctionalCalculator : ScoreCalculator {

    override fun calculateBowlingGameScore(pointsByShot: Array<Int?>): BowlingGameScoreModel {
        val scoresByFrame = mutableListOf<Int>()
        var currentFrame = 0
        var currentIndex = 0
        var prevFrameSum = 0

        while (currentIndex < pointsByShot.size) {
            //basic shot
            val isBasicShot = isBasicShot(pointsByShot, currentIndex)
            if (isBasicShot == null) {
                break
            } else if (isBasicShot) {
                scoresByFrame.add(
                    calculateFrameScoreByShotType(
                        pointsByShot,
                        currentIndex,
                        FrameType.BASIC
                    ) + prevFrameSum
                )
                prevFrameSum = scoresByFrame.last()
                currentIndex += BASIC_SPARE_STEP
                currentFrame++
                continue
            }

            //spare shot
            val isSpareShot = isSpareShot(pointsByShot, currentIndex)
            if (isSpareShot == null) {
                break
            } else if (isSpareShot) {
                scoresByFrame.add(
                    calculateFrameScoreByShotType(
                        pointsByShot,
                        currentIndex,
                        FrameType.SPARE
                    ) + prevFrameSum
                )
                prevFrameSum = scoresByFrame.last()
                currentIndex += if (currentFrame == BowlingGame.BOWLING_GAME_FRAMES - 1) BASIC_SPARE_STEP + EXTRA_SPARE_SHOTS else BASIC_SPARE_STEP
                currentFrame++
                continue
            }

            //strike shot
            val isStrikeShot = isStrikeShot(pointsByShot, currentIndex)
            if (isStrikeShot == null) {
                break
            } else if (isStrikeShot) {
                scoresByFrame.add(
                    calculateFrameScoreByShotType(
                        pointsByShot,
                        currentIndex,
                        FrameType.STRIKE
                    ) + prevFrameSum
                )
                prevFrameSum = scoresByFrame.last()
                currentIndex += if (currentFrame == BowlingGame.BOWLING_GAME_FRAMES - 1) STRIKE_STEP + EXTRA_STRIKE_SHOTS else STRIKE_STEP
                currentFrame++
                continue
            }
        }

        return BowlingGameScoreModel(
            when {
                currentFrame > BowlingGame.BOWLING_GAME_FRAMES -> BowlingGameState.TOO_MUCH_SHOTS
                currentFrame == BowlingGame.BOWLING_GAME_FRAMES -> BowlingGameState.SUCCESS
                else -> BowlingGameState.NOT_ENOUGH_SHOTS
            }, scoresByFrame.toIntArray()
        )
    }

    private fun isBasicShot(pointsByShot: Array<Int?>, index: Int) =
        if (pointsByShot[index] == null || pointsByShot.indexOfLast { it != null } < index + 1) {
            null
        } else {
            pointsByShot[index]!! + pointsByShot[index + 1]!! < BowlingGame.BOWLING_GAME_MAX_FRAME_SCORE
        }

    private fun isSpareShot(pointsByShot: Array<Int?>, index: Int) =
        if (pointsByShot[index] == null || pointsByShot.indexOfLast { it != null } < index + 2) {
            null
        } else {
            pointsByShot[index]!! != 10 &&
                    pointsByShot[index]!! + pointsByShot[index + 1]!! == BowlingGame.BOWLING_GAME_MAX_FRAME_SCORE
        }

    private fun isStrikeShot(pointsByShot: Array<Int?>, index: Int) =
        if (pointsByShot[index] == null || pointsByShot.indexOfLast { it != null } < index + 2) {
            null
        } else {
            pointsByShot[index]!! == BowlingGame.BOWLING_GAME_MAX_FRAME_SCORE
        }

    private fun calculateFrameScoreByShotType(pointsByShot: Array<Int?>, index: Int, frameType: FrameType): Int =
        when (frameType) {
            FrameType.BASIC -> {
                pointsByShot[index]!! + pointsByShot[index + 1]!!
            }
            FrameType.SPARE,
            FrameType.STRIKE -> {
                pointsByShot[index]!! + pointsByShot[index + 1]!! + pointsByShot[index + 2]!!
            }
            else -> 0
        }
}