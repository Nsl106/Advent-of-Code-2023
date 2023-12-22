data object Day21: Day() {
    private val grid = gridOf(input.map { it.toList() })
    private val start = grid.all.first { it.value == 'S' }.position as Position

    private fun getNeighbors(position: Position) = setOf(
        position.move(Direction.NORTH),
        position.move(Direction.EAST),
        position.move(Direction.SOUTH),
        position.move(Direction.WEST),
    ).filter { grid.contains(it) && grid[it].value != '#' }

    override fun partOne(): Int {
        var lastPoints = setOf(start)

        for (i in 1..64) {
            lastPoints = lastPoints.flatMap { getNeighbors(it) }.toSet()
        }

        return lastPoints.size
    }

    //https://github.com/villuna/aoc23/wiki/A-Geometric-solution-to-advent-of-code-2023,-day-21
    override fun partTwo(): Any {
        val unvisited = grid.all.filter { it.value != '#' }.map { it.position as Position }.toMutableList()
        val allReachablePoints = mutableSetOf<Position>()

        // Get all reachable points
        // This is needed in case these unreachable ones exist: (and they do)
        // .#.
        // #.#
        // .#.
        var lastPoints = setOf(start)
        var lastSize: Int
        do {
            lastSize = unvisited.size
            lastPoints = lastPoints.flatMap { getNeighbors(it) }.toSet()
            allReachablePoints.addAll(lastPoints)
            unvisited.removeAll(lastPoints)
        } while (lastSize != unvisited.size)


        val oddPoints = mutableSetOf<Position>()
        val evenPoints = mutableSetOf<Position>()

        // From the reachable points, get all that are equal in an even number of steps, and all that are reachable in an odd number
        allReachablePoints.forEach {
            if (it.col.isOdd() && it.row.isEven() || it.col.isEven() && it.row.isOdd()) oddPoints.add(it) else evenPoints.add(it)
        }


        val gridWidth = input[0].length
        val stepCount = 26501365L
        val squareCount = stepCount / gridWidth
        val remainder = stepCount % gridWidth

        val oddSquareCount = (squareCount + 1).squared()
        val evenSquareCount = squareCount.squared()

        val oddCornerSquareCount = squareCount + 1
        val evenCornerSquareCount = squareCount

        val oddCornerSquares = oddPoints.filter { manhattenDistance(it, start) > remainder }
        val evenCornerSquares = evenPoints.filter { manhattenDistance(it, start) > remainder }


        return (oddSquareCount * oddPoints.size) + (evenSquareCount * evenPoints.size) - (oddCornerSquareCount * oddCornerSquares.size) + (evenCornerSquareCount * evenCornerSquares.size)
    }
}