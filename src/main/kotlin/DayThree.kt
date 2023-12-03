import kotlin.math.max
import kotlin.math.min

object DayThree: BaseDay<Int>(3) {
    override fun partOne(): Int {
        fun Char.isSymbol() = !this.isDigit() && this != '.'
        fun String.symbolInRange(start: Int, end: Int) = subSequence(max(start, 0), min(end, length)).any { it.isSymbol() }

        var sum = 0
        for ((index, line) in input.withIndex()) {
            var tmpLine = line
            while (tmpLine.isNotEmpty()) {
                val charIndex = line.length - tmpLine.length
                val number = tmpLine.takeWhile { it.isDigit() }
                if (number.isNotBlank()) {
                    if (input.getRange(index - 1, index + 1).any { it.symbolInRange(charIndex - 1, charIndex + number.length + 1) }) {
                        sum += number.toInt()
                    }
                    tmpLine = tmpLine.dropWhile { it.isDigit() }
                } else {
                    tmpLine = tmpLine.drop(1)
                }

            }
        }
        return sum
    }

    override fun partTwo(): Int {
        return 0
    }
}