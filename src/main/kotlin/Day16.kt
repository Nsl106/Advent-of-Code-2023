import Grid.Direction

object Day16: BaseDay(16) {
    private val grid = gridOf(input.map { it.toList() })

    private val activated = mutableSetOf<Grid.BasePosition>()

    // Don't need to follow a path twice
    private val alreadyChecked = mutableSetOf<Pair<Grid.BasePosition, Direction>>()

    // Save splits and check them later to prevent stack overflow
    private val splitsToCheck = mutableSetOf<Pair<Grid.GridIndex<Char>, Direction>>()

    private tailrec fun calculateBeam(start: Grid.GridIndex<Char>, direction: Direction) {
        val pair = start.position to direction
        if (alreadyChecked.contains(pair)) return
        alreadyChecked.add(pair)
        val char = start.value

        activated.add(start.position)
        when (char) {
            '.' -> {
                calculateBeam(start.move(direction) ?: return, direction)
            }

            '/' -> {
                when (direction) {
                    Direction.NORTH -> calculateBeam(start.east ?: return, Direction.EAST)
                    Direction.SOUTH -> calculateBeam(start.west ?: return, Direction.WEST)
                    Direction.WEST -> calculateBeam(start.south ?: return, Direction.SOUTH)
                    Direction.EAST -> calculateBeam(start.north ?: return, Direction.NORTH)
                }
            }

            '\\' -> {
                when (direction) {
                    Direction.NORTH -> calculateBeam(start.west ?: return, Direction.WEST)
                    Direction.SOUTH -> calculateBeam(start.east ?: return, Direction.EAST)
                    Direction.WEST -> calculateBeam(start.north ?: return, Direction.NORTH)
                    Direction.EAST -> calculateBeam(start.south ?: return, Direction.SOUTH)
                }
            }

            '-' -> {
                when (direction) {
                    Direction.NORTH, Direction.SOUTH -> {
                        start.east?.let { splitsToCheck.add(it to Direction.EAST) }
                        start.west?.let { splitsToCheck.add(it to Direction.WEST) }
                    }

                    Direction.WEST, Direction.EAST -> calculateBeam(start.move(direction) ?: return, direction)
                }
            }

            '|' -> {
                when (direction) {
                    Direction.WEST, Direction.EAST -> {
                        start.north?.let { splitsToCheck.add(it to Direction.NORTH) }
                        start.south?.let { splitsToCheck.add(it to Direction.SOUTH) }
                    }

                    Direction.NORTH, Direction.SOUTH -> calculateBeam(start.move(direction) ?: return, direction)
                }
            }
        }
    }

    override fun partOne() = activatedTilesFrom(Grid.Position(0, 0), Direction.EAST)

    override fun partTwo(): Any {
        var max = 0
        grid.rows.first().forEach { max = maxOf(max, activatedTilesFrom(it.position, Direction.SOUTH)) }
        grid.rows.last().forEach { max = maxOf(max, activatedTilesFrom(it.position, Direction.NORTH)) }
        grid.cols.first().forEach { max = maxOf(max, activatedTilesFrom(it.position, Direction.EAST)) }
        grid.cols.last().forEach { max = maxOf(max, activatedTilesFrom(it.position, Direction.WEST)) }
        return max
    }

    private fun activatedTilesFrom(coordinate: Grid.BasePosition, direction: Direction): Int {
        activated.clear()
        alreadyChecked.clear()
        splitsToCheck.clear()
        calculateBeam(grid[coordinate], direction)
        while (true) {
            val split = splitsToCheck.firstOrNull() ?: break
            calculateBeam(split.first, split.second)
            splitsToCheck.remove(split)
        }

        return activated.size
    }
}