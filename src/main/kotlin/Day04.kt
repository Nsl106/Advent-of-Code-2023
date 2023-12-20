import kotlin.math.pow

object Day04: BaseDay(4) {
    override fun partOne(): Int {
        return input.sumOf { line ->
            val (winningNumbers, ourNumbers) = line.substringAfter(":").split("|").map { it.split(" ").filter(String::isNotBlank) }
            val matches = ourNumbers.count { it in winningNumbers }
            2.0.pow(matches - 1).toInt()
        }
    }

    private fun getTotalNewCards(line: String): Int {
        val cardID = line.firstInt()
        val (winningNumbers, ourNumbers) = line.substringAfter(":").split("|").map { it.split(" ").filter(String::isNotBlank) }

        val matches = ourNumbers.filter { it in winningNumbers }

        var totalNewCards = 0

        for (i in matches.indices) totalNewCards += getTotalNewCards(input[i + cardID])

        return totalNewCards + matches.size
    }

    override fun partTwo(): Int {
        val newCardCount = input.sumOf {
            getTotalNewCards(it)
        }
        return newCardCount + input.size
    }
}