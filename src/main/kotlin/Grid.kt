class Grid<T>(lists: List<List<T>> = emptyList()) {
    /*
     * /   N   \
     * | W * E |
     * \   S  /
     */
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

    private val inner = lists.map { it.toMutableList() }.toMutableList()

    operator fun get(row: Int, col: Int) = GridIndex(Position(row, col), inner[row][col], this)
    operator fun get(position: BasePosition) = GridIndex(position, inner[position.row][position.col], this)

    val rowCount get() = inner.size
    val colCount get() = inner[0].size
    val rowIndices get() = 0..<inner.size
    val colIndices get() = 0..<inner[0].size

    fun contains(position: BasePosition) = position.col in colIndices && position.row in rowIndices

    val all: List<GridIndex<T>>
        get() = rows.flatten()

    val allPositions: List<BasePosition>
        get() = rows.flatten().map { it.position }

    val rows: List<List<GridIndex<T>>>
        get() {
            val rows = MutableList(rowCount) { mutableListOf<GridIndex<T>>() }
            for (rowNumber in 0..<rowCount) {
                for (colNumber in 0..<colCount) {
                    rows[rowNumber].add(colNumber, get(rowNumber, colNumber))
                }
            }
            return rows
        }
    val cols: List<List<GridIndex<T>>>
        get() {
            val cols = MutableList(colCount) { mutableListOf<GridIndex<T>>() }
            for (rowNumber in 0..<rowCount) {
                for (colNumber in 0..<colCount) {
                    cols[colNumber].add(rowNumber, get(rowNumber, colNumber))
                }
            }
            return cols
        }

    fun getOrNull(row: Int, col: Int): GridIndex<T>? {
        return GridIndex(Position(row, col), inner.getOrNull(row)?.getOrNull(col) ?: return null, this)
    }

    fun getOrNull(position: BasePosition): GridIndex<T>? {
        return GridIndex(position, inner.getOrNull(position.row)?.getOrNull(position.col) ?: return null, this)
    }

    operator fun set(position: BasePosition, newValue: T) {
        inner[position.row][position.col] = newValue
    }

    operator fun set(row: Int, col: Int, newValue: T) {
        inner[row][col] = newValue
    }

    class GridIndex<T> internal constructor(val position: BasePosition, val value: T, private val reference: Grid<T>) {
        val north get() = reference.getOrNull(position.move(Direction.NORTH))
        val east get() = reference.getOrNull(position.move(Direction.EAST))
        val south get() = reference.getOrNull(position.move(Direction.SOUTH))
        val west get() = reference.getOrNull(position.move(Direction.WEST))

        fun move(direction: Direction) = reference.getOrNull(position.move(direction))
    }

    abstract class BasePosition(open val row: Int, open val col: Int) {
        abstract fun move(direction: Direction): BasePosition
    }

    data class Position(override val row: Int, override val col: Int): BasePosition(row, col) {
        override fun move(direction: Direction) = when (direction) {
            Direction.NORTH -> Position(row - 1, col)
            Direction.EAST -> Position(row, col + 1)
            Direction.SOUTH -> Position(row + 1, col)
            Direction.WEST -> Position(row, col - 1)
        }
    }

    data class FacingPosition(override val row: Int, override val col: Int, val facing: Direction): BasePosition(row, col) {
        constructor(position: BasePosition, facing: Direction): this(position.row, position.col, facing)

        override fun move(direction: Direction) = when (direction) {
            Direction.NORTH -> FacingPosition(row - 1, col, facing)
            Direction.EAST -> FacingPosition(row, col + 1, facing)
            Direction.SOUTH -> FacingPosition(row + 1, col, facing)
            Direction.WEST -> FacingPosition(row, col - 1, facing)
        }

        fun turnRight() = FacingPosition(row, col, facing.right())
        fun turnLeft() = FacingPosition(row, col, facing.left())

        /** Move once in the direction you are facing */
        fun walk() = FacingPosition(move(facing), facing)
    }
}

fun <T> gridOf(lists: List<List<T>>): Grid<T> {
    return Grid(lists)
}
