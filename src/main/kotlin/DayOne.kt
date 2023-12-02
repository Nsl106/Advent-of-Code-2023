object DayOne: BaseDay(1) {

    override fun printPartOne(input: List<String>) {
        var sum = 0
        input.forEach { line ->
            val digitOne = line.first { it.isDigit() }
            val digitTwo = line.last { it.isDigit() }
            sum += "$digitOne$digitTwo".toInt()
        }
        println(sum)
    }

    override fun printPartTwo(input: List<String>) {

    }
}