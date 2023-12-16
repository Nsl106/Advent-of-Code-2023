object Day16: BaseDay(16) {
    private val grid = input.map { it.toMutableList() }.toMutableList()

    enum class Direction {
        UP, DOWN, LEFT, RIGHT;
    }

    private fun GridCoordinate.move(direction: Direction) = when (direction) {
        Direction.UP -> GridCoordinate(row - 1, col)
        Direction.DOWN -> GridCoordinate(row + 1, col)
        Direction.LEFT -> GridCoordinate(row, col - 1)
        Direction.RIGHT -> GridCoordinate(row, col + 1)
    }

    private val activated = mutableSetOf<GridCoordinate>()

    // Don't need to follow a path twice
    private val alreadyChecked = mutableSetOf<Pair<GridCoordinate, Direction>>()

    // Save splits and check them later to prevent stack overflow
    private val splitsToCheck = mutableSetOf<Pair<GridCoordinate, Direction>>()

    private tailrec fun calculateBeam(start: GridCoordinate, direction: Direction) {
        val pair = start to direction
        if (alreadyChecked.contains(pair)) return
        alreadyChecked.add(pair)
        val char = grid.getOrNull(start.row)?.getOrNull(start.col) ?: return
        activated.add(start)
        when (char) {
            '.' -> {
                calculateBeam(start.move(direction), direction)
            }

            '/' -> {
                when (direction) {
                    Direction.UP -> calculateBeam(start.move(Direction.RIGHT), Direction.RIGHT)
                    Direction.DOWN -> calculateBeam(start.move(Direction.LEFT), Direction.LEFT)
                    Direction.LEFT -> calculateBeam(start.move(Direction.DOWN), Direction.DOWN)
                    Direction.RIGHT -> calculateBeam(start.move(Direction.UP), Direction.UP)
                }
            }

            '\\' -> {
                when (direction) {
                    Direction.UP -> calculateBeam(start.move(Direction.LEFT), Direction.LEFT)
                    Direction.DOWN -> calculateBeam(start.move(Direction.RIGHT), Direction.RIGHT)
                    Direction.LEFT -> calculateBeam(start.move(Direction.UP), Direction.UP)
                    Direction.RIGHT -> calculateBeam(start.move(Direction.DOWN), Direction.DOWN)
                }
            }

            '-' -> {
                when (direction) {
                    Direction.UP, Direction.DOWN -> {
                        splitsToCheck.add(start.move(Direction.RIGHT) to Direction.RIGHT)
                        splitsToCheck.add(start.move(Direction.LEFT) to Direction.LEFT)
                    }

                    Direction.LEFT, Direction.RIGHT -> calculateBeam(start.move(direction), direction)
                }
            }

            '|' -> {
                when (direction) {
                    Direction.LEFT, Direction.RIGHT -> {
                        splitsToCheck.add(start.move(Direction.UP) to Direction.UP)
                        splitsToCheck.add(start.move(Direction.DOWN) to Direction.DOWN)
                    }

                    Direction.UP, Direction.DOWN -> calculateBeam(start.move(direction), direction)
                }
            }
        }
    }

    override fun partOne() = activatedTilesFrom(GridCoordinate(0, 0), Direction.RIGHT)

    override fun partTwo(): Any {
        var max = 0
        for (i in grid.first().indices) max = maxOf(max, activatedTilesFrom(GridCoordinate(0, i), Direction.DOWN))
        for (i in grid.first().indices) max = maxOf(max, activatedTilesFrom(GridCoordinate(grid.size - 1, i), Direction.UP))
        for (i in grid.indices) max = maxOf(max, activatedTilesFrom(GridCoordinate(i, 0), Direction.RIGHT))
        for (i in grid.indices) max = maxOf(max, activatedTilesFrom(GridCoordinate(i, grid.first().size - 1), Direction.LEFT))
        return max
    }

    private fun activatedTilesFrom(coordinate: GridCoordinate, direction: Direction): Int {
        activated.clear()
        alreadyChecked.clear()
        splitsToCheck.clear()
        calculateBeam(coordinate, direction)
        while (true) {
            val split = splitsToCheck.firstOrNull() ?: break
            calculateBeam(split.first, split.second)
            splitsToCheck.remove(split)
        }

        return activated.size
    }
}