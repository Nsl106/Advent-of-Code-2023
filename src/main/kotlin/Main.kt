fun main() {
    val currentDay = "17".toRegex()

    val allDays = BaseDay::class.sealedSubclasses
    val day = allDays.first { currentDay.containsMatchIn(it.simpleName!!) }.objectInstance!!

    println("answers for day $currentDay:")
    println("part one: initial answer: ${day.partOne()}")
    println("part two: initial answer: ${day.partTwo()}")
}