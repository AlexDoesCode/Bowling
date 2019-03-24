// Here you can find an entry point to run BowlingGame
// there are 2 ways to calculate bowling score here
// 1. Functional - calculates the score by analyzing an initial array of scores
// 2. ObjectOriented - restores a game progress using using appropriate models for further analysis
//
// To switch between calculation implementations use `isFunctional` Boolean setting it to true or false appropriately
//
// You can use few ways to set scores:
// 1. game.setShotsFromArray(<YOUR_SCORES_INT_ARRAY>)
// 2. game.SetShot(<SCORE: Int>)
// 3. game.setShotsSequence(<SCORE: Int>, <AMOUNT_OF_SHOTS_TO_FILL: Int>)
//
// As a result of starting a game by < game.startGame() > after setting scores you will see game state and score table printed in console

fun main(args: Array<String>) {

    //Change this to false to use ObjectCalculator
    val isFunctional = false

    val calculator = when(isFunctional) {
        true -> FunctionalCalculator()
        else -> ObjectCalculator()
    }

    val game = BowlingGame(calculator)

    //133
    val scoresArray = intArrayOf(1, 4, 4, 5, 6, 4, 5, 5, 10, 0, 1, 7, 3, 6, 4, 10, 2, 8, 6)
    game.setShotsFromArray(scoresArray)
    game.startGame()


    println("\nGAME STATE:\n${game.gameState}\n\nFRAME SCORES: ")
    for (i in 0 until game.scoreByFrame.size) {
        print("${game.scoreByFrame[i]} | ")
    }
    println()
}