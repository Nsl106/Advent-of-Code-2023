fun main() {
    val currentDay = 20

    printDay(getDayByInt(currentDay))
}

fun printDay(day: Day) {
    val partOne = "part one: ${day.partOne()}"
    val partTwo = "part two: ${day.partTwo()}"

    val maxWidth = maxOf(partOne.length, partTwo.length)
    val widthHalved = (((maxWidth - day.toString().length) + 1) / 2)
    val header = "+" + "-".repeat(widthHalved) + " $day " + "-".repeat(widthHalved) + "+"

    println(header)
    println("| ${partOne.padEnd(header.length - 4)} |")
    println("| ${partTwo.padEnd(header.length - 4)} |")
    println("+" + "-".repeat(header.length - 2) + "+")
}