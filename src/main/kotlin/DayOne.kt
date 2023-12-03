object DayOne: BaseDay(1) {
    override fun printPartOne() {
        var sum = 0
        input.forEach { line ->
            val digitOne = line.first { it.isDigit() }
            val digitTwo = line.last { it.isDigit() }
            sum += "$digitOne$digitTwo".toInt()
        }
        println(sum)
    }

    override fun printPartTwo() {
        val textDigits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine").mapIndexed { index, it -> it to index + 1 }
        val reversedTextDigits = textDigits.map { it.first.reversed() to it.second }
        val numberDigits = (1..9).associateBy { it.toString() }
        val digitsMap = mutableMapOf<String, Int>()
        digitsMap.putAll(textDigits)
        digitsMap.putAll(numberDigits)
        val reversedDigitsMap = mutableMapOf<String, Int>()
        reversedDigitsMap.putAll(reversedTextDigits)
        reversedDigitsMap.putAll(numberDigits)

        val regex = digitsMap.keys.joinToString(separator = "|") { "($it)" }.toRegex()
        val reverseRegex = reversedDigitsMap.keys.joinToString(separator = "|") { "($it)" }.toRegex()

        var sum = 0
        input.forEach { line ->
            val digitOne = digitsMap[regex.findAll(line).first().value]
            val digitTwo = reversedDigitsMap[reverseRegex.findAll(line.reversed()).first().value]
            sum += "$digitOne$digitTwo".toInt()
        }
        println(sum)
    }
}