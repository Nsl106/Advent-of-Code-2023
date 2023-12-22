import kotlin.math.abs

abstract class BasePosition(open val row: Int, open val col: Int) {
    abstract fun move(direction: Direction): BasePosition
    abstract fun moveSteps(direction: Direction, steps: Int): BasePosition

    override fun equals(other: Any?): Boolean {
        return other is BasePosition && this.row == other.row && this.col == other.col
    }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + col
        return result
    }
}

fun manhattenDistance(a: BasePosition, b: BasePosition) = abs(a.row - b.row) + abs(a.col - b.col)

data class Position(override val row: Int, override val col: Int): BasePosition(row, col) {
    override fun move(direction: Direction) = moveSteps(direction, 1)
    override fun moveSteps(direction: Direction, steps: Int) = when (direction) {
        Direction.NORTH -> Position(row - steps, col)
        Direction.EAST -> Position(row, col + steps)
        Direction.SOUTH -> Position(row + steps, col)
        Direction.WEST -> Position(row, col - steps)
    }
}

data class LongPoint(val row: Long, val col: Long) {
    fun moveSteps(direction: Direction, steps: Long) = when (direction) {
        Direction.NORTH -> LongPoint(row - steps, col)
        Direction.EAST -> LongPoint(row, col + steps)
        Direction.SOUTH -> LongPoint(row + steps, col)
        Direction.WEST -> LongPoint(row, col - steps)
    }
}

data class FacingPosition(override val row: Int, override val col: Int, val facing: Direction): BasePosition(row, col) {
    constructor(position: BasePosition, facing: Direction): this(position.row, position.col, facing)

    override fun move(direction: Direction) = moveSteps(direction, 1)
    override fun moveSteps(direction: Direction, steps: Int) = when (direction) {
        Direction.NORTH -> FacingPosition(row - steps, col, facing)
        Direction.EAST -> FacingPosition(row, col + steps, facing)
        Direction.SOUTH -> FacingPosition(row + steps, col, facing)
        Direction.WEST -> FacingPosition(row, col - steps, facing)
    }

    fun turnRight() = FacingPosition(row, col, facing.right())
    fun turnLeft() = FacingPosition(row, col, facing.left())

    /** Move once in the direction you are facing */
    fun walk() = FacingPosition(move(facing), facing)
    fun walkSteps(steps: Int) = FacingPosition(moveSteps(facing, steps), facing)
}

enum class Direction {
    NORTH, EAST, SOUTH, WEST;

    fun right() = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
    }

    fun left() = when (this) {
        NORTH -> WEST
        EAST -> NORTH
        SOUTH -> EAST
        WEST -> SOUTH
    }
}