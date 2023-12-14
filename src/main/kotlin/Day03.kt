import kotlin.math.max
import kotlin.math.min

object Day03: BaseDay(3) {
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
        // 0 is empty, -1 is gear, anything else is the number it's part of
        // ..98..923...*... == [0,0,98,98,0,0,923,923,923,0,0,0,-1,0,0,0]
        fun parseInput(input: List<String>): List<List<Int>> {
            val lines = mutableListOf<List<Int>>()
            input.forEach { line ->
                val row = mutableListOf<Int>()
                var tmpLine = line
                while (tmpLine.isNotEmpty()) {
                    when {
                        tmpLine.first() == '*' -> {
                            row.add(-1)
                            tmpLine = tmpLine.drop(1)
                        }

                        tmpLine.first().isDigit() -> {
                            val num = tmpLine.takeWhile { it.isDigit() }
                            num.forEach { _ -> row.add(num.toInt()) }
                            tmpLine = tmpLine.drop(num.length)
                        }

                        else -> {
                            row.add(0)
                            tmpLine = tmpLine.drop(1)
                        }
                    }
                }
                lines.add(row)
            }
            return lines
        }

        val input = parseInput(input)

        var sum = 0
        for ((index, line) in input.withIndex()) {
            var tmpLine = line
            while (tmpLine.isNotEmpty()) {
                val charIndex = line.size - tmpLine.size
                if (tmpLine.first() == -1) {
                    //get surrounding numbers
                    val allSurroundingNumbers = mutableSetOf<Int>()
                    input.getRange(index - 1, index + 1).forEach { ln ->
                        ln.getRange(charIndex - 1, charIndex + 1).forEach {
                            if (it != 0 && it != -1) {
                                allSurroundingNumbers.add(it)
                            }
                        }
                    }

                    if (allSurroundingNumbers.size == 2) {
                        sum += (allSurroundingNumbers.first() * allSurroundingNumbers.last())
                    }
                }
                tmpLine = tmpLine.drop(1)
            }
        }
        return sum
    }
}