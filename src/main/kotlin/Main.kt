fun main() {
    val currentDay = 2
    val days = listOf(DayOne, DayTwo)

    println("answers for day $currentDay:")

    val day = days.first { it.day == currentDay }
    println("part one: ${day.partOne()}")
    println("part two: ${day.partTwo()}")
}