import java.io.File

fun List<String>.getRange(start: Int, end: Int): List<String> {
    val list = mutableListOf<String?>()
    (start..end).forEach { list.add(this.getOrNull(it)) }
    return list.filterNotNull()
}

abstract class BaseDay<T>(val day: Int) {
    protected open val input by lazy { File(javaClass.getResource("inputs/$day")?.toURI() ?: error("Missing input for day $day")).readLines() }
    abstract fun partOne(): T
    abstract fun partTwo(): T
}