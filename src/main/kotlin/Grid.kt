class Grid<T> {
    private val backing = mutableMapOf<BasePosition, GridIndex<T>>()
    operator fun get(row: Int, col: Int) = get(Position(row, col))
    operator fun get(position: BasePosition) = backing[Position(position.row, position.col)]!!

    fun getOrNull(row: Int, col: Int) = backing[Position(row, col)]
    fun getOrNull(position: BasePosition) = backing[position as (Position)]

    operator fun set(position: BasePosition, newValue: T) = set(position.row, position.col, newValue)
    operator fun set(row: Int, col: Int, newValue: T) {
        val position = Position(row, col)
        if (!contains(position)) updateIndicesToContain(position)
        backing[position] = GridIndex(position, newValue, this)
    }

    fun getRow(number: Int) = backing.filter { it.key.row == number }.map { it.key.col to it.value }.toMap()
    fun getCol(number: Int) = backing.filter { it.key.col == number }.map { it.key.row to it.value }.toMap()

    val all get() = backing.values.toList()
    val rows get() = buildList { for (index in rowIndices) add(getRow(index)) }
    val cols get() = buildList { for (index in colIndices) add(getCol(index)) }

    fun contains(position: BasePosition) = position.col in colIndices && position.row in rowIndices

    fun topLeft() = get(rowIndices.first, colIndices.first)
    fun topRight() = get(rowIndices.first, colIndices.last)
    fun bottomLeft() = get(rowIndices.last, colIndices.first)
    fun bottomRight() = get(rowIndices.last, colIndices.last)

    var rowIndices = 0..0
        private set

    var colIndices = 0..0
        private set

    private fun updateIndicesToContain(position: BasePosition) {
        if (position.row < rowIndices.first) rowIndices = position.row..rowIndices.last
        else if (position.row > rowIndices.last) rowIndices = rowIndices.first..position.row
        if (position.col < colIndices.first) colIndices = position.col..colIndices.last
        else if (position.col > colIndices.last) colIndices = colIndices.first..position.col
    }

    class GridIndex<T> internal constructor(val position: BasePosition, val value: T, private val reference: Grid<T>) {
        val north get() = reference.getOrNull(position.move(Direction.NORTH))
        val east get() = reference.getOrNull(position.move(Direction.EAST))
        val south get() = reference.getOrNull(position.move(Direction.SOUTH))
        val west get() = reference.getOrNull(position.move(Direction.WEST))

        fun move(direction: Direction) = reference.getOrNull(position.move(direction))
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
