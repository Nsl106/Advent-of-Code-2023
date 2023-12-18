// https://github.com/Mistborn94/advent-of-code-2023/blob/master/src/main/kotlin/day17/Day17.kt
object Day17: BaseDay(17) {
    data class PathPosition(val position: Grid.FacingPosition, val distanceInDirection: Int)

    private val grid = gridOf(input.map { it.toList().map(Char::digitToInt) })
    private val end = grid.bottomRight().position

    private fun getNormalMoves(position: PathPosition) = buildList {
        if (position.distanceInDirection < 3) {
            add(PathPosition(position.position.walk(), position.distanceInDirection + 1))
        }
        add(PathPosition(position.position.turnLeft().walk(), 1))
        add(PathPosition(position.position.turnRight().walk(), 1))
    }.filter { grid.contains(it.position) }

    private fun getUltraMoves(position: PathPosition) = buildList {
        if (position.distanceInDirection < 10) {
            add(PathPosition(position.position.walk(), position.distanceInDirection + 1))
        }
        if (position.distanceInDirection >= 4) {
            add(PathPosition(position.position.turnLeft().walk(), 1))
            add(PathPosition(position.position.turnRight().walk(), 1))
        }
    }.filter { grid.contains(it.position) }

    override fun partOne() = runDijkstra(
        ::getNormalMoves,
        { _, end -> grid[end.position].value },
        PathPosition(Grid.FacingPosition(0, 0, Grid.Direction.EAST), 0),
        { it.position.col == end.col && it.position.row == end.row }
    ).getCost()

    override fun partTwo() = runDijkstra(
        ::getUltraMoves,
        { _, end -> grid[end.position].value },
        PathPosition(Grid.FacingPosition(0, 0, Grid.Direction.EAST), 0),
        { it.position.col == end.col && it.position.row == end.row }
    ).getCost()
}