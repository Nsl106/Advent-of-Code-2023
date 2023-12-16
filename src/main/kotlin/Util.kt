import java.io.File

fun <T> List<T>.getRange(start: Int, end: Int): List<T> {
    val list = mutableListOf<T?>()
    (start..end).forEach { list.add(this.getOrNull(it)) }
    return list.filterNotNull()
}

fun String.getFirstInt() = substring(indexOfFirst { it.isDigit() }).takeWhile { it.isDigit() }.toInt()

fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> {
    var currentOffset = 0

    var nextIndex = indexOfFirst(predicate)
    if (nextIndex == -1) return listOf(this)

    val result = mutableListOf<List<T>>()
    do {
        if (currentOffset != nextIndex)
        result.add(subList(currentOffset, nextIndex))
        currentOffset = nextIndex + 1

        val relativeIndex = drop(currentOffset).indexOfFirst(predicate)
        nextIndex = currentOffset + relativeIndex
    } while (relativeIndex != -1)

    result.add(drop(currentOffset))
    return result
}

fun <T> List<List<T>>.toCols(): List<List<T>> {
    val rowCount = this.size
    val colCount = this[0].size
    val rows = this
    val cols = mutableListOf<List<T>>()

    for (i in 0..<colCount) {
        val col = mutableListOf<T>()
        rows.forEachIndexed { index, _ ->
            col.add(this[index][i])
        }
        cols.add(col)
    }
    return cols
}

fun Int.isEven() = this % 2 == 0
fun Int.isOdd() = this % 2 != 0

fun println(vararg values: Any?) = kotlin.io.println(values.joinToString())

data class Coordinate(val x: Int, val y: Int)
data class GridCoordinate(val row: Int, val col: Int)

abstract class BaseDay(val day: Int) {
    protected open val input by lazy { File(javaClass.getResource("inputs/$day")?.toURI() ?: error("Missing input for day $day")).readLines() }
    abstract fun partOne(): Any
    abstract fun partTwo(): Any
}