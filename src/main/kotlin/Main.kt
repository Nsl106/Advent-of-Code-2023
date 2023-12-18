fun main() {
    val currentDay = 1
    val days = listOf(Day01, Day02, Day03, Day04, Day05, Day06, Day07, Day08, Day09, Day10, Day11, Day12, Day13, Day14, Day15, Day16, Day17)

    println("answers for day $currentDay:")

    val day = days.first { it.day == currentDay }
    println("part one: initial answer: ${day.partOne()}")
    println("part two: initial answer: ${day.partTwo()}")
}