fun main() {
    val currentDay = "20".toRegex()

    val allDays = BaseDay::class.sealedSubclasses
    val day = allDays.first { currentDay.containsMatchIn(it.simpleName!!) }.objectInstance!!

    println("answers for day $currentDay:")
    println("part one: ${day.partOne()}")
    println("part two: ${day.partTwo()}")
}