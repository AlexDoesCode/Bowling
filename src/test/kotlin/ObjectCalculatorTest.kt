import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ObjectCalculatorTest {

    private lateinit var game: BowlingGame

    private val scoreCalculator = ObjectCalculator()

    @Before
    fun setUp() {
        game = BowlingGame(scoreCalculator)
    }

    @Test
    fun `when no data proper game state is set`() {
        game.startGame()

        junit.framework.Assert.assertEquals(BowlingGameState.NOT_ENOUGH_SHOTS, game.gameState)
    }

    @Test
    fun `when not enough data proper game state is set`() {
        game.setShotsSequence(0, 3)
        game.startGame()

        junit.framework.Assert.assertEquals(BowlingGameState.NOT_ENOUGH_SHOTS, game.gameState)
    }

    @Test
    fun `when first strike and rest zero points should produce correct result`() {
        game.setShot(10)
        game.setShotsSequence(0, 18)
        game.startGame()

        Assert.assertEquals(BowlingGameState.SUCCESS, game.gameState)
        Assert.assertEquals(10, game.scoreByFrame.last())
    }

    @Test
    fun `when first spare and rest zero points should produce correct result`() {
        game.setShot(5)
        game.setShot(5)
        game.setShotsSequence(0, 18)
        game.startGame()

        Assert.assertEquals(BowlingGameState.SUCCESS, game.gameState)
        Assert.assertEquals(10, game.scoreByFrame.last())
    }

    @Test
    fun `when first + second basic and rest zero points should produce correct result`() {
        game.setShot(0)
        game.setShot(5)
        game.setShot(5)
        game.setShotsSequence(0, 17)
        game.startGame()

        Assert.assertEquals(BowlingGameState.SUCCESS, game.gameState)
        Assert.assertEquals(10, game.scoreByFrame.last())
    }

    @Test
    fun `when too much shots for a game produce correct result`() {
        game.setShot(0)
        game.setShot(5)
        game.setShot(5)
        game.setShotsSequence(0, 17)
        game.startGame()

        Assert.assertEquals(BowlingGameState.SUCCESS, game.gameState)
        Assert.assertEquals(10, game.scoreByFrame.last())
    }

    @Test
    fun `when score greater max score is set for a frame produce bad input state`() {
        game.setShotsSequence(5, 4)
        game.setShot(1)
        game.setShot(11)
        game.setShotsSequence(0, 14)
        game.startGame()

        Assert.assertEquals(BowlingGameState.BAD_INPUT_DATA, game.gameState)
    }

    @Test
    fun `when score lesser than zero is set for a frame produce bad input state`() {
        game.setShotsSequence(5, 4)
        game.setShot(1)
        game.setShot(-1)
        game.setShotsSequence(0, 14)
        game.startGame()

        Assert.assertEquals(BowlingGameState.BAD_INPUT_DATA, game.gameState)
    }

    @Test
    fun `when input for 133 points success`() {
        val scores1 = intArrayOf(1, 4, 4, 5, 6, 4, 5, 5, 10, 0, 1, 7, 3, 6, 4, 10, 2, 8, 6)
        game.setShotsFromArray(scores1)
        game.startGame()

        Assert.assertEquals(BowlingGameState.SUCCESS, game.gameState)
        Assert.assertEquals(133, game.scoreByFrame.last())
    }

    @Test
    fun `when input for 183 points success`() {
        val scores1 = intArrayOf(9, 1, 10, 10, 9, 1, 6, 3, 8, 2, 7, 3, 6, 4, 10, 9, 1, 6)
        game.setShotsFromArray(scores1)
        game.startGame()

        Assert.assertEquals(BowlingGameState.SUCCESS, game.gameState)
        Assert.assertEquals(183, game.scoreByFrame.last())
    }

    @Test
    fun `when input for 159 points success`() {
        val scores1 = intArrayOf(8, 2, 8, 2, 10, 8, 2, 10, 9, 1, 7, 1, 10, 8, 1, 5, 3)
        game.setShotsFromArray(scores1)
        game.startGame()

        Assert.assertEquals(BowlingGameState.SUCCESS, game.gameState)
        Assert.assertEquals(159, game.scoreByFrame.last())
    }

    @Test
    fun `when input for 269 points success`() {
        val scores1 = intArrayOf(10, 9, 1, 10, 10, 10, 10, 10, 10, 10, 10, 9, 1)
        game.setShotsFromArray(scores1)
        game.startGame()

        Assert.assertEquals(BowlingGameState.SUCCESS, game.gameState)
        Assert.assertEquals(269, game.scoreByFrame.last())
    }

    @Test
    fun `when input for 182 points success`() {
        val scores1 = intArrayOf(8, 1, 9, 1, 9, 0, 10, 7, 2, 10, 10, 8, 2, 10, 10, 9, 1)
        game.setShotsFromArray(scores1)
        game.startGame()

        Assert.assertEquals(BowlingGameState.SUCCESS, game.gameState)
        Assert.assertEquals(182, game.scoreByFrame.last())
    }

    @Test
    fun `when input for 222 points success`() {
        val scores1 = intArrayOf(8, 2, 8, 2, 8, 2, 10, 7, 3, 9, 1, 8, 2, 10, 10, 10, 10, 9)
        game.setShotsFromArray(scores1)
        game.startGame()

        Assert.assertEquals(BowlingGameState.SUCCESS, game.gameState)
        Assert.assertEquals(222, game.scoreByFrame.last())
    }

    @Test
    fun `when input for 172 points success`() {
        val scores1 = intArrayOf(8, 0, 9, 0, 9, 1, 6, 4, 8, 2, 10, 10, 8, 1, 10, 10, 6, 3)
        game.setShotsFromArray(scores1)
        game.startGame()

        Assert.assertEquals(BowlingGameState.SUCCESS, game.gameState)
        Assert.assertEquals(172, game.scoreByFrame.last())
    }

    @Test
    fun `when input for 191 points success`() {
        val scores1 = intArrayOf(10, 6, 3, 10, 9, 0, 10, 10, 10, 9, 1, 9, 1, 9, 1, 8)
        game.setShotsFromArray(scores1)
        game.startGame()

        Assert.assertEquals(BowlingGameState.SUCCESS, game.gameState)
        Assert.assertEquals(191, game.scoreByFrame.last())
    }

    @Test
    fun `when input for 130 points success`() {
        val scores1 = intArrayOf(9, 1, 8, 0, 7, 2, 9, 1, 7, 3, 7, 2, 9, 1, 8, 0, 8, 2, 7, 2)
        game.setShotsFromArray(scores1)
        game.startGame()

        Assert.assertEquals(BowlingGameState.SUCCESS, game.gameState)
        Assert.assertEquals(130, game.scoreByFrame.last())
    }

    @Test
    fun `when input for 144 points success`() {
        val scores1 = intArrayOf(9, 1, 9, 1, 8, 0, 5, 4, 8, 2, 8, 2, 6, 4, 3, 7, 9, 1, 6, 2)
        game.setShotsFromArray(scores1)
        game.startGame()

        Assert.assertEquals(BowlingGameState.SUCCESS, game.gameState)
        Assert.assertEquals(144, game.scoreByFrame.last())
    }

    @Test
    fun `when input for 300 points success`() {
        game.setShotsSequence(10, 12)
        game.startGame()

        Assert.assertEquals(BowlingGameState.SUCCESS, game.gameState)
        Assert.assertEquals(300, game.scoreByFrame.last())
    }

    @Test
    fun `when input for 10 points success`() {
        game.setShotsSequence(5, 2)
        game.setShotsSequence(0, 18)
        game.startGame()

        Assert.assertEquals(BowlingGameState.SUCCESS, game.gameState)
        Assert.assertEquals(10, game.scoreByFrame.last())
    }
}