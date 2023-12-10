import java.io.File

fun <T> List<T>.getRange(start: Int, end: Int): List<T> {
    val list = mutableListOf<T?>()
    (start..end).forEach { list.add(this.getOrNull(it)) }
    return list.filterNotNull()
}

fun String.getFirstInt() = substring(indexOfFirst { it.isDigit() }).takeWhile { it.isDigit() }.toInt()

fun Int.isEven() = this % 2 == 0
fun Int.isOdd() = this % 2 != 0

fun println(vararg values: Any) = kotlin.io.println(values.joinToString())

abstract class BaseDay<T>(val day: Int) {
    protected open val input by lazy { File(javaClass.getResource("inputs/$day")?.toURI() ?: error("Missing input for day $day")).readLines() }
    abstract fun partOne(): T
    abstract fun partTwo(): T
}