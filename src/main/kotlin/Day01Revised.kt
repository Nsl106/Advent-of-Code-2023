object Day01Revised: BaseDay<Int>(1) {
    override fun partOne()=input.sumOf{it.filter(Char::isDigit).let{""+it[0]+it.last()}.toInt()}

    override fun partTwo(): Int {
        val digitsMap = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine").mapIndexed { index, it -> it to index + 1 }

        return input.sumOf { line ->
            val digits = mutableListOf<Int>()
            var temporaryLine = line
            while (temporaryLine.isNotEmpty()) {
                temporaryLine.first().digitToIntOrNull()?.let { digits.add(it) }
                digitsMap.firstOrNull { temporaryLine.startsWith(it.first) }?.let { digits.add(it.second) }
                temporaryLine = temporaryLine.drop(1)
            }

            (digits.first() * 10) + digits.last()
        }
    }
}