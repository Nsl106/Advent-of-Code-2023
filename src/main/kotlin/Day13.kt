data object Day13: Day() {
    private val puzzles get() = input.split { it.isEmpty() }.map { it.map(String::toList) }

    private fun getMirrorPos(values: List<List<Char>>, equivalent: (List<Char>, List<Char>) -> Boolean, isValid: (Int) -> Boolean): Int {
        centers@ for (i in 0..values.size - 2) {
            // If this isn't a potential mirror center, continue
            if (!equivalent(values[i], values[i + 1])) continue@centers

            val indices = i to i + 1

            // Make sure this center is valid
            if (!isValid(indices.second)) continue@centers

            var distance = 0
            while (true) {
                val previous = values.getOrNull(indices.first - distance)
                val next = values.getOrNull(indices.second + distance)

                // If either extend outside the range while it's still matching, it's a valid range
                if (previous == null || next == null) return indices.second
                // Check that they're still matching and move to the next center if they aren't
                if (equivalent(previous, next)) distance++ else continue@centers
            }
        }
        return 0
    }

    // Check if first and second are equal or off by one
    private fun isEqualOrBySmudge(first: List<Char>, second: List<Char>): Boolean {
        var errorCount = 0
        first.zip(second).forEach { if (it.first != it.second) errorCount++ }
        return errorCount <= 1
    }

    private fun getPartOneDistance(values: List<List<Char>>) = getMirrorPos(values, { a, b -> a == b }, { true })
    private fun getPartTwoDistance(values: List<List<Char>>) = getMirrorPos(values, ::isEqualOrBySmudge) { it != getPartOneDistance(values) }

    override fun partOne() = puzzles.sumOf { rows -> getPartOneDistance(rows.toCols()) + (getPartOneDistance(rows) * 100) }
    override fun partTwo() = puzzles.sumOf { rows -> getPartTwoDistance(rows.toCols()) + (getPartTwoDistance(rows) * 100) }
}