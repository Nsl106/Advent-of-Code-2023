fun main() {
    val currentDay = 20

    val dayRegex = currentDay.toString().padStart(2, '0').toRegex()
    val allDays = Day::class.sealedSubclasses
    val day = allDays.first { dayRegex.containsMatchIn(it.simpleName!!) }.objectInstance!!

    printDay(day)
}

fun printDay(day: Day) {
    val partOne = "part one: ${day.partOne()}"
    val partTwo = "part two: ${day.partTwo()}"

    val maxWidth = maxOf(partOne.length, partTwo.length)
    val widthHalved = (maxWidth / 2) - 2
    val header = "+" + "-".repeat(widthHalved) + " $day " + "-".repeat(widthHalved) + "+"

    println(header)
    println("| ${partOne.padEnd(header.length - 4)} |")
    println("| ${partTwo.padEnd(header.length - 4)} |")
    println("+" + "-".repeat(header.length - 2) + "+")
}