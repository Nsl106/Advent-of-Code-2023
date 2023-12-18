class Grid<T> {
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

//    val inner = lists.map { it.toMutableList() }.toMutableList()

    operator fun get(row: Int, col: Int) = get(Position(row, col))//GridIndex(Position(row, col), inner[row][col], this)
    operator fun get(position: BasePosition) = nodes[Position(position.row, position.col)]!!//GridIndex(position, inner[position.row][position.col], this)

    val nodes = mutableMapOf<BasePosition, GridIndex<T>>()

    var rowIndices = 0..0
        private set
    var colIndices = 0..0
        private set
//    val colCount get() = inner[0].size
//    val rowCount get() = rowIndices.

    fun contains(position: BasePosition) = position.col in colIndices && position.row in rowIndices

    val all get() = nodes.values.toList()

    fun getRow(number: Int): Map<Int, GridIndex<T>> {
        return nodes.filter { it.key.row == number }.map { it.key.col to it.value }.toMap()
    }

    fun getCol(number: Int): Map<Int, GridIndex<T>> {
        return nodes.filter { it.key.col == number }.map { it.key.row to it.value }.toMap()
    }

    val rows get() = buildList { for (index in rowIndices) add(getRow(index)) }
    val cols get() = buildList { for (index in colIndices) add(getCol(index)) }

    fun getOrNull(row: Int, col: Int) = nodes[Position(row, col)]
    fun getOrNull(position: BasePosition) = nodes[position as (Position)]

    operator fun set(position: BasePosition, newValue: T) = set(position.row, position.col, newValue)

    operator fun set(row: Int, col: Int, newValue: T) {
        val position = Position(row, col)
        if (!contains(position)) updateIndicesToContain(position)
        nodes[position] = GridIndex(position, newValue, this)
    }

    private fun updateIndicesToContain(position: BasePosition) {
        if (position.row < rowIndices.first) rowIndices = position.row..rowIndices.last
        else if (position.row > rowIndices.last) rowIndices = rowIndices.first..position.row
        if (position.col < colIndices.first) colIndices = position.col..colIndices.last
        else if (position.col > colIndices.last) colIndices = colIndices.first..position.col
    }

    fun topLeft() = get(rowIndices.first, colIndices.first)
    fun topRight() = get(rowIndices.first, colIndices.last)
    fun bottomLeft() = get(rowIndices.last, colIndices.first)
    fun bottomRight() = get(rowIndices.last, colIndices.last)

    class GridIndex<T> internal constructor(val position: BasePosition, val value: T, private val reference: Grid<T>) {
        val north get() = reference.getOrNull(position.move(Direction.NORTH))
        val east get() = reference.getOrNull(position.move(Direction.EAST))
        val south get() = reference.getOrNull(position.move(Direction.SOUTH))
        val west get() = reference.getOrNull(position.move(Direction.WEST))

        fun move(direction: Direction) = reference.getOrNull(position.move(direction))
    }

    abstract class BasePosition(open val row: Int, open val col: Int) {
        abstract fun move(direction: Direction): BasePosition

        override fun equals(other: Any?): Boolean {
            return other is BasePosition && this.row == other.row && this.col == other.col
        }

        override fun hashCode(): Int {
            var result = row
            result = 31 * result + col
            return result
        }
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
    val grid = Grid<T>()
    for ((rowIndex, row) in lists.withIndex()) {
        for ((colIndex, item) in row.withIndex()) {
            grid[rowIndex, colIndex] = item
        }
    }
    return grid
}
