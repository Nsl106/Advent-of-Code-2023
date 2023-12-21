import kotlin.math.abs

data object Day11: Day() {
    private val galaxies = mutableListOf<Coordinate>()

    private val expandedRowIndices = mutableListOf<Int>()
    private val expandedColIndices = mutableListOf<Int>()

    init {
        input.forEachIndexed { index, row ->
            val col = row.mapIndexed { index2, _ ->
                input[index2][index]
            }

            if (row.all { it == '.' }) expandedRowIndices.add(index)
            if (col.all { it == '.' }) expandedColIndices.add(index)
        }


        for (i in input.indices) {
            for (j in input[0].indices) {
                if (input[i][j] == '#') galaxies.add(Coordinate(j, i))
            }
        }
    }

    private fun distanceBetween(coord1: Coordinate, coord2: Coordinate, spaceMultiplier: Long): Long {
        // Count the number of times there was an expansion in between the two points
        val xExpansionsCrossed = expandedColIndices.count { it >= minOf(coord1.x, coord2.x) && it <= maxOf(coord1.x, coord2.x) }
        val yExpansionsCrossed = expandedRowIndices.count { it >= minOf(coord1.y, coord2.y) && it <= maxOf(coord1.y, coord2.y) }

        return abs((coord1.x - coord2.x)) + abs((coord1.y - coord2.y)) + ((xExpansionsCrossed + yExpansionsCrossed) * (spaceMultiplier - 1))
    }

    private fun calculate(expansionDistance: Long): Long {
        var totalDistance = 0L

        // For each galaxy, check against all galaxies after it
        for (i in galaxies.indices) {
            for (j in (i + 1)..<galaxies.size) {
                totalDistance += distanceBetween(galaxies[j], galaxies[i], expansionDistance)
            }
        }

        return totalDistance
    }

    override fun partOne() = calculate(2L)
    override fun partTwo() = calculate(1000000L)
}