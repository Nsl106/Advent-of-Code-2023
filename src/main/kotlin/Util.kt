import kotlin.math.abs

fun <T> List<T>.getRange(start: Int, end: Int): List<T> {
    val list = mutableListOf<T?>()
    (start..end).forEach { list.add(this.getOrNull(it)) }
    return list.filterNotNull()
}

fun String.firstInt() = substring(indexOfFirst { it.isDigit() }).takeWhile { it.isDigit() }.toInt()
fun String.firstWord() = substring(indexOfFirst { it.isLetter() }).takeWhile { it.isLetter() }

fun <T> MutableList<T>.pop() = first().also { removeFirst() }

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

fun polygonArea(points: List<LongPoint>): Long {
    var area = 0L
    var previousIndex = points.size - 1
    for (index in points.indices) {
        val current = points[index]
        val previous = points[previousIndex]
        area += (previous.col + current.col) * (previous.row - current.row)
        previousIndex = index
    }
    return abs(area / 2)
}

fun gcd(a: Long, b: Long): Long {
    var a = a
    var b = b
    while (b > 0) {
        val temp = b
        b = a % b
        a = temp
    }
    return a
}

fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b))
}

fun Int.isEven() = this % 2 == 0
fun Int.isOdd() = this % 2 != 0

fun println(vararg values: Any?) = kotlin.io.println(values.joinToString())

data class Coordinate(val x: Int, val y: Int)