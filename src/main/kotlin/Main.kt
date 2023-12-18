fun main() {
    val currentDay = "17".toRegex()

    val allDays = BaseDay::class.sealedSubclasses
    val day = allDays.first { currentDay.containsMatchIn(it.simpleName!!) }.objectInstance!!

    val g = gridOf("""
        1234
        abcd
        5678
        efgh
    """.trimIndent().lines().map { it.toList() })


    g[Grid.FacingPosition(0, 1, Grid.Direction.EAST)] = 'M'

    println("answers for day $currentDay:")
    println("part one: initial answer: ${day.partOne()}")
    println("part two: initial answer: ${day.partTwo()}")
}