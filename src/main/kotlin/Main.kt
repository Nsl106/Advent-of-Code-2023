fun main() {
    val currentDay = 2
    val days = listOf(DayOne, DayTwo)

    println("answers for day $currentDay:")

    val day = days.first { it.day == currentDay }
    print("part one: ")
    day.printPartOne()
    print("part two: ")
    day.printPartTwo()
}