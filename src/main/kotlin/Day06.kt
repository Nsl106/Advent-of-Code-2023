object Day06: BaseDay<Int>(6) {
    override fun partOne(): Int {
        val (times, records) = input.map { it.substringAfter(":").trim().split(Regex(" +")).map(String::toLong) }

        val allCounts = mutableListOf<Int>()

        for (i in times.indices) allCounts.add(calculate(times[i], records[i]))

        return allCounts.reduce(Int::times)
    }

    override fun partTwo() = input.map { it.filter(Char::isDigit).toLong() }.let { calculate(it[0], it[1]) }

    private fun calculate(time: Long, currentRecord: Long): Int {
        return (1..time).count { seconds -> (time - seconds) * seconds > currentRecord }
    }
}
