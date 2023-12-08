fun main() {
    val currentDay = 8
    val days = listOf(Day01, Day02, Day03, Day04, Day05, Day06, Day07, Day08)
    val revisedDays = listOf(Day01Revised, Day04Revised)

    println("answers for day $currentDay:")

    val day = days.first { it.day == currentDay }
    val revisedDay = revisedDays.firstOrNull { it.day == currentDay }
    println("part one: initial answer: ${day.partOne()}, revised answer: ${revisedDay?.partOne()}")
    println("part two: initial answer: ${day.partTwo()}, revised answer: ${revisedDay?.partTwo()}")
}