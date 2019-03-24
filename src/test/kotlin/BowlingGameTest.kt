import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verifyZeroInteractions
import kotlin.random.Random

class BowlingGameTest {

    private lateinit var game: BowlingGame

    @Mock
    private val scoreCalculatorMock = mock(ScoreCalculator::class.java)

    @Before
    fun setUp() {
        game = BowlingGame(scoreCalculatorMock)
    }

    @Test
    fun `can start bowling game`() {
        assertNotNull(game)
    }

    @Test
    fun `when game is not started proper state present`() {
        assertEquals(BowlingGameState.READY_TO_START, game.gameState)

        verifyZeroInteractions(scoreCalculatorMock)
    }

    @Test(expected = Test.None::class)
    fun `when too much data no exception present and proper game state is set`() {
        game.setShotsSequence(Random.nextInt(11), BowlingGame.BOWLING_GAMES_MAX_SHOTS + 1)
        game.startGame()

        assertEquals(BowlingGameState.TOO_MUCH_SHOTS, game.gameState)
        verifyZeroInteractions(scoreCalculatorMock)
    }
}