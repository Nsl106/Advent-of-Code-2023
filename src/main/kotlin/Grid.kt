class Grid<T>(lists: List<List<T>> = emptyList()) {
    /*
     * /   N   \
     * | W * E |
     * \   S  /
     */
    enum class Direction {
        NORTH, EAST, SOUTH, WEST
    }

    private val inner = lists.map { it.toMutableList() }.toMutableList()

    operator fun get(row: Int, col: Int) = GridIndex(Position(row, col), inner[row][col], this)
    operator fun get(position: Position) = GridIndex(position, inner[position.row][position.col], this)

    val rowCount get() = inner.size
    val colCount get() = inner[0].size

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

    fun getOrNull(position: Position): GridIndex<T>? {
        return GridIndex(position, inner.getOrNull(position.row)?.getOrNull(position.col) ?: return null, this)
    }

    operator fun set(position: Position, newValue: T) {
        inner[position.row][position.col] = newValue
    }

    operator fun set(row: Int, col: Int, newValue: T) {
        inner[row][col] = newValue
    }

    class GridIndex<T> internal constructor(val position: Position, val item: T, private val reference: Grid<T>) {
        val north get() = reference.getOrNull(position.move(Direction.NORTH))
        val east get() = reference.getOrNull(position.move(Direction.EAST))
        val south get() = reference.getOrNull(position.move(Direction.SOUTH))
        val west get() = reference.getOrNull(position.move(Direction.WEST))

        fun move(direction: Direction) = reference.getOrNull(position.move(direction))
    }

    data class Position(val row: Int, val col: Int) {
        fun move(direction: Direction) = when (direction) {
            Direction.NORTH -> Position(row - 1, col)
            Direction.EAST -> Position(row, col + 1)
            Direction.SOUTH -> Position(row + 1, col)
            Direction.WEST -> Position(row, col - 1)
        }
    }
}

fun <T> gridOf(lists: List<List<T>>): Grid<T> {
    return Grid(lists)
}
