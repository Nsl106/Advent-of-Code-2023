fun main() {
    val currentDay = 2
    val days = listOf(DayOne, DayTwo)

    println("answers for day $currentDay:")

    val day = days.first { it.day == currentDay }
    val input = getInput(currentDay)
    print("part one: ")
    day.printPartOne(input)
    print("part two: ")
    day.printPartTwo(input)
}