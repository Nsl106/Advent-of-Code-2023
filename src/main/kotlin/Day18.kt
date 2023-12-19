object Day18: BaseDay(18) {
    val grid = Grid<Char>()

    override fun partOne(): Any {
        Position(0, 0)
        val instructions = input.map {
            val direction = when (it[0]) {
                'U' -> Direction.NORTH
                'R' -> Direction.EAST
                'D' -> Direction.SOUTH
                'L' -> Direction.WEST
                else -> throw Exception("")
            }
            val steps = it.getFirstInt().toLong()
            Instruction(direction, steps)
        }
        return solve(instructions)
    }

    override fun partTwo(): Any {
        val instructions = input.map {
            val hexCode = it.substringAfter('#').take(6)
            val direction = when (hexCode.last()) {
                '0' -> Direction.EAST
                '1' -> Direction.SOUTH
                '2' -> Direction.WEST
                '3' -> Direction.NORTH
                else -> throw Exception("")
            }
            val steps = hexCode.take(5).toLong(radix = 16)
            Instruction(direction, steps)
        }
        return solve(instructions)
    }

    private fun solve(instructions: List<Instruction>): Long {
        val points = mutableListOf<LongPoint>()
        var currentPosition = LongPoint(0, 0)
        var borderCount = 0L

        instructions.forEach { (direction, stepCount) ->
            currentPosition = currentPosition.moveSteps(direction, stepCount)
            borderCount += stepCount
            points.add(currentPosition)
        }
        return polygonArea(points) - (borderCount / 2) + borderCount + 1
    }

    data class Instruction(val direction: Direction, val movementCount: Long)
}