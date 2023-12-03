fun main() {
    val currentDay = 3
    val days = listOf(DayOne, DayTwo, DayThree)
    val revisedDays = listOf(DayOneRevised)

    println("answers for day $currentDay:")

    val day = days.first { it.day == currentDay }
    val revisedDay = revisedDays.firstOrNull { it.day == currentDay }
    println("part one: initial answer: ${day.partOne()}, revised answer: ${revisedDay?.partOne()}")
    println("part two: initial answer: ${day.partTwo()}, revised answer: ${revisedDay?.partTwo()}")
}