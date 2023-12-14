object Day12: BaseDay<Long>(12) {

    // https://github.com/eagely/adventofcode/blob/main/src/main/kotlin/solutions/y2023/Day12.kt
    private val cache = hashMapOf<Pair<String, List<Int>>, Long>()
    private fun recurse(springs: String, groups: List<Int>): Long {
        return cache.getOrPut(springs to groups) {
            // After all groups are accounted for, check that this variant has no unused broken springs and then add it to the count
            if (groups.isEmpty()) {
                if (!springs.contains('#')) {
                    return 1
                }
                return 0
            }
            // End when all springs are checked
            if (springs.isEmpty()) return 0

            fun startFixedVariant(): Long {
                // When the spring isn't broken, proceed as a variant without it
                return recurse(springs.drop(1), groups)
            }

            fun startBrokenVariant(): Long {
                val currentGroupSize = groups.first()

                // Ensure that the remaining springs can fit in the group
                if (springs.length >= currentGroupSize) {
                    // Ensure that the springs that would be part of the group either are or could be broken
                    if (springs.take(currentGroupSize).all { it == '#' || it == '?' }) {
                        // If the remaining springs are all part of the group OR
                        // The spring directly after the specified group size isn't broken
                        if (springs.length == currentGroupSize || springs[currentGroupSize] != '#') {
                            // Proceed as a variant without this group
                            return recurse(springs.drop(currentGroupSize + 1), groups.drop(1))
                        }
                    }
                }
                return 0
            }

            var total = 0L
            when (springs.first()) {
                // Create a variant based on the spring state
                '.' -> total += startFixedVariant()
                '#' -> total += startBrokenVariant()

                // When a spring is unknown, start both variants!
                '?' -> {
                    total += startFixedVariant()
                    total += startBrokenVariant()
                }
            }
            return@getOrPut total
        }
    }

    override fun partOne(): Long {
        val x = input.map { ln -> ln.split(' ').let { it.first() to it.last().split(',').map(String::toInt) } }
        return x.sumOf { recurse(it.first, it.second) }
    }

    override fun partTwo(): Long {
        val x = input.map { ln ->
            val split = ln.split(' ')
            val springs = (split.first() + '?').repeat(5).dropLast(1)
            val nums = (split.last() + ',').repeat(5).dropLast(1).split(',').map(String::toInt)
            springs to nums
        }
        return x.sumOf { recurse(it.first, it.second) }
    }
}