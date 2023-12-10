object Day10: BaseDay<Int>(10) {
    data class Coordinate(val x: Int, val y: Int)

    private val moves = mapOf(
        '|' to (Coordinate(0, 1) to Coordinate(0, -1)),
        '-' to (Coordinate(1, 0) to Coordinate(-1, 0)),
        'L' to (Coordinate(0, -1) to Coordinate(1, 0)),
        'J' to (Coordinate(0, -1) to Coordinate(-1, 0)),
        '7' to (Coordinate(0, 1) to Coordinate(-1, 0)),
        'F' to (Coordinate(0, 1) to Coordinate(1, 0)),
        '.' to (Coordinate(0, 0) to Coordinate(0, 0)),
    )

    private val grid = input.map { it.toList() }

    private fun Coordinate.getValidMoves(): List<Coordinate> {
        val options = moves.getValue(grid[y][x])
        return listOf(Coordinate(x + options.first.x, y + options.first.y), Coordinate(x + options.second.x, y + options.second.y))
    }

    private fun Coordinate.surrounding(): List<Coordinate> {
        return listOf(
            Coordinate(x + 1, y + 1),
            Coordinate(x + 1, y),
            Coordinate(x + 1, y - 1),
            Coordinate(x, y - 1),
            Coordinate(x - 1, y - 1),
            Coordinate(x - 1, y),
            Coordinate(x - 1, y + 1),
            Coordinate(x, y + 1),
        )
    }

    private val path = mutableListOf<Coordinate>()

    private var lastCoordinate = Coordinate(-1, -1)

    private tailrec fun follow(start: Coordinate) {
        if (grid[start.y][start.x] == 'S') return
        val nextMove = start.getValidMoves().first { it != lastCoordinate }
        lastCoordinate = start
        path.add(nextMove)
        follow(nextMove)
    }

    init {
        val startY = grid.indexOfFirst { it.contains('S') }
        val startX = grid[startY].indexOf('S')

        val startCoordinate = Coordinate(startX, startY)

        val pathStart = startCoordinate.surrounding().first { it.getValidMoves().contains(startCoordinate) }
        lastCoordinate = startCoordinate
        path.add(pathStart)
        follow(pathStart)
    }

    override fun partOne() = path.size / 2

    // https://www.reddit.com/r/adventofcode/comments/18evyu9/comment/kcqtow6/
    override fun partTwo(): Int {
        var insideCount = 0

        for (yIndex in grid.indices) {
            for (xIndex in grid[yIndex].indices) {
                val coordinate = Coordinate(xIndex, yIndex)
                if (path.contains(coordinate)) continue

                val ray = mutableListOf(coordinate)
                var nextX = coordinate.x
                var nextY = coordinate.y
                do {
                    ray.add(Coordinate(nextX, nextY))
                    nextX++
                    nextY++
                } while (nextX < grid[0].size && nextY < grid.size)

                val intersectionCount = ray.count {
                    val char = grid[it.y][it.x]

                    // Ignore tangent corners
                    char != '7' && char != 'L' && path.contains(it)
                }

                if (intersectionCount.isOdd()) insideCount++
            }
        }

        return insideCount
    }
}