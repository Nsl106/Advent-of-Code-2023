import kotlin.math.pow

object Day04: BaseDay<Int>(4) {
    override fun partOne(): Int {
        return input.sumOf { line ->
            val winningNumbers = line.substringAfter(":").substringBefore("|").split(" ").filter { it.isNotBlank() }
            val ourNumbers = line.substringAfter("|").split(" ").filter { it.isNotBlank() }
            val matches = ourNumbers.count { winningNumbers.contains(it) }
            when (matches) {
                0, 1 -> matches
                else -> 2.0.pow(matches - 1).toInt()
            }
        }
    }

    private fun getTotalNewCards(line: String): Int {
        val cardID = line.getFirstInt()
        val winningNumbers = line.substringAfter(":").substringBefore("|").split(" ").filter { it.isNotBlank() }
        val ourNumbers = line.substringAfter("|").split(" ").filter { it.isNotBlank() }
        val matches = ourNumbers.filter { winningNumbers.contains(it) }

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